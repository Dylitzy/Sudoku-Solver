package config;

/**
 * Represents any 9x9 sudoku configuration.
 *
 * @author Dylan Sturr
 */
public class SudokuConfig {
    final static int ROWS = 9;
    final static int COLS = 9;
    int[][] grid;

    public SudokuConfig(String filename){
        grid = new int[ROWS][COLS];
    }

    public SudokuConfig(int[][] grid){
        this.grid = grid;
    }
}
