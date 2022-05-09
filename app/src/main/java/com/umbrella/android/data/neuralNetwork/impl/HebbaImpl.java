package com.umbrella.android.data.neuralNetwork.impl;

import com.umbrella.android.data.Const;
import com.umbrella.android.data.neuralNetwork.interfaces.learningAlgorithm;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;

public class HebbaImpl implements learningAlgorithm {

    private double[] hiddenValues; // значение скрытого нейрона
    private double[] outputValues; // выходы
    private double[] hiddenValuesPrev; // значение скрытого нейрона
    private double[] outputValuesPrev; // выходы
    private double[][][] weightsPrev; // вес от предыдущего к следующему
    private int numberHiddenNeurons;
    private double[] pixelsValues;
    private String[] percent = new String[27];
    private int[] answers = new int[Const.NUMBER_OUTPUT_NEURONS];
    private double[][] bias; // байасы скрытых нейронов
    private double[] inputValues; // входы
    private List<double[]> patterns;
    private double learningRateFactor;
    private int numberCycles = 0;
    private double error = 0.0;
    private double globalNetworkError;
    private String answer;
    private double[][][] delta;
    private double[][][] deltaWeightPrev;
    private double[][][] deltaWeight;
    private double[][][] proizvodnayaPrev;
    private double[][][] proizvodnaya;
    private double[][][] weights;

    public void HebbaImpl(String str1, String str2) throws IOException, URISyntaxException {
        init();
    }
    public void init() {
        delta = new double[2][][];
        weights = new double[2][][];
        deltaWeight = new double[2][][];
        deltaWeightPrev = new double[2][][];
        proizvodnayaPrev = new double[2][][];
        proizvodnaya = new double[2][][];
        outputValues = new double[Const.NUMBER_OUTPUT_NEURONS];
        hiddenValues = new double[numberHiddenNeurons];
        outputValuesPrev = new double[Const.NUMBER_OUTPUT_NEURONS];
        hiddenValuesPrev = new double[numberHiddenNeurons];
        delta[0] = new double[inputValues.length][hiddenValues.length];
        delta[1] = new double[hiddenValues.length][outputValues.length];
        deltaWeight[0] = new double[inputValues.length][hiddenValues.length];
        deltaWeight[1] = new double[hiddenValues.length][outputValues.length];
        deltaWeightPrev[0] = new double[inputValues.length][hiddenValues.length];
        deltaWeightPrev[1] = new double[hiddenValues.length][outputValues.length];
        proizvodnayaPrev[0] = new double[inputValues.length][hiddenValues.length];
        proizvodnayaPrev[1] = new double[hiddenValues.length][outputValues.length];
        proizvodnaya[0] = new double[inputValues.length][hiddenValues.length];
        proizvodnaya[1] = new double[hiddenValues.length][outputValues.length];
        weights[0] = new double[inputValues.length][hiddenValues.length];
        weights[1] = new double[hiddenValues.length][outputValues.length];
    }

    @Override
    public void train() {

        //  study();
        if (numberCycles != 0) {
            for (int i = 0; i < numberCycles; i++) {
                //1  System.out.println("------------------------ Epoch " + i + " ------------------------");
                study();
            }
        } else if (error >= 0.0) {
            int i = 1;
            while (globalNetworkError > error) {
                i++;
                //System.out.println("------------------------ Epoch " + i + " ------------------------");
                // study();
            }
        }
        double chance = 0;
        double[] chancesPerNuml = new double[patterns.size()];
        int found = 0;
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
        DecimalFormat df = new DecimalFormat("#.###");
       // writePercent(str, chancesPerNuml, df, chance);
    }

    @Override
    public void writePercent(String str, double[] chancesPerNuml, DecimalFormat df, double chance) {
        for (int i = 0; i < 26; i++) {
            percent[i] = str + "_" + Const.ARRAY_LETTERS[i] + " = " + df.format((chancesPerNuml[i] / chance) * 100) + " %";
            System.out.println(str + "_" + Const.ARRAY_LETTERS[i] + " = " + df.format((chancesPerNuml[i] / chance) * 100) + " %");
        }
        System.out.println("All = " + df.format((chance / 26) * 100) + " %");
        percent[26] = "All = " + df.format((chance / 26) * 100) + " %";
    }

    @Override
    public void countValues() {
        for (int i = 0; i < hiddenValues.length; i++) {
            hiddenValues[i] = bias[0][i];
            for (int j = 0; j < inputValues.length; j++) {
                hiddenValues[i] += inputValues[j] * weights[0][j][i];
            }
            hiddenValues[i] = 1d / (1 + Math.exp(-hiddenValues[i]));
        }
        for (int j = 0; j < outputValues.length; j++) {
            outputValues[j] = bias[1][j];
            for (int i = 0; i < hiddenValues.length; i++) {
                outputValues[j] += hiddenValues[i] * weights[1][i][j];
            }
            outputValues[j] = 1d / (1 + Math.exp(-outputValues[j]));
        }
    }

    @Override
    public void study() {
        DecimalFormat df = new DecimalFormat("#.#######");
        double[] err = new double[hiddenValues.length];
        globalNetworkError = 0.0;
        int h = 0;
        double sum = 0;
        double[] lErr = new double[outputValues.length]; // ошибка выходных нейронов
        while (h != patterns.size()) {
            for (int p = 0; p < Const.NUMBER_OUTPUT_NEURONS; p++) {
                inputValues = patterns.get(h);
                countValues();
                for (int n = 0; n < outputValues.length; n++) {
                    if (n == answers[p]) {
                        lErr[n] = (outputValues[n] - 1) * (1 - outputValues[n]); // если правильно
                    } else lErr[n] = (outputValues[n] - 0) * (1 - outputValues[n]); // если неправильно
                    globalNetworkError += Math.abs(lErr[n]); // глобальная ошибка на эпохе
                }
                for (int j = 0; j < hiddenValues.length; j++) { // обучение слоя перед ВЫХОДНЫМ
                    sum = 0;
                    for (int n = 0; n < outputValues.length; n++) {
                        sum += lErr[n] * weightsPrev[1][j][n]; // локальная ошибка * вес между последним скрытым и выходным
                    }
                    err[j] = sum * hiddenValues[j] * (1 - hiddenValues[j]); // ошибка нейрона перед выходными
                }
                for (int n = 0; n < outputValues.length; n++) {
                    for (int m = 0; m < hiddenValues.length; m++) {
                        if (n == answers[p]) {
                            deltaWeight[1][m][n] = learningRateFactor * hiddenValues[m] * 1;
                        }else deltaWeight[1][m][n] = learningRateFactor * hiddenValues[m] * 0;
                        weights[1][m][n] = weightsPrev[1][m][n] * (1 - 0.01) + deltaWeight[1][m][n];

                    }
                }
                for (int n = 0; n < hiddenValues.length; n++) {
                    for (int m = 0; m < inputValues.length; m++) {
                        if (n == answers[p]) {
                        //    deltaWeight[0][m][n] = learningRateFactor * (inputValues[m] * );
                        }else deltaWeight[0][m][n] =  learningRateFactor * 0;
                        weights[0][m][n] = weightsPrev[0][m][n] * (1 - 0.01) + deltaWeight[0][m][n];

                    }
                }
                weightsPrev = weights;
                for (int y = 0; y < hiddenValues.length; y++) {
                    bias[0][y] += -learningRateFactor * err[y];
                }
                for (int y = 0; y < outputValues.length; y++) {
                    bias[1][y] += -learningRateFactor * lErr[y];
                }
            }
            h++;
        }
        System.out.println(df.format(globalNetworkError / 100));
    }

    @Override
    public void answer() {
        for (int p = 0; p < Const.NUMBER_OUTPUT_NEURONS; p++) {
            inputValues = pixelsValues;
            countValues();
            int found = 0;
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
