package com.umbrella.android.data;

import lombok.Data;

@Data
public class Image {

    private String imageName;

    private String flagName;

    public Image(String countryName, String flagName) {
        this.imageName = countryName;
        this.flagName= flagName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getFlagName() {
        return flagName;
    }

    @Override
    public String toString()  {
        return this.imageName;
    }
}
