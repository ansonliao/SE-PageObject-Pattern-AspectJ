package example.aspect;

import com.aventstack.extentreports.Status;
import com.github.ansonliao.selenium.annotations.PageName;
import com.github.ansonliao.selenium.report.factory.ExtentTestManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.WebElement;
import org.testng.util.Strings;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static java.util.stream.Collectors.toList;

@Aspect
public class ExtentReportAspect {

    @Pointcut("within(example.internal.CommonSeleniumActions)")
    public void commonActionPointCut() {

    }

    @Pointcut("@annotation(Trace)")
    public void annotationTracePointCutDefinition() {

    }

    @After("commonActionPointCut() && annotationTracePointCutDefinition()")
    public void commonActionAfterAdvice(JoinPoint joinPoint) throws ClassNotFoundException {
        Class<?> callingClass = Class.forName(getCallingClass(joinPoint));
        String pageName = getPageName(callingClass);
        String methodName = getPointCutMethod(getMethodSignature(joinPoint)).getName();

        String log = "";
        if (Strings.isNotNullAndNotEmpty(pageName)) {
            log = log.concat(withBoldHTML("Page: ")).concat(pageName).concat(", ");
        }
        List<Object> argList = Lists.newArrayList(joinPoint.getArgs());
        if (argList.size() > 0) {
            if (WebElement.class.isAssignableFrom(argList.get(0).getClass())
                    || TypifiedElement.class.isAssignableFrom(argList.get(0).getClass())
                    || HtmlElement.class.isAssignableFrom(argList.get(0).getClass())) {
                log = log.concat(withBoldHTML("Element: ")).concat(argList.get(0).toString()).concat(", ");
                argList.remove(0);
            }
        }

        String action = Joiner.on(" ").join(
                Arrays.asList(LOWER_CAMEL.to(UPPER_UNDERSCORE, methodName).split("_"))
                        .parallelStream()
                        .map(ExtentReportAspect::firstCapitalize)
                        .collect(toList()));
        log = log.concat(withBoldHTML("Action: ")).concat(action);

        for (int i = 0; i < argList.size(); i++) {
            if (i == 0) {
                log = log.concat(", ").concat(withBoldHTML("args: ")).concat("[");
            }
            Object arg = argList.get(i);
            String fieldType = arg.getClass().getSimpleName();
            log = log.concat(withBoldHTML(fieldType)).concat(": ").concat(arg.toString());
            log = (i == argList.size() - 1)
                    ? log.concat("]")
                    : log.concat(", ");
        }

        // log step details to extent report
        ExtentTestManager.getExtentTest().log(Status.INFO, log);
    }

    private MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    private Method getPointCutMethod(MethodSignature signature) {
        return signature.getMethod();
    }

    private synchronized String getCallingClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    private synchronized String getPageName(Class<?> clazz) {
        return clazz.isAnnotationPresent(PageName.class)
                ? clazz.getAnnotation(PageName.class).value()
                : "";
    }

    private synchronized String withBoldHTML(String s) {
        return !s.trim().isEmpty() ? "<b>" + s + "</b>" : "";
    }

    private synchronized static String firstCapitalize(String s) {
        if (com.google.common.base.Strings.isNullOrEmpty(s)) {
            return s;
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
