package app;

import com.sun.javafx.runtime.VersionInfo;

public class App {

    public static void main(String[] args) {
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println(("JavaFX Version: " + VersionInfo.getVersion()));
    }
}
