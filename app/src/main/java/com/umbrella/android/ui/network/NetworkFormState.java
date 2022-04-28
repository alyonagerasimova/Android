package com.umbrella.android.ui.network;

import androidx.annotation.Nullable;

import com.umbrella.android.R;

import lombok.Data;
import lombok.Setter;

/**
 * Data validation state of the login form.
 */
public class NetworkFormState {
    public static void setNumberHiddenError(Integer numberHiddenError) {
        NetworkFormState.numberHiddenError = numberHiddenError;
    }

    private static Integer numberHiddenError;

    public static void setNumberCycleError(Integer numberCycleError) {
        NetworkFormState.numberCycleError = numberCycleError;
    }

    private static Integer numberCycleError;

    public static void setLearningRateError(Integer learningRateError) {
        NetworkFormState.learningRateError = learningRateError;
    }

    private static Integer learningRateError;

    public static void setError(Integer error) {
        NetworkFormState.error = error;
    }

    private static Integer error;
    private boolean isDataValid;

    public static Integer getIsImage() {
        return isImage;
    }

    private static Integer isImage;

    public NetworkFormState(@Nullable Integer numberHiddenError, @Nullable Integer learningRateError,
                            Integer numberCycleError, Integer error) {
        this.numberHiddenError = numberHiddenError;
        this.learningRateError = learningRateError;
        this.numberCycleError = numberCycleError;
        this.error = error;
        this.isDataValid = false;
    }

    NetworkFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    NetworkFormState(Integer isImage) {
        NetworkFormState.isImage = isImage;
    }

    @Nullable
    public static Integer getNumberHiddenError() {
        return numberHiddenError;
    }

    public static Integer getNumberCycleError() {
        return numberCycleError;
    }

    public static Integer getError() {
        return error;
    }

    @Nullable
    public static Integer getLearningRate() {
        return learningRateError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}