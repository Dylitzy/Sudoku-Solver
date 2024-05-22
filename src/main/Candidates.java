package main;

import config.SudokuCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return null;
    }

    /**
     * Fills in a hidden single found in a specified box of the grid
     * @param grid the grid
     * @param row box row bound
     * @param col box column bound
     * @return a map that maps the row and column index of the cell to the necessary value
     */
    public static Map<List<Integer>, Integer> boxCandidateCheck(SudokuCell[][] grid, int row, int col){
        return null;
    }
}
