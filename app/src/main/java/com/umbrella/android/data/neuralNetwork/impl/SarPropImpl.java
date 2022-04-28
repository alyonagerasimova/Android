//package com.umbrella.android.data.neuralNetwork.impl;
//
//import lombok.Data;
//import org.apache.commons.math3.exception.NumberIsTooLargeException;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
//@Data
//public class SarPropImpl {
//
//    private static final String[] ARRAY_LETTERS = new String[]{"A", "B", "C", "D", "E", "F",
//            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
//    private final int NUMBER_INPUT_NEURONS = 400;
//    private final int NUMBER_OUTPUT_NEURONS = 26;
//    private final double A = 1.2;
//    private final double B = 0.6;
//    private final double K1 = 0.01;
//    private final double K2 = 0.02;
//    private final double K3 = 0.03;
//    private final double T = 0.015;
//
//    private double[] hiddenValues; // значение скрытого нейрона
//    private double[] outputValues; // выходы
//    private double[] hiddenValuesPrev; // значение скрытого нейрона
//    private double[] outputValuesPrev; // выходы
//    private double[][][] weightsPrev; // вес от предыдущего к следующему
//    private int numberHiddenNeurons;
//    private double[] pixelsValues;
//    private String[] percent = new String[27];
//    private int[] answers = new int[NUMBER_OUTPUT_NEURONS];
//    private double[][] bias; // байасы скрытых нейронов
//    private double[] inputValues; // входы
//    private List<double[]> patterns;
//    private double learningRateFactor;
//    private int numberCycles = 0;
//    private double error = 0.0;
//    private double globalNetworkError;
//    private double globalNetworkErrorPrev;
//    private String answer;
//    private double[][][] delta;
//    private double[][][] deltaWeightPrev;
//    private double[][][] deltaWeight;
//    private double[][][] proizvodnayaPrev;
//    private double[][][] proizvodnaya;
//    private double[][][] weights;
//
//    private void init() {
//        delta = new double[2][][];
//        weights = new double[2][][];
//        deltaWeight = new double[2][][];
//        deltaWeightPrev = new double[2][][];
//        proizvodnayaPrev = new double[2][][];
//        proizvodnaya = new double[2][][];
//        outputValues = new double[NUMBER_OUTPUT_NEURONS];
//        hiddenValues = new double[numberHiddenNeurons];
//        outputValuesPrev = new double[NUMBER_OUTPUT_NEURONS];
//        hiddenValuesPrev = new double[numberHiddenNeurons];
//        delta[0] = new double[inputValues.length][hiddenValues.length];
//        delta[1] = new double[hiddenValues.length][outputValues.length];
//        deltaWeight[0] = new double[inputValues.length][hiddenValues.length];
//        deltaWeight[1] = new double[hiddenValues.length][outputValues.length];
//        deltaWeightPrev[0] = new double[inputValues.length][hiddenValues.length];
//        deltaWeightPrev[1] = new double[hiddenValues.length][outputValues.length];
//        proizvodnayaPrev[0] = new double[inputValues.length][hiddenValues.length];
//        proizvodnayaPrev[1] = new double[hiddenValues.length][outputValues.length];
//        proizvodnaya[0] = new double[inputValues.length][hiddenValues.length];
//        proizvodnaya[1] = new double[hiddenValues.length][outputValues.length];
//        weights[0] = new double[inputValues.length][hiddenValues.length];
//        weights[1] = new double[hiddenValues.length][outputValues.length];
//    }
//
//    public void train(String str) {
//        init();
//        if (numberCycles != 0) {
//            for (int i = 0; i < numberCycles; i++) {
//                //1  System.out.println("------------------------ Epoch " + i + " ------------------------");
//                study();
//            }
//        } else if (error != 0) {
//            int i = 1;
//            for(int j = 0; j < 2; j++) {
//                while (globalNetworkError == 0 || globalNetworkError > error) {
//                    i++;
//                    System.out.println("------------------------ Epoch " + i + " ------------------------");
//                    study();
//                }
//                globalNetworkError = 0;
//            }
//        }
//        double chance = 0;
//        double[] chancesPerNuml = new double[patterns.size()];
//        int found = 0;
//        for (int p = 0; p < NUMBER_OUTPUT_NEURONS; p++) {
//            inputValues = patterns.get(p);
//            countValues();
//            for (int i = 1; i < outputValues.length; i++) {
//                if (outputValues[i] > outputValues[found]) {
//                    found = i;
//                }
//            }
//            if (found == answers[p]) {
//                chancesPerNuml[answers[p]]++;
//                chance++;
//            }
//        }
//        DecimalFormat df = new DecimalFormat("#.###");
//        writePercent(str, chancesPerNuml, df, chance);
//    }
//
//    private void writePercent(String str, double[] chancesPerNuml, DecimalFormat df, double chance) {
//        for (int i = 0; i < 26; i++) {
//            percent[i] = str + "_" + ARRAY_LETTERS[i] + " = " + df.format((chancesPerNuml[i] / chance) * 100) + " %";
//            System.out.println(str + "_" + ARRAY_LETTERS[i] + " = " + df.format((chancesPerNuml[i] / chance) * 100) + " %");
//        }
//        System.out.println("All = " + df.format((chance / 26) * 100) + " %");
//        percent[26] = "All = " + df.format((chance / 26) * 100) + " %";
//    }
//    private void countValues() {
//        for (int i = 0; i < hiddenValues.length; i++) {
//            hiddenValues[i] = bias[0][i];
//            for (int j = 0; j < inputValues.length; j++) {
//                hiddenValues[i] += inputValues[j] * weights[0][j][i];
//            }
//            hiddenValues[i] = 1d / (1 + Math.exp(-hiddenValues[i]));
//        }
//        for (int j = 0; j < outputValues.length; j++) {
//            outputValues[j] = bias[1][j];
//            for (int i = 0; i < hiddenValues.length; i++) {
//                outputValues[j] += hiddenValues[i] * weights[1][i][j];
//            }
//            outputValues[j] = 1d / (1 + Math.exp(-outputValues[j]));
//        }
//    }
//
//    private void countWeights(int index, double[] left, double[] right, double[] error){
//        for (int n = 0; n < right.length; n++) {
//            for (int m = 0; m < left.length; m++) {
//                proizvodnaya[index][m][n] = left[m] * error[n];
//                deltaWeight[index][m][n] = -learningRateFactor * proizvodnaya[index][m][n];
//                if (proizvodnayaPrev[index][m][n] * proizvodnaya[index][m][n] > 0) {
//                    delta[index][m][n] *= A;
//                    if (proizvodnaya[index][m][n] > 0) {
//                        deltaWeight[index][m][n] -= delta[index][m][n];
//                    } else if (proizvodnaya[index][m][n] < 0) {
//                        if(deltaWeight[index][m][n] > K2 * globalNetworkError * globalNetworkError){
//                            deltaWeight[index][m][n] += delta[index][m][n] * learningRateFactor + K3 *globalNetworkError*0.5;
//                        }
//                    } else if (proizvodnaya[index][m][n] == 0) {
//                        deltaWeight[index][m][n] = 0;
//                    }
//                } else if (proizvodnayaPrev[index][m][n] * proizvodnaya[index][m][n] < 0) {
//                    delta[index][m][n] *= B;
//                    if (proizvodnaya[index][m][n] > 0) {
//                        deltaWeight[index][m][n] -= delta[index][m][n];
//                    } else if (proizvodnaya[index][m][n] < 0) {
//                        deltaWeight[index][m][n] += delta[index][m][n];
//                    } else if (proizvodnaya[index][m][n] == 0) {
//                        deltaWeight[index][m][n] = 0;
//                    }
//                }
//                weights[index][m][n] = weightsPrev[index][m][n] + deltaWeight[index][m][n];
//            }
//        }
//    }
//
//    private void countProizvodnayaPrevAndDeltaWeightPrev(int index, double[] left, double[] right, double[] error){
//        for (int n = 0; n < right.length; n++) {
//            for (int m = 0; m < left.length; m++) {
//                if(left == hiddenValues){
//                    proizvodnayaPrev[index][m][n] = left[m] * error[n];
//                }else proizvodnayaPrev[index][m][n] = error[n];
//                deltaWeightPrev[index][m][n] = -learningRateFactor * proizvodnayaPrev[index][m][n];
//            }
//        }
//    }
//    private void countBias(int index, double[] layer, double[] error){
//        for (int y = 0; y < layer.length; y++) {
//            bias[index][y] += -learningRateFactor * error[y];
//        }
//    }
//    /**
//     * работает
//     **/
//    private void study() throws NumberIsTooLargeException {
//        DecimalFormat df = new DecimalFormat("#.#######");
//        double[] err = new double[hiddenValues.length];
//        globalNetworkError = 0.0;
//        int h = 0;
//        double sum = 0;
//        int k = 1;
//        double[] lErr = new double[outputValues.length]; // ошибка выходных нейронов
//        while (h != patterns.size()) {
//            for (int p = 0; p < NUMBER_OUTPUT_NEURONS; p++) {
//                if(k == 2){
//                    h--;
//                    inputValues = patterns.get(h);
//                    p--;
//                    k++;
//                }else{
//                    inputValues = patterns.get(h);
//                }
//                countValues();
//                if (k == 1) {
//                    for (int n = 0; n < outputValues.length; n++) {
//                        if (n == answers[p]) {
//                            lErr[n] = (outputValues[n] - 1) * (1 - outputValues[n]);
//                        } else lErr[n] = (outputValues[n] - 0) * (1 - outputValues[n]);
//                        globalNetworkErrorPrev += Math.abs(lErr[n]);
//                    }
//                    for (int j = 0; j < hiddenValues.length; j++) {
//                        sum = 0;
//                        for (int n = 0; n < outputValues.length; n++) {
//                            sum += lErr[n] * weightsPrev[1][j][n];
//                        }
//                        err[j] = sum * hiddenValues[j] * (1 - hiddenValues[j]);
//                    }
//                    countProizvodnayaPrevAndDeltaWeightPrev(1, hiddenValues, outputValues, lErr);
//                    countProizvodnayaPrevAndDeltaWeightPrev(0, inputValues, hiddenValues, err);
//                    k++;
//                } else {
//                    for (int n = 0; n < outputValues.length; n++) {
//                        if (n == answers[p]) {
//                            lErr[n] = (outputValues[n] - 1) * (1 - outputValues[n]);
//                        } else lErr[n] = (outputValues[n] - 0) * (1 - outputValues[n]);
//                        globalNetworkError += Math.abs(lErr[n]);
//                    }
//                    for (int j = 0; j < hiddenValues.length; j++) {
//                        sum = 0;
//                        for (int n = 0; n < outputValues.length; n++) {
//                            sum += lErr[n] * weightsPrev[1][j][n];
//                        }
//                        err[j] = sum * hiddenValues[j] * (1 - hiddenValues[j]);
//                    }
//                    countWeights(1, hiddenValues, outputValues, lErr);
//                    countWeights(0, inputValues, hiddenValues, err);
//                    proizvodnayaPrev = proizvodnaya;
//                    weightsPrev = weights;
//                }
//                countBias(1, outputValues, lErr);
//                countBias(0, hiddenValues, err);
//                h++;
//            }
//        }
//        System.out.println(df.format(globalNetworkError / 100));
//    }
//
//    public void answer() {
//        for (int p = 0; p < NUMBER_OUTPUT_NEURONS; p++) {
//            inputValues = pixelsValues;
//            countValues();
//            int found = 0;
//            for (int i = 1; i < outputValues.length; i++) {
//                if (outputValues[i] > outputValues[found]) {
//                    found = i;
//                }
//            }
//            if (found == answers[p]) {
//                answer = ARRAY_LETTERS[answers[p]];
//                System.out.println(answer);
//                return;
//            }
//        }
//    }
//}
