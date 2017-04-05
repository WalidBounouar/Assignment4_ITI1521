// Ajoutez les directives 'import' ici.
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the followiung information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author  Walid Bounouar
 */

//The model is cloneable and serializable
public class GameModel implements Cloneable, 
                                    Serializable{

    /**
     * predefined values to capture the color of a DotInfo
     */
    public static final int COLOR_0 = 0;
    public static final int COLOR_1 = 1;
    public static final int COLOR_2 = 2;
    public static final int COLOR_3 = 3;
    public static final int COLOR_4 = 4;
    public static final int COLOR_5 = 5;
    public static final int NUMBER_OF_COLORS = 6;

    // Ajoutez vos variables d'instance ici.
    
    /**
     * The size of the game.
     */
    private int size;
    
    /**
     * The number of steps played since the last reset
     */
    private int rounds;
    
    /**
     * The number of captured dots
     */
    private int capturedDots;
    
    /**
     * The current selection color
     */
    private int currentSelectedColor;
    
    
    /**
     * A 2 dimentionnal array of sizeOfGame*sizeOfGame recording the 
     * state of each dot
     */
    private DotInfo[][] board;

    /**
     * Random generator
     */
    private Random generator;
    
    /**
     * A boolean representing if the game is into torusMode.
     */
    private boolean torusMode;
    
    /**
     * A boolean representing if the game is into diagonalMode.
     */
    private boolean diagonalMode;
    
    /**
     * Stacks containing gameModel clones to be used with the undo and redo
     * features
     */
    private Stack<GameModel> undoStack;
    private Stack<GameModel> redoStack;
    
    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {

        // Ajoutez votre code ici.
        this.generator  = new Random();
        
        this.size = size;
        
        this.torusMode = false;
        this.diagonalMode = false;
        
        this.reset(); //reset will take care of most of the work

    }

    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset() {

         // Ajoutez votre code ici.
        this.currentSelectedColor = -1; //value this is not in the possible colors 
        this.capturedDots = 0; //starts with no captured dots
        this.rounds = -1; //-1 so that the first step is choosing a starting point
        this.undoStack = new LinkedStack<GameModel>();
        this.redoStack = new LinkedStack<GameModel>();
                
        this.board = new DotInfo[size][size];        
        
        for (int i = 0; i < size; i++) { //row(y)
            
            for (int j = 0; j < size; j++) { //column(x)
              
            int tmpColor = generator.nextInt(this.NUMBER_OF_COLORS);
                
            this.board[i][j] = new DotInfo(j, i, tmpColor);
                
            }
            
        }

    }

    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */ 
    public int getSize() {

        // Ajoutez votre code ici.
        return (this.size);

    }

    /**
     * returns the color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */  
    public int getColor(int i, int j) {

        // Ajoutez votre code ici.
        if( this.board[j][i].isCaptured() ) {
            return this.currentSelectedColor;
        } else {
            return this.board[j][i].getColor();
        }

    }

    /**
     * returns true is the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isCaptured(int i, int j) {

        // Ajoutez votre code ici.
        return ( this.board[j][i].isCaptured() );

    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */ 
    public void capture(int i, int j) {

        // Ajoutez votre code ici.
        this.board[j][i].setCaptured(true);
                
        this.capturedDots++;

    }

    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */
    public int getNumberOfSteps() {

        // Ajoutez votre code ici.
        return (this.rounds);

    }

    /**
     * Setter method for currentSelectedColor and changes the color of all 
     * captured dots.
     * 
     * @param val
     *            the new value for currentSelectedColor
    */ 
    public void setCurrentSelectedColor(int val) {

        // Ajoutez votre code ici.
        this.currentSelectedColor = val;

    }

    /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */  
    public int getCurrentSelectedColor() {

        // Ajoutez votre code ici.
        return this.currentSelectedColor;

    }
    
    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {

        // Ajoutez votre code ici.
        return (this.board[j][i]);

    }

    /**
     * The method <b>step</b> updates the number of steps.
     */
    public void step() {

        // Ajoutez votre code ici.
        this.rounds++;

    }

    /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the dats are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished() {

        // Ajoutez votre code ici.
        return (this.capturedDots == this.size * this.size);

    }
    
    /**
     * Getter method for torusMode
     * 
     * @return torusMode
     */
    public boolean getTorusMode() {
        return (this.torusMode);
    }
    
    /**
     * Setter method for torusMode. 
     * 
     * @param value
     *            the new value for the torusMode
    */ 
    public void setTorusMode(boolean value) {
        this.torusMode = value;
    }
    
    /**
     * Getter method for diagonalMode
     * 
     * @return diagonalMode
     */
    public boolean getDiagonalMode() {
        return (this.diagonalMode);
    }
    
    /**
     * Setter method for diagonalMode. 
     * 
     * @param value
     *            the new value for the diagonalMode
    */ 
    public void setDiagonalMode(boolean value) {
        this.diagonalMode = value;
    }
    
    /**
     * The new clone method for gameModel.
     * 
     * @return a deep copy of the gameModel
     */
    @Override
    public GameModel clone() {
        
        GameModel clone = null;
        try {
            clone = (GameModel) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
        //Elements that do not need to be adjusted;
        //-The primitives
        //-The stacks, because it is fine if all clones share the undo and redo stacks
        
        clone.board = new DotInfo[clone.size][clone.size];
        
        for (int i = 0; i < clone.size; i++) { // y
            for (int j = 0; j < clone.size; j++) { // x
                
                //clone was overwitten in DotInfo
                //the exception was handle in DotInfo
                clone.board[i][j] = this.board[i][j].clone();                
                
            }
        }
        
        return clone;
        
    }
    
    /**
     * Getter method for the undoStack
     * 
     * @return a reference to the undoStack
     */
    public Stack<GameModel> getUndoStack() {
        return (this.undoStack);
    }
    
    /**
     * Getter method for the redoStack
     * 
     * @return a reference to the redoStack
     */
    public Stack<GameModel> getRedoStack() {
        return (this.redoStack);
    }    

    /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString() {

        // Ajoutez votre code ici.
        StringBuffer model = new StringBuffer();
        
        //twice as many lines because I want to show the capture state
        for (int i = 0; i < (this.size * 2); i++) { 
            
            for (int j = 0; j < this.size; j++) {
                
                if ( i % 2 == 0) { //even numbered lines are the colors
                    model.append(" " + this.board[i/2][j].getColor() + " "); 
                    // i/2 to not bust the permitted index
                }
                
                if ( i % 2 == 1) { //uneven numbered lines are the capture states
                    if ( this.board[i/2][j].isCaptured() ) {
                        model.append( " " + "T" + " " );
                        // i/2 to not bust the permitted index
                    } else {
                        model.append( " " + "F" + " " );
                        // i/2 to not bust the permitted index
                    }
                }
                
                if (j == this.size - 1) {
                    model.append("\n");
                }
                
            }
            
        }
        
        model.append( "Number of captured dots = " + this.capturedDots + "\n" );
        model.append( "Present color = " + this.currentSelectedColor + "\n" );
        model.append( "Number of steps = " + this.rounds + "\n" );
        model.append( "Torus mode = " + this.torusMode + "\n" );
        model.append( "Diagonal mode = " + this.diagonalMode + "\n" );
        model.append( "undoStack is empty = " + this.undoStack.isEmpty() + "\n" );
        model.append( "redoStack is empty = " + this.redoStack.isEmpty() + "\n" );
        
        return ( model.toString() );

    }

}
