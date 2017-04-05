
/**
 * The class <b>EmptyStackException</b> is a custom exception to be thrown if
 * a stack is empty and we try to pop or peek and element.
 * 
 * @author Walid Bounouar
 */

//extends RuntimeException so that the exception is optional declaration
public class EmptyStackException extends RuntimeException{
 
    EmptyStackException() {
        super();
    }
    
    EmptyStackException(String message) {
        super(message);
    }
    
}
