
// Ajoutez les directives 'import' ici.

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out an instance of  <b>BoardView</b> (the actual game) and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Walid Bounouar
 */

public class GameView extends JFrame {

    // Ajoutez vos variables d'instance ici.

    
    /**
     * Reference to the model of the game
     */
    private GameModel model;
    
    /**
     * Reference to the controller of the game
     */
    private GameController controller;
    
    /**
     * A reference to a JLabel that will show the number of steps
     */
    private JLabel counter;
    
    /**
     * A reference to a the JButton undo
     */
    private JButton undo;
    
    /**
     * A reference to a the JButton redo
     */
    private JButton redo;
    
    /**
     * The board is a 2D array of DotButtons
     */
    private DotButton[][] iconBoard;
    
    

    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {
        super("FloodIt!");//we call the JFrame constructor, with the title.
        
        this.model = model;
        this.controller = gameController;
        this.iconBoard = new DotButton[this.model.getSize()][this.model.getSize()];

        // Ajoutez votre code ici.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        JPanel grid = new JPanel();//1st component, the dots
        grid.setBackground(Color.WHITE);
        //set the layout as a grid
        grid.setLayout(new GridLayout(this.model.getSize(), this.model.getSize()));
        
        //We determine the size of the dots
        //the sizes are static in the DotButton class
        int buttonSize;
        if (this.model.getSize() <= 25) {
            buttonSize = DotButton.MEDIUM_SIZE;
        } else {
            buttonSize = DotButton.SMALL_SIZE;
        }
        
        
        for (int i = 0; i < this.model.getSize(); i++) {
            for (int j = 0; j < this.model.getSize(); j++) {

                DotButton button;
                button = new DotButton(i, j, this.model.getColor(j, i)
                        , buttonSize);
                button.addActionListener(this.controller);
                this.iconBoard[j][i] = button;

                grid.add(button);

            }
        }

        //empty borders to create space, esthetic
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //we place the grid at the center of the view.
        this.add(grid, BorderLayout.CENTER);
        
        //A panel for reset/quit options and the counter
        JPanel options = new JPanel();
        options.setBackground(Color.WHITE);
        options.setLayout(new FlowLayout());
       
        //the content of the lower panel
        this.counter = new JLabel("Select initial dot");
        options.add(this.counter);
        
        JButton reset = new JButton("Reset");
        reset.setBackground(Color.WHITE);
        reset.setPreferredSize(new Dimension(70, 27));
        //A ligth gray border of width 2 with rounded borders
        //The goal was to achieve a look similar to the solution
        reset.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
        reset.addActionListener(this.controller);
        options.add(reset);
        
        JButton quit = new JButton("Quit");
        quit.setBackground(Color.WHITE);
        quit.setPreferredSize(new Dimension(70, 27));
        quit.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));        
        quit.addActionListener(this.controller);
        options.add(quit);
        //End of what the lower panel contains
        
        this.add(options, BorderLayout.SOUTH);
        
        //Below is the creation of the top part of the vues(undo, redo, settings)
        JPanel northOptions = new JPanel();
        northOptions.setBackground(Color.WHITE);
        northOptions.setLayout(new FlowLayout());
        
        this.undo = new JButton("Undo");
        this.undo.setBackground(Color.WHITE);
        this.undo.setPreferredSize(new Dimension(70, 27));
        this.undo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));        
        this.undo.addActionListener(this.controller);
        //If the button is enable depends on the state of the undoStack
        this.undo.setEnabled(!this.model.getUndoStack().isEmpty());
        northOptions.add(this.undo);
        
        this.redo = new JButton("Redo");
        this.redo.setBackground(Color.WHITE);
        this.redo.setPreferredSize(new Dimension(70, 27));
        this.redo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));        
        this.redo.addActionListener(this.controller);
        //If the button is enable depends on the state of the redoStack
        this.redo.setEnabled(!this.model.getRedoStack().isEmpty());
        northOptions.add(this.redo);
        
        JButton settings = new JButton("Settings");
        settings.setBackground(Color.WHITE);
        settings.setPreferredSize(new Dimension(70, 27));
        settings.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));  
        //the JoptionPane is created in a private method lower in this class
        settings.addActionListener(this.controller);
        northOptions.add(settings);      

        
        this.add(northOptions, BorderLayout.NORTH);
        
        this.pack();
        this.setVisible(true);

    }

    /**
     * Updated the view to the state of the model given in argument.
     * 
     * @param newModel 
     *                 a new model to replace the old one.
     */
    public void update(GameModel newModel) {
        
        //we change the model
        this.model = newModel;

        // Ajoutez votre code ici.
        for (int i = 0; i < this.model.getSize(); i++) { //here, i --> x
            for (int j = 0; j < this.model.getSize(); j++) { // j --> y

                int color = this.model.getColor(i, j); //takes the color of the model
                this.iconBoard[i][j].setColor(color); //changes the color of the view

            }
        }
        
        if (this.model.getNumberOfSteps() == -1) {
            this.counter.setText("Select initial dot");
        } else {
            this.counter.setText("Number Of Steps : " + this.model.getNumberOfSteps());
        }
        
        this.undo.setEnabled(!this.model.getUndoStack().isEmpty());
        this.redo.setEnabled(!this.model.getRedoStack().isEmpty());
    }
    
    //Methods for the dialog boxes
    
    /**
     * Creates dialog box for the end game and acts depending on the user choice
	 * Note to corrector: This method is public in the view because the
         * controller needs to access this method and I thought that it 
         * belonged here since it's heavily graphical. I could have made a 
         * private method in the controller, but chose this approach.
     */
    public void showDialogBox() {
        String message = "Congratulations you won in " 
                + this.model.getNumberOfSteps() + " steps !" + "\n" 
                + "Would you like to play again?";
        
        Object[] options = {"Yes", "Quit"}; //the possible options
        
        //add a personnalized icon, to look like solution
        //creates java logo
        Image image = new ImageIcon("data/javaIcon.png").getImage();
        //change the size
        Image scaledImage = image.getScaledInstance(40, 40, 0);
        
        int dialogAnswer = JOptionPane.showOptionDialog(this, //the compenent where the box will appear
            message, //the message to be shown
            "Won", //title of the window
            JOptionPane.YES_NO_OPTION, //types of options
            JOptionPane.QUESTION_MESSAGE, //type of message
            new ImageIcon(scaledImage), //the icon
            options, //the possible options
            null); //default value not important
        
        if (dialogAnswer == 1) { // 1 --> quit
            System.exit(0);
        } else { // 0 --> yes
            this.controller.reset();
            this.update(this.model);
        }
        
    }
    
    /**
     * Creates dialog box to warn the player that he's wasting steps.
	 * Note to corrector: This method is public in the view because the
         * controller needs to access this method and I thought that it 
         * belonged here since it's heavily graphical. I could have made a 
         * private method in the controller, but chose this approach.
     */
    public void waistingSteps() {
                
        String message = "You are waisting steps.\n" 
                + "You should try a different color...";
        
        Image image = new ImageIcon("data/javaIcon.png").getImage();
        Image scaledImage = image.getScaledInstance(40, 40, 0);
                
        JOptionPane.showMessageDialog(this,
            message,
            "Helpfull Tip!",
            JOptionPane.WARNING_MESSAGE,
            new ImageIcon(scaledImage) );
        
    }
    
    /**
     * A method that will show the possible settings
     */
    public void showSeetings() {
        
        //This panel will be the object of the JOptionPane
        JPanel panel = new JPanel();
        //Using a grid layout to easily control the arrangement of the comppnents
        panel.setLayout(new GridLayout(6, 1));
        
        //Text, top component
        JLabel surface = new JLabel("Play on plane or torus?");
        panel.add(surface);
        
        //We create a ButtonGroup so that only one button at a time can be checked
        ButtonGroup surfaceButtons = new ButtonGroup();
        
        //The button is selected depending on the state of the model
        JRadioButton plane = new JRadioButton("Plane", !this.model.getTorusMode());
        plane.addActionListener(this.controller);
        //2nd component
        surfaceButtons.add(plane);
        panel.add(plane);
                
        //The button is selected depending on the state of the model
        JRadioButton torus = new JRadioButton("Torus", this.model.getTorusMode());
        torus.addActionListener(this.controller);
        //3rd component
        surfaceButtons.add(torus);
        panel.add(torus);
        
        //The same logic is used for the rest of the settings
        JLabel diagonalMode = new JLabel("Diagonal moves?");
        panel.add(diagonalMode);
        
        ButtonGroup captureModeButtons = new ButtonGroup();
        
        JRadioButton orthogonal = new JRadioButton("Orthogonal", !this.model.getDiagonalMode());
        orthogonal.addActionListener(this.controller);
        captureModeButtons.add(orthogonal);
        panel.add(orthogonal);
        
        JRadioButton diagonal = new JRadioButton("Diagonal", this.model.getDiagonalMode());
        diagonal.addActionListener(this.controller);
        captureModeButtons.add(diagonal);        
        panel.add(diagonal);
        
        //Personalized image 
        Image image = new ImageIcon("data/javaIcon.png").getImage();
        Image scaledImage = image.getScaledInstance(40, 40, 0);
        
        JOptionPane.showMessageDialog(this,
            panel, //the panel containng the buttons is the object of the JOptionPane
            "Settings",
            JOptionPane.OK_OPTION,
            new ImageIcon(scaledImage) );
        
    }
	
	/**
     * Creates dialog box to give a few instructions
     */
    public void Instructions() {
                
        String message = "Hi! here are a few instructions : \n \n "
                + "- To save, press Quit \n "
                + "- Close the window if you do not wish to save \n "
                + "(Warning: the last saved file will have been \n destroyed,"
                + " remember the undo if needed) \n "
                + "- If you launch a game with a new size, the \n previous save will"
                + " be ignored.\n"
                + "- Have Fun!";
        
        Image image = new ImageIcon("data/javaIcon.png").getImage();
        Image scaledImage = image.getScaledInstance(40, 40, 0);
                
        JOptionPane.showMessageDialog(this,
            message,
            "A Few Instructions!",
            JOptionPane.WARNING_MESSAGE,
            new ImageIcon(scaledImage) );
        
    }

}