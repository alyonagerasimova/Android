package com.umbrella.android.ui.network;

import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.umbrella.android.data.NetworkRepository;
import com.umbrella.android.data.Result;
import com.umbrella.android.data.model.LoggedInNetwork;
import com.umbrella.android.R;

public class Validation extends ViewModel {

    public static MutableLiveData<NetworkFormState> getLoginFormState() {
        return loginFormState;
    }

    public static MutableLiveData<NetworkFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<NetworkResult> loginResult = new MutableLiveData<>();
    private NetworkRepository networkRepository;

    public Validation(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void network(String numberHidden, String numberCycle, String learningRate, String error) {
        // can be launched in a separate asynchronous job
        Result<LoggedInNetwork> result = networkRepository.network(numberHidden, numberCycle, learningRate, error);

        if (result instanceof Result.Success) {
            LoggedInNetwork data = ((Result.Success<LoggedInNetwork>) result).getData();
        }
    }

    public void NetworkDataChanged(String numberHidden, String numberCycle, String learningRate, String error) {
        if (!isNumberHiddenValid(numberHidden)) {
            loginFormState.setValue(new NetworkFormState(R.string.invalid_number_hidden, null, null, null));
        } else if (!isLearningRate(learningRate)) {
            loginFormState.setValue(new NetworkFormState(null, R.string.invalid_learning_rate, null, null));
        } else if (!isNumberCycleValid(numberCycle) && error.isEmpty()) {
            loginFormState.setValue(new NetworkFormState(null, null, R.string.invalid_number_cycle, null));
        } else if (isNumberCycleValid(numberCycle) && !error.isEmpty()) {
            loginFormState.setValue(new NetworkFormState(null, null, R.string.invalid_number_cycle_and_error, null));
        } else if (!isError(error) && numberCycle.trim().length() == 0) {
            loginFormState.setValue(new NetworkFormState(null, null, null, R.string.invalid_error));
        } else {
            loginFormState.setValue(new NetworkFormState(null, null, null, null));
            loginFormState.setValue(new NetworkFormState(true));
        }
        //loginFormState.setValue(new NetworkFormState(null, null, null, null));
    }

    public static boolean networkImage(ImageView imageView) {
        if (imageView.getDrawable() == null) {
            loginFormState.setValue(new NetworkFormState(R.string.invalid_image));
        } else {
            loginFormState.setValue(new NetworkFormState(1));
            return true;
        }
        return false;
    }

    private boolean isNumberHiddenValid(String numberHidden) {
        if (!numberHidden.matches("[0-9]+") || Integer.parseInt(numberHidden) > 500
                || Integer.parseInt(numberHidden) < 0) {
            return false;
        }
        return Integer.parseInt(numberHidden) >= 26;
    }

    private boolean isLearningRate(String learningRate) {
        if (!learningRate.matches("[0-9]+[\\.\\,][0-9]+") || Double.parseDouble(learningRate) > 1.0
                || Double.parseDouble(learningRate) < 0) {
            return false;
        }
        return Double.parseDouble(learningRate) > 0;
    }

    private boolean isNumberCycleValid(String numberCycle) {
        if ((!numberCycle.matches("[0-9]+") || Integer.parseInt(numberCycle) > 1000
                || Integer.parseInt(numberCycle) < 0)) {
            return false;
        }
        return Integer.parseInt(numberCycle) > 0;
    }

    private boolean isError(String error) {
        if (!error.matches("[0-9]+[\\.][0-9]+") || Double.parseDouble(error) > 10.0
                || Double.parseDouble(error) < 0) {
            return false;
        }
        return Double.parseDouble(error) > 0;
    }
}