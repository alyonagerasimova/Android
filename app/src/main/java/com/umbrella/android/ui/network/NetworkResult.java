package com.umbrella.android.ui.network;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class NetworkResult {
    @Nullable
    private LoggedInNetworkView success;
    @Nullable
    private Integer error;

    NetworkResult(@Nullable Integer error) {
        this.error = error;
    }

    NetworkResult(@Nullable LoggedInNetworkView success) {
        this.success = success;
    }

    @Nullable
    LoggedInNetworkView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}