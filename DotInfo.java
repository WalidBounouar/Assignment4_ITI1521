
import java.io.Serializable;
import java.util.Arrays;


/**
 * The class <b>DotInfo</b> is a simple helper class to store the initial color and state
 * (captured or not) at the dot position (x,y)
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Walid Bounouar
 */

//The class implements Cloneable and Serializable, each are used to make the
//objects of that class to Cloneable and Serializable
public class DotInfo implements Cloneable, Serializable {

    
    /**
     * The x coordinate of this DotInfo.
     */
    private int x;
    
    /**
     * The x coordinate of this DotInfo.
     */
    private int y;
    
    /**
     * Boolean representing if the DotInfo is captured.
     */
    private boolean isCaptured;
    
    /**
     * The color of this DotInfo.
     */
    private int color;

	/**
	 * Default constructor. Good practice when using Serializable
	 *
	 */
	public DotInfo() {
		
	}
    
    /**
     * Constructor 
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param color
     *            the initial color
     */
    public DotInfo(int x, int y, int color) {

        this.x = x;
        this.y = y;
        this.color = color;
        this.isCaptured = false; //not necessary, but I put it to be clear.

    }


    /**
     * Getter method for the attribute x.
     * 
     * @return the value of the attribute x
     */
    public int getX() {

        return (this.x);

    }

    /**
     * Getter method for the attribute y.
     * 
     * @return the value of the attribute y
     */
    public int getY() {

        return (this.y);

    }

    /**
     * Setter for captured
     * @param captured
     *            the new value for captured
     */
    public void setCaptured(boolean captured) {

        this.isCaptured = captured;

    }

    /**
     * Get for captured
     *
     * @return captured
     */
    public boolean isCaptured() {

        return (this.isCaptured);

    }

    /**
     * Get for color
     *
     * @return color
     */
    public int getColor() {

        return (this.color);

    }

    /**
     * The override of the clone method. Throws an exception that will actually
     * not be thrown, since the class implements Cloneable.
     * 
     * @return DotInfo 
     *            a clone of the DotInfo.
     */
    
    @Override
    public DotInfo clone()  {
        DotInfo clone = null;
        try {
			//Simple casting since the DotInfo only contains primitives
            clone = (DotInfo) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
        
        return clone;

    }
    

}
