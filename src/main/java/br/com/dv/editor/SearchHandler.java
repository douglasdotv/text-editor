package br.com.dv.editor;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchHandler {

    private final JTextArea textArea;
    private final JTextField searchField;
    private final JCheckBox regexCheckBox;
    private Pattern pattern;
    private Matcher matcher;
    private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
            new Color(200, 200, 200, 200));
    private final List<Integer> matchStartIndices = new ArrayList<>();
    private final List<Integer> matchEndIndices = new ArrayList<>();
    private int currentMatchIndex = -1;

    public SearchHandler(JTextField searchField, JTextArea textArea, JCheckBox regexCheckBox) {
        this.searchField = searchField;
        this.textArea = textArea;
        this.regexCheckBox = regexCheckBox;
    }

    public void startSearch() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<Integer> matchStarts;
            List<Integer> matchEnds;

            @Override
            protected Void doInBackground() {
                String searchText = searchField.getText();
                String textAreaText = textArea.getText();

                matchStarts = new ArrayList<>();
                matchEnds = new ArrayList<>();

                if (regexCheckBox.isSelected()) {
                    pattern = Pattern.compile(searchText);
                    matcher = pattern.matcher(textAreaText);
                } else {
                    matcher = Pattern.compile(Pattern.quote(searchText)).matcher(textAreaText);
                }

                while (matcher.find()) {
                    matchStarts.add(matcher.start());
                    matchEnds.add(matcher.end());
                }

                return null;
            }

            @Override
            protected void done() {
                matchStartIndices.clear();
                matchEndIndices.clear();
                currentMatchIndex = -1;

                matchStartIndices.addAll(matchStarts);
                matchEndIndices.addAll(matchEnds);

                if (!matchStartIndices.isEmpty()) {
                    goToNextMatch();
                }
            }
        };

        worker.execute();
    }

    public void goToNextMatch() {
        if (currentMatchIndex < matchStartIndices.size() - 1) {
            currentMatchIndex++;
            highlightMatch(matchStartIndices.get(currentMatchIndex), matchEndIndices.get(currentMatchIndex));
        }
    }

    public void goToPreviousMatch() {
        if (currentMatchIndex > 0) {
            currentMatchIndex--;
            highlightMatch(matchStartIndices.get(currentMatchIndex), matchEndIndices.get(currentMatchIndex));
        }
    }

    public void highlightMatch(int start, int end) {
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

    public void toggleRegexCheckbox() {
        regexCheckBox.setSelected(!regexCheckBox.isSelected());
    }

}
