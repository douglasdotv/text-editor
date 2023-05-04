package br.com.dv.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TextEditor extends JFrame {

    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JTextField textField = new JTextField();
        textField.setName("FilenameField");
        forceSize(textField, 250, 25);

        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setName("ScrollPane");
        setMargin(textAreaScrollPane, 10, 10, 10, 10);
        forceSize(textAreaScrollPane, 500, 500);

        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        forceSize(saveButton, 75, 25);

        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        forceSize(loadButton, 75, 25);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(textField);
        topPanel.add(saveButton);
        topPanel.add(loadButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setName("MainPanel");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(textAreaScrollPane, BorderLayout.CENTER);
        setMargin(mainPanel, 5, 5, 5, 5);

        add(mainPanel);
    }

    private void setMargin(JComponent aComponent, int aTop, int aRight, int aBottom, int aLeft) {
        Border border = aComponent.getBorder();
        Border marginBorder = new EmptyBorder(new Insets(aTop, aLeft, aBottom, aRight));
        aComponent.setBorder(border == null ? marginBorder : new CompoundBorder(marginBorder, border));
    }

    public void forceSize(JComponent component, int width, int height) {
        Dimension d = new Dimension(width, height);
        component.setMinimumSize(d);
        component.setMaximumSize(d);
        component.setPreferredSize(d);
    }

}
