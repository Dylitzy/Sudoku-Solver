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

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

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

    @Override
    public void start(Stage stage){

        // GUI top
        bp = new BorderPane();
        BorderPane.setAlignment(TopText, Pos.CENTER);
        TopText.setStyle("-fx-font-size:20px");
        bp.setTop(TopText);

        // GUI center
        makeSudokuGrid(new String[9][9]);

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

            // Take the file and convert into a 2D array of strings, helpful when setting up the GUI for the loaded puzzle.
            String[][] boardsetup = new String[9][9];
            try{
                try (Scanner in = new Scanner(file)){
                    while (in.hasNextLine()){
                        for (int i = 0; i < 9; i++) {
                            String line = in.nextLine();
                            String[] fields = line.split(" ");
                            for (int j = 0; j < fields.length; j++){
                                if (!fields[j].equals("-")){
                                    boardsetup[i][j] = fields[j];
                                }
                            }
                        }
                    }
                }
                makeSudokuGrid(boardsetup);
                TopText.setText("Loaded " + file.getName() + "!");
            }
            catch(FileNotFoundException ignored){
                System.err.println("File could not be found!");
            }
        });
        fp.getChildren().add(solveButton);
        solveButton.setMinHeight(50);
        solveButton.setMinWidth(75);
        solveButton.setStyle("-fx-font-size:16px");
        solveButton.setOnAction(event -> {
           // TODO
        });
        fp.getChildren().add(resetButton);
        resetButton.setMinHeight(50);
        resetButton.setMinWidth(75);
        resetButton.setStyle("-fx-font-size:16px");
        resetButton.setOnAction(event -> {
            makeSudokuGrid(new String[9][9]);
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

    private void makeSudokuGrid(String[][] text){
        gp = new GridPane();
        for (int r = 0; r < 3; r++){
            for (int c = 0; c < 3; c++){
                GridPane box = new GridPane();
                for (int row = 0; row < 3; row++){
                    for (int col = 0; col < 3; col++){
                        Button button = new Button(text[r * 3 + row][c * 3 + col]);
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