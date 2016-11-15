package FF_13312i_Nefedov_Filt.InputFilters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class OffsetFilter extends DocumentFilter {

    private boolean isValid(String testText) {
        int value;

        if (testText.isEmpty()) {
            return true;
        }

        if (!testText.matches("(-)?\\d*"))
            return false;

        try {
            if (testText.charAt(0) != '-') {
                value = Integer.parseInt(testText.trim());

                if (value < -255 || value > 255)
                    return false;
            }
            else if (testText.charAt(0) == '-' && testText.length() > 1) {
                value = Integer.parseInt(testText.trim());

                if (value < -255 || value > 255)
                    return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
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
