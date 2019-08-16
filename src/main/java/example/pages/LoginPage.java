package example.pages;

import com.github.ansonliao.selenium.annotations.PageName;
import example.internal.CommonSeleniumActions;
import example.objects.LoginPO;
import example.utils.PageUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

@PageName("Login Page")
public class LoginPage extends CommonSeleniumActions {
    LoginPO po = new LoginPO();

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new HtmlElementDecorator(
                new HtmlElementLocatorFactory(driver)), po);

        PageUtils.waitForPageLoaded(driver);
    }

    @Step("Input user name with: {0}")
    public LoginPage inputUserName(String value) {
        clearUserName();
        type(po.userNameInput, value);
        return this;
    }

    @Step("Clear user name")
    public LoginPage clearUserName() {
        clearText(po.userNameInput);
        return this;
    }

    @Step("Input password with: {0}")
    public LoginPage inputPassword(String value) {
        clearPassword();
        type(po.passwordInput, value);
        return this;
    }

    @Step("Clear password")
    public LoginPage clearPassword() {
        clearText(po.passwordInput);
        return this;
    }

    @Step("Click login button for switching to secure page")
    public SecurePage clickLoginButton() {
        click(po.loginBtn);
        return new SecurePage(getDriver());
    }


}
