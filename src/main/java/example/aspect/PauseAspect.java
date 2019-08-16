package example.aspect;

import com.aventstack.extentreports.Status;
import com.github.ansonliao.selenium.report.factory.ExtentTestManager;
import com.google.common.collect.Lists;
import example.utils.StrUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class PauseAspect {

    @Pointcut("within(example.Interrupt.Pause)")
    public void pointcut() {

    }

    @Pointcut("@annotation(Trace)")
    public void tracePointcut() {

    }

    @Pointcut("execution(* example.Interrupt.Pause.byMillisecond(..))")
    public void exactlyPointCut() {

    }

    @After("exactlyPointCut() && tracePointcut()")
    public void afterAdvice(JoinPoint joinPoint) {
        List<Object> argList = Lists.newArrayList(joinPoint.getArgs());
        String log = StrUtils.withBoldHTML("Action: ").concat("Pause");
        if (argList.size() > 0) {
            log = log.concat(", ").concat(StrUtils.withBoldHTML("Duration: "))
                    .concat(argList.get(0).toString()).concat(" ms");
        }
        // log step details to extent reports
        ExtentTestManager.getExtentTest().log(Status.INFO, log);
    }
}
