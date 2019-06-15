import java.util.Arrays;
import java.util.Random;

public class KohonenAlgorithm {

    private int sizeOfFrame;
    private Neuron[] neurons;
    private double learningStep;

    private Random random = new Random();

    public KohonenAlgorithm(int sizeOfFrame, int numberOfNeurons, double learningStep) {
        this.sizeOfFrame = sizeOfFrame;
        this.learningStep = learningStep;
        initializeNeurons(numberOfNeurons);
    }

    public void trainNetwork(int trainingEpochAmount,
                             int numberOfTrainingFrames,
                             int[][] imageArray) {
        displayNeuron();

        for (int i = 0; i < trainingEpochAmount; i++) {
            for (int j = 0; j < numberOfTrainingFrames; j++) {
                int[] beginningOfFrameIndex =
                        selectRandomIndexesBeginningOfFrame(imageArray.length, sizeOfFrame);
                int[] frame = getFrame(beginningOfFrameIndex, imageArray, sizeOfFrame);
                double[] normalizedFrame = normalizeFrame(frame);
                Neuron neuron = chooseBestNeuron(normalizedFrame);
                changeWeightsOfBestNeuron(neuron, normalizedFrame);
            }
        }

        System.out.println();
        displayNeuron();
    }

    public CompressionResult compressImage(int[][] imageArray) {
        int totalFramesCount = (imageArray.length * imageArray[0].length) /
                (sizeOfFrame * sizeOfFrame);
        int framesInOneRowCount = imageArray.length / sizeOfFrame;

        CompressionResult compressionResult =
                new CompressionResult(totalFramesCount);

        int counter = 0;
        for (int i = 0; i < framesInOneRowCount; i++) {
            for (int j = 0; j < framesInOneRowCount; j++) {
                int[] beginningOfFrameIndex = {sizeOfFrame * i, sizeOfFrame * j};
                int[] frame = getFrame(beginningOfFrameIndex, imageArray, sizeOfFrame);

                double[] normalizedFrame = normalizeFrame(frame);
                Neuron bestNeuron = chooseBestNeuron(normalizedFrame);

                compressionResult.getBestNeurons()[counter] = bestNeuron;
                compressionResult.getLengths()[counter] = getSqrtOfSumOfSquaresVector(frame);

                counter++;
            }
        }

        return compressionResult;
    }

    public int[][] decompressImage(CompressionResult compressionResult) {
        int sizeOfImage = (int) Math.sqrt(compressionResult.getBestNeurons().length *
                (sizeOfFrame * sizeOfFrame));

        int[][] decompressionResult = new int[sizeOfImage][sizeOfImage];
        int framesInOneRow = decompressionResult.length / sizeOfFrame;

        int counter = 0;
        for (int i = 0; i < framesInOneRow; i++) {
            for (int j = 0; j < framesInOneRow; j++) {
                double[] weights = compressionResult.getBestNeurons()[counter].getWeights();
                int[] decompressedFrame = decompressFrame(weights, compressionResult.getLengths()[counter]);
                putFrame(decompressionResult, decompressedFrame, i * sizeOfFrame, j * sizeOfFrame, sizeOfFrame);
                counter++;
            }
        }

        return decompressionResult;
    }

    private int[] decompressFrame(double[] weights, double length) {
        double[] decompressedFrame = new double[weights.length];

        for (int i = 0; i < weights.length; i++) {
            decompressedFrame[i] = weights[i] * length;
        }

        return convertFrameToInt(decompressedFrame);
    }

    private int[] convertFrameToInt(double[] frame) {
        int[] intFame = new int[frame.length];

        for (int i = 0; i < frame.length; i++) {
            intFame[i] = (int) frame[i];
        }

        return intFame;
    }

    private void putFrame(int[][] decompressionResult,
                          int[] decompressedFrame,
                          int i,
                          int j,
                          int sizeOfFrame) {
        int counter = 0;
        for (int k = 0; k < sizeOfFrame; k++) {
            for (int l = 0; l < sizeOfFrame; l++) {
                decompressionResult[i + k][j + l] = decompressedFrame[counter];
                counter++;
            }
        }
    }

    private void initializeNeurons(int numberOfNeurons) {
        neurons = new Neuron[numberOfNeurons];
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons[i] = new Neuron(sizeOfFrame * sizeOfFrame);
        }
    }

    private void displayNeuron() {
        for (int i = 0; i < neurons.length; i++) {
            System.out.println("NEURON " + i + ": " + Arrays.toString(neurons[i].getWeights()));
        }
    }

    private int[] selectRandomIndexesBeginningOfFrame(int imageArraySize, int sizeOfFrame) {
        int[] indexes = new int[2];

        indexes[0] = random.nextInt(imageArraySize - sizeOfFrame);
        indexes[1] = random.nextInt(imageArraySize - sizeOfFrame);

        return indexes;
    }

    private int[] getFrame(int[] beginningOfFrameIndex, int[][] imageArray, int sizeOfFrame) {
        int[] frame = new int[sizeOfFrame * sizeOfFrame];
        int counter = 0;

        for (int i = 0; i < sizeOfFrame; i++) {
            for (int j = 0; j < sizeOfFrame; j++) {
                frame[counter] = imageArray[beginningOfFrameIndex[0] + i]
                        [beginningOfFrameIndex[1] + j];
                counter++;
            }
        }

        return frame;
    }

    private double[] normalizeFrame(int[] frame) {
        double sqrtOfSumOfVector = getSqrtOfSumOfSquaresVector(frame);

        double[] normalizedFrame = new double[frame.length];

        for (int i = 0; i < frame.length; i++) {
            if (sqrtOfSumOfVector == 0) {
                normalizedFrame[i] = 0;
            } else {
                normalizedFrame[i] = frame[i] / sqrtOfSumOfVector;
            }
        }

        return normalizedFrame;
    }

    private double getSqrtOfSumOfSquaresVector(int[] frame) {
        double sum = 0;

        for (int pixel : frame) {
            sum += Math.pow(pixel, 2);
        }

        return Math.sqrt(sum);
    }

    private Neuron chooseBestNeuron(double[] normalizedFrame) {
        int bestNeuronIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < neurons.length; i++) {
            neurons[i].setInputs(normalizedFrame);
            double distance = neurons[i].countOutput();
            if (distance < closestDistance) {
                closestDistance = distance;
                bestNeuronIndex = i;
            }
        }

        return neurons[bestNeuronIndex];
    }

    private void changeWeightsOfBestNeuron(Neuron neuron, double[] normalizedFrame) {
        double[] weights = neuron.getWeights();

        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] + learningStep * (normalizedFrame[i] - weights[i]);
        }

        double[] normalizedWeights = normalizeWeights(weights);
        neuron.setWeights(normalizedWeights);
    }

    private double[] normalizeWeights(double[] weights) {
        double sqrtOfSumOfVector = getSqrtOfSumOfSquaresVector(weights);

        double[] normalizedWeights = new double[weights.length];

        for (int i = 0; i < weights.length; i++) {
            if (sqrtOfSumOfVector == 0) {
                weights[i] = 0;
            } else {
                normalizedWeights[i] = weights[i] / sqrtOfSumOfVector;
            }
        }

        return normalizedWeights;
    }

    private double getSqrtOfSumOfSquaresVector(double[] weights) {
        double sum = 0;

        for (double weight : weights) {
            sum += Math.pow((weight), 2);
        }

        return Math.sqrt(sum);
    }

}
