package solver;

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
                sc.getCellCandidates(i, j);
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
