package pl.gdan.elsy.qconf;

import pl.gdan.elsy.tool.RR;
import pl.gdan.elsy.tool.Rand;
import tool.Mat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Main class of the framework, contains the whole Connectionist Q-learning algorithm.
 * Takes information from the Perception object and executes one of the Actions.
 *
 * @author Elser
 */
public class MultiNNBrain implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Number of layers in each NN
     */
    private final int layersNr;
    /**
     * Neuron activations function mode
     */
    private boolean unipolar = false;
    /**
     * An instance of class extending Perception
     */
    private Perception perception;
    /**
     * Array of actions that can be taken
     */
    private Action[] actionsArray;
    /**
     * Input for the whole Brain
     */
    private double[] input;
    /**
     * Array of Q-values
     */
    private double[] Q;
    /**
     * Inputs for each layer of every action's NN [action][layer][i]
     */
    private double layerInput[][][];
    /**
     * Neurons' activations values [action][layer][i]
     */
    private double activations[][][];
    /**
     * Weight matrix for every NN [action][layer][j][i]
     */
    private double w[][][][];
    /**
     * Eligibility traces matrix [action][layer][i][j]
     */
    private double e[][][][];
    /**
     * Gradient matrix [action][layer][i]
     */
    private double g[][][];
    /**
     * Learning rate
     */
    private double alpha = 0.8;
    /**
     * Eligibility traces forgetting rate
     */
    private double lambda = 0.5;
    /**
     * Q-learning Discount factor
     */
    private double gamma = 0.9;
    /**
     * Probability with which random action is selected instead
     * of being selected by the NN
     */
    private double randActionsPercentage = 0;
    /**
     * Use Boltzmann probability (instead of maximum Q-value)
     */
    private boolean useBoltzmann = false;

    /**
     * Boltzmann temperature
     */
    private double boltzmanTemperature = 0.03;
    /**
     * Maximal current Q-value
     */
    private double Qmax;
    /**
     * Q-value of previous action
     */
    private double QPrev;
    /**
     * Array used to calculate Boltzmann probability (based on Q array)
     */
    private double boltzValues[];
    private int tactCounter;
    private int currentAction;
    private int executionResult;
    private int actionsNr;

    /**
     * @param perception      an instance of class extending Perception
     * @param actionsArray    array of actions that can be taken
     * @param hiddenNeuronsNo numbers of neurons in hidden layers
     */
    public MultiNNBrain(Perception perception, Action[] actionsArray, int[] hiddenNeuronsNo) {
        this.unipolar = perception.isUnipolar();
        perception.start();
        this.perception = perception;
        this.input = perception.getOutput();
        this.actionsArray = actionsArray;
        layersNr = hiddenNeuronsNo.length + 1;
        actionsNr = actionsArray.length;
        int[] neuronsNo = new int[layersNr];
        for (int i = 0; i < layersNr - 1; i++) {
            neuronsNo[i] = hiddenNeuronsNo[i];
        }
        neuronsNo[layersNr - 1] = 1; // each NN has only 1 output neuron
        activations = createActivationTable(neuronsNo);
        layerInput = createLayerInputs(neuronsNo);
        w = createWeightTable(neuronsNo);
        e = createWeightTable(neuronsNo);
        g = createActivationTable(neuronsNo);
        Q = new double[actionsNr];
        boltzValues = new double[actionsNr];
        randomize();
        tactCounter = 0;
    }

    /**
     * One step of the Q-learning algorithm. Should be invoked at every time step.
     * It is responsible for selecting the action and updating weights.
     * DOES NOT execute any action. For this use Brain.execute() method.
     *
     * @see MultiNNBrain#executeAction()
     */
    public void count() {
        currentAction = selectAction();
        if (tactCounter > 0) {
            double r = perception.getReward();        // r(t-1)
            if (r != 0) {
                double error = r + gamma * Qmax - QPrev;
                updateWeights(currentAction, error);        // w(t)
            }
        }
        propagate();
        countEligibilities(currentAction);        // e(t), g(t)
        tactCounter++;
        QPrev = Q[currentAction];
    }

    /**
     * Selects an action to execute, basing on the values of Q-function.
     *
     * @return number of the selected action
     */
    private int selectAction() {
        int selectedAction = -1;
        Qmax = -1;
        propagate();
        for (int a = 0; a < actionsNr; a++) {
            if (useBoltzmann) {
                boltzValues[a] = countBoltzman(Q[a]);
            }
            if (Qmax < Q[a]) {
                selectedAction = a;
                Qmax = Q[selectedAction];
            }
        }
        //int aMax = currentAction;
        if (useBoltzmann) {
            selectedAction = RR.pickBestIndex(boltzValues);
        }
        if (randActionsPercentage != 0 && Rand.successWithPercent(randActionsPercentage)) {
            selectedAction = Rand.i(Q.length);
        }
        Qmax = Q[selectedAction]; // TODO shouldn't be max anyway (in case of random action)?
        return selectedAction;
    }

    protected double countBoltzman(double q) {
        return Math.exp(q / boltzmanTemperature);
    }

    /**
     * Counts gradients with respect to the chosen action only and
     * updates all the eligibility traces. See algorithm description
     * for the details.
     *
     * @param a
     */
    private void countEligibilities(int a) {
        for (int l = layersNr - 1; l >= 0; l--) {
            for (int i = 0; i < activations[a][l].length; i++) {
                double error = 0;
                if (l == layersNr - 1) {
                    error = i==a ? 0.5 : 0; // TODO suspicious - why always 1?
                } else {
                    for (int j = 0; j < activations[a][l + 1].length; j++) {
                        error += w[a][l + 1][j][i] * g[a][l + 1][j];
                    }
                }
                double activation = activations[a][l][i];
                double gli;
                if (unipolar) {
                    gli = activation * (1 - activation) * error; //uni
                } else {
                    gli = 0.5 * (1 - activation * activation) * error; //bi
                }
                g[a][l][i] = gli;
                for (int j = 0; j < w[a][l][i].length; j++) {
                    e[a][l][i][j] = gamma * lambda * e[a][l][i][j] + gli * layerInput[a][l][j];
                }
            }
        }
    }

    /**
     * Randomizes all the weights of neurons' connections.
     */
    public void randomize() {
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < w[a].length; l++) {
                for (int i = 0; i < w[a][l].length; i++) {
                    for (int j = 0; j < w[a][l][i].length; j++) {
                        w[a][l][i][j] = randWeight();
                    }
                }
            }
        }
    }

    /**
     * Gives random weight value
     *
     * @return random weight value
     */
    private double randWeight() {
        return Rand.d(-0.5, 0.5);
    }

    /**
     * Propagates the input signal throughout the network to the output.
     * In other words, it updates the activations of all the neurons.
     */
    protected void propagate() {
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < layersNr; l++) {
                for (int i = 0; i < w[a][l].length; i++) {
                    double weightedSum = 0;
                    for (int j = 0; j < w[a][l][i].length; j++) {
                        weightedSum += w[a][l][i][j] * layerInput[a][l][j];
                    }
                    if (unipolar) {
                        activations[a][l][i] = Mat.sigmoidUni(weightedSum);
                    } else {
                        activations[a][l][i] = Mat.sigmoidBi(weightedSum);
                    }
                }
            }
            Q[a] = activations[a][layersNr - 1][0];
        }
    }

    /**
     * Used to teach the neural network. Updates all the weights
     * basing on eligibility traces and the change value.
     *
     * @param change
     */
    private void updateWeights(int a, double change) {
        if (change != 0) {
            for (int l = w[a].length - 1; l >= 0; l--) {
                for (int i = 0; i < w[a][l].length; i++) {
                    for (int j = 0; j < w[a][l][i].length; j++) {
                        final double delta = alpha * change * e[a][l][i][j];
//                            if (w[l][i][j] + delta == w[l][i][j] && delta != 0.0) {
//                                int totalChange = 1;
//                            }
                        w[a][l][i][j] = w[a][l][i][j] + delta;
                    }
                }
//                }
            }
        }
    }

    /**
     * Mutates the neural network by given percent.
     * Usually it is not used in the algorithm, however you may want use it,
     * if you implement a genetic algorithm.
     *
     * @param percent
     */
    public void mutate(double percent) {
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < w[a].length; l++) {
                for (int i = 0; i < w[a][l].length; i++) {
                    for (int j = 0; j < w[a][l][i].length; j++) {
                        if (Rand.successWithPercent(percent)) {
                            w[a][l][i][j] = randWeight();
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets the gradients and eligibility traces. Should be called everytime before
     * the new learning episode starts.
     */
    public void reset() {
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < e[a].length; l++) {
                for (int i = 0; i < e[a][l].length; i++) {
                    for (int j = 0; j < e[a][l][i].length; j++) {
                        e[a][l][i][j] = 0;
                    }
                    g[a][l][i] = 0;
                }
            }
        }
        tactCounter = 0;
    }

    /**
     * Returns the index of the selected action
     */
    public int getAction() {
        return currentAction;
    }

    /**
     * Returns the result of Action.execute() method of previously executed action.
     *
     * @see Action#execute()
     */
    public int getExecutionResult() {
        return executionResult;
    }

    /**
     * Executes selected action.
     * Call it after calling Brain.count() method.
     */
    public void executeAction() {
        executionResult = actionsArray[currentAction].execute();
    }

    public double[] getOutput() {
        return Q;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getBoltzmanTemperature() {
        return boltzmanTemperature;
    }

    public void setBoltzmanTemperature(double boltzmanTemperature) {
        this.boltzmanTemperature = boltzmanTemperature;
    }

    public boolean isUseBoltzmann() {
        return useBoltzmann;
    }

    public void setUseBoltzmann(boolean useBoltzmann) {
        this.useBoltzmann = useBoltzmann;
    }

    public boolean isUnipolar() {
        return unipolar;
    }

    public void setUnipolar(boolean unipolar) {
        this.unipolar = unipolar;
    }

    public double getRandActionsPercentage() {
        return randActionsPercentage;
    }

    public void setRandActionsPercentage(double randActionsPercentage) {
        this.randActionsPercentage = randActionsPercentage;
    }

    /**
     * Method allocating input arrays for all the NN layers
     *
     * @param neuronsNo
     * @return
     */
    private double[][][] createLayerInputs(int[] neuronsNo) {
        double[][][] ret = new double[actionsNr][neuronsNo.length][];
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < neuronsNo.length; l++) {
                if (l == 0) {
                    ret[a][l] = input;
                } else {
                    ret[a][l] = activations[a][l - 1];
                }
            }
        }
        return ret;
    }

    /**
     * Method allocating neuron activation values' arrays
     *
     * @param neuronsNo
     * @return
     */
    private double[][][] createActivationTable(int[] neuronsNo) {
        double[][][] ret = new double[actionsNr][layersNr][];
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < layersNr; l++) {
                ret[a][l] = new double[neuronsNo[l]];
            }
        }
        return ret;
    }

    /**
     * Method allocating neuron weights' arrays
     *
     * @param neuronsNo
     * @return
     */
    private double[][][][] createWeightTable(int[] neuronsNo) {
        double[][][][] ret = new double[actionsNr][layersNr][][];
        for (int a = 0; a < actionsNr; a++) {
            for (int l = 0; l < layersNr; l++) {
                ret[a][l] = new double[neuronsNo[l]][layerInput[a][l].length];
            }
        }
        return ret;
    }

    public void set(MultiNNBrain brain) {
        for (int l = 0; l < w.length; l++) {
            for (int i = 0; i < w[l].length; i++) {
                for (int j = 0; j < w[l][i].length; j++) {
                    w[l][i][j] = brain.w[l][i][j];
                }
            }
        }
    }

    public double[] getInput() {
        return input;
    }

    public double[][][][] getE() {
        return e;
    }

    public double[][][] getG() {
        return g;
    }

    public double[][][][] getW() {
        return w;
    }

    public double[][][] getActivations() {
        return activations;
    }

    public void save(String filename) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(w);
        out.close();
    }

    public void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        w = (double[][][][]) in.readObject();
        in.close();
    }

    public Perception getPerception() {
        return perception;
    }

    public double[] getBoltzValues() {
        return boltzValues;
    }
}
