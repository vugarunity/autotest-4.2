package seminar.pom;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.codeborne.selenide.Condition.visible;

public class ProfilePage {

    @FindBy(xpath = "//h3/following-sibling::div//div[contains(text(), 'Full name')]/following-sibling::div")
    private SelenideElement fullNameInAdditionalInfo;

    @FindBy(css = "div.mdc-card h2")
    private SelenideElement fullNameInAvatarSection;

    @FindBy(css = "div.mdc-card div.mdc-card__action-icons button[title= 'More options']")
    private SelenideElement editIconInAvatarSection;

    @FindBy(xpath = "//h3/following-sibling::div//div[contains(text(), 'Date of birth')]/following-sibling::div")
    private SelenideElement DateOfbirthInAdditionalInfo;

    @FindBy(xpath = "//form[@id= 'update-item']//button[contains(text(), 'Save')]")
    private SelenideElement saveButton;


    public String getFullNameFromAdditionalInfo() {
        return fullNameInAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameFromAvatarSection() {
        return fullNameInAvatarSection.shouldBe(visible).text();
    }

    public ProfilePage clickEditIconInAvatarSection() {
        editIconInAvatarSection.shouldBe(visible).click();
        return this;
    }

    public void setDateOfbirthInAdditionalInfo(String newDatebirth) {
       DateOfbirthInAdditionalInfo.shouldBe(visible).setValue(newDatebirth);
    }

    public void clickSaveButton() {
        saveButton.shouldBe(visible).click();
    }
    public String getDateOfbirthFromAdditionalInfo() {
        return DateOfbirthInAdditionalInfo.shouldBe(visible).text();
    }
}
