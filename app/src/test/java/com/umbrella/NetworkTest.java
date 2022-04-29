package com.umbrella;

import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.ui.network.NetworkActivity;

import org.junit.Assert;
import org.junit.Test;


public class NetworkTest {

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

}
