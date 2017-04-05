// Ajoutez vos directives 'import' ici.

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * In the application <b>FlodIt</b>, a <b>DotButton</b> is a specialized color of
 * <b>JButton</b> that represents a dot in the game. It can have one of six colors
 * 
 * The icon images are stored in a subdirectory ``data''. We have 3 sizes, ``normal'',
 * ``medium'' and ``small'', respectively in directory ``N'', ``M'' and ``S''.
 *
 * The images are 
 * ball-0.png => grey icon
 * ball-1.png => orange icon
 * ball-2.png => blue icon
 * ball-3.png => green icon
 * ball-4.png => purple icon
 * ball-5.png => red icon
 *
 *  <a href=
 * "http://developer.apple.com/library/safari/#samplecode/Puzzler/Introduction/Intro.html%23//apple_ref/doc/uid/DTS10004409"
 * >Based on Puzzler by Apple</a>.
 * 
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Walid Bounouar
 */

public class DotButton extends JButton {
    
    /**
     * The predefined values shared by all DotButtons
     */
    public static final int NUMBER_OF_COLORS = 6;
    public static final int SMALL_SIZE     = 0;
    public static final int MEDIUM_SIZE    = 1;
    public static final int LARGE_SIZE     = 2;
    
    /**
     * The row (y) of the button.
     */
    private int row;
    
    /**
     * The column (x) of the button.
     */
    private int column;
    
    /**
     * The color of the button.
     */
    private int color;  
    
    /**
     * An array that contains the possible icons for the button.
     */
    private ImageIcon icons[];

    /**
     * Constructor used for initializing a cell of a specified color.
     * 
     * @param row
     *            the row of this Cell
     * @param column
     *            the column of this Cell
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */

    public DotButton(int row, int column, int color, int iconSize) {


        this.icons = new ImageIcon[NUMBER_OF_COLORS];
        
        if (iconSize == 0) { //we check the size wanted for the buttons  
            //we fill the array with the possible icons
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/S/ball-" + i + ".png");
            }            
        } else if (iconSize == 1) {            
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/M/ball-" + i + ".png");
            }            
        } else {            
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/N/ball-" + i + ".png");
            }            
        }
        
        this.color = color;
        this.setIcon( this.icons[this.color] );
        
        this.column = column;
        
        this.row = row;
        
        this.setBackground(Color.WHITE);
        this.setBorderPainted(false); //hide the border
        
        if (iconSize == this.SMALL_SIZE) {
            this.setPreferredSize(new Dimension (14, 14) );
        } else if (iconSize == this.MEDIUM_SIZE) {
            this.setPreferredSize(new Dimension (30, 30) );
        }
        
    }

    /**
     * A second constructor. In this case, you have to use -1 and -1 as the 
     * values of the row and colum.
     * 
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */
    public DotButton(int color, int iconSize) {

        this.icons = new ImageIcon[NUMBER_OF_COLORS];
        
        if (iconSize == 0) {            
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/S/ball-" + i + ".png");
            }            
        } else if (iconSize == 1) {            
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/M/ball-" + i + ".png");
            }            
        } else {            
            for (int i = 0; i < this.NUMBER_OF_COLORS; i++) {
                this.icons[i] = new ImageIcon("data/N/ball-" + i + ".png");
            }            
        }
        
        this.color = color;
        this.setIcon( this.icons[this.color] );
        
        this.column = -1;
        
        this.row = -1;

    }

    /**
     * Changes the cell color of this cell. The image is updated accordingly.
     * 
     * @param color
     *            the color to set
     */

    public void setColor(int color) {

        this.color = color;
        this.setIcon( this.icons[this.color] );

    }

    /**
     * Getter for color
     *
     * @return color
     */
    public int getColor() {

        return (this.color);

    }

    /**
     * Getter method for the attribute row.
     * 
     * @return the value of the attribute row
     */

    public int getRow() {

        return (this.row);

    }

    /**
     * Getter method for the attribute column.
     * 
     * @return the value of the attribute column
     */

    public int getColumn() {

        return (this.column);

    }

}
