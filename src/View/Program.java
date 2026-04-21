package View;

public class Program {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Please provide the levels directory as an argument.");
            System.exit(-1);
        }
        GameExecute gameExecute = new GameExecute();
        gameExecute.initialize(args[0]);
        System.out.println("Game started!");
        gameExecute.start();
    }

}
