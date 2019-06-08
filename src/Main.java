import java.io.IOException;

public class Main {

    private static final int SIZE_OF_FRAME = 4;
    private static final int NUMBER_OF_NEURONS = 20;
    private static final double LEARNING_STEP = 0.01;
    private static final int BITS_PER_VALUE = 8;

    public static void main(String[] args) {
        try {
            int[][] imageArray = Images.loadImage("images/boat.png");
            KohonenAlgorithm network =
                    new KohonenAlgorithm(SIZE_OF_FRAME, NUMBER_OF_NEURONS, LEARNING_STEP);

            network.trainNetwork(2000, imageArray);

            CompressionResult compressionResult = network.compressImage(imageArray);
            int[][] decompressedImage = network.decompressImage(compressionResult);

            Images.saveImage(decompressedImage, "saved.png");

            double PSNR = computePSNR(imageArray, decompressedImage);
            System.out.println("PSNR: " + PSNR + " dB");
            double compressionFactor = computeCompressionFactor(imageArray);
            System.out.println("Compression factor: " + round(compressionFactor) + "%");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static double computePSNR(int[][] originalImage, int[][] decompressedImage) {
        double MSE = computeMSE(originalImage, decompressedImage);
        return 10 * Math.log10(Math.pow(255, 2) / MSE);
    }

    private static double computeMSE(int[][] originalImage, int[][] decompressedImage) {
        int sizeOfPicture = originalImage.length;
        double pixels = sizeOfPicture * sizeOfPicture;

        int sum = 0;
        for (int i = 0; i < sizeOfPicture; i++) {
            for (int j = 0; j < sizeOfPicture; j++) {
                sum += Math.pow(originalImage[i][j] - decompressedImage[i][j], 2);
            }
        }

        return 1 / pixels * sum;
    }

    private static double computeCompressionFactor(int[][] imageArray) {
        int numberOfPixels = imageArray.length * imageArray[0].length;
        double b1 = numberOfPixels * 8;
        int pixelsPerFrame = SIZE_OF_FRAME * SIZE_OF_FRAME;
        int numberOfFrames = numberOfPixels / pixelsPerFrame;
        double b2 = numberOfFrames * (Math.ceil(log2(NUMBER_OF_NEURONS)) + BITS_PER_VALUE)
                + (NUMBER_OF_NEURONS * pixelsPerFrame * BITS_PER_VALUE);
        double b = b2 / b1;
        return (1 - b) * 100;
    }

    private static double log2(int n) {
        return (Math.log(n) / Math.log(2));
    }

    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
