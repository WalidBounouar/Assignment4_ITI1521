
import java.io.Serializable;

/**
 *
 * @author Walid Bounouar
 * 
 * @param <E> reference to generic type
 */
//LinkedStack implements Serializable so that the model can
//serialize without problems
public class LinkedStack<E> implements Stack<E> , Serializable{
 
    //Defining a nested class that will be the chained elements
    /**
     * A simple object that holds a value of type T and a reference to
     * the another Elem object.
     * 
     * @param <T> reference to generic type
     */
    //Serializable so that the elements can be serialized
    private static class Elem<T> implements Serializable {
        private T value;
        private Elem<T> next;
        
        public Elem(T value, Elem<T> next) {
            this.value = value;
            this.next = next;
        }
    }
    
    /**
     * A reference to the top element.
     */
    private Elem<E> top;
    
    /**
     * The basic constructor.
     */
    public LinkedStack() {
        
    }
    
    /**
     * Determines if the stack is empty.
     * 
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        
        return (this.top == null);
        
    }
    
    
    /**
     * Returns a reference to the value of the top element. Does not modify the
     * stack.
     * 
     * @return reference to the value of the top element.
     */
    public E peek() {
        
        if (this.isEmpty()) {
            throw new EmptyStackException("The stack is empty, you cannot peek.");
        }
        
        return (this.top.value);
        
    }
    
    /**
     * Returns a reference to the value of the top element and removes the 
     * element from the stack.
     * 
     * @return reference to the value of the top element.
     */
    public E pop() {
        
        if (this.isEmpty()) {
            throw new EmptyStackException("The stack is empty, you cannot pop.");
        }
        
        Elem<E> saved = this.top;
        this.top = this.top.next; //or saved.next, same thing
        return (saved.value);
        
    }
    
    /**
     * Pushes a new element to the top of the stack (changes instance variable
     * top), the value of the new element is the argument.
     * 
     * @param value 
     *              a reference to the value of the new element.
     */
    public void push (E value) {
        
        if (value == null) {
            throw new NullPointerException("You cannot push a null value into"
                    + " the stack.");
        }
        
        Elem<E> elem = new Elem<E>(value, this.top);
        this.top = elem;
        
    }
    
}
