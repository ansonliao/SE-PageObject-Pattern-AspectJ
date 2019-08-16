package example;


import java.util.stream.Stream;

public class MyApp {

    public void sayHi() {
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {
        Stream.of(args).forEach(System.out::println);
        MyApp myApp = new MyApp();
        myApp.sayHi();
    }

}
