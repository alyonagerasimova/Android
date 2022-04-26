package com.umbrella.android.data.neuralNetwork.pictureService;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.umbrella.android.data.Const;
import com.umbrella.android.ui.network.NetworkActivity;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

public class Serialization {

    private Context context;

    public Serialization(Context context) {
        this.context = context;
    }

    public static void writeInputValues(double[] inp) throws IOException {
        Writer writer = new FileWriter("D:\\Files\\IVT\\4_cours\\Diplom\\StoredValues\\Z1.txt");
        PrintWriter outChar = new PrintWriter(writer);
        double[] mas = inp;
        for (int i = 0; i < 400; i++)
            outChar.println(mas[i]);
        outChar.flush();
    }

    public double[] readPatterns(String str)  {
        double[] patterns = new double[Const.NUMBER_INPUT_NEURONS];
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = context.getAssets().open( str + ".txt");
            reader = new BufferedReader(new InputStreamReader(is));
            for(int i = 0; i < patterns.length; i++){
                patterns[i] = Double.parseDouble(reader.readLine());
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return patterns;
    }

    public void readImage(String letter) {
        try (InputStream inputStream = context.getAssets().open(letter)) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            NetworkActivity.getImageForRecognize().setImageDrawable(drawable);
            NetworkActivity.getImageForRecognize().setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readImageForTesting() {
        try (InputStream inputStream = context.getAssets().open("Arabic_H.jpeg")) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            NetworkActivity.getImageForRecognize().setImageDrawable(drawable);
            NetworkActivity.getImageForRecognize().setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readImageForView() {
        try (InputStream inputStream = context.getAssets().open("Arabic_A_view.jpeg")) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            NetworkActivity.getImageForView().setImageDrawable(drawable);
            NetworkActivity.getImageForView().setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] readValuesForTraining(String letter) throws IOException {
        return readPatterns(letter);
    }

}


