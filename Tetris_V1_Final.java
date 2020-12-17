package Tetris_OOP;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Tetris_V1_Final {

    public static final int width = 10;
    public static final int height = 24;
    public static int centre_x = 4;
    public static int centre_y = 11;
    public static int high_score = 0;
    public static String file_name = "Highscore.dat";


    // Initializes the Program
    public static void main(String[] args) {
        highscoreFile();
        menu();
    }

    public static void menu() {
        boolean infinite_loop = true;
        while (infinite_loop) {
            Scanner input = new Scanner(System.in);
            System.out.print("\n\nWelcome to Tetris \n" +
                    "1. Play Game\n" +
                    "2. Controls\n" +
                    "3. Instructions\n" +
                    "4. Highscore\n" +
                    "5. Exit Game\n" +
                    "Choice: ");

            String choice = input.next();

            switch (choice) {

                case "1":
                    System.out.println("Play Game\n");
                    playGame();
                    break;
                case "2":
                    System.out.println("View Controls");
                    viewControls();
                    break;
                case "3":
                    System.out.println("Instructions");
                    viewInstructions();
                    break;
                case "4":
                    System.out.println("View Highscore!");
                    readHighscore();
                    break;
                case "5":
                    System.out.println("Exiting The Game!");
                    infinite_loop = false;
                    break;
                default:
                    System.err.println("Menu Error: That's not a listed command!");
                    break;
            }
        }
    }

    public static void viewControls() {
        System.out.println("Controls\n" +
                "Press \"R\" - Move Block to Right Side\n" +
                "Press \"L\" - Move Block to Left Side\n" +
                "Press \"N\" - Move Block Downwards\n" +
                "Write \"EXIT\" - Exits Game at Any Point While Playing!\n");
    }

    public static void viewInstructions() {

        System.out.println("\nHere is how to play Tetris\n" +
                "1. Random Shapes appear on the screen one at a time\n" +
                "2. The player has to adjust the shapes in a way that makes a whole horizontal line\n" +
                "3. Each line completed rewards the player with 200 score\n" +
                "4. Game ends if the the shapes stack up and touch top boundary of the game.\n" +
                "That's all you need to know.\n" +
                "Go ahead and Give it a try!");
    }


    public static void playGame() {
        String[][] game_grid = new String[height][width];
        high_score = 0;
        gridInitialize(game_grid);
        printGrid(game_grid);
        while (true) {
            randomShapes(game_grid);
            for (int i = 0; i < height; i++) {
                lineClear(game_grid);
            }
            if (gameOver(game_grid)) {
                System.out.println("Game Over! The Shapes have reached the Top Boundary!");
                System.out.println("Your Final Score is " + high_score);
                writeHighscore();
                break;
            }
            centre_x = 4;
        }
    }

    //Takes 2D array as input, replaces the indexes with spaces, return the array
    public static void gridInitialize(String[][] list) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                list[i][j] = "  ";
                System.out.print(list[i][j]);
            }
        }
    }

    //Takes 2D array as input, prints indexes in orderly fashion in form of grid
    public static void printGrid(String[][] list) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(list[i][j] + "|");
                if (i == centre_y && j == width - 1) {
                    System.out.print("\t \t \t Current Score =  " + high_score);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //Takes 2D array as input, chooses random number and initialize the shapes respectively on the grid
    public static void randomShapes(String[][] list) {
        int random_number = (int) (Math.random() * 6);
        switch (random_number) {
            case 0:
                S_Shape(list);
                printGrid(list);
                collisionDetector_S(list);
                break;
            case 1:
                T_Shape(list);
                printGrid(list);
                collisionDetector_T(list);
                break;
            case 2:
                L_Shape(list);
                printGrid(list);
                collisionDetector_L(list);
                break;
            case 3:
                O_Shape(list);
                printGrid(list);
                collisionDetector_O(list);
                break;
            case 4:
                Bar_Shape(list);
                printGrid(list);
                collisionDetector_Bar(list);
                break;
            case 5:
                list[0][centre_x] = "**";
                printGrid(list);
                collisionDetectorPoint(list);
        }
    }

    //Takes 2D array as input as initializes S Shaped Piece of tetris on grid.
    public static void S_Shape(String[][] list) {
        list[1][centre_x] = "**";
        list[0][centre_x + 1] = "**";
        list[1][centre_x + 1] = "**";
        list[0][centre_x + 2] = "**";
    }

    //Takes 2D array as input as initializes T Shaped Piece of tetris on grid.
    public static void T_Shape(String[][] list) {
        list[1][centre_x + 2] = "**";
        list[0][centre_x + 1] = "**";
        list[1][centre_x + 1] = "**";
        list[1][centre_x] = "**";
    }

    //Takes 2D array as input as initializes L Shaped Piece of tetris on grid.
    public static void L_Shape(String[][] list) {
        list[0][centre_x] = "**";
        list[1][centre_x] = "**";
        list[1][centre_x + 1] = "**";
        list[1][centre_x + 2] = "**";
    }

    //Takes 2D array as input as initializes O Shaped Piece of tetris on grid.
    public static void O_Shape(String[][] list) {
        list[0][centre_x] = "**";
        list[1][centre_x] = "**";
        list[0][centre_x + 1] = "**";
        list[1][centre_x + 1] = "**";
    }

    //Takes 2D array as input as initializes Bar Shaped Piece of tetris on grid.
    public static void Bar_Shape(String[][] list) {
        list[0][centre_x - 1] = "**";
        list[0][centre_x] = "**";
        list[0][centre_x + 1] = "**";
        list[0][centre_x + 2] = "**";
    }

    //Takes 2D array as input as initializes, drops the S shape on the grid and detect where to stop the block
    public static void collisionDetector_S(String[][] list) {
        for (int i = 1; i < height - 1; i++) {
            if (list[i][centre_x + 2].equals("  ") && list[i + 1][centre_x + 1].equals("  ") && list[i + 1][centre_x].equals("  ")) {
                list[i + 1][centre_x + 1] = "**";
                list[i - 1][centre_x + 1] = "  ";
                list[i][centre_x + 2] = "**";
                list[i - 1][centre_x + 2] = "  ";
                list[i + 1][centre_x] = "**";
                list[i][centre_x] = "  ";

                printGrid(list);
                try {
                    movementDetection_S(list, i);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Can't Move Any Further!");
                }
            } else
                break;
        }
    }

    //Takes 2D array as input as initializes, drops the T shape on the grid and detect where to stop the block
    public static void collisionDetector_T(String[][] list) {
        for (int i = 1; i < height - 1; i++) {
            if (list[i + 1][centre_x].equals("  ") && list[i + 1][centre_x + 1].equals("  ") && list[i + 1][centre_x + 2].equals("  ")) {
                list[i + 1][centre_x + 1] = "**";
                list[i - 1][centre_x + 1] = "  ";
                list[i + 1][centre_x + 2] = "**";
                list[i][centre_x + 2] = "  ";
                list[i + 1][centre_x] = "**";
                list[i][centre_x] = "  ";

                printGrid(list);
                try {
                    movementDetection_T(list, i);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Cant Move Any Further!");
                }
            } else
                break;
        }
    }

    //Takes 2D array as input as initializes, drops the T shape on the grid and detect where to stop the block
    public static void collisionDetector_L(String[][] list) {
        for (int i = 1; i < height - 1; i++) {
            if (list[i + 1][centre_x].equals("  ") && list[i + 1][centre_x + 1].equals("  ") && list[i + 1][centre_x + 2].equals("  ")) {
                list[i + 1][centre_x + 1] = "**";
                list[i][centre_x + 1] = "  ";
                list[i + 1][centre_x + 2] = "**";
                list[i][centre_x + 2] = "  ";
                list[i + 1][centre_x] = "**";
                list[i - 1][centre_x] = "  ";

                printGrid(list);
                try {
                    movementDetection_L(list, i);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Cant Move Any Further!");
                }

            } else
                break;
        }
    }

    //Takes 2D array as input as initializes, drops the O shape on the grid and detect where to stop the block
    public static void collisionDetector_O(String[][] list) {
        for (int i = 1; i < height - 1; i++) {
            if (list[i + 1][centre_x].equals("  ") && list[i + 1][centre_x + 1].equals("  ")) {
                list[i + 1][centre_x] = "**";
                list[i - 1][centre_x] = "  ";
                list[i + 1][centre_x + 1] = "**";
                list[i - 1][centre_x + 1] = "  ";

                printGrid(list);
                try {
                    movementDetection_O(list, i);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Can't Move Any Further!");
                }
            } else
                break;
        }
    }

    //Takes 2D array as input as initializes, drops the Bar shape on the grid and detect where to stop the block
    public static void collisionDetector_Bar(String[][] list) {
        for (int i = 1; i < height; i++) {
            if (list[i][centre_x - 1].equals("  ") && list[i][centre_x].equals("  ") && list[i][centre_x + 1].equals("  ") && list[i][centre_x + 2].equals("  ")) {
                list[i][centre_x - 1] = "**";
                list[i - 1][centre_x - 1] = "  ";
                list[i][centre_x] = "**";
                list[i - 1][centre_x] = "  ";
                list[i][centre_x + 1] = "**";
                list[i - 1][centre_x + 1] = "  ";
                list[i][centre_x + 2] = "**";
                list[i - 1][centre_x + 2] = "  ";

                printGrid(list);
                try {
                    movementDetection_Bar(list, i);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Can't Move Any Further");
                }
            } else
                break;
        }
    }

    public static void collisionDetectorPoint(String[][]list){
        for (int i = 1; i < height; i++) {
            if (list[i][centre_x].equals("  ")) {
                list[i][centre_x] = "**";
                list[i - 1][centre_x] = "  ";
                printGrid(list);

                try {
                    movementDetectionPoint(list, i);
                }
                catch (ArrayIndexOutOfBoundsException ex){
                    System.out.println("Can't Move Any Further!");
                }
            }
            else
                break;
        }
    }

    //Takes 2D array as input and detects the block touching the top of the grid boundary to announce game over
    public static boolean gameOver(String[][] list) {
        for (int i = 0; i < width; i++) {
            if (list[0][i].equals("**"))
                return true;
        }
        return false;
    }

    //Takes 2D array as input and allows the user to move the S block across array using commands
    public static void movementDetection_S(String[][] list, int number) {
        Scanner input = new Scanner(System.in);
        System.out.print("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
        String movement;
        movement = input.next();

        label:
        while (true) {
            switch (movement.toUpperCase()) {
                case "R":
                    if (list[number][centre_x + 3].equals("  ") && list[number + 1][centre_x + 2].equals("  ")) {
                        list[number][centre_x + 1] = "  ";
                        list[number + 1][centre_x] = "  ";
                        centre_x++;
                        list[number][centre_x + 2] = "**";
                        list[number + 1][centre_x + 1] = "**";
                    }
                    break label;
                case "L":
                    if (list[number][centre_x].equals("  ") && list[number + 1][centre_x - 1].equals("  ")) {
                        list[number][centre_x + 2] = "  ";
                        list[number + 1][centre_x + 1] = "  ";
                        centre_x--;
                        list[number][centre_x + 1] = "**";
                        list[number + 1][centre_x] = "**";
                    }
                    break label;
                case "N":
                    break label;
                case "EXIT":
                    System.out.println("\nGame Over!\n" +
                            "Your Final Score is " + high_score);
                    writeHighscore();
                    menu();
                    break;
                default:
                    System.out.print("Choose Left, Right, No Movement or Exit!: ");
                    movement = input.next();
                    break;
            }
        }
    }

    //Takes 2D array as input and allows the user to move the Bar block across array using commands
    public static void movementDetection_Bar(String[][] list, int number) {
        Scanner input = new Scanner(System.in);
        System.out.print("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
        String movement;
        movement = input.next();

        label:
        while (true) {
            switch (movement.toUpperCase()) {
                case "R":
                    if (list[number][centre_x + 3].equals("  ")) {
                        list[number][centre_x - 1] = "  ";
                        centre_x++;
                        list[number][centre_x + 2] = "**";
                    }
                    break label;
                case "L":
                    if (list[number][centre_x - 2].equals("  ")) {
                        list[number][centre_x + 2] = "  ";
                        centre_x--;
                        list[number][centre_x - 1] = "**";
                    }
                    break label;
                case "N":
                    break label;
                case "EXIT":
                    System.out.println("\nGame Over!\n" +
                            "Your Final Score is " + high_score);
                    writeHighscore();
                    menu();
                    break;
                default:
                    System.out.print("Choose Left, Right, No Movement or Exit!: ");
                    movement = input.next();
                    break;
            }
        }
    }

    //Takes 2D array as input and allows the user to move the O block across array using commands
    public static void movementDetection_O(String[][] list, int number) {
        Scanner input = new Scanner(System.in);
        System.out.print("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
        String movement;
        movement = input.next();

        label:
        while (true) {
            switch (movement.toUpperCase()) {
                case "R":
                    if (list[number][centre_x + 2].equals("  ") && list[number + 1][centre_x + 2].equals("  ")) {
                        list[number][centre_x] = "  ";
                        list[number + 1][centre_x] = "  ";
                        centre_x++;
                        list[number][centre_x + 1] = "**";
                        list[number + 1][centre_x + 1] = "**";
                    }
                    break label;
                case "L":
                    if (list[number][centre_x - 1].equals("  ") && list[number + 1][centre_x - 1].equals("  ")) {
                        list[number][centre_x + 1] = "  ";
                        list[number + 1][centre_x + 1] = "  ";
                        centre_x--;
                        list[number][centre_x] = "**";
                        list[number + 1][centre_x] = "**";
                    }
                    break label;
                case "N":
                    break label;
                case "EXIT":
                    System.out.println("\nGame Over!\n" +
                            "Your Final Score is " + high_score);
                    writeHighscore();
                    menu();
                    break;
                default:
                    System.out.print("Choose Left, Right, No Movement or Exit!: ");
                    movement = input.next();
                    break;
            }
        }
    }

    //Takes 2D array as input and allows the user to move the T block across array using commands
    public static void movementDetection_T(String[][] list, int number) {
        Scanner input = new Scanner(System.in);
        System.out.println("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
        String movement;
        movement = input.next();

        label:
        while (true) {
            switch (movement.toUpperCase()) {
                case "R":
                    if (list[number + 1][centre_x + 3].equals("  ") && list[number][centre_x + 2].equals("  ")) {
                        list[number + 1][centre_x] = "  ";
                        list[number][centre_x + 1] = "  ";
                        centre_x++;
                        list[number + 1][centre_x + 2] = "**";
                        list[number][centre_x + 1] = "**";
                    }
                    break label;
                case "L":
                    if (list[number + 1][centre_x - 1].equals("  ") && list[number][centre_x].equals("  ")) {
                        list[number + 1][centre_x + 2] = "  ";
                        list[number][centre_x + 1] = "  ";
                        centre_x--;
                        list[number + 1][centre_x] = "**";
                        list[number][centre_x + 1] = "**";
                    }
                    break label;
                case "N":
                    break label;
                case "EXIT":
                    System.out.println("\nGame Over!\n" +
                            "Your Final Score is " + high_score);
                    writeHighscore();
                    menu();
                    break;
                default:
                    System.out.print("Choose Left, Right, No Movement or Exit!: ");
                    movement = input.next();
                    break;
            }
        }
    }

    //Takes 2D array as input and allows the user to move the L block across array using commands
    public static void movementDetection_L(String[][] list, int number) {
        Scanner input = new Scanner(System.in);
        System.out.println("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
        String movement;
        movement = input.next();

        label:
        while (true) {
            switch (movement.toUpperCase()) {
                case "R":
                    if (list[number][centre_x + 1].equals("  ") & list[number + 1][centre_x + 3].equals("  ")) {
                        list[number][centre_x] = "  ";
                        list[number + 1][centre_x] = "  ";
                        centre_x++;
                        list[number][centre_x] = "**";
                        list[number + 1][centre_x + 2] = "**";
                    }
                    break label;
                case "L":
                    if (list[number][centre_x - 1].equals("  ") && list[number + 1][centre_x - 1].equals("  ")) {
                        list[number][centre_x] = "  ";
                        list[number + 1][centre_x + 2] = "  ";
                        centre_x--;
                        list[number][centre_x] = "**";
                        list[number + 1][centre_x] = "**";
                    }
                    break label;
                case "N":
                    break label;
                case "EXIT":
                    System.out.println("\nGame Over!\n" +
                            "Your Final Score is " + high_score);
                    writeHighscore();
                    menu();
                    break;
                default:
                    System.out.print("Choose Left, Right, No Movement or Exit!: ");
                    movement = input.next();
                    break;
            }
        }
    }


    public static void movementDetectionPoint(String[][] list, int number){
        {
            Scanner input = new Scanner(System.in);
            System.out.println("For Right Movement Write \"R\". For Left Movement Write \"L\". For No Movement Write \"N\".");
            String movement;
            movement = input.next();

            label:
            while (true) {
                switch (movement.toUpperCase()) {
                    case "R":
                        if (list[number][centre_x + 1].equals("  ")) {
                           list[number][centre_x] = "  ";
                           centre_x ++;
                            list[number][centre_x] = "**";
                        }
                        break label;
                    case "L":
                        if (list[number][centre_x - 1].equals("  ")){
                            list[number][centre_x] = "  ";
                            centre_x --;
                            list[number][centre_x] = "**";
                        }
                        break label;
                    case "N":
                        break label;
                    case "EXIT":
                        System.out.println("\nGame Over!\n" +
                                "Your Final Score is " + high_score);
                        writeHighscore();
                        menu();
                        break;
                    default:
                        System.out.print("Choose Left, Right, No Movement or Exit!: ");
                        movement = input.next();
                        break;
                }
            }
        }

    }

    //Takes the 2D array as input, detects a full horizontal line across the array and removes the line.
    public static void lineClear(String[][] list) {
        for (int i = height - 1; i >= 0; i--) {
            String[] check = new String[width];
            Arrays.fill(check, "**");
            if (Arrays.equals(list[i], check)) {
                high_score += 200;
                Arrays.fill(list[i], "  ");
                blocksDrop(list, i);
            }
        }
    }

    public static void blocksDrop(String[][] list, int line_number) {
        for (int i = line_number; i > 0; i--) {
            list[i] = list[i - 1].clone();
        }
        Arrays.fill(list[0], "  ");
    }

    public static void writeHighscore(){
        try{
            File file = new File(file_name);
            DataInputStream input = new DataInputStream(new FileInputStream(file));
            int previous = input.readInt();
            DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
            output.writeInt(Math.max(high_score, previous));
            output.close();
            input.close();
        }
        catch (IOException ex){
            System.out.println("There was an error writing Highscore!");
        }
    }

    public static void highscoreFile(){
        File file = new File(file_name);
        if (!file.exists()) {
            try {
                boolean creation = file.createNewFile();
                if (!creation){
                    System.out.println("There was an error creating Highscore File. Clear some space and try again!");
                    System.exit(0);
                }
                else{
                    DataOutputStream output = new DataOutputStream(new FileOutputStream(file_name));
                    output.writeInt(high_score);
                }
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        }
    }


    public static void readHighscore() {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(file_name));
            int score = input.readInt();
            System.out.println("\nCurrent Highscore is "+ score);
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

}