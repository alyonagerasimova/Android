package com.umbrella;

import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.ui.network.Validation;

import org.junit.Assert;
import org.junit.Test;


public class BackendTest {

    private String numberHidden = "200";
    private String learningRate = "0.2";
    private String numberCycle = "10";
    private String error = "0.0";
    @Test
    public void createNetworkTest() {
        NetworkDataSource.network(numberHidden, numberCycle, learningRate, error);
        Assert.assertNotNull(NetworkDataSource.getNetwork());
        Assert.assertTrue(NetworkDataSource.getNetwork() instanceof Network);
    }

    @Test
    public void getWeights() {
        NetworkDataSource.network(numberHidden, numberCycle, learningRate, error);
        Assert.assertNotNull(NetworkDataSource.getNetwork());
        Assert.assertTrue(NetworkDataSource.getNetwork() instanceof Network);
        Assert.assertNotNull(NetworkDataSource.getNetwork().getWeights());
    }

    @Test
    public void isNumberHiddenNotValid() {
        Assert.assertFalse(Validation.isNumberHiddenValid("600"));
    }

    @Test
    public void isLearningRateValid() {
        Validation.isLearningRate("1");
    }

    @Test
    public void isNumberCycleNotValid() {
        Assert.assertFalse(Validation.isNumberCycleValid("101"));
    }

    @Test
    public void isErrorNotValid() {
        Assert.assertFalse(Validation.isError("10.1"));
    }

}
