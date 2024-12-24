package seminar.pom;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.ElementsCollection;
import seminar.pom.elements.GroupTableRow;
import seminar.pom.elements.StudentTableRow;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkInNavBar;

    @FindBy(xpath = "//nav//li[contains(@class, 'mdc-menu-surface--anchor')]//span[text()='Profile']")
    private SelenideElement profileLinkInNavBar;

    @FindBy(id = "create-btn")
    private SelenideElement createGroupButton;

    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private SelenideElement groupNameField;

    @FindBy(css = "form div.submit button")
    private SelenideElement submitButtonOnModalWindow;

    @FindBy(xpath = "//span[text()='Creating Study Group']//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private SelenideElement closeCreateGroupIcon;

    @FindBy(css = "div#generateStudentsForm-content input")
    private SelenideElement createStudentsFormInput;

    @FindBy(css = "div#generateStudentsForm-content div.submit button")
    private SelenideElement saveCreateStudentsForm;

    @FindBy(xpath = "//h2[@id='generateStudentsForm-title']/../button")
    private SelenideElement closeCreateStudentsFormIcon;

    @FindBy(xpath = "//table[@aria-label='Tutors list']/tbody/tr")
    private ElementsCollection rowsInGroupTable;

    @FindBy(xpath = "//table[@aria-label='User list']/tbody/tr")
    private ElementsCollection rowsInStudentTable;

    // Инициализация элементов через PageFactory
//    public MainPage() {
//        PageFactory.initElements(new com.codeborne.selenide.WebDriverRunner().getWebDriver(), this);
//    }

    // Создание группы
    public void createGroup(String groupName) {
        createGroupButton.click();
        groupNameField.setValue(groupName);
        submitButtonOnModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public void closeCreateGroupModalWindow() {
        closeCreateGroupIcon.shouldBe(visible).click();
    }

    public void typeAmountOfStudentsInCreateStudentsForm(int amount) {
        createStudentsFormInput.shouldBe(visible).setValue(String.valueOf(amount));
    }

    public void clickSaveButtonOnCreateStudentsForm() {
        saveCreateStudentsForm.shouldBe(visible).click();
    }

    public void closeCreateStudentsModalWindow() {
        closeCreateStudentsFormIcon.click();
    }

    public void clickUsernameLabel() {
        usernameLinkInNavBar.shouldBe(visible).click();
    }

    public void clickProfileLink() {
        profileLinkInNavBar.shouldBe(visible).click();
    }

    public String getUsernameLabelText() {
        return usernameLinkInNavBar.shouldBe(visible).getText().replace("\n", " ");
    }

    public SelenideElement waitAndGetGroupTitleByText(String groupName) {
        SelenideElement groupTitle = $x("//td[text()='" + groupName + "']");
        groupTitle.shouldBe(visible);
        return groupTitle;
    }


    // Group Table Section
    public void clickTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickRestoreFromTrashIcon();
    }

    public void clickAddStudentsIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickAddStudentsIcon();
    }

    public void clickZoomInIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).clickZoomInIcon();
    }

    public String getStatusOfGroupWithTitle(String title) {
        return getGroupRowByTitle(title).getStatus();
    }

    public void waitStudentsCount(String groupTestName, int studentsCount) {
        getGroupRowByTitle(groupTestName).waitStudentsCount(studentsCount);
    }

    private GroupTableRow getGroupRowByTitle(String title) {
        return rowsInGroupTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(title))
                .findFirst().orElseThrow();
    }

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickRestoreFromTrashIcon();
    }

    public String getStatusOfStudentWithName(String name) {
        return getStudentRowByName(name).getStatus();
    }

    public String getStudentNameByIndex(int index) {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(StudentTableRow::new)
                .toList().get(index).getName();
    }

    private StudentTableRow getStudentRowByName(String name) {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(name))
                .findFirst().orElseThrow();
    }
}
