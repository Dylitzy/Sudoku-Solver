package main;

import config.SudokuCell;
import config.SudokuConfig;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
    // display candidates button somewhere in the future
    private GridPane gp;
    private BorderPane bp;
    private Button currentButton;
    private char[][] grid;

    @Override
    public void start(Stage stage){

        // GUI top
        bp = new BorderPane();
        BorderPane.setAlignment(TopText, Pos.CENTER);
        TopText.setStyle("-fx-font-size:20px");
        bp.setTop(TopText);

        // GUI center
        grid = makeSudokuGrid(new char[9][9]);

        // GUI bottom
        FlowPane fp = new FlowPane();
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
            load(file);
        });
        fp.getChildren().add(solveButton);
        solveButton.setMinHeight(50);
        solveButton.setMinWidth(75);
        solveButton.setStyle("-fx-font-size:16px");
        solveButton.setOnAction(event -> solve());
        fp.getChildren().add(resetButton);
        resetButton.setMinHeight(50);
        resetButton.setMinWidth(75);
        resetButton.setStyle("-fx-font-size:16px");
        resetButton.setOnAction(event -> {
            makeSudokuGrid(new char[9][9]);
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

    private void setClicked(Button button){
        currentButton = button;
    }

    private void keyPressed(KeyEvent event){
        if (currentButton != null){
            String text = event.getText();
            if (text.matches("[1-9]")){
                currentButton.setText(text);
                currentButton = null;
            }
            else if (text.isEmpty()){
                currentButton.setText("");
                currentButton = null;
            }
        }
    }

    private char[][] makeSudokuGrid(char[][] grid){
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
        return grid;
    }

    private char[][] showSolution(SudokuCell[][] solution, char[][] grid){
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
        return grid;
    }

    private void load(File file){
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

    private void solve(){
        try{
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("data/custom.txt"))){
                for (int r = 0; r < 9; r++){
                    for (int c = 0; c < 9; c++){
                        fileWriter.write(grid[r][c]);
                        fileWriter.write(" ");
                    }
                    fileWriter.newLine();
                }
            }

            // TODO: checks for less than 8 unique values and less than 16 values

            SudokuConfig sc = new SudokuConfig("data/custom.txt");
            if (sc.isSolution()){
                TopText.setText("Already solved!");
            }
            else{
                TopText.setText("Working...");
                SudokuSolver solver = new SudokuSolver();
                sc = solver.soften(sc);
                Optional<SudokuConfig> solution = solver.solve(sc);
                if (solution.isPresent()){
                    showSolution(solution.get().getGrid(), grid);
                    TopText.setText("Solved!");
                }
                else{
                    TopText.setText("No Solution!");
                }
            }
        }
        catch(IOException ignored){
            System.err.println("Something went wrong!");
        }
    }

    public static void main(String[] args) {
        try{
            new SudokuConfig("data/empty.txt");
        }
        catch (FileNotFoundException ignored){
            System.err.println("Something went wrong!");
        }
        Application.launch(args);
    }
}