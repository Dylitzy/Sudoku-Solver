package main;

import config.SudokuCell;
import config.SudokuConfig;

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
                count[c - '1']++;
            }
        }

        Map<Integer, Character> modifications = new HashMap<>();
        for (int i = 0; i < 9; i++){
            if (count[i] == 1){
                for (int j = 0; j < 9; j++){
                    if (grid[row][j].getCandidates().contains((char)('1' + i))){
                        modifications.put(j, (char)('1' + i));
                        break;
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
                count[c - '1']++;
            }
        }

        Map<Integer, Character> modifications = new HashMap<>();
        for (int i = 0; i < 9; i++){
            if (count[i] == 1){
                for (int j = 0; j < 9; j++){
                    if (grid[j][col].getCandidates().contains((char)('1' + i))){
                        modifications.put(j, ((char)('1' + i)));
                        break;
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
        int[] count = new int[9];
        int rowbound = (row / 3) * 3;
        int colbound = (col / 3) * 3;

        for (int r = rowbound; r < rowbound + 3; r++){
            for (int c = colbound; c < colbound + 3; c++){
                for (Character ch : grid[r][c].getCandidates()){
                    count[ch - '1']++;
                }
            }
        }

        Map<Integer[], Character> modifications = new HashMap<>();
        for (int i = 0; i < 9; i++){
            if (count[i] == 1){
                for (int r = rowbound; r < rowbound + 3; r++){
                    for (int c = colbound; c < colbound + 3; c++){
                        if (grid[r][c].getCandidates().contains((char)('1' + i))){
                            modifications.put(new Integer[]{r, c}, (char)('1' + i));
                            break;
                        }
                    }
                }
            }
        }
        return modifications;
    }

    /**
     * Updates necessary candidates for the newly updated cell at the specified row and column position.
     * @param config the sudoku config
     * @param row cell's row to be updated
     * @param col cell's column to be updated
     * @param val the value to be placed at the given row and column position
     */
    public static void configure(SudokuConfig config, int row, int col, char val, char check){

        // remove any instance of the same candidate within the cell's row if a row was not solved for
        if (check != 'r'){
            for (SudokuCell sc : config.getGrid()[row]){
                sc.getCandidates().removeIf(c -> c == val);
            }
        }

        // remove any instance of the same candidate within the cell's column if a column was not solved for
        if (check != 'c'){
            for (int i = 0; i < 9; i++){
                config.getGrid()[i][col].getCandidates().removeIf(c -> c == val);
            }
        }

        // remove any instance of the same candidate within the cell's box if a box was not solved for
        if (check != 'b'){
            int rowbound = (row / 3) * 3;
            int colbound = (col / 3) * 3;

            for (int r = rowbound; r < rowbound + 3; r++){
                for (int c = colbound; c < colbound + 3; c++){
                    config.getGrid()[r][c].getCandidates().removeIf(ch -> ch == val);
                }
            }
        }

        config.getGrid()[row][col].setCandidates(new ArrayList<>());
    }
}
