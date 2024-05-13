package main;
import config.SudokuConfig;
import GUI.SudokuGUI;
import solver.SudokuSolver;

/**
 * The Main Sudoku class represents the model for the GUI and holds solutions to valid sudoku puzzles.
 *
 * @author Dylan Sturr
 */
public class SudokuMain {
    public static void main(String[] args) {
        SudokuConfig sc = null;
        if (args.length == 0){
            sc = new SudokuConfig(new int[9][9]);
        }
        else{
            sc = new SudokuConfig(args[0]);
        }
    }
}
