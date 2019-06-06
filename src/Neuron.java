import java.util.Random;

public class Neuron {

    private double[] inputs;
    private double[] weights;

    public Neuron(int numberOfWeights) {
        Random random = new Random();
        weights = new double[numberOfWeights];
        for (int i = 0; i < numberOfWeights; i++) {
            weights[i] = random.nextDouble();
        }
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    public double countOutput() {
        double sum = 0;

        for (int i = 0; i < inputs.length; i++) {
            sum += Math.pow(inputs[i] - weights[i], 2);
        }

        return Math.sqrt(sum);
    }

}
