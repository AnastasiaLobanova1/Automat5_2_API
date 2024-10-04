package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class InternetBankTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }
    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(exactText("Личный кабинет"));
    }
    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }
    @Test
    void shouldErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification].notification_status_error .notification__title").shouldHave(exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=error-notification].notification_status_error .notification__content").shouldHave(exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));
    }
    @Test
    void shouldErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__title").shouldHave(exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }
    @Test
    void shouldErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__title").shouldHave(exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }
}
