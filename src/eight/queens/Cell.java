package eight.queens;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Saad
 */
public class Cell extends StackPane {

    Rectangle rect;
    Paint fill;
    String image;
    int x , y;
    int index;

    public Cell() {
        rect = new Rectangle(70, 70);
        rect.setFill(Color.TRANSPARENT);
        getChildren().add(rect);
    }

    public Cell(double width, double height, Paint fill, int x, int y) {
        this.fill = fill;
        this.x = x;
        this.y = y;
        rect = new Rectangle(width, height);
        rect.setFill(fill);
        rect.setStroke(Color.DARKCYAN);
        getChildren().add(rect);
    }

    //get row of the cell
    public int getX() {
        return x;
    }

    //get column of the cell
    public int getY() {
        return y;
    }

    public void setNullImage(){
        this.image = null;
        if(getChildren().size() == 1){
        }else{
            getChildren().remove(1);
        }
    }

    public void setImage() {
        Image omg = new Image(Cell.class.getResourceAsStream("queen.png"));
        this.image = "queen";
        ImageView imageView = new ImageView();
        imageView.setImage(omg);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        getChildren().add(imageView);
    }

    public void setBackgroundColor(){
        rect.setFill(Color.TEAL);
    }

    public Paint getFill() {
        return fill;
    }

    public void setFill(Paint fill) {
        this.fill = fill;
        this.rect.setFill(fill);
    }

    public boolean hasImage(){
        if(this.image == null){
            return false;
        }else{
            return true;
        }
    }

}
