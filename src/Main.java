import java.io.IOException;

public class Main {

    private static final int SIZE_OF_FRAME = 4;
    private static final int NUMBER_OF_TRAINING_FRAMES = 4;
    private static final int NUMBER_OF_NEURONS = 10;
    private static final double LEARNING_STEP = 0.01;
    private static final int BITS_PER_VALUE = 8;
    private static final int NUMBER_OF_TRAINING_EPOCHS = 500;

    public static void main(String[] args) {
        try {
            int[][] imageArray = Images.loadImage("images/lena.png");
            KohonenAlgorithm network =
                    new KohonenAlgorithm(SIZE_OF_FRAME, NUMBER_OF_NEURONS, LEARNING_STEP);

            network.trainNetwork(NUMBER_OF_TRAINING_EPOCHS, NUMBER_OF_TRAINING_FRAMES, imageArray);

            CompressionResult compressionResult = network.compressImage(imageArray);

            int[][] decompressedImage = network.decompressImage(compressionResult);
            Images.saveImage(decompressedImage, "saved.png");

            computeResult(imageArray, decompressedImage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void computeResult(int[][] originalImage, int[][] decompressedImage) {
        double PSNR = Utils.computePSNR(originalImage, decompressedImage);
        System.out.println("PSNR: " + Utils.round(PSNR) + " dB");
        double compressionFactor = Utils.computeCompressionFactor(originalImage, SIZE_OF_FRAME,
                NUMBER_OF_NEURONS, BITS_PER_VALUE);
        System.out.println("Compression factor: " + Utils.round(compressionFactor) + "%");
    }

}
