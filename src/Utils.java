public class Utils {

    public static double computePSNR(int[][] originalImage, int[][] decompressedImage) {
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

    public static double computeCompressionFactor(int[][] imageArray,
                                                  int sizeOfFrame,
                                                  int numberOfNeurons,
                                                  int bitsPerValue) {
        int numberOfPixels = imageArray.length * imageArray[0].length;
        double b1 = numberOfPixels * 8;
        int pixelsPerFrame = sizeOfFrame * sizeOfFrame;
        int numberOfFrames = numberOfPixels / pixelsPerFrame;
        double b2 = numberOfFrames * (Math.ceil(log2(numberOfNeurons)) + bitsPerValue)
                + (numberOfNeurons * pixelsPerFrame * bitsPerValue);
        double b = b2 / b1;
        return (1 - b) * 100;
    }

    private static double log2(int n) {
        return (Math.log(n) / Math.log(2));
    }

    public static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
