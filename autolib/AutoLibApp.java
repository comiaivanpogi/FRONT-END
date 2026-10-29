package autolib;

import javax.swing.*;

/**
 * Entry point for AutoLib.
 * Run: javac -d out -sourcepath src src/autolib/AutoLibApp.java
 * java -cp out autolib.AutoLibApp
 */
public class AutoLibApp {
    public static void main(String[] args) {
        // Enable sub-pixel font antialiasing on Windows
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Use cross-platform L&F (consistent across Windows/Linux/Mac)
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new PortalScreen().setVisible(true));
    }
}
