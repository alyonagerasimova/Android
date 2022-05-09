package com.umbrella.android.data.neuralNetwork.impl;

import com.umbrella.android.data.Const;
import com.umbrella.android.data.neuralNetwork.interfaces.learningAlgorithm;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;

import lombok.Getter;

public class BackPropImpl implements learningAlgorithm {

    public double[] getHiddenValues() {
        return hiddenValues;
    }

    private double[] hiddenValues; // значение скрытого нейрона

    public double[] getOutputValues() {
        return outputValues;
    }

    private double[] outputValues; // выходы

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public double[][][] getWeights() {
        return weights;
    }

    private double[][][] weights; // вес от предыдущего к следующему

    public void setBias(double[][] bias) {
        this.bias = bias;
    }

    public double[][] getBias() {
        return bias;
    }

    private double[][] bias; // байасы скрытых нейронов

    public int getNumberHiddenNeurons() {
        return numberHiddenNeurons;
    }

    private int numberHiddenNeurons;

    public void setPixelsValues(double[] pixelsValues) {
        this.pixelsValues = pixelsValues;
    }

    private double[] pixelsValues;
    private String[] percent = new String[27];

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }

    private int[] answers = new int[Const.NUMBER_OUTPUT_NEURONS];

    public void setInputValues(double[] inputValues) {
        this.inputValues = inputValues;
    }

    private double[] inputValues; // входы

    public void setPatterns(List<double[]> readedPatterns) {
        this.patterns = readedPatterns;
    }

    private List<double[]> patterns;

    public void setLearningRateFactor(double learningRateFactor) {
        this.learningRateFactor = learningRateFactor;
    }

    public double getLearningRateFactor() {
        return learningRateFactor;
    }

    private double learningRateFactor;

    public void setNumberCycles(int numberCycles) {
        this.numberCycles = numberCycles;
    }

    public int getNumberCycles() {
        return numberCycles;
    }

    private int numberCycles;

    public void setError(Double error) {
        this.error = error;
    }

    public double getError() {
        return error;
    }

    private double error;
    private double globalNetworkError;
    private String answer;

    public BackPropImpl(int numberHiddenNeurons, double numberLearningRate) {
        this.numberHiddenNeurons = numberHiddenNeurons;
        this.learningRateFactor = numberLearningRate;
        init();
    }

    @Override
    public void init() {
        weights = new double[2][][];
        outputValues = new double[Const.NUMBER_OUTPUT_NEURONS];
        hiddenValues = new double[numberHiddenNeurons];
        weights[0] = new double[Const.NUMBER_INPUT_NEURONS][numberHiddenNeurons];
        weights[1] = new double[numberHiddenNeurons][Const.NUMBER_OUTPUT_NEURONS];
    }

    @Override
    public void train() {
        if (numberCycles != 0) {
            for (int i = 1; i < numberCycles + 1; i++) {
                // System.out.println("------------------------ Epoch " + i + " ------------------------");
                study();
            }
        } else if (error != 0.0) {
            int i = 1;
            for (int j = 0; j < 2; j++) {
                while (globalNetworkError == 0 || globalNetworkError > error) {
                    i++;
                    //  System.out.println("------------------------ Epoch " + i + " ------------------------");
                    study();
                }
                globalNetworkError = 0;
            }
        }
        double chance = 0;
        double[] chancesPerNuml = new double[patterns.size()];
        int found = 0;
        if (patterns.size() != 0) {
            for (int p = 0; p < Const.NUMBER_OUTPUT_NEURONS; p++) {
                inputValues = patterns.get(p);
                countValues();
                for (int i = 1; i < outputValues.length; i++) {
                    if (outputValues[i] > outputValues[found]) {
                        found = i;
                    }
                }
                if (found == answers[p]) {
                    chancesPerNuml[answers[p]]++;
                    chance++;
                }
            }
            DecimalFormat df = new DecimalFormat("#.####");
        }
        // writePercent(str, chancesPerNuml, df, chance);
    }

    @Override
    public void writePercent(String str, double[] chancesPerNuml, DecimalFormat df, double chance) {
        for (int i = 0; i < 26; i++) {
            percent[i] = str + "_" + Const.ARRAY_LETTERS[i] + " = " + df.format(chancesPerNuml[i] / chance * 100) + " %";
            System.out.println(str + "_" + Const.ARRAY_LETTERS[i] + " = " + df.format(chancesPerNuml[i] / chance * 100) + " %");
        }
        System.out.println("All = " + df.format(chance / 26 * 100) + " %");
        percent[26] = "All = " + df.format(chance / 26 * 100) + " %";
    }

    @Override
    public void countValues() {
        for (int i = 0; i < numberHiddenNeurons; i++) {
            hiddenValues[i] = bias[0][i];
            for (int j = 0; j < Const.NUMBER_INPUT_NEURONS; j++) {
                hiddenValues[i] += inputValues[j] * weights[0][j][i];
            }
            hiddenValues[i] = 1d / (1 + Math.exp(-hiddenValues[i]));
        }
        for (int j = 0; j < Const.NUMBER_OUTPUT_NEURONS; j++) {
            outputValues[j] = bias[bias.length - 1][j];
            for (int i = 0; i < numberHiddenNeurons; i++) {
                outputValues[j] += hiddenValues[i] * weights[1][i][j];
            }
            outputValues[j] = 1d / (1 + Math.exp(-outputValues[j]));
        }
    }

    @Override
    public void study() {
        double[] err = new double[hiddenValues.length];
        globalNetworkError = 0.0;
        int h = 0;
        double[] lErr = new double[outputValues.length]; // ошибка выходных нейронов

        while (h != patterns.size()) {
            for (int p = 0; p < Const.NUMBER_OUTPUT_NEURONS; p++) {
                inputValues = patterns.get(h);
                countValues();
                for (int n = 0; n < outputValues.length; n++) {
                    if (n == answers[p]) {
                        lErr[n] = (outputValues[n] - 1) * (1 - outputValues[n]); // если правильно
                    } else
                        lErr[n] = (outputValues[n] - 0) * (1 - outputValues[n]); // если неправильно
                    globalNetworkError += Math.abs(lErr[n]); // глобальная ошибка на эпохе
                }
                for (int i = 0; i < hiddenValues.length; i++) { // обучение слоя перед ВЫХОДНЫМ
                    double sum = 0;
                    for (int n = 0; n < outputValues.length; n++) {
                        sum += lErr[n] * weights[1][i][n]; // локальная ошибка * вес между последним скрытым и выходным
                    }
                    err[i] = sum * hiddenValues[i] * (1 - hiddenValues[i]); // ошибка нейрона перед выходными
                }

                for (int j = 0; j < hiddenValues.length; j++) {
                    double delta = -learningRateFactor * err[j];
                    bias[0][j] += delta; // обновление байасов от ошибки
                    for (int k = 0; k < inputValues.length; k++) {
                        weights[0][k][j] += delta * inputValues[k]; // обновление весов
                    }
                }

                for (int n = 0; n < outputValues.length; n++) {
                    double delta = -learningRateFactor * lErr[n];
                    bias[bias.length - 1][n] += delta; // обновление байасов от ошибки
                    for (int i = 0; i < hiddenValues.length; i++) {
                        weights[weights.length - 1][i][n] += delta * hiddenValues[i]; // обновление весов
                    }
                }
                h++;
            }
        }
    }

    @Override
    public void answer() {
        int found;
        for (int p = 0; p < Const.NUMBER_OUTPUT_NEURONS; p++) {
            inputValues = pixelsValues;
            countValues();
            found = 0;
            for (int i = 1; i < outputValues.length; i++) {
                if (outputValues[i] > outputValues[found]) {
                    found = i;
                }
            }
            if (found == answers[p]) {
                answer = Const.ARRAY_LETTERS[answers[p]];
                Network.setAnswer(answer);
            }
        }
    }
}
