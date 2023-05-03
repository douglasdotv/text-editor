package br.com.dv.editor;

import javax.swing.*;

public class TextEditor extends JFrame {
    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        initComponents();
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBounds(10, 10, 465, 445);
        add(textArea);
    }
}

