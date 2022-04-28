package com.umbrella.android.data.neuralNetwork.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

public interface learningAlgorithmRProp {
    void init();
    void study();
    void train();
    void writePercent(String str, double[] chancesPerNuml, DecimalFormat df, double chance);
    void countWeights(int index, double[] left, double[] right, double[] error);
    void countDerivativePrevAndDeltaWeightPrev(int index, double[] left, double[] right, double[] error);
    void countBias(int index, double[] layer, double[] error);
    void countValues();
    void answer();
}
