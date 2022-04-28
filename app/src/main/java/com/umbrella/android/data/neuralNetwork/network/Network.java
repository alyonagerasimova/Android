package com.umbrella.android.data.neuralNetwork.network;

import com.umbrella.android.data.Const;
import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.model.LoggedInNetwork;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Network implements Serializable {

    public Network(String string) {
    }

    public double[] getInputValues() {
        return inputValues;
    }

    private double[] inputValues;
    private double[] hiddenValues;

    public void setOutputValues(double[] outputValues) {
        this.outputValues = outputValues;
    }

    private double[] outputValues;

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    private double[][][] weights;

    public double[][] getBias() {
        return bias;
    }

    public void setBias(double[][] bias) {
        this.bias = bias;
    }

    private double[][] bias;

    public double[] getOutputValues() {
        return outputValues;
    }

    public double[] getPixelsValues() {
        return pixelsValues;
    }

    private static int numberCycles = 0;
    private static double learningRateFactor;
    private static double error = 0.0;

    public double[] getHiddenValues() {
        return hiddenValues;
    }

    private static int numberHiddenNeurons;
    private double[] pixelsValues;
    private String[] percent = new String[27];

    public int[] getAnswers() {
        return answers;
    }

    private final int[] answers = new int[Const.NUMBER_OUTPUT_NEURONS];
    private static String answer;

    public void setInputValues(double[] pixels) {
        this.inputValues = pixels;
    }

    public void setPatterns(List<double[]> patterns) {
        this.patterns = patterns;
    }

    public List<double[]> getPatterns() {
        return patterns;
    }

    private List<double[]> patterns;

    public int getNumberHiddenNeurons() {
        return numberHiddenNeurons;
    }

    public double getLearningRateFactor() {
        return learningRateFactor;
    }

    public int getNumberCycles() {
        return numberCycles;
    }

    public double getError() {
        return error;
    }

    public Network(int numberHiddenNeurons, double learningRateFactor, boolean flag) {
        outputValues = new double[Const.NUMBER_OUTPUT_NEURONS];
        this.numberHiddenNeurons = numberHiddenNeurons;
        this.learningRateFactor = learningRateFactor;
        this.numberCycles = LoggedInNetwork.getNumberCycle();
        this.error = NetworkDataSource.getErrorValue();
        init(numberHiddenNeurons);
        initialization();
        initAnswers();
        patterns = new ArrayList<>();
    }

    public Network(int readNumberHiddenNeurons, double readLearningRateFactor, int readNumberCycles, double readError, double[][][] readWeights,
                   double[][] readBias, double[] readHiddenValues, double[] readOutputValues, List<double[]> readPatterns, String[] readPercent) {
        init(numberHiddenNeurons);
        this.numberHiddenNeurons = readNumberHiddenNeurons;
        this.learningRateFactor = readLearningRateFactor;
        this.numberCycles = readNumberCycles;
        this.error = readError;
        this.weights = readWeights;
        this.bias = readBias;
        this.hiddenValues = readHiddenValues;
        this.outputValues = readOutputValues;
        this.patterns = readPatterns;
        this.percent = readPercent;
        initAnswers();
    }

    public Network(int numberHiddenNeurons) {
        init(numberHiddenNeurons);
        initialization();
        initAnswers();
    }

//    public Network(int numberHiddenNeuronsRestudy, double learningRateFactorRestudy,
//                   int numberCyclesRestudy, double errorRestudy) {
//         numberHiddenNeurons = numberHiddenNeuronsRestudy;
//         numberCycles = numberCyclesRestudy;
//         learningRateFactor = learningRateFactorRestudy;
//         error = errorRestudy;
//    }

    private void init(int numberHiddenNeurons) {
        inputValues = new double[Const.NUMBER_INPUT_NEURONS];
        outputValues = new double[Const.NUMBER_OUTPUT_NEURONS];
        hiddenValues = new double[numberHiddenNeurons];
        bias = new double[numberHiddenNeurons][];
        weights = new double[2][][];
    }

    private void initAnswers() {
        for (int i = 0; i < Const.NUMBER_OUTPUT_NEURONS; i++) {
            answers[i] = i;
        }
    }

    public void initialization() {
        for (int i = 0; i < bias.length - 1; i++) {
            bias[i] = new double[hiddenValues.length];
        }
        bias[bias.length - 1] = new double[outputValues.length];
        for (int i = 0; i < bias.length; i++) {
            for (int j = 0; j < bias[i].length; j++) {
                bias[i][j] = Math.random() * (0.7 + 0.5) - 0.5;
            }
        }
        weights[0] = new double[inputValues.length][hiddenValues.length];
        weights[1] = new double[hiddenValues.length][outputValues.length];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++)
                    weights[i][j][k] = Math.random() * 2 - 1;
            }
        }
    }

    public static String getAnswer() {
        return answer;
    }

    public static void setAnswer(String answerFrom) {
        answer = answerFrom;
    }

    public void setNumberHiddenNeurons(Integer numberHidden) {
        this.numberHiddenNeurons = numberHidden;
    }

    public void setNumberCycles(Integer numberCycle) {
        this.numberCycles = numberCycle;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRateFactor = learningRate;
    }

    public void setError(Double error) {
        this.error = error;
    }
}