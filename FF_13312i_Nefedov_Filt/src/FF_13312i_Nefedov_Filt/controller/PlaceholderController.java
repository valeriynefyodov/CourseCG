package FF_13312i_Nefedov_Filt.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderController {

    public FocusListener createPlaceholderListener(JTextField field, String placeholder) {
        FocusListener listener;

        listener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setText("");
                field.setForeground(new Color(50, 50, 50));
            }

            @Override
            public void focusLost(FocusEvent e) {
               if (field.getText().length() == 0) {
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
               }
            }
        };

        return listener;
    }
}
