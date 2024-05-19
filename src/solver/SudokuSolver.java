package solver;

import config.SudokuConfig;

import java.util.List;
import java.util.Optional;

/**
 * Utilizes Depth-first search and Backtracking to solve a given Sudoku Configuration.
 *
 * @author Dylan Sturr
 */
public class SudokuSolver {
    public Optional<SudokuConfig> solve(SudokuConfig sc){
        if (sc.isSolution()){
            return Optional.of(sc);
        }
        else{
            List<SudokuConfig> successors = sc.getSuccessors();
            for (SudokuConfig ss : successors){
                if (ss.isValid()){
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
