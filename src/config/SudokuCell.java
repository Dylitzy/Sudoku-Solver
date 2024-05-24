package config;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single cell in the Sudoku Grid.
 *
 * @author Dylan Sturr
 */
public class SudokuCell {
    private final int row;
    private final int col;
    private char val;
    private List<Character> candidates;

    /**
     * Constructor for the SudokuCell. Candidates will always begin as an empty set.
     * @param row the cell's row
     * @param col the cell's column
     * @param val the character value of the cell
     */
    public SudokuCell(int row, int col, char val){
        this.row = row;
        this.col = col;
        this.val = val;
        candidates = new ArrayList<>();
    }

    /**
     * Gets the value of this cell
     * @return the value
     */
    public char getVal(){
        return val;
    }

    /**
     * Changes the value of this cell
     * @param newVal the new value to replace the old value
     */
    public void setVal(char newVal){
        val = newVal;
    }

    /**
     * Gets the column location of the cell
     * @return the cell's column
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the row location of the cell
     * @return the cell's row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the list of this cell's candidates
     * @return the list of candidates
     */
    public List<Character> getCandidates() {
        return candidates;
    }

    /**
     * Sets the list of this cell's candidates, only used when
     * copying from another configuration or clearing the list
     */
    public void setCandidates(List<Character> candidates) {
        this.candidates = candidates;
    }

    /**
     * Adds a candidate to the list of this cell's candidates
     * @param c the candidate to be added
     */
    public void addCandidate(char c){
        candidates.add(c);
    }

    /**
     * The string representation of a SudokuCell is just its value.
     * @return a string representation of this SudokuCell
     */
    @Override
    public String toString() {
        return "" + val;
    }

    /**
     * Two SudokuCells are equal if they have the same row, column, and value.
     * @param other the other sudoku cell
     * @return whether the sudoku cells are equal or not
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof SudokuCell sc){
            return this.row == sc.row && this.col == sc.col && this.val == sc.val;
        }
        return false;
    }
}
