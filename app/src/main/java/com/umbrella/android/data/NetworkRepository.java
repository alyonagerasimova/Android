package com.umbrella.android.data;

import com.umbrella.android.data.model.LoggedInNetwork;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class NetworkRepository {

    private static volatile NetworkRepository instance;

    private NetworkDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInNetwork loggedInNetwork = null;

    // private constructor : singleton access
    private NetworkRepository(NetworkDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static NetworkRepository getInstance(NetworkDataSource dataSource) {
        if (instance == null) {
            instance = new NetworkRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return loggedInNetwork != null;
    }

    public void logout() {
        loggedInNetwork = null;
        dataSource.logout();
    }

    private void setLoggedInNetwork(LoggedInNetwork loggedInNetwork) {
        this.loggedInNetwork = loggedInNetwork;
    }

    public Result<LoggedInNetwork> network(String numberHidden, String numberCycle, String learningRate, String error) {
        Result<LoggedInNetwork> result = dataSource.network(numberHidden, numberCycle, learningRate, error);
        if (result instanceof Result.Success) {
            setLoggedInNetwork(((Result.Success<LoggedInNetwork>) result).getData());
        }
        return result;
    }
}