package main;

import config.SudokuConfig;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import solver.SudokuSolver;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * The Main Sudoku Class represents the Model, View, and Controller for the GUI of this Sudoku Solver project.
 *
 * @author Dylan Sturr
 */
public class SudokuMain extends Application {
    Label TopText = new Label("Welcome to Sudoku Solver!");
    Button loadButton = new Button("Load");
    Button solveButton = new Button("Solve");
    Button resetButton = new Button("Reset");
    // display candidates button somewhere in the future

    @Override
    public void start(Stage stage) {
        // GUI top
        BorderPane bp = new BorderPane();
        BorderPane.setAlignment(TopText, Pos.CENTER);
        TopText.setStyle("-fx-font-size:20px");
        bp.setTop(TopText);

        // GUI center
        GridPane gp = new GridPane();
        for (int r = 0; r < 3; r++){
            for (int c = 0; c < 3; c++){
                GridPane box = new GridPane();
                for (int row = 0; row < 3; row++){
                    for (int col = 0; col < 3; col++){
                        Button button = new Button();
                        button.setMinSize(50, 50);
                        box.add(button, row, col);
                    }
                }
                gp.add(box, r, c);
            }
        }
        gp.setGridLinesVisible(true);
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // GUI bottom
        FlowPane fp = new FlowPane();
        fp.getChildren().add(loadButton);
        loadButton.setMinHeight(50);
        loadButton.setMinWidth(75);
        loadButton.setStyle("-fx-font-size:16px");
        fp.getChildren().add(solveButton);
        solveButton.setMinHeight(50);
        solveButton.setMinWidth(75);
        solveButton.setStyle("-fx-font-size:16px");
        fp.getChildren().add(resetButton);
        resetButton.setMinHeight(50);
        resetButton.setMinWidth(75);
        resetButton.setStyle("-fx-font-size:16px");
        fp.setAlignment(Pos.CENTER);
        bp.setBottom(fp);

        // Put all the elements together
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        SudokuConfig sc = null;
        try{
            sc = new SudokuConfig("data/empty.txt");
        }
        catch (FileNotFoundException ignored){
            System.err.println("Something went wrong!");
        }
        Application.launch(args);
    }
}