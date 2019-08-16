package example.Interrupt;

import example.aspect.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Pause {
    private static Logger logger = LoggerFactory.getLogger(Pause.class);

    public static void bySecond(int time) {
        byMillisecond(time * 1000);
    }

    public static void bySecondWithNoLog(int time) {
        byMillisecondWithNoLog(time * 1000);
    }

    @Trace
    public static void byMillisecond(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void byMillisecondWithNoLog(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
