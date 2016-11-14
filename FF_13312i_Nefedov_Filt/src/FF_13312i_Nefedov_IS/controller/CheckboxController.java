package FF_13312i_Nefedov_IS.controller;


import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CheckboxController {
    Controller controller;

    public CheckboxController(Controller controller) {
        this.controller = controller;
    }

    public ItemListener createCheckboxListener() {
        ItemListener listener = event -> {
            JCheckBox checkBox = (JCheckBox) event.getSource();

            if (checkBox.isSelected() && controller.mainView.matrixDivider.isEditable())
                controller.mainView.matrixDivider.setEditable(false);
            else
                controller.mainView.matrixDivider.setEditable(true);

        };

        return listener;
    }
}