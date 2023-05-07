package br.com.dv.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class TextEditor extends JFrame {

    private JTextField textField;
    private JTextArea textArea;

    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setName("MainPanel");
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createTextAreaScrollPane(), BorderLayout.CENTER);
        setMargin(mainPanel, 5, 5, 5, 5);
        add(mainPanel);
        setJMenuBar(createMenuBar());
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        textField = createTextField();
        topPanel.add(textField);

        List<JButton> buttons = createButtons();
        buttons.forEach(topPanel::add);

        return topPanel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setName("FilenameField");
        forceSize(textField, 250, 25);
        return textField;
    }

    private List<JButton> createButtons() {
        JButton saveButton = createButton(
                getIconURL("save_file_icon.jpg"),
                "SaveButton",
                e -> saveFile()
        );
        JButton loadButton = createButton(
                getIconURL("load_file_icon.jpg"),
                "LoadButton",
                e -> loadFile()
        );
        return List.of(saveButton, loadButton);
    }

    private JButton createButton(URL iconURL, String name, ActionListener actionListener) {
        JButton button = new JButton(new ImageIcon(iconURL));
        configureButton(button, name, actionListener);
        return button;
    }

    private URL getIconURL(String fileName) {
        URL iconURL = getClass().getClassLoader().getResource(fileName);
        if (iconURL == null) {
            throw new RuntimeException("Resource not found: " + fileName);
        }
        return iconURL;
    }

    private void configureButton(JButton button, String name, ActionListener actionListener) {
        button.setName(name);
        button.addActionListener(actionListener);
        forceSize(button, 25, 25);
    }

    private JScrollPane createTextAreaScrollPane() {
        textArea = new JTextArea();
        textArea.setName("TextArea");

        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setName("ScrollPane");
        setMargin(textAreaScrollPane, 10, 10, 10, 10);
        forceSize(textAreaScrollPane, 500, 500);

        return textAreaScrollPane;
    }

    private JMenuBar createMenuBar() {
        JMenu menu = createMenu();
        List<JMenuItem> menuItems = createMenuItems();
        menuItems.forEach(menu::add);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        return menuBar;
    }

    private JMenu createMenu() {
        JMenu menu = new JMenu("File");
        menu.setName("MenuFile");
        menu.setMnemonic(KeyEvent.VK_F);
        return menu;
    }

    private List<JMenuItem> createMenuItems() {
        JMenuItem loadMenuItem = createMenuItem("Load", "MenuLoad", e -> loadFile());
        JMenuItem saveMenuItem = createMenuItem("Save", "MenuSave", e -> saveFile());
        JMenuItem exitMenuItem = createMenuItem("Exit", "MenuExit", e -> dispose());
        return List.of(loadMenuItem, saveMenuItem, exitMenuItem);
    }

    private JMenuItem createMenuItem(String text, String name, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        configureMenuItem(menuItem, name, actionListener);
        return menuItem;
    }

    private void configureMenuItem(JMenuItem menuItem, String name, ActionListener actionListener) {
        menuItem.setName(name);
        menuItem.addActionListener(actionListener);
    }

    private void saveFile() {
        String fileName = textField.getText();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(TextEditor.this, "Please enter a file name.");
            return;
        }
        try {
            Files.writeString(Paths.get("src", fileName), textArea.getText());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(TextEditor.this,
                    "Error saving file: " + ex.getMessage());
        }
    }

    private void loadFile() {
        String fileName = textField.getText();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(TextEditor.this, "Please enter a file name.");
            return;
        }
        try {
            textArea.setText(Files.readString(Paths.get("src", fileName), StandardCharsets.UTF_8));
        } catch (NoSuchFileException ex) {
            textArea.setText("");
            JOptionPane.showMessageDialog(TextEditor.this, "File not found: " + fileName);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(TextEditor.this,
                    "Error loading file: " + ex.getMessage());
        }
    }

    private void setMargin(JComponent aComponent, int aTop, int aRight, int aBottom, int aLeft) {
        Border border = aComponent.getBorder();
        Border marginBorder = new EmptyBorder(new Insets(aTop, aLeft, aBottom, aRight));
        aComponent.setBorder(border == null ? marginBorder : new CompoundBorder(marginBorder, border));
    }

    private void forceSize(JComponent component, int width, int height) {
        Dimension d = new Dimension(width, height);
        component.setMinimumSize(d);
        component.setMaximumSize(d);
        component.setPreferredSize(d);
    }

}
