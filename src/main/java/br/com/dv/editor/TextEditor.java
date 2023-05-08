package br.com.dv.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class TextEditor extends JFrame {

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
        JPanel topPanel = createTopPanel();
        JScrollPane textAreaScrollPane = createTextAreaScrollPane();

        JPanel mainPanel = createMainPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(textAreaScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setJMenuBar(createMenuBar());
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setName("MainPanel");
        setMargin(mainPanel, 5, 5, 5, 5);
        return mainPanel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        List<JButton> buttons = createButtons();
        buttons.forEach(topPanel::add);

        setMargin(topPanel, 5, 5, 5, 5);
        return topPanel;
    }

    private List<JButton> createButtons() {
        JButton saveButton = createButton(
                getIconURL("save_file_icon.jpg"),
                "SaveButton",
                e -> saveFile()
        );
        JButton openButton = createButton(
                getIconURL("open_file_icon.jpg"),
                "OpenButton",
                e -> openFile()
        );
        return List.of(saveButton, openButton);
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
        textArea = createTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setName("ScrollPane");
        setMargin(textAreaScrollPane, 10, 10, 10, 10);
        forceSize(textAreaScrollPane, 500, 500);
        return textAreaScrollPane;
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        return textArea;
    }

    private JMenuBar createMenuBar() {
        JMenu menu = createMenu();
        List<JComponent> menuItems = createMenuItems();
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

    private List<JComponent> createMenuItems() {
        JMenuItem openMenuItem = createMenuItem("Open", "MenuOpen", e -> openFile());
        JMenuItem saveMenuItem = createMenuItem("Save", "MenuSave", e -> saveFile());
        JSeparator separator = new JSeparator();
        JMenuItem exitMenuItem = createMenuItem("Exit", "MenuExit", e -> dispose());
        return List.of(openMenuItem, saveMenuItem, separator, exitMenuItem);
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
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showSaveDialog(TextEditor.this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(jfc.getSelectedFile().toPath(), textArea.getText(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(TextEditor.this,
                        "Error saving file: " + ex.getMessage());
            }
        }
    }

    private void openFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(TextEditor.this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String content = Files.readString(jfc.getSelectedFile().toPath(), StandardCharsets.UTF_8);
                textArea.setText(content);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(TextEditor.this,
                        "Error opening file: " + ex.getMessage());
            }
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
