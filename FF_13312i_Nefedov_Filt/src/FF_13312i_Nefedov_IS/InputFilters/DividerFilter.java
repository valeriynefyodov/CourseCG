package FF_13312i_Nefedov_IS.InputFilters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DividerFilter extends DocumentFilter {

    private boolean isValid(String testText) {
        int value = 0;

        if (testText.isEmpty()) {
            return true;
        }

        try {
            value = Integer.parseInt(testText.trim());
        } catch (NumberFormatException e) {
            return false;
        }

        if (value <= 0 || value > 9999) {
            return false;
        }

        return true;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder();

        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, text);

        if (isValid(sb.toString())) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int end = offset + length;
        StringBuilder sb = new StringBuilder();

        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, end, text);

        if (isValid(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        int end = offset + length;
        StringBuilder sb = new StringBuilder();

        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.delete(offset, end);

        if (isValid(sb.toString())) {
            super.remove(fb, offset, length);
        }
    }
}
