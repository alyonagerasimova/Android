package com.umbrella.android.ui.network;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInNetworkView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInNetworkView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}