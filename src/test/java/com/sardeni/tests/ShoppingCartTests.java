package com.sardeni.tests;

import com.codeborne.selenide.Condition;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ShoppingCartTests extends TestBase{

    @Test
    @DisplayName("Unable buy item without authorization/registration")
    public void checkoutForUnauthorizedCustomer() {

        step("Getting cookies for unathorized user", () -> {
         Response response =
                    given()
                    .log().all()
                    .when()
                    .get("/")
                    .then()
                    .log().all()
                    .statusCode(200)
                            .extract().response();

            String NopCustomer = response.cookie("Nop.customer");
            String ARRAffinityData = response.cookie("ARRAffinity");
            String ARRAffinitySameSiteData = response.cookie("ARRAffinitySameSite");


        step("Add cookies on the site", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));
                    getWebDriver().manage().addCookie(new Cookie("Nop.customer", NopCustomer));
                    getWebDriver().manage().addCookie(new Cookie("ARRAffinity", ARRAffinityData));
                    getWebDriver().manage().addCookie(new Cookie("ARRAffinitySameSite", ARRAffinitySameSiteData));


        step("add Notebook to the shopping cart", () ->
            given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .cookie("Nop.customer", NopCustomer)
                    .cookie("ARRAffinity", ARRAffinityData)
                    .cookie("ARRAffinitySameSite", ARRAffinitySameSiteData)
                    .when()
                    .post("/addproducttocart/catalog/31/1/1")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>")));

        step("Check data in cart", () ->
           open("/cart"));
            $("li#topcartlink .cart-qty").shouldHave(text("(1)"));
            $("a.product-name").shouldHave(text("14.1-inch Laptop"));
            $("#termsofservice").click();
            $("#checkout").click();
            $(".page-title h1").shouldBe(Condition.and("clickable", visible, enabled));
            $(".page-title h1").shouldHave(text("Welcome, Please Sign In!"));
        });
    }
}
