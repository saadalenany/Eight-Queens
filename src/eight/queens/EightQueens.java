/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eight.queens;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Saad
 */
public class EightQueens extends Application {

    int axis = 8;

    public static int HEIGHT = 600;
    public static int WIDTH = 600;

    public static Text time;
    public static Calendar c;
    public static DateFormat dateFormat;

    Cell[][] cells;
    Cell[] images;

    int index;

    VBox vbox;
    HBox centerHBox;

    Timer t = new Timer(1000, new ClockListener());

    boolean imageDropped = false;
    boolean available[][];

    @Override
    public void start(Stage primaryStage) {

        available = new boolean[axis][axis];

        //get current date
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date oldtime = new Date();

        dateFormat.format(oldtime);
        c = Calendar.getInstance();
        c.setTime(oldtime);

        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);

        t.setInitialDelay(0);

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        vbox = new VBox();
        vbox.setPadding(new Insets(0, 10, 0, 10));
        vbox.setSpacing(4);
        vbox.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(1), new Insets(0))));

        centerHBox = new HBox();
        centerHBox.getChildren().addAll(grid, vbox);

        images = new Cell[axis];

        for (int i = 0; i < axis; i++) {
            images[i] = new Cell();
            images[i].setImage();
            vbox.getChildren().add(images[i]);
        }

        time = new Text("Time");
        time.setStyle("-fx-font: 20 Arial, Tahoma;");
        time.setFill(Color.DARKBLUE);

        t.start();

        root.setTop(time);
        root.setCenter(centerHBox);
        cells = new Cell[8][8];

        for (int i = 0; i < axis; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < axis; j++) {
                    if (j % 2 == 0) {
                        cells[i][j] = new Cell(WIDTH / axis, HEIGHT / axis, Color.CORNFLOWERBLUE, i, j);
                        grid.add(cells[i][j], j, i);
                    } else {
                        cells[i][j] = new Cell(WIDTH / axis, HEIGHT / axis, Color.ANTIQUEWHITE, i, j);
                        grid.add(cells[i][j], j, i);
                    }
                    available[i][j] = true;
                }
            } else {
                for (int j = 0; j < axis; j++) {
                    if (j % 2 == 0) {
                        cells[i][j] = new Cell(WIDTH / axis, HEIGHT / axis, Color.ANTIQUEWHITE, i, j);
                        grid.add(cells[i][j], j, i);
                    } else {
                        cells[i][j] = new Cell(WIDTH / axis, HEIGHT / axis, Color.CORNFLOWERBLUE, i, j);
                        grid.add(cells[i][j], j, i);
                    }
                    available[i][j] = true;
                }
            }
        }

        for (int i = 0; i < axis; i++) {
            for (int j = 0; j < axis; j++) {
                int x = i, y = j;

                cells[i][j].setOnMousePressed(e -> {
                    if (images[index].getChildren().size() > 2) {
                        images[index].getChildren().remove(2);
                    }
                    setDefaultAvailibilities();
                    setQueenRanges();
                });

                cells[i][j].setOnDragDetected(e -> {
                    if (cells[x][y].hasImage()) {
                        Dragboard db = cells[x][y].startDragAndDrop(TransferMode.ANY);
                        cells[x][y].setNullImage();
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(new Image(EightQueens.class.getResourceAsStream("queen.png"), 70, 70, true, true));
                        db.setContent(content);
                    }
                });

                cells[i][j].setOnDragOver(e -> {
                    e.acceptTransferModes(TransferMode.ANY);
                    setAvailability();
                    if (!testCell(x, y)) {
                        cells[x][y].setFill(Color.BROWN);
                    }
                });

                cells[i][j].setOnDragExited(e -> {
                    e.acceptTransferModes(TransferMode.ANY);
                    setInitialBackgrounds();
                    if (!testCell(x, y)) {
                        cells[x][y].setFill(Color.BROWN);
                    }
                });

                cells[i][j].setOnDragDropped(e -> {
                    e.acceptTransferModes(TransferMode.ANY);
                    if (cells[x][y].hasImage()) {
                        images[index].setImage();
                        imageDropped = false;
                    } else if (testCell(x, y)) {
                        cells[x][y].setImage();
                        setCellConstraints(x,y);
                        images[index].setNullImage();
                        imageDropped = true;
                        if (testImagesBuffer()) {
                            System.out.println("U win!");
                            t.stop();
                            time.setText("U won in " + time.getText());
                        }
                    } else {
                        cells[x][y].setNullImage();
                        images[index].setImage();
                        imageDropped = false;
                    }
                    testImagesNumber();
                });

                cells[i][j].setOnDragDone(e -> {
                    setInitialBackgrounds();
                });

            }
        }

        for (int i = 0; i < axis; i++) {
            int x = i;

            images[i].setOnMousePressed(e -> {
                if (images[x].getChildren().size() > 2) {
                    images[x].getChildren().remove(2);
                }
                index = x;
            });

            images[i].setOnDragDetected(e -> {
                if (images[x].hasImage()) {
                    Dragboard db = images[x].startDragAndDrop(TransferMode.ANY);
                    images[x].setNullImage();
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(new Image(EightQueens.class.getResourceAsStream("queen.png"), 70, 70, true, true));
                    db.setContent(content);
                }
            });

            images[i].setOnDragDone(e -> {
                if (!imageDropped) {
                    images[index].setImage();
                }
                imageDropped = false;
                setInitialBackgrounds();
            });
        }

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());

        primaryStage.setTitle("Eight Queens!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setAvailability(){
        for(int i=0 ; i<axis ; i++){
            for(int j=0 ; j<axis ; j++){
                if(available[i][j]){
                    cells[i][j].setFill(Color.GREEN);
                }else{
                    cells[i][j].setFill(Color.BROWN);
                }
            }
        }
    }

    public void testImagesNumber() {
        //if empty images < grid images
        if (testNumberOfImages().size() < testNumImagesInGrid()) {
            for (int z = 0; z < testNumImagesInGrid() - testNumberOfImages().size(); z++) {
                images[testNumberOfImages().get(z)].setImage();
            }
        } //if empty images > grid images
        else if (testNumberOfImages().size() > testNumImagesInGrid()) {
            for (int z = 0; z < testNumberOfImages().size() - testNumImagesInGrid(); z++) {
                images[testNumberOfImages().get(z)].setImage();
            }
        }
    }

    public void setDefaultAvailibilities(){
        for(int i=0 ; i<axis ; i++){
            for(int j=0 ; j<axis ; j++){
                available[i][j] = true;
            }
        }
    }

    public void setQueenRanges(){
        for(int i=0 ; i<axis ; i++){
            for(int j=0 ; j<axis ; j++){
                if(cells[i][j].hasImage()){
                    setCellConstraints(i, j);
                }
            }
        }
    }

    public void setCellConstraints(int x, int y){
        //down right
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if(i <= 7 && j <= 7){
                available[i][j] = false;
            }
        }

        //down left
        for (int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if(i <= 7 && j >= 0){
                available[i][j] = false;
            }
        }

        //up left
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if(i >= 0 && j >= 0){
                available[i][j] = false;
            }
        }

        //up right
        for (int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if(i >= 0 && j <= 7){
                available[i][j] = false;
            }
        }

        for (int i = x + 1; i < 8; i++) {
            if(i <= 7){
                available[i][y] = false;
            }
        }

        for (int i = x - 1; i >= 0; i--) {
            if(i >= 0){
                available[i][y] = false;
            }
        }

        for (int i = y + 1; i < 8; i++) {
            if(i <= 7){
                available[x][i] = false;
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if(i >= 0){
                available[x][i] = false;
            }
        }
    }

    public boolean testCell(int x, int y) {

        for (int i = 0; i < axis; i++) {
            //cells on the same row
            if (cells[x][i].hasImage()) {
                cells[x][i].setFill(Color.BROWN);
                return false;
            }

            //cells on the same column
            if (cells[i][y].hasImage()) {
                cells[i][y].setFill(Color.BROWN);
                return false;
            }

            //cells on the same diagonal on decreasing order
            if (x - i >= 0 && y - i >= 0) {
                if (cells[x - i][y - i].hasImage()) {
                    cells[x - i][y - i].setFill(Color.BROWN);
                    return false;
                }
            }

            //cells on the same diagonal on increasing order
            if (x + i <= 7 && y + i <= 7) {
                if (cells[x + i][y + i].hasImage()) {
                    cells[x + i][y + i].setFill(Color.BROWN);
                    return false;
                }
            }

            //cells on the same diagonal on decreasing order for x & increasing order for y
            if (x - i >= 0 && y + i <= 7) {
                if (cells[x - i][y + i].hasImage()) {
                    cells[x - i][y + i].setFill(Color.BROWN);
                    return false;
                }
            }

            //cells on the same diagonal on increasing order for x & decreasing order for y
            if (x + i <= 7 && y - i >= 0) {
                if (cells[x + i][y - i].hasImage()) {
                    cells[x + i][y - i].setFill(Color.BROWN);
                    return false;
                }
            }

        }

        return true;
    }

    public boolean testImagesBuffer() {
        for (int i = 0; i < axis; i++) {
            if (images[i].hasImage()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> testNumberOfImages() {
        ArrayList<Integer> available = new ArrayList<>();
        if (testNumImagesInGrid() > 0) {
            for (int i = 0; i < images.length; i++) {
                if (!images[i].hasImage()) {
                    available.add(i);
                }
            }
        }
        return available;
    }

    public int testNumImagesInGrid() {
        int counter = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].hasImage()) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public void setInitialBackgrounds() {
        for (int i = 0; i < axis; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < axis; j++) {
                    if (j % 2 == 0) {
                        cells[i][j].setFill(Color.CORNFLOWERBLUE);
                    } else {
                        cells[i][j].setFill(Color.ANTIQUEWHITE);
                    }
                }
            } else {
                for (int j = 0; j < axis; j++) {
                    if (j % 2 == 0) {
                        cells[i][j].setFill(Color.ANTIQUEWHITE);
                    } else {
                        cells[i][j].setFill(Color.CORNFLOWERBLUE);
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
