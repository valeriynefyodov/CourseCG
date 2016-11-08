package FF_13312i_Nefedov_IS.controller;

import FF_13312i_Nefedov_IS.view.View;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {

    public View view;
    public boolean showFilters;
    public boolean isSplitted;

    public Controller() {
        this.view = new View(this, 640, 480);
        this.showFilters = false;
    }


    /**
     * Loads an information about the program and shows it
     */
    /*
    public void showAbout() {
        File about = new File("FF_13312i_Nefedov_IS_Data/About.txt");
        String fileLine;
        DefaultListModel aboutText = new DefaultListModel();

        if (about != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(about));

                while((fileLine = reader.readLine()) != null) {
                    aboutText.addElement(fileLine + "\n");
                }

                view.showScrollableMessageDialog(aboutText, "About");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}