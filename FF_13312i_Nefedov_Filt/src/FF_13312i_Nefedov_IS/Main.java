package FF_13312i_Nefedov_IS;

import FF_13312i_Nefedov_IS.controller.Controller;

/**
 * The main class of an application
 */
public class Main {

    /**
     * Initializes the app: creates controller and main frame of the app
     */
    private static void initApp(){
        Controller controller = new Controller();
        controller.mainView.setVisible(true);
    }

    /**
     * Main function
     * Runs the app
     * @param args - main function's arguments
     */
    public static void main(String[] args){
        initApp();
    }

}
