package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Represents any 9x9 sudoku configuration.
 *
 * @author Dylan Sturr
 */
public class SudokuConfig {
    final static int ROWS = 9;
    final static int COLS = 9;
    char[][] grid;

    /**
     * Initialize this SudokuConfig based on a given file
     * @param filename the name of the file
     * @throws FileNotFoundException if the filename is invalid or not found
     */
    public SudokuConfig(String filename) throws FileNotFoundException {
        grid = new char[ROWS][COLS];
        int i = 0;
        try (Scanner in = new Scanner(new File(filename))){
            while (in.hasNextLine()){
                String line = in.nextLine();
                String[] fields = line.split(" ");
                if (!line.isEmpty()){ // why do these files have random extra lines at the end?
                    for (int j = 0; j < fields.length; j++){
                        grid[i][j] = fields[j].charAt(0);
                    }
                    i++;
                }
            }
        }
    }

    /**
     * Initializes the Sudoku configuration based on a given grid, also used to load an empty puzzle
     * @param grid the grid
     */
    public SudokuConfig(char[][] grid){
        this.grid = grid;
    }

    /**
     * Is this Sudoku Configuration valid or not?
     * @return whether the sudoku config is valid or not.
     */
    public boolean isValid(){
        // TODO
        return true;
    }

    /**
     * A Sudoku Configuration is a solution if is valid and there are no empty cells.
     * @return whether the sudoku config is a solution or not.
     */
    public boolean isSolution(){
        if (!isValid()){
            return false;
        }
        else{
            for (int r = 0; r < ROWS; r++){
                for (int c = 0; c < COLS; c++){
                    if (grid[r][c] == '-'){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Two Sudoku Configurations are equal if they have the same grids.
     * @param other the other Sudoku config.
     * @return whether the two sudoku configs are equal or not.
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof SudokuConfig othersc){
            return Arrays.deepEquals(this.grid, othersc.grid);
        }
        return false;
    }

    /**
     * The Hash Code of a Sudoku Configuration is the hash code of its grid.
     * @return the hash code
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(grid);
    }

    /**
     * Represents this Sudoku Configuration as a string.
     * @return a string version of this sudoku config
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < ROWS; r++){
            for (int c = 0; c < COLS; c++){
                result.append(grid[r][c]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}
