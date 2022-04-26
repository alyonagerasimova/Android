package com.umbrella.android.data.neuralNetwork.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

public interface learningAlgorithm {

    void init();
    void study();
    void train();
    void writePercent(String str, double[] chancesPerNuml, DecimalFormat df, double chance);
    void countValues();
    void answer();
}
