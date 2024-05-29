package solver;

import config.SudokuCell;
import config.SudokuConfig;
import main.Candidates;

import java.util.List;
import java.util.Optional;
import java.util.Map;

/**
 * Utilizes Backtracking to solve a given Sudoku Configuration.
 *
 * @author Dylan Sturr
 */
public class SudokuSolver {
    /**
     * Utilizes logic to partially solve a sudoku puzzle, allowing the backtracker to be more efficient.
     * @param sc the Sudoku puzzle to partially solve
     * @return the partially solved puzzle
     */
    public SudokuConfig soften(SudokuConfig sc){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                List<Character> candidates = sc.getCellCandidates(i, j);
                if (candidates.size() == 1){
                    sc.getGrid()[i][j].setVal(candidates.getFirst());
                    Candidates.configure(sc, i, j, candidates.getFirst(), 'n');
                }
            }
            Map<Integer, Character> newRow = Candidates.rowCandidateCheck(sc.getGrid(), i);
            for (int k : newRow.keySet()){
                sc.getGrid()[i][k].setVal(newRow.get(k));
                Candidates.configure(sc, i, k, newRow.get(k), 'r');
            }
        }

        for (int i = 0; i < 9; i++){
            SudokuCell[][] grid = sc.getGrid();
            Map<Integer, Character> newCol = Candidates.colCandidateCheck(grid, i);
            for (int j : newCol.keySet()){
                grid[j][i].setVal(newCol.get(j));
                Candidates.configure(sc, j, i, newCol.get(j), 'c');
            }
        }

        for (int i = 1; i < 7; i += 2){
            for (int j = 1; j < 7; j += 2){
                Map<Integer[], Character> newBox = Candidates.boxCandidateCheck(sc.getGrid(), i, j);
                for (Integer[] k : newBox.keySet()){
                    sc.getGrid()[k[0]][k[1]].setVal(newBox.get(k));
                    Candidates.configure(sc, k[0], k[1], newBox.get(k), 'b');
                }
            }
        }

        return sc;
    }

    /**
     * The brute force method for solving a sudoku (backtracking)
     * @param sc the Sudoku puzzle to solve
     * @return the solved puzzle, or Optional.empty() is there is no solution.
     */
    public Optional<SudokuConfig> solve(SudokuConfig sc){
        if (sc.isSolution()){
            return Optional.of(sc);
        }
        else{
            List<SudokuConfig> successors = sc.getSuccessors();
            for (SudokuConfig ss : successors){
                if (ss.isValid(ss.getCursorRow(), ss.getCursorCol())){
                    Optional<SudokuConfig> sol = solve(ss);
                    if (sol.isPresent()){
                        return sol;
                    }
                }
            }
            return Optional.empty();
        }
    }
}
