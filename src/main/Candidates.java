package main;

import config.SudokuCell;
import config.SudokuConfig;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class used to hold extra methods for dealing with logical sudoku solving.
 *
 * @author Dylan Sturr
 */
public class Candidates {
    /**
     * Fills in any hidden singles found in a specified row of the grid
     * @param grid the grid
     * @param row row to check
     * @return a map that maps the column index of the cell to the necessary value
     */
    public static Map<Integer, Character> rowCandidateCheck(SudokuCell[][] grid, int row){
        int[] count = new int[9];
        for (SudokuCell sc : grid[row]){
            for (Character c : sc.getCandidates()){
                count[Integer.parseInt(String.valueOf(c)) - 1]++;
            }
        }

        Map<Integer, Character> modifications = new HashMap<>();
        for (int i = 0; i < 9; i++){
            if (count[i] == 1){
                for (int j = 0; j < 9; j++){
                    if (grid[row][j].getCandidates().contains((char)('0' + (i + 1)))){
                        modifications.put(j, (char)('0' + (i + 1)));
                    }
                }
            }
        }
        return modifications;
    }

    /**
     * Fills in a hidden single found in a specified column of the grid
     * @param grid the grid
     * @param col column to check
     * @return a map that maps the row index of the cell to the necessary value
     */
    public static Map<Integer, Character> colCandidateCheck(SudokuCell[][] grid, int col){
        int[] count = new int[9];
        for (int i = 0; i < 9; i++){
            for (Character c : grid[i][col].getCandidates()){
                count[Integer.parseInt(String.valueOf(c)) - 1]++;
            }
        }

        Map<Integer, Character> modifications = new HashMap<>();
        for (int j = 0; j < 9; j++){
            if (count[j] == 1){
                for (int k = 0; k < 9; k++){
                    if (grid[k][col].getCandidates().contains((char)('0' + (j + 1)))){
                        modifications.put(k, (char)('0' + (j + 1)));
                    }
                }
            }
        }
        return modifications;
    }

    /**
     * Fills in a hidden single found in a specified box of the grid
     * @param grid the grid
     * @param row box row bound
     * @param col box column bound
     * @return a map that maps the row and column index of the cell to the necessary value
     */
    public static Map<Integer[], Character> boxCandidateCheck(SudokuCell[][] grid, int row, int col){
        return null;
    }

    /**
     * Places val at the specified row and column position in the grid, and also updates any necessary candidates.
     * @param config the sudoku config
     * @param row cell's row to be updated
     * @param col cell's column to be updated
     * @param val the value to be placed at the given row and column position
     */
    public static void configure(SudokuConfig config, int row, int col, char val){

        // remove any instance of the same candidate within the cell's row
        for (SudokuCell sc : config.getGrid()[row]){
            sc.getCandidates().removeIf(c -> c == val);
        }

        // remove any instance of the same candidate within the cell's column
        for (int i = 0; i < 9; i++){
            config.getGrid()[i][col].getCandidates().removeIf(c -> c == val);
        }

        // TODO remove any instance of the same candidate within the cell's box
    }
}
