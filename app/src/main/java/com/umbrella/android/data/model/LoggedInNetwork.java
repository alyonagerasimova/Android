package com.umbrella.android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInNetwork {

    private static Integer numberHidden;
    private static Integer numberCycle;
    private static Double learningRate;
    private static Double error;

    public LoggedInNetwork(Integer numberHidden, Integer numberCycle, Double learningRate, Double error) {
        this.numberHidden = numberHidden;
        this.numberCycle = numberCycle;
        this.learningRate = learningRate;
        this.error = error;
    }

    public static int getNumberHidden() {
        return numberHidden;
    }

    public static int getNumberCycle() {
        return numberCycle;
    }
}