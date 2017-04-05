/**
 * The class <b>FloodIt</b> launches the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Walid Bounouar
 */
public class FloodIt {

    /**
     * <b>main</b> of the application. Creates the instance of  GameController 
     * and starts the game. If a game size (>10) is passed as parameter, it is 
     * used as the board size. Otherwise, a default value is passed
     * 
     * @param args
     *            command line parameters
     */
    
    public static void main(String[] args) {

        // ADD YOUR CODE HERE
        
        StudentInfo.display();
		System.out.println("Note au correcteur(trice) : J'ai comment out les affichage"
								+ "du modele sur la console pour ne pas surcharger." 
								+ " \n Si vous voulez voir, enlever le // aux lignes 56, 150" 
								+ " et 216 du controlleur. \n Aussi, tous mes commentaires sont" 
								+ " en anglais parce que je comptais peut-être montrer ce devoir" 
								+ " a des employeurs.");
        
        int size = 12;
        
        if (args.length != 0) {
            if (args.length > 1) {
                System.out.println("Invalide argument, using default size.");
                size = 12;
            } else {
                try{
                    size = Integer.parseInt(args[0]);
                    if(size < 10){
                        System.out.println("Invalide argument, using minimum size");
                        size = 10;
                    }
                } catch(NumberFormatException e){
                    System.out.println("Invalide argument, using default size");
                    size = 12;
                }   
            }
        }
        
        GameController game = new GameController(size);

    }

}
