package example.listeners;

import com.automation.remarks.video.annotations.Video;
import com.github.ansonliao.selenium.annotations.Headless;
import com.github.ansonliao.selenium.annotations.Incognito;
import com.github.ansonliao.selenium.annotations.URL;
import com.github.ansonliao.selenium.factory.DriverManager;
import com.github.ansonliao.selenium.factory.DriverManagerFactory;
import com.github.ansonliao.selenium.factory.WDManager;
import com.github.ansonliao.selenium.parallel.SeleniumParallelTestListener;
import com.github.ansonliao.selenium.report.factory.ExtentTestManager;
import com.github.ansonliao.selenium.utils.AuthorUtils;
import com.github.ansonliao.selenium.utils.StringUtils;
import com.github.ansonliao.selenium.utils.TestGroupUtils;
import com.github.ansonliao.selenium.utils.config.SEConfigs;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.ansonliao.selenium.factory.WDManager.getDriver;


public class CustomParallelTestListener extends SeleniumParallelTestListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String browserName = method.getTestMethod().getXmlTest()
                .getParameter(StringUtils.removeQuoteMark(SEConfigs.getConfigInstance().testngXmlBrowserParamKey()));
        Method m = method.getTestMethod().getConstructorOrMethod().getMethod();

        // Update recording video name to the package name + class name + test method name
        if (m.isAnnotationPresent(Video.class)) {
            Video videoAnnotation = m.getAnnotation(Video.class);
            System.out.println("Before modify, the video name: " + videoAnnotation.name());

            InvocationHandler invocationHandler = Proxy.getInvocationHandler(videoAnnotation);
            try {
                Field field = invocationHandler.getClass().getDeclaredField("memberValues");
                field.setAccessible(true);
                Map memberValues = (Map) field.get(invocationHandler);
                String className = method.getTestMethod().getConstructorOrMethod().getDeclaringClass().getCanonicalName();
                String methodName = m.getName();
                memberValues.put("name", String.format("%s_%s_%s_", className, methodName, browserName));
                System.out.println(videoAnnotation.name());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        DriverManager driverManager = DriverManagerFactory.getManager(browserName);
        driverManager.setHeadless(m.isAnnotationPresent(Headless.class));
        driverManager.setIncognito(m.isAnnotationPresent(Incognito.class));
        WDManager.setDriver(driverManager.getDriver());
        List<String> groups = TestGroupUtils.getMethodTestGroups(m);
        if (SEConfigs.getConfigInstance().addBrowserGroupToReport()) {
            groups.add(browserName);
        }

        ExtentTestManager.createTest(
                m, browserName, AuthorUtils.getMethodAuthors(m),
                groups, testResult.getParameters());

        Optional.ofNullable(m.getAnnotation(URL.class))
                .ifPresent(url -> getDriver().get(url.value().trim()));
    }
}
