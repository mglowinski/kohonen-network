import java.io.IOException;

public class Main {

    private static final int SIZE_OF_FRAME = 2;
    private static final int NUMBER_OF_NEURONS = 5;
    private static final double LEARNING_STEP = 0.01;

    public static void main(String[] args) {
        try {
            int[][] imageArray = Images.loadImage("images/boat.png");
            KohonenAlgorithm network =
                    new KohonenAlgorithm(SIZE_OF_FRAME, NUMBER_OF_NEURONS, LEARNING_STEP);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
