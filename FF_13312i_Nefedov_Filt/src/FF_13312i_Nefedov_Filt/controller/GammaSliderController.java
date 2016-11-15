package FF_13312i_Nefedov_Filt.controller;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public class GammaSliderController {
    private Controller controller;

    public GammaSliderController(Controller controller) {
        this.controller = controller;
    }

    public ChangeListener createChangeListener(JOptionPane parent, JSlider slider) {
        ChangeListener listener;

        listener = event -> {
            JSlider sourceSlider = (JSlider) event.getSource();
            int value = 0;

            if ((value = sourceSlider.getValue()) != 0) {
                controller.setGamma((double) value / 10.);
                controller.doGammaCorrection();
            }

            parent.setMessage(new Object[] {"Select a gamma: ", slider, "Current gamma: " + controller.getGamma()});
        };

        return listener;
    }
}
