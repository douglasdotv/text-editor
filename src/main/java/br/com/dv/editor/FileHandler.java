package br.com.dv.editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileHandler {

    private final JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    private final JTextArea textArea;
    private int returnValue;

    public FileHandler(JTextArea textArea) {
        this.textArea = textArea;
        fileChooser.setName("FileChooser");
    }

    public void saveFile() {
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setControlButtonsAreShown(true);
        fileChooser.setSelectedFile(null);
        returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(fileChooser.getSelectedFile().toPath(), textArea.getText(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                System.out.println("Error saving file. " + ex.getMessage());
            }
        }
    }

    public void openFile() {
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setControlButtonsAreShown(true);
        returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String content = Files.readString(fileChooser.getSelectedFile().toPath(), StandardCharsets.UTF_8);
                textArea.setText(content);
            } catch (IOException ex) {
                textArea.setText("");
                System.out.println("Error opening file. " + ex.getMessage());
            }
        }
        fileChooser.setControlButtonsAreShown(false);
    }

}
