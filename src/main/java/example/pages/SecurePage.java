package example.pages;

import com.github.ansonliao.selenium.annotations.PageName;
import example.internal.CommonSeleniumActions;
import example.objects.SecurePO;
import example.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

@PageName("Secure Page")
public class SecurePage extends CommonSeleniumActions {
    public SecurePO po = new SecurePO();

    public SecurePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new HtmlElementDecorator(
                new HtmlElementLocatorFactory(driver)), po);
        PageUtils.waitForPageLoaded(driver);
    }

    public LoginPage clickLogoutButton() {
        click(po.logoutBtn);
        return new LoginPage(getDriver());
    }
}
