package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Represents any 9x9 sudoku configuration.
 *
 * @author Dylan Sturr
 */
public class SudokuConfig {
    private final static int DIM = 9;
    private final char[][] grid;
    private int cursorRow;
    private int cursorCol;

    /**
     * Initialize this SudokuConfig based on a given file
     *
     * @param filename the name of the file
     * @throws FileNotFoundException if the filename is invalid or not found
     */
    public SudokuConfig(String filename) throws FileNotFoundException {
        grid = new char[DIM][DIM];
        cursorRow = 0;
        cursorCol = 0;
        int i = 0;
        try (Scanner in = new Scanner(new File(filename))){
            while (in.hasNextLine()){
                String line = in.nextLine();
                String[] fields = line.split(" ");
                for (int j = 0; j < fields.length; j++){
                    grid[i][j] = fields[j].charAt(0);
                }
                i++;
            }
        }
    }

    /**
     * The copy constructor which advances the cursor, creates a new grid,
     * and populates the grid at the cursor location with val
     *
     * @param other the board to copy
     * @param val   the value to store at new cursor location
     */
    public SudokuConfig(SudokuConfig other, char val){
        this.grid = new char[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            this.grid[i] = Arrays.copyOf(other.grid[i], DIM);
        }

        this.cursorRow = other.cursorRow;
        this.cursorCol = other.cursorCol + 1;

        if (cursorCol >= DIM) {
            cursorCol = 0;
            cursorRow++;
        }

        this.grid[cursorRow][cursorCol] = val;
    }

    public List<SudokuConfig> getSuccessors(){
        return null;
    }

    /**
     * Is this Sudoku Configuration valid or not?
     *
     * @return whether the sudoku config is valid or not.
     */
    public boolean isValid(){
        return rowCheck() && colCheck() && boxCheck();
    }

    /**
     * Checks the current row for validity.
     *
     * @return whether the current row is valid or not.
     */
    private boolean rowCheck(){
        Set<Character> uniqueSet = new HashSet<>();
        for (char c : grid[cursorRow]){
            if (c != '-' && !uniqueSet.add(c)){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the current column for validity.
     *
     * @return whether the current row is valid or not.
     */
    private boolean colCheck(){
        Set<Character> uniqueSet = new HashSet<>();
        int i = 0;
        while (i < DIM){
            char c = grid[i][cursorCol];
            if (c != '-' && !uniqueSet.add(c)){
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Checks the current box for validity.
     *
     * @return whether the current box is valid or not.
     */
    private boolean boxCheck(){
        if (cursorRow <= 2){
            if (cursorCol <= 2){
                return iterateBoxBounds(0, 0);
            }
            else if (cursorCol <= 5){
                return iterateBoxBounds(0, 3);
            }
            else{
                return iterateBoxBounds(0, 6);
            }
        }
        else if (cursorRow <= 5){
            if (cursorCol <= 2){
                return iterateBoxBounds(3, 0);
            }
            else if (cursorCol <= 5){
                return iterateBoxBounds(3, 3);
            }
            else{
                return iterateBoxBounds(3, 6);
            }
        }
        else{
            if (cursorCol <= 2){
                return iterateBoxBounds(6, 0);
            }
            else if (cursorCol <= 5){
                return iterateBoxBounds(6, 3);
            }
            else{
                return iterateBoxBounds(6, 6);
            }
        }
    }

    /**
     * Helper method for the method that checks for box validity.
     *
     * @param row the row the box ends at
     * @param col the col the box ends at
     * @return whether the box is unique or not.
     */
    private boolean iterateBoxBounds(int row, int col){
        Set<Character> uniqueSet = new HashSet<>();
        for (int r = row; r < row + 3; r++){
            for (int c = col; c < col + 3; c++){
                char val = grid[r][c];
                if (val != '-' && !uniqueSet.add(val)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * A Sudoku Configuration is a solution if is valid and there are no empty cells.
     *
     * @return whether the sudoku config is a solution or not.
     */
    public boolean isSolution(){
        if (!isValid()){
            return false;
        }
        else{
            for (int r = 0; r < DIM; r++){
                for (int c = 0; c < DIM; c++){
                    if (grid[r][c] == '-'){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getCursorCol() {
        return cursorCol;
    }

    public int getCursorRow() {
        return cursorRow;
    }

    public char getVal(){
        return grid[cursorRow][cursorCol];
    }

    /**
     * Two Sudoku Configurations are equal if they have the same grids.
     *
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
     *
     * @return the hash code
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(grid);
    }

    /**
     * Represents this Sudoku Configuration as a string.
     *
     * @return a string version of this sudoku config
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < DIM; r++){
            for (int c = 0; c < DIM; c++){
                result.append(grid[r][c]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}
