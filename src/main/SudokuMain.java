package main;
import config.SudokuConfig;
import GUI.SudokuGUI;
import solver.SudokuSolver;

import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * The Main Sudoku class represents the model for the GUI and holds solutions to valid sudoku puzzles.
 *
 * @author Dylan Sturr
 */
public class SudokuMain {
    public static void main(String[] args) {
        SudokuConfig sc = null;
        if (args.length == 0){
            try{
                sc = new SudokuConfig("data/empty.txt");
            }
            catch (FileNotFoundException ignored){
                System.err.println("Something went wrong!");
            }
        }
        else{
            try{
                sc = new SudokuConfig("data/" + args[0]);
            }
            catch (FileNotFoundException ignored){
                System.err.println("File not found: " + args[0]);
            }
        }
        SudokuSolver solver = new SudokuSolver();
        Optional<SudokuConfig> solution = solver.solve(sc);
        if (solution.isPresent()){
            System.out.println(solution.get());
        }
        else{
            System.out.println("No Solution!");
        }
    }
}
