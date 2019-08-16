package example.aspect;


import com.github.ansonliao.selenium.annotations.PageName;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class AspectUtils {

    public synchronized static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    public synchronized static Method getPointCutMethod(MethodSignature signature) {
        return signature.getMethod();
    }

    public synchronized static String getCallingClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    public synchronized static String getPageName(Class<?> clazz) {
        return clazz.isAnnotationPresent(PageName.class)
                ? clazz.getAnnotation(PageName.class).value()
                : "";
    }

    public synchronized static String withBoldHTML(String s) {
        return !s.trim().isEmpty() ? "<b>" + s + "</b>" : "";
    }
}
