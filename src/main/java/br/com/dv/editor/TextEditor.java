package br.com.dv.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

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
        textField = createTextField();
        JButton saveButton = createSaveButton();
        JButton loadButton = createLoadButton();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(textField);
        topPanel.add(saveButton);
        topPanel.add(loadButton);

        return topPanel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setName("FilenameField");
        forceSize(textField, 250, 25);
        return textField;
    }

    private JButton createSaveButton() {
        URL iconURL = getClass().getClassLoader().getResource("save_file_icon.jpg");
        if (iconURL == null) {
            throw new RuntimeException("Resource not found: save_file_icon.jpg");
        }
        JButton saveButton = new JButton(new ImageIcon(iconURL));
        saveButton.setName("SaveButton");
        forceSize(saveButton, 30, 30);
        saveButton.addActionListener(e -> saveFile());
        return saveButton;
    }

    private JButton createLoadButton() {
        URL iconURL = getClass().getClassLoader().getResource("load_file_icon.jpg");
        if (iconURL == null) {
            throw new RuntimeException("Resource not found: load_file_icon.jpg");
        }
        JButton saveButton = new JButton(new ImageIcon(iconURL));
        saveButton.setName("SaveButton");
        forceSize(saveButton, 30, 30);
        saveButton.addActionListener(e -> saveFile());
        return saveButton;
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
        JMenu menu = new JMenu("File");
        menu.setName("MenuFile");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem loadMenuItem = createMenuItem("Load", "MenuLoad", "load");
        JMenuItem saveMenuItem = createMenuItem("Save", "MenuSave", "save");
        JMenuItem exitMenuItem = createMenuItem("Exit", "MenuExit", "exit");

        menu.add(loadMenuItem);
        menu.add(saveMenuItem);
        menu.add(new JSeparator());
        menu.add(exitMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        return menuBar;
    }

    private JMenuItem createMenuItem(String text, String name, String action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName(name);
        menuItem.addActionListener(e -> {
            switch (action) {
                case "load" -> loadFile();
                case "save" -> saveFile();
                case "exit" -> dispose();
            }
        });

        return menuItem;
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
