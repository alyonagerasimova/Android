package com.umbrella.android.data.neuralNetwork.pictureService;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class PictureService {

    private static final double blackPixel = 1.0;
    private static final double whitePixel = 0.0;

    public static double[] getPixelColor(ImageView image) {
        int clr, red, green, blue;
        int k = -1;

        double[] pixelsValues = new double[400];
        double[][] pixelsValuesMatrix = new double[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                k++;
                clr = ((BitmapDrawable) image.getDrawable()).getBitmap().getPixel(j, i);
                red = (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue = clr & 0x000000ff;
                if (red <= 204 && green <= 204 && blue <= 204) {
                    pixelsValuesMatrix[i][j] = blackPixel;
                } else {
                    pixelsValuesMatrix[i][j] = whitePixel;
                }
                pixelsValues[k] = pixelsValuesMatrix[i][j];
            }
        }
        return pixelsValues;
    }

}