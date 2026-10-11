
/*
 * Program:  MenuInterface.java
 * Course:   CSC372 - Module 3 Critical Thinking
 * Author:   Ayden Hartman 
 * Purpose:  A Swing window with a top menu bar holding four menu items:
 *             1. Show the current date and time in the text box
 *             2. Write the text box contents to "log.txt"
 *             3. Change the background to a random green hue that is
 *                picked once per run and shown in the menu item's label
 *             4. Exit the program
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MenuInterface extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String LOG_FILE_NAME = "log.txt";
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    // Green hues sit roughly between 90 and 150 degrees on the color wheel
    private static final float MIN_GREEN_HUE = 90f / 360f;
    private static final float MAX_GREEN_HUE = 150f / 360f;

    private final JTextArea textArea;
    private final JPanel contentPanel;
    private final Color randomGreen;   // chosen once for this run

    public MenuInterface() {
        super("CSC372 Module 3 - Menu Interface");

        randomGreen = createRandomGreen(new Random());

        // Text box inside a padded panel so the background color shows around it
        textArea = new JTextArea(10, 40);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setContentPane(contentPanel);

        setJMenuBar(buildMenuBar());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    /**
     * Builds the top menu bar with its four menu items and their actions.
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem dateTimeItem = new JMenuItem("1. Show Date and Time");
        dateTimeItem.addActionListener(e -> showDateTime());

        JMenuItem saveLogItem = new JMenuItem("2. Write Text to log.txt");
        saveLogItem.addActionListener(e -> writeTextToLog());

        JMenuItem greenItem = new JMenuItem(
                "3. Green Background (" + toHex(randomGreen) + ")");
        greenItem.addActionListener(e -> applyGreenBackground());

        JMenuItem exitItem = new JMenuItem("4. Exit");
        exitItem.addActionListener(e -> System.exit(0));

        optionsMenu.add(dateTimeItem);
        optionsMenu.add(saveLogItem);
        optionsMenu.add(greenItem);
        optionsMenu.addSeparator();
        optionsMenu.add(exitItem);

        menuBar.add(optionsMenu);
        return menuBar;
    }

    /** Menu item 1: prints the current date and time in the text box. */
    private void showDateTime() {
        String now = LocalDateTime.now().format(DATE_TIME_FORMAT);
        textArea.append("Current date and time: " + now + "\n");
    }

    /** Menu item 2: writes the text box contents to log.txt. */
    private void writeTextToLog() {
        File logFile = new File(LOG_FILE_NAME);

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(textArea.getText());
            JOptionPane.showMessageDialog(this,
                    "Text box contents written to:\n" + logFile.getAbsolutePath(),
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not write to " + LOG_FILE_NAME + ":\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Menu item 3: sets the frame background to this run's random green. */
    private void applyGreenBackground() {
        contentPanel.setBackground(randomGreen);
        contentPanel.repaint();
    }

    /**
     * Creates a random shade of green by picking a hue in the green range
     * and a saturation and brightness high enough to stay clearly green.
     */
    static Color createRandomGreen(Random random) {
        float hue = MIN_GREEN_HUE + random.nextFloat() * (MAX_GREEN_HUE - MIN_GREEN_HUE);
        float saturation = 0.5f + random.nextFloat() * 0.5f;   // 0.5 to 1.0
        float brightness = 0.5f + random.nextFloat() * 0.4f;   // 0.5 to 0.9
        return Color.getHSBColor(hue, saturation, brightness);
    }

    /** Formats a color as an HTML hex code such as #3FA45B. */
    static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuInterface().setVisible(true));
    }
}