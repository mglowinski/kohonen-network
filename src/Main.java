import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            int[][] imageArray = Images.loadImage("images/boat.png");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
