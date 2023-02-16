package com.sardeni.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.open;
import static com.sardeni.tests.helpers.CustomApiListener.withCustomTemplates;

public class TestBase {
    @BeforeAll
    public static void setUp() {
       // RestAssured.filters(withCustomTemplates());
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        Configuration.pageLoadStrategy = "eager";

    }

    @BeforeEach
    void addListenerAndOpenPage() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
}