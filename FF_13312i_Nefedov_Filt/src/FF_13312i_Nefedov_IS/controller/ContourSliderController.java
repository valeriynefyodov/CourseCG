package FF_13312i_Nefedov_IS.controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContourSliderController {
    Controller controller;

    public ContourSliderController(Controller controller) {
        this.controller = controller;
    }

    public ChangeListener createChangeListener(JOptionPane parent, JSlider slider) {
        ChangeListener listener;

        listener = event -> {
            JSlider sourceSlider = (JSlider) event.getSource();

            controller.setContourLevel(sourceSlider.getValue());
            controller.doContourSelection();

            parent.setMessage(new Object[] {"Select a level: ", slider, "Current level: " + controller.getContourLevel()});
        };

        return listener;
    }
}
