package br.com.dv.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JTextField searchField;
    private JCheckBox regexCheckBox;
    private final FileHandler fileHandler;
    private final SearchHandler searchHandler;

    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        fileHandler = new FileHandler(textArea);
        searchHandler = new SearchHandler(searchField, textArea, regexCheckBox);
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
        Utils.setMargin(mainPanel, 5, 5, 5, 5);
        return mainPanel;
    }

    private JPanel createTopPanel() {
        List<JButton> buttons = createButtons();
        searchField = createSearchField();
        regexCheckBox = createRegexCheckBox();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttons.subList(0, 2).forEach(topPanel::add);
        topPanel.add(searchField);
        buttons.subList(2, 5).forEach(topPanel::add);
        topPanel.add(regexCheckBox);

        Utils.setMargin(topPanel, 5, 5, 5, 5);

        return topPanel;
    }

    private List<JButton> createButtons() {
        JButton saveButton = createButton(
                getIconURL("save_file_icon.jpg"),
                "SaveButton",
                e -> fileHandler.saveFile()
        );
        JButton openButton = createButton(
                getIconURL("open_file_icon.jpg"),
                "OpenButton",
                e -> fileHandler.openFile()
        );
        JButton startSearchButton = createButton(
                getIconURL("search_icon.jpg"),
                "StartSearchButton",
                e -> searchHandler.startSearch()
        );
        JButton previousMatchButton = createButton(
                getIconURL("previous_icon.jpg"),
                "PreviousMatchButton",
                e -> searchHandler.goToPreviousMatch()
        );
        JButton nextMatchButton = createButton(
                getIconURL("next_icon.jpg"),
                "NextMatchButton",
                e -> searchHandler.goToNextMatch()
        );

        return List.of(
                saveButton,
                openButton,
                startSearchButton,
                previousMatchButton,
                nextMatchButton
        );
    }

    private JButton createButton(URL iconURL, String name, ActionListener actionListener) {
        JButton button = new JButton(new ImageIcon(iconURL));
        configureButton(button, name, actionListener);
        return button;
    }

    private void configureButton(JButton button, String name, ActionListener actionListener) {
        button.setName(name);
        button.addActionListener(actionListener);
        Utils.forceSize(button, 25, 25);
    }

    private URL getIconURL(String fileName) {
        try {
            return new URL("file:src/main/resources/" + fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JTextField createSearchField() {
        JTextField textField = new JTextField();
        textField.setName("SearchField");
        Utils.forceSize(textField, 200, 25);
        return textField;
    }

    private JCheckBox createRegexCheckBox() {
        JCheckBox checkBox = new JCheckBox("Use regex");
        checkBox.setName("UseRegExCheckbox");
        return checkBox;
    }

    private JScrollPane createTextAreaScrollPane() {
        textArea = createTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setName("ScrollPane");
        Utils.setMargin(textAreaScrollPane, 10, 10, 10, 10);
        Utils.forceSize(textAreaScrollPane, 500, 500);
        return textAreaScrollPane;
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        return textArea;
    }

    private JMenuBar createMenuBar() {
        JMenu fileMenu = createMenu("File", "MenuFile", KeyEvent.VK_F);
        List<JComponent> fileMenuItems = createFileMenuItems();
        fileMenuItems.forEach(fileMenu::add);

        JMenu searchMenu = createMenu("Search", "MenuSearch", KeyEvent.VK_S);
        List<JComponent> searchMenuItems = createSearchMenuItems();
        searchMenuItems.forEach(searchMenu::add);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);

        return menuBar;
    }

    private JMenu createMenu(String text, String name, int mnemonic) {
        JMenu menu = new JMenu(text);
        menu.setName(name);
        menu.setMnemonic(mnemonic);
        return menu;
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

    private List<JComponent> createFileMenuItems() {
        JMenuItem openMenuItem = createMenuItem("Open", "MenuOpen", e -> fileHandler.openFile());
        JMenuItem saveMenuItem = createMenuItem("Save", "MenuSave", e -> fileHandler.saveFile());
        JSeparator separator = new JSeparator();
        JMenuItem exitMenuItem = createMenuItem("Exit", "MenuExit", e -> dispose());

        return List.of(
                openMenuItem,
                saveMenuItem,
                separator,
                exitMenuItem
        );
    }

    private List<JComponent> createSearchMenuItems() {
        JMenuItem startSearchMenuItem = createMenuItem("Start search", "MenuStartSearch",
                e -> searchHandler.startSearch());
        JMenuItem previousMatchMenuItem = createMenuItem("Previous match", "MenuPreviousMatch",
                e -> searchHandler.goToPreviousMatch());
        JMenuItem nextMatchMenuItem = createMenuItem("Next match", "MenuNextMatch",
                e -> searchHandler.goToNextMatch());
        JMenuItem useRegexMenuItem = createMenuItem("Use regular expressions", "MenuUseRegExp",
                e -> searchHandler.toggleRegexCheckbox());

        return List.of(
                startSearchMenuItem,
                previousMatchMenuItem,
                nextMatchMenuItem,
                useRegexMenuItem
        );
    }

}
