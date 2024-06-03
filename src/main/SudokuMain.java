package main;

import config.SudokuCell;
import config.SudokuConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import solver.SudokuSolver;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * The Main Sudoku Class represents the Model, View, and Controller for the GUI of this Sudoku Solver project.
 *
 * @author Dylan Sturr
 */
public class SudokuMain extends Application {
    private Label TopText = new Label("Welcome to Sudoku Solver!");
    private Button loadButton = new Button("Load");
    private Button solveButton = new Button("Solve");
    private Button resetButton = new Button("Reset");
    // TODO: In the future, a button to show display a cell's candidates
    private GridPane gp;
    private BorderPane bp;
    private Button currentButton;
    private char[][] grid;

    /**
     * Creates the Sudoku solver GUI.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage){

        // GUI top
        bp = new BorderPane();
        BorderPane.setAlignment(TopText, Pos.CENTER);
        TopText.setStyle("-fx-font-size:20px");
        bp.setTop(TopText);

        // GUI center
        initGrid();
        makeSudokuGrid(grid);

        // GUI bottom
        FlowPane fp = new FlowPane();

        // initialize the load button
        fp.getChildren().add(loadButton);
        loadButton.setMinHeight(50);
        loadButton.setMinWidth(75);
        loadButton.setStyle("-fx-font-size:16px");
        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Sudoku File");
            String currentPath = Paths.get(".", "data").toAbsolutePath().normalize().toString();
            fileChooser.setInitialDirectory(new File(currentPath));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null){
                load(file);
            }
        });

        // initialize the solve button
        fp.getChildren().add(solveButton);
        solveButton.setMinHeight(50);
        solveButton.setMinWidth(75);
        solveButton.setStyle("-fx-font-size:16px");
        solveButton.setOnAction(event -> {
            TopText.setText("Working...");

            // Solving occurs on a different thread from JavaFX to avoid interruptions of GUI text updates
            Task<Void> solveTask = new Task<>() {
                @Override
                protected Void call() {
                    solve();
                    return null;
                }
            };
            new Thread(solveTask).start();
        });

        // initialize the reset button
        fp.getChildren().add(resetButton);
        resetButton.setMinHeight(50);
        resetButton.setMinWidth(75);
        resetButton.setStyle("-fx-font-size:16px");
        resetButton.setOnAction(event -> {
            initGrid();
            makeSudokuGrid(grid);
            TopText.setText("Reset!");
        });
        fp.setAlignment(Pos.CENTER);
        bp.setBottom(fp);

        // Put all the elements together
        Scene scene = new Scene(bp);
        scene.setOnKeyPressed(this::keyPressed);
        stage.setScene(scene);
        stage.setTitle("Sudoku Solver");
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Keeps track of the button that gets pressed, so that the key listener knows where to place the number.
     * @param button the button that was pushed
     */
    private void setClicked(Button button){
        currentButton = button;
    }

    /**
     * Key listener for whenever a button is pushed
     * @param event the key action event
     */
    private void keyPressed(KeyEvent event){
        if (currentButton != null){
            String text = event.getText();
            int row = GridPane.getRowIndex(currentButton.getParent());
            int col = GridPane.getColumnIndex(currentButton.getParent());
            int subrow = GridPane.getRowIndex(currentButton);
            int subcol = GridPane.getColumnIndex(currentButton);
            int absoluterow = row * 3 + subrow;
            int absolutecol = col * 3 + subcol;
            if (text.matches("[1-9]")){
                grid[absoluterow][absolutecol] = text.charAt(0);
                currentButton.setText(text);
                currentButton = null;
            }
            else if (text.isEmpty()){
                grid[absoluterow][absolutecol] = '-';
                currentButton.setText("");
                currentButton = null;
            }
        }
    }

    /**
     * Initializes the grid as a grid of empty cells. '-'
     */
    private void initGrid(){
        grid = new char[9][9];
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                grid[i][j] = '-';
            }
        }
    }

    /**
     * Initializes the grid GUI based on the grid being worked with.
     * @param grid the grid of the Sudoku puzzle
     */
    private void makeSudokuGrid(char[][] grid){
        gp = new GridPane();
        for (int r = 0; r < 3; r++){
            for (int c = 0; c < 3; c++){
                GridPane box = new GridPane();
                for (int row = 0; row < 3; row++){
                    for (int col = 0; col < 3; col++){
                        String buttonText = String.valueOf(grid[r * 3 + row][c * 3 + col]);
                        Button button = new Button(buttonText.equals("-") ? "" : buttonText);
                        button.setStyle("-fx-font-size:16px; -fx-font-weight:bold");
                        button.setMinSize(50, 50);
                        box.add(button, col, row);
                        button.setOnAction(e -> setClicked(button));
                    }
                }
                gp.add(box, c, r);
            }
        }
        gp.setGridLinesVisible(true);
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);
    }

    /**
     * Recreates the grid GUI to display the solution to the given Sudoku puzzle
     * @param solution the solution to the Sudoku puzzle
     * @param grid the initial grid of the Sudoku puzzle
     */
    private void showSolution(SudokuCell[][] solution, char[][] grid){
        Platform.runLater(() -> {
            gp = new GridPane();
            for (int r = 0; r < 3; r++){
                for (int c = 0; c < 3; c++){
                    GridPane box = new GridPane();
                    for (int row = 0; row < 3; row++){
                        for (int col = 0; col < 3; col++){
                            String buttonText = String.valueOf(solution[r * 3 + row][c * 3 + col]);
                            Button button = new Button(buttonText.equals("-") ? "" : buttonText);
                            if (solution[r * 3 + row][c * 3 + col].getVal() == grid[r * 3 + row][c * 3 + col]){
                                button.setStyle("-fx-font-size:16px; -fx-font-weight:bold");
                            }
                            else{
                                button.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:rgb(30,165,215)");
                            }
                            grid[r * 3 + row][c * 3 + col] = solution[r * 3 + row][c * 3 + col].getVal();
                            button.setMinSize(50, 50);
                            box.add(button, col, row);
                            button.setOnAction(e -> setClicked(button));
                        }
                    }
                    gp.add(box, c, r);
                }
            }
            gp.setGridLinesVisible(true);
            gp.setAlignment(Pos.CENTER);
            bp.setCenter(gp);
        });
    }

    /**
     * The action that occurs when the load file is pushed. It loads a sudoku puzzle given a valid file.
     * @param file the file to be loaded
     */
    private void load(File file){
        // TODO: in the future, check to make sure files follow the valid sudoku syntax.
        grid = new char[9][9];
        try{
            try (Scanner in = new Scanner(file)){
                while (in.hasNextLine()){
                    for (int i = 0; i < 9; i++) {
                        String line = in.nextLine();
                        String[] fields = line.split(" ");
                        for (int j = 0; j < fields.length; j++){
                            grid[i][j] = fields[j].charAt(0);
                        }
                    }
                }
            }
            makeSudokuGrid(grid);
            TopText.setText("Loaded " + file.getName() + "!");
        }
        catch(FileNotFoundException ignored){
            System.err.println("File could not be found!");
        }
    }

    /**
     * The action that occurs when the solve button is pushed. This method is broken down into three steps,
     * which are documented as the code goes on.
     */
    private void solve(){
        try{
            // write the grid to an output file, custom.txt, so a Sudoku Configuration can be made from it later
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("data/custom.txt"))){
                for (int r = 0; r < 9; r++){
                    for (int c = 0; c < 9; c++){
                        fileWriter.write(grid[r][c]);
                        fileWriter.write(" ");
                    }
                    fileWriter.newLine();
                }
            }

            // checks for puzzles that are unsolvable due to lack of information, like not enough values
            // TODO: in the future, checks for blatantly invalid puzzles (break the rules)
            int[] count = new int[9];
            int sum;
            int j = 0;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (Character.isDigit(grid[r][c])) {
                        int index = grid[r][c] - '1';
                        if (index >= 0 && index < 9) {
                            count[index]++;
                        }
                    }
                }
            }
            for (int i : count){
                if (i == 0){
                    j++;
                }
                if (j == 2){
                    Platform.runLater(() -> TopText.setText("Error: Not enough unique values"));
                    return;
                }
            }
            sum = Arrays.stream(count).sum();
            if (sum < 16){
                Platform.runLater(() -> TopText.setText("Error: Need at least 16 values, there are only " + sum));
                return;
            }

            // Solve the puzzle, unless it is already solved
            SudokuConfig sc = new SudokuConfig("data/custom.txt");
            if (sc.isSolution()){
                Platform.runLater(() -> TopText.setText("Already solved!"));
            }
            else{
                SudokuSolver solver = new SudokuSolver();
                sc = solver.soften(sc); // partial logical solver
                Optional<SudokuConfig> solution = solver.solve(sc); // solver using backtracking
                if (solution.isPresent()){
                    showSolution(solution.get().getGrid(), grid);
                    Platform.runLater(() -> TopText.setText("Solved!"));
                }
                else{
                    Platform.runLater(() -> TopText.setText("No Solution!"));
                }
            }
        }
        catch(IOException ignored){
            System.err.println("Something went wrong!");
        }
    }

    /**
     * The main method simply launches the GUI application.
     * @param args these are unused
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}