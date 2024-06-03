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
    private final SudokuCell[][] grid;
    private int cursorRow;
    private int cursorCol;

    /**
     * Initialize this SudokuConfig based on a given file
     *
     * @param filename the name of the file
     * @throws FileNotFoundException if the filename is invalid or not found
     */
    public SudokuConfig(String filename) throws FileNotFoundException {
        grid = new SudokuCell[DIM][DIM];
        cursorRow = 0;
        cursorCol = -1;
        try (Scanner in = new Scanner(new File(filename))){
            while (in.hasNextLine()){
                for (int i = 0; i < DIM; i++) {
                    String line = in.nextLine();
                    String[] fields = line.split(" ");
                    for (int j = 0; j < fields.length; j++){
                        grid[i][j] = new SudokuCell(i, j, fields[j].charAt(0));
                    }
                }
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
        this.grid = new SudokuCell[DIM][DIM];
        for (int i = 0; i < DIM; i++){
            for (int j = 0; j < DIM; j++){
                this.grid[i][j] = new SudokuCell(i, j, other.grid[i][j].getVal());
            }
        }

        this.cursorRow = other.cursorRow;
        this.cursorCol = other.cursorCol + 1;

        if (cursorCol >= DIM) {
            cursorCol = 0;
            if (cursorRow + 1 < DIM){
                cursorRow++;
            }
        }

        this.grid[cursorRow][cursorCol].setVal(val);
    }

    /**
     * The constructor used to determine validity of a cell's candidates.
     *
     * @param other the board to copy
     * @param val the value to store at the cell's location
     * @param row cell's row
     * @param col cell's column
     */
    public SudokuConfig(SudokuConfig other, char val, int row, int col){
        this.grid = new SudokuCell[DIM][DIM];
        for (int i = 0; i < DIM; i++){
            for (int j = 0; j < DIM; j++){
                this.grid[i][j] = new SudokuCell(i, j, other.grid[i][j].getVal());
                this.grid[i][j].setCandidates(other.grid[i][j].getCandidates());
            }
        }
        this.grid[row][col].setVal(val);
    }

    /**
     * Constructs and returns a list of this configuration's valid successors.
     *
     * @return the list of this sudoku config's valid successors.
     */
    public List<SudokuConfig> getSuccessors(){
        List<SudokuConfig> successors = new ArrayList<>();
        SudokuCell nextCell = cursorCol + 1 < DIM ? grid[cursorRow][cursorCol + 1] : cursorRow + 1 < DIM ? grid[cursorRow + 1][0] : grid[0][0];
        if (nextCell.getVal() != '-'){
            successors.add(new SudokuConfig(this, nextCell.getVal()));
        }
        else{
            List<Character> candidates = getCellCandidates(nextCell.getRow(), nextCell.getCol());
            for (char c : candidates){
                successors.add(new SudokuConfig(this, c));
            }
        }

        return successors;
    }

    /**
     * Constructs the list of a cell's candidates, based on that cell's row and column position
     * @param row cell's row
     * @param col cell's column
     * @return a list of the cell's candidates
     */
    public List<Character> getCellCandidates(int row, int col){
        if (!grid[row][col].getCandidates().isEmpty()){
            return grid[row][col].getCandidates();
        }
        else if (grid[row][col].getVal() == '-'){
            for (char c = '1'; c <= '9'; c++){
                SudokuConfig candidate = new SudokuConfig(this, c, row, col);
                if (candidate.isValid(row, col)){
                    grid[row][col].addCandidate(c);
                }
            }
            return grid[row][col].getCandidates();
        }
        return new ArrayList<>();
    }

    /**
     * Is this Sudoku Configuration valid or not?
     *
     * @return whether the sudoku config is valid or not.
     */
    public boolean isValid(int row, int col){
        return rowCheck(row) && colCheck(col) && boxCheck(row, col);
    }

    /**
     * Checks the current row for validity.
     *
     * @return whether the current row is valid or not.
     */
    public boolean rowCheck(int row){
        Set<Character> uniqueSet = new HashSet<>();
        for (SudokuCell c : grid[row]){
            if (c.getVal() != '-' && !uniqueSet.add(c.getVal())){
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
    public boolean colCheck(int col){
        Set<Character> uniqueSet = new HashSet<>();
        int i = 0;
        while (i < DIM){
            char c = grid[i][col].getVal();
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
    public boolean boxCheck(int row, int col){
        int absoluterow = (row / 3) * 3;
        int absolutecol = (col / 3) * 3;
        Set<Character> uniqueSet = new HashSet<>();
        for (int r = absoluterow; r < absoluterow + 3; r++){
            for (int c = absolutecol; c < absolutecol + 3; c++){
                char val = grid[r][c].getVal();
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
        for (int r = 0; r < DIM; r++){
            for (int c = 0; c < DIM; c++){
                if (grid[r][c].getVal() == '-'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the row the cursor is pointing at
     * @return cursor row
     */
    public int getCursorRow() {
        return cursorRow;
    }

    /**
     * Gets the column the cursor is pointing at
     * @return cursor column
     */
    public int getCursorCol() {
        return cursorCol;
    }

    /**
     * Gets the grid that represents the Sudoku Puzzle
     * @return the grid
     */
    public SudokuCell[][] getGrid() {
        return grid;
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
                result.append(grid[r][c]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
