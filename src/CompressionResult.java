public class CompressionResult {

    private Neuron[] bestNeurons;
    private double[] lengths;

    public CompressionResult(int totalFramesCount) {
        bestNeurons = new Neuron[totalFramesCount];
        lengths = new double[totalFramesCount];
    }

    public double[] getLengths() {
        return lengths;
    }

    public Neuron[] getBestNeurons() {
        return bestNeurons;
    }
}
