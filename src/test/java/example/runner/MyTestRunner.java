package example.runner;

import com.github.ansonliao.selenium.testng.TestNGRunner;
import com.github.ansonliao.selenium.testng.XmlSuiteBuilder;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.util.stream.Stream;

public class MyTestRunner {

    // @Test
    // public void run() {
    //     System.setProperty("testing.test.groups", "debug");
    //     TestNGRunner.Run();
    // }

    public static void main(String[] args) {
        Stream.of(args).forEach(System.out::println);
        System.out.println("I am Anson");
        // System.out.println("Hello World");

        // System.setProperty("testing.test.groups", "debug");
        TestNGRunner.Run();
    }

}
