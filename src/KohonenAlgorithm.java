public class KohonenAlgorithm {

    private int sizeOfFrame;
    private Neuron[] neurons;
    private double learningStep;

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

}
