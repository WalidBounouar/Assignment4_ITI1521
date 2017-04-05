// Ajoutez les directives 'import' ici.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computesthe next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Walid Bounouar
 */

public class GameController implements ActionListener {

    // Ajoutez vos variables d'instance ici.
    
    /**
     * Reference to the view of the board
     */
    private GameView view;
    
    /**
     * Reference to the model of the game
     */
    private GameModel model;
    
    /**
     * A stack that will contain the references to the DotInfo objects for the
     * flooding algorithm.
     */
    private Stack<DotInfo> stack;

    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {

        // Ajoutez votre code ici.
        this.loadGame(size); //we start be trying to load a game.
        this.view = new GameView(this.model, this);
		this.view.Instructions();
        //System.out.println(this.model.toString()); //Commented out as to not overload the console
    }

    /**
     * resets the game
     */
    public void reset() {

        // Ajoutez votre code ici.
        this.model.reset();

    }

    /**
     * Callback used when the user clicks a button (reset, quit, new color, undo,
     * redo, settings or any of the settings)
     *
     * @param e
     *            the ActionEvent
     */

	@SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {

        // ADD YOUR CODE HERE
       //If the action is neither Quit, Settings, Undo or Redo, we push a clone
       //of the model into the UndoStack. We also scrub the RedoStack.
        if (!e.getActionCommand().equals("Quit") 
                && !e.getActionCommand().equals("Settings")
                && !e.getActionCommand().equals("Undo")
                && !e.getActionCommand().equals("Redo")) {
            this.model.getUndoStack().push( this.model.clone() );
            while(!this.model.getRedoStack().isEmpty()) {
                this.model.getRedoStack().pop();
            }
        }
        
        //If the user undos an action, we push a clone of the current model into
        //the redo stack, and me change the model of the controller with the top
        //model of the undoStack
        if (e.getActionCommand().equals("Undo")) {
            this.model.getRedoStack().push( this.model.clone() );
            this.model = this.model.getUndoStack().pop();
        }
        
        //Similar logic with the Redo button.
        if (e.getActionCommand().equals("Redo")) {
            this.model.getUndoStack().push( this.model.clone() );
            this.model = this.model.getRedoStack().pop();
        }
        
        //we check if the action comes from a DotButton
        if (e.getSource() instanceof DotButton) {
            //if so, we cast
            DotButton presentButton = (DotButton) e.getSource();
            
            //If we are at the first step, we also choose the starting point
            if (this.model.getNumberOfSteps() == -1) {
                this.model.capture(presentButton.getColumn(),
                        presentButton.getRow());
            }
            
            this.selectColor( presentButton.getColor() );
            
        }
        
        if ( e.getActionCommand().equals("Reset") ) { //if the action is reset
            this.reset();
        }
        if ( e.getActionCommand().equals("Quit") ) { //id the action is quit
            this.saveGame(); //If the quit button is pressed, we save the game
            System.exit(0); //close application
        }
        
        //if the settings button is pressed, we only show the options dialog box
        if (e.getActionCommand().equals("Settings")) {
            this.view.showSeetings();
        }
        
        //Depending on the action of the JRadioButton selected, we change the
        //model accordingly.
        if (e.getActionCommand().equals("Plane")) {
            this.model.setTorusMode(false);
        } else if (e.getActionCommand().equals("Torus")) {
            this.model.setTorusMode(true);
        } else if (e.getActionCommand().equals("Orthogonal")) {
            this.model.setDiagonalMode(false);
        } else if (e.getActionCommand().equals("Diagonal")) {
            this.model.setDiagonalMode(true);
        }
        
        
        //No matter the event, we update the view.
        this.view.update(this.model);
        //System.out.println(this.model.toString()); //Commented out as to not overload the console

    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     * 
     * @param color
     *            the newly selected color
     */
	@SuppressWarnings("unchecked")
    public void selectColor(int color) {
        
        //no matter the color, even if it's the same as the one currntly
        //selected, we increment the steps.
        this.model.step();
        
        
        //If the player chooses the same color as the present one, we warn him.
        if (this.model.getCurrentSelectedColor() == color) {
            this.view.waistingSteps();
            return;
        }
        
        //Note: setCurentSelectedColor changes de color of the DotButton
        //for each ball
        this.model.setCurrentSelectedColor(color);
        
        this.stack = new LinkedStack();
        
        for (int i = 0; i < this.model.getSize(); i++) { //y
            for (int j = 0; j < this.model.getSize(); j++) { //x
                if (this.model.get(j, i).isCaptured()) {
                    this.stack.push(this.model.get(j, i));
                }
            }
        }

        //implementation of the flooding algorythm
        while (! this.stack.isEmpty()) { 
            
            DotInfo tmpDot = this.stack.pop();
            
            //an array will contain all the neighboors (min of 2, max of 8)
            DotInfo[] neighboors = this.findNeighboors(tmpDot);
            
            for (DotInfo dot: neighboors) {
                if (dot != null){
                    
                    if ( !dot.isCaptured() && dot.getColor() == color) {
                        this.model.capture(dot.getX(), dot.getY() );
                        this.stack.push(dot);
                    }
                    
                }
            }

        }
        
        //we check if the game is finished and act accordingly
        if (this.model.isFinished()) {
            this.view.update(this.model);
            //System.out.println(this.model.toString()); //Commented out as to not overload the console
            this.view.showDialogBox();
        }

    }
    
    // Ajoutez vos méthodes auxiliaires (private) ici.
    
    /**
     * This method determines the neighbors of a Dot. Takes into account the
     * settings stored in the gameModel.
     * 
     * @param dot
     *            a reference to a DotInfo object.

     * @return an array that contains the references to the neighboring DotInfo.
     */
    private DotInfo[] findNeighboors(DotInfo dot) { 
        
        int x = dot.getX();
        int y = dot.getY();
        DotInfo[] neighboors;
        int pos = 0;
        
        //Since the game if fairly simple, we don't deem it necessary to make 
        //sure the array is the perfect size, we simply use the max size
        neighboors = new DotInfo[8];
        
        //set the neighboring coordinates
        int rightX  = x + 1;
        int leftX = x - 1;
        int topY = y - 1;
        int bottomY = y + 1;
        
        //if we are in torusMode, we need to adjust the coordinates set
        if (this.model.getTorusMode()) {
            if (rightX == this.model.getSize()) {
                rightX = 0;
            }
            if (leftX < 0) {
                leftX = this.model.getSize() - 1;
            }
            if (topY < 0) {
                topY = this.model.getSize() - 1;
            }
            if (bottomY == this.model.getSize()) {
                bottomY = 0;
            }
        }

        if ( rightX < this.model.getSize() ) {
            neighboors[pos] = this.model.get(rightX, y);
            pos++;
        }

        if ( leftX >= 0 ) {
            neighboors[pos] = this.model.get(leftX, y);
            pos++;
        }

        if ( bottomY < this.model.getSize() ) {
            neighboors[pos] = this.model.get(x, bottomY);
            pos++;
        }

        if ( topY >= 0 ) {
            neighboors[pos] = this.model.get(x, topY);
            pos++;
        }
        
        //analysis of the diagonal neighboors
        
        if ( rightX < this.model.getSize() && bottomY < this.model.getSize()
                && this.model.getDiagonalMode()) {
            neighboors[pos] = this.model.get(rightX, bottomY);
            pos++;
        }
        
        if ( rightX < this.model.getSize() && topY >= 0 && this.model.getDiagonalMode()) {
            neighboors[pos] = this.model.get(rightX, topY);
            pos++;
        }
        
        if ( leftX >= 0 && bottomY < this.model.getSize() && this.model.getDiagonalMode()) {
            neighboors[pos] = this.model.get(leftX, bottomY);
            pos++;
        }
        
        if ( leftX >= 0 && topY >= 0 && this.model.getDiagonalMode()) {
            neighboors[pos] = this.model.get(leftX, topY);
            pos++;
        }
        
        return neighboors;

    }
    
    
    /**
     * This method creates a save for the game by serializing the GameModel.
     */
    private void saveGame() {
                
        try {
            File save = new File("savedGame.ser"); //create a file
            //create an output stream to handle the information
            FileOutputStream fileOut = new FileOutputStream(save);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.model); //write the save file
            out.flush(); //emptys the buffer
            out.close(); //frees ressources (buffer)
            fileOut.close();
        } catch (IOException ex) {
            //If an exception is caugth, we print the stack trace.
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
        
    }
    
    
    /**
     * This method loads a game, if possible.
     * 
     * @param size 
     *             The size of the model to be created.
     */
    private void loadGame(int size) {
        
        try {
            this.model = new GameModel(size); //create new model with desired size
            
            //create new file for InputStream
            File saveFile = new File("savedGame.ser");
            if (saveFile.exists()) { //of the file exists
                //create InputStream
                FileInputStream fileIn = new FileInputStream(saveFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                //change the model with the onw deserialized
                this.model = (GameModel) in.readObject();
                
                //if the user wanted a new size, we ignore the save and give a
                //new model
                if (size != this.model.getSize()) {
                    this.model = new GameModel(size);
                }
                
                in.close();
                fileIn.close();
                saveFile.delete();
            } 
        } catch (IOException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        } catch (ClassNotFoundException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
        
    }
   
}
