import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginNegativeParametrizedTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }

    //Parametrized test with ValueSource
    @ValueSource(strings = {"testLogin", "locked_out_user"})
    @ParameterizedTest(name = "Негативное тестирование аутентификации с логином: {0}")
    public void valueSourceLoginTest(String testUsername) {
        open("https://www.saucedemo.com/");
        $("input[data-test='username']" ).setValue(testUsername);
        $("input[data-test='password']" ).setValue("secret_sauce");
        $("input[data-test='login-button']" ).click();
        $("h3[data-test='error']" ).shouldBe(Condition.visible);
    }

    //Parametrized test with CsvSource
    @CsvSource(value = {
            "testLogin, Epic sadface: Username and password do not match any user in this service",
            "locked_out_user, Epic sadface: Sorry, this user has been locked out"})
    @ParameterizedTest(name = "Негативное тестирование аутентификации с логином: {0}")
    public void csvSourceLoginTest(String testUsername, String expectedError) {
        open("https://www.saucedemo.com/" );
        $("input[data-test='username']" ).setValue(testUsername);
        $("input[data-test='password']" ).setValue("secret_sauce");
        $("input[data-test='login-button']" ).click();
        $("h3[data-test='error']" ).shouldHave(Condition.text(expectedError));
    }

    //Parametrized test with MethodSource
    static Stream<Arguments> methodSourceLoginTest() {
        return Stream.of(
                Arguments.of("testLogin", "Epic sadface: Username and password do not match any user in this service"),
                Arguments.of("locked_out_user", "Epic sadface: Sorry, this user has been locked out")
        );
    }

    @MethodSource("methodSourceLoginTest")
    @ParameterizedTest(name = "Негативное тестирование аутентификации с логином: {0}")
    public void methodSourceLoginTest(String testUsername, String expectedError) {
        open("https://www.saucedemo.com/" );
        $("input[data-test='username']" ).setValue(testUsername);
        $("input[data-test='password']" ).setValue("secret_sauce");
        $("input[data-test='login-button']" ).click();
        $("h3[data-test='error']" ).shouldHave(Condition.text(expectedError));
    }

}