package br.com.dv.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JTextField searchField;
    private JCheckBox regexCheckBox;
    private final JFileChooser fileChooser;
    private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    private Pattern pattern;
    private Matcher matcher;
    private final List<Integer> matchStartIndices = new ArrayList<>();
    private final List<Integer> matchEndIndices = new ArrayList<>();
    private int currentMatchIndex = -1;

    public TextEditor() {
        super("Text Editor");
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setName("FileChooser");
        add(fileChooser);
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
        searchField = createSearchField();
        regexCheckBox = createRegexCheckBox();

        buttons.subList(0, 2).forEach(topPanel::add);
        topPanel.add(searchField);
        buttons.subList(2, 5).forEach(topPanel::add);
        topPanel.add(regexCheckBox);

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
        JButton startSearchButton = createButton(
                getIconURL("search_icon.jpg"),
                "StartSearchButton",
                e -> startSearch()
        );
        JButton previousMatchButton = createButton(
                getIconURL("previous_icon.jpg"),
                "PreviousMatchButton",
                e -> goToPreviousMatch()
        );
        JButton nextMatchButton = createButton(
                getIconURL("next_icon.jpg"),
                "NextMatchButton",
                e -> goToNextMatch()
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
        forceSize(button, 25, 25);
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
        forceSize(textField, 200, 25);
        return textField;
    }

    private JCheckBox createRegexCheckBox() {
        JCheckBox checkBox = new JCheckBox("Use regex");
        checkBox.setName("UseRegExCheckbox");
        forceSize(checkBox, 100, 25);
        return checkBox;
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
        JMenuItem openMenuItem = createMenuItem("Open", "MenuOpen", e -> openFile());
        JMenuItem saveMenuItem = createMenuItem("Save", "MenuSave", e -> saveFile());
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
                e -> startSearch());
        JMenuItem previousMatchMenuItem = createMenuItem("Previous match", "MenuPreviousMatch",
                e -> goToPreviousMatch());
        JMenuItem nextMatchMenuItem = createMenuItem("Next match", "MenuNextMatch",
                e -> goToNextMatch());
        JMenuItem useRegexMenuItem = createMenuItem("Use regular expressions", "MenuUseRegExp",
                e -> toggleUseRegExp());

        return List.of(
                startSearchMenuItem,
                previousMatchMenuItem,
                nextMatchMenuItem,
                useRegexMenuItem
        );
    }

    private void saveFile() {
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setControlButtonsAreShown(true);
        fileChooser.setSelectedFile(null); // Reset the selected file
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(fileChooser.getSelectedFile().toPath(), textArea.getText(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not save file");
            }
        }
    }

    private void openFile() {
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setControlButtonsAreShown(true);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String content = Files.readString(fileChooser.getSelectedFile().toPath(), StandardCharsets.UTF_8);
                textArea.setText(content);
            } catch (IOException ex) {
                textArea.setText("");
                JOptionPane.showMessageDialog(this, "Could not open file");
            }
        }
        fileChooser.setControlButtonsAreShown(false);
    }

    private void startSearch() {
        new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String searchText = searchField.getText();
                String textAreaText = textArea.getText();

                matchStartIndices.clear();
                matchEndIndices.clear();
                currentMatchIndex = -1;

                if (regexCheckBox.isSelected()) {
                    pattern = Pattern.compile(searchText);
                    matcher = pattern.matcher(textAreaText);
                } else {
                    matcher = Pattern.compile(Pattern.quote(searchText)).matcher(textAreaText);
                }

                while (matcher.find()) {
                    matchStartIndices.add(matcher.start());
                    matchEndIndices.add(matcher.end());
                }

                return null;
            }

            @Override
            protected void done() {
                if (!matchStartIndices.isEmpty()) {
                    goToNextMatch();
                }
            }
        }.execute();
    }

    private void highlightMatch(int start, int end) {
        try {
            textArea.getHighlighter().removeAllHighlights();
            textArea.getHighlighter().addHighlight(start, end, painter);
            textArea.setSelectionStart(start);
            textArea.setSelectionEnd(end);
            textArea.grabFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goToNextMatch() {
        if (currentMatchIndex < matchStartIndices.size() - 1) {
            currentMatchIndex++;
            highlightMatch(matchStartIndices.get(currentMatchIndex), matchEndIndices.get(currentMatchIndex));
        }
    }

    private void goToPreviousMatch() {
        if (currentMatchIndex > 0) {
            currentMatchIndex--;
            highlightMatch(matchStartIndices.get(currentMatchIndex), matchEndIndices.get(currentMatchIndex));
        }
    }

    public void toggleUseRegExp() {
        regexCheckBox.setSelected(!regexCheckBox.isSelected());
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
