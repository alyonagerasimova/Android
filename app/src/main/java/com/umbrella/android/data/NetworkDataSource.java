package com.umbrella.android.data;

import androidx.annotation.Nullable;

import com.umbrella.android.R;
import com.umbrella.android.data.model.LoggedInNetwork;
import com.umbrella.android.data.neuralNetwork.impl.BackPropImpl;
import com.umbrella.android.data.neuralNetwork.impl.RPropImpl;
import com.umbrella.android.data.neuralNetwork.impl.iRPropImpl;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.data.neuralNetwork.pictureService.PictureService;
import com.umbrella.android.ui.network.NetworkActivity;
import com.umbrella.android.ui.network.NetworkFormState;
import com.umbrella.android.ui.network.Validation;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class NetworkDataSource {

    private static iRPropImpl iRProp;

    public static BackPropImpl getBackProp() {
        return backProp;
    }

    public static iRPropImpl getiRProp() {
        return iRProp;
    }

    private static BackPropImpl backProp;

    public static Integer getNumberHiddenNeurons() {
        return numberHiddenNeurons;
    }

    public static RPropImpl getRProp() {
        return rProp;
    }

    private static RPropImpl rProp;
    private static Integer numberHiddenNeurons;
    private static Double numberLearningRate;
    private static Integer numberCycleValue;

    public static void setErrorValue(Double errorValue) {
        NetworkDataSource.errorValue = errorValue;
    }

    private static Double errorValue;
    private static final String IRPROP = "IRProp";
    private static final String RPROP = "RProp";
    private static final String BACKPROP = "BackProp";

    public static void setNetwork(Network network) {
        NetworkDataSource.network = network;
    }

    private static Network network;

    public static Network getNetwork() {
        return network;
    }

    public static Result<LoggedInNetwork> network(@Nullable String numberHidden, String numberCycle, @Nullable String learningRate, String error) {
        try {
            NetworkDataSource.numberHiddenNeurons = Integer.parseInt(numberHidden);
            NetworkDataSource.numberCycleValue = Integer.parseInt(numberCycle);
            if (!learningRate.equals("")) {
                NetworkDataSource.numberLearningRate = Double.parseDouble(learningRate);
            }
            if (!error.equals("")) {
                NetworkDataSource.errorValue = Double.parseDouble(error);
            }
            LoggedInNetwork loggedInNetwork = new LoggedInNetwork(NetworkDataSource.numberHiddenNeurons, NetworkDataSource.numberCycleValue,
                    NetworkDataSource.numberLearningRate, NetworkDataSource.errorValue);
            network = new Network(numberHiddenNeurons);
            if (numberCycleValue != null) {
                network.setNumberCycles(NetworkDataSource.numberCycleValue);
            } else if (errorValue != null) {
                network.setError(NetworkDataSource.errorValue);
            }
            network.setLearningRate(NetworkDataSource.numberLearningRate);
            return new Result.Success<>(loggedInNetwork);
        } catch (Exception e) {
            return new Result.Error(new IOException("Ошибка создания нейронной сети", e));
        }
    }

    public static void initDataAndChooseAlgorithm() throws IOException, URISyntaxException {
        if (NetworkActivity.getFlagAlgorithm() == null) {
            Validation.loginFormState.setValue(new NetworkFormState(null, R.string.invalid_type_algorithm, null, null));
        } else {
            double[] inputValues = null;
            if (NetworkActivity.getFlagAlgorithm().equals(IRPROP)) {
                iRProp = new iRPropImpl(numberHiddenNeurons, numberLearningRate);
                if (numberCycleValue == null) {
                    iRProp.setNumberCycles(0);
                } else {
                    iRProp.setNumberCycles(numberCycleValue);
                }
                if (errorValue == null) {
                    iRProp.setError(0.0);
                } else {
                    iRProp.setError(errorValue);
                }
                inputValues = PictureService.getPixelColor(NetworkActivity.getImageForRecognize());
                iRProp.setPatterns(NetworkActivity.getPatterns());
                iRProp.setInputValues(inputValues);
                iRProp.setPixelsValues(inputValues);
                iRProp.setBias(network.getBias());
                iRProp.setAnswers(network.getAnswers());
                iRProp.setWeightsPrev(network.getWeights());
                network.setHiddenValues(iRProp.getHiddenValues());
                iRProp.train();
                iRProp.answer();
            } else if (NetworkActivity.getFlagAlgorithm().equals(RPROP)) {
                rProp = new RPropImpl(numberHiddenNeurons, numberLearningRate);
                if (numberCycleValue == null) {
                    rProp.setNumberCycles(0);
                } else {
                    rProp.setNumberCycles(numberCycleValue);
                }
                if (errorValue == null) {
                    rProp.setError(0.0);
                }
                inputValues = PictureService.getPixelColor(NetworkActivity.getImageForRecognize());
                rProp.setPatterns(NetworkActivity.getPatterns());
                rProp.setInputValues(inputValues);
                rProp.setPixelsValues(inputValues);
                rProp.setBias(network.getBias());
                rProp.setAnswers(network.getAnswers());
                rProp.setWeightsPrev(network.getWeights());
                network.setHiddenValues(rProp.getHiddenValues());
                rProp.train();
                rProp.answer();
            } else if (NetworkActivity.getFlagAlgorithm().equals(BACKPROP)) {
                backProp = new BackPropImpl(numberHiddenNeurons, numberLearningRate);
                if (numberCycleValue == null) {
                    backProp.setNumberCycles(0);
                } else {
                    backProp.setNumberCycles(numberCycleValue);
                }
                if (errorValue == null) {
                    backProp.setError(0.0);
                } else {
                    backProp.setError(errorValue);
                }
                inputValues = PictureService.getPixelColor(NetworkActivity.getImageForRecognize());
                backProp.setPatterns(NetworkActivity.getPatterns());
                backProp.setInputValues(inputValues);
                backProp.setPixelsValues(inputValues);
                backProp.setBias(network.getBias());
                backProp.setAnswers(network.getAnswers());
                backProp.setWeights(network.getWeights());
                backProp.train();
                backProp.answer();
                network.setNumberCycles(backProp.getNumberCycles());
                network.setLearningRate(backProp.getLearningRateFactor());
                network.setWeights(backProp.getWeights());
                network.setHiddenValues(backProp.getHiddenValues());
                network.setOutputValues(backProp.getOutputValues());
                network.setError(backProp.getError());
                network.setBias(backProp.getBias());
            }
        }
    }

    public static Double getErrorValue() {
        return errorValue;
    }

    public void logout() {
    }
}