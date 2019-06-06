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

    private void initializeNeurons(int numberOfNeurons) {
        neurons = new Neuron[numberOfNeurons];
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons[i] = new Neuron(sizeOfFrame * sizeOfFrame);
        }
    }

    public void trainNetwork(int trainingEpochAmount, int[][] imageArray) {
        displayNeuron();

        for (int i = 0; i < trainingEpochAmount; i++) {
            int[] indexesOfBeginningOfFrame =
                    selectRandomIndexesBeginningOfFrame(imageArray.length, sizeOfFrame);
            int[] frame = getFrame(indexesOfBeginningOfFrame, imageArray, sizeOfFrame);
            double[] normalizedFrame = normalizeFrame(frame);
            Neuron neuron = chooseBestNeuron(normalizedFrame);
            changeWeightsOfBestNeuron(neuron, normalizedFrame);
        }

        System.out.println();
        displayNeuron();
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

    private int[] getFrame(int[] indexesOfBeginningOfFrame, int[][] imageArray, int sizeOfFrame) {
        int[] frame = new int[sizeOfFrame * sizeOfFrame];
        int counter = 0;

        for (int i = 0; i < sizeOfFrame; i++) {
            for (int j = 0; j < sizeOfFrame; j++) {
                frame[counter] = imageArray[indexesOfBeginningOfFrame[0] + i]
                        [indexesOfBeginningOfFrame[1] + j];
                counter++;
            }
        }

        return frame;
    }

    private double[] normalizeFrame(int[] frame) {
        double sqrtOfSumOfVector = getSqrtOfSumOfVector(frame);

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

    private double getSqrtOfSumOfVector(int[] frame) {
        double sum = 0;

        for (int pixel : frame) {
            sum += Math.pow(pixel, 2);
        }

        return Math.sqrt(sum);
    }

    private Neuron chooseBestNeuron(double[] frame) {
        int bestNeuronIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < neurons.length; i++) {
            neurons[i].setInputs(frame);
            double distance = neurons[i].countOutput();
            if (distance < closestDistance) {
                closestDistance = distance;
                bestNeuronIndex = i;
            }
        }

        return neurons[bestNeuronIndex];
    }

    private void changeWeightsOfBestNeuron(Neuron neuron, double[] frame) {
        double[] weights = neuron.getWeights();

        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] + learningStep * (frame[i] - weights[i]);
        }

        double[] normalizedWeights = normalizeWeights(weights);
        neuron.setWeights(normalizedWeights);
    }

    private double[] normalizeWeights(double[] weights) {
        double sqrtOfSumOfVector = getSqrtOfSumOfVector(weights);

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

    private double getSqrtOfSumOfVector(double[] frame) {
        double sum = 0;

        for (double aFrame : frame) {
            sum += Math.pow((aFrame), 2);
        }

        return Math.sqrt(sum);
    }

}
