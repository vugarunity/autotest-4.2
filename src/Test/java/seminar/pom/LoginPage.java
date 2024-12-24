package seminar.pom;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    @FindBy(css = "form#login input[type='text']")
    private SelenideElement usernameField;

    @FindBy(css = "form#login input[type='password']")
    private SelenideElement passwordField;

    @FindBy(css = "form#login button")
    private SelenideElement loginButton;

    @FindBy(css = "div.error-block")
    private SelenideElement errorBlock;

    public LoginPage() {
        Selenide.open("https://test-stand.gb.ru/login");
    }

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        usernameField.shouldBe(visible).setValue(username);
    }

    public void typePasswordInField(String password) {
        passwordField.shouldBe(visible).setValue(password);
    }

    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    public String getErrorBlockText() {
        return errorBlock.shouldBe(visible).getText().replace("\n", " ");
    }
}
