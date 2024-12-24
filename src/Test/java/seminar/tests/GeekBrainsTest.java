package seminar.tests;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import seminar.pom.LoginPage;
import seminar.pom.MainPage;
import seminar.pom.ModalWindow;
import seminar.pom.ProfilePage;
import seminar.pom.elements.GroupTableRow;
import seminar.pom.elements.StudentTableRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GeekBrainsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String USERNAME = "Student-20";
    private static final String PASSWORD = "30f1aee69f";
    private static final String FULL_NAME = "20 Student";


    @BeforeEach
    void setUp() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserVersion = "128";
        Map<String, Object> options = new HashMap<>();
        options.put("enableVNC", true);
        options.put("enableLog", true);
        Configuration.browserCapabilities.setCapability("selenoid:options", options);

        loginPage = page(LoginPage.class);
        mainPage = page(MainPage.class);
    }

    @Test
    public void testGeekBrainsStandLogin() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage();
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    @Test
    public void loginWithoutCredentialsShouldShowError() {
        loginPage.clickLoginButton();
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
    }

    @Test
    public void testAddingGroupOnMainPage() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        SelenideElement createdGroup = mainPage.waitAndGetGroupTitleByText(groupTestName);
        assertThat(createdGroup.exists()).isTrue();
        assertThat(createdGroup.isDisplayed()).isTrue();
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(
                Path.of("src/test/resources/screenshot_" + System.currentTimeMillis() + ".png"),
                screenshotBytes
        );
    }

    @Test
    void testArchiveGroupOnMainPage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        mainPage.waitAndGetGroupTitleByText(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        mainPage.waitAndGetGroupTitleByText(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));

    }

    @Test
    public void studentCountShouldChangeWhenAddingAndRemovingStudents() {
        // Обычный логин + создание группы
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateGroupModalWindow();
        // Добавление студентов
        int studentsCount = 3;
        mainPage.clickAddStudentsIconOnGroupWithTitle(groupTestName);
        mainPage.typeAmountOfStudentsInCreateStudentsForm(studentsCount);
        mainPage.clickSaveButtonOnCreateStudentsForm();
        mainPage.closeCreateStudentsModalWindow();
        mainPage.waitStudentsCount(groupTestName, studentsCount);
        mainPage.clickZoomInIconOnGroupWithTitle(groupTestName);
        // Проверка переходов статуса первого студента из таблицы
        String firstGeneratedStudentName = mainPage.getStudentNameByIndex(1);
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("block", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
    }

    @Test
    void testFullNameOnProfilePage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        assertEquals(FULL_NAME, profilePage.getFullNameFromAdditionalInfo());
        assertEquals(FULL_NAME, profilePage.getFullNameFromAvatarSection());
    }

    @Test
    void shouldEditBirthdateSuccessfully() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        String newDatebirth = "20.03.2000";
        assertEquals(newDatebirth, profilePage.getDateOfbirthFromAdditionalInfo());
    }

    @AfterEach
    void tearDownTest() {

        WebDriverRunner.closeWebDriver();
    }
}
