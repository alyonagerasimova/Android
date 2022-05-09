package com.umbrella.android.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.umbrella.android.R;
import com.umbrella.android.data.Image;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListImagesActivity extends AppCompatActivity {

    public static Drawable getDrawable() {
        return drawable;
    }

    private static Drawable drawable = null;

    public static Drawable getDrawableForTrain() {
        return drawableForTrain;
    }

    private static Drawable drawableForTrain = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_upload);
        List<Image> image_details = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapterImages(this, image_details));

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Image image = (Image) o;
                Serialization serialization = new Serialization(getApplicationContext());
                drawable = serialization.getMipmapResIdByName(image.getFlagName());
                try {
                    drawableForTrain = serialization.readImage(image.getFlagName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), NetworkActivity.class);
                startActivity(intent);
               // Toast.makeText(ListImagesActivity.this, "Selected :" + " " + image, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Image> getListData() {
        List<Image> list = new ArrayList<Image>();
        Image arabA = new Image("Арабская A", "arabic_a");
        Image arabB = new Image("Арабская B", "arabic_b");
        Image arabC = new Image("Арабская C", "arabic_c");
        Image arabD = new Image("Арабская D", "arabic_d");
        Image arabE = new Image("Арабская E", "arabic_e");
        Image arabF = new Image("Арабская F", "arabic_f");
        Image arabG = new Image("Арабская G", "arabic_g");
        Image arabH = new Image("Арабская H", "arabic_h");
        Image arabI = new Image("Арабская I", "arabic_i");
        Image arabJ = new Image("Арабская J", "arabic_j");
        Image arabK = new Image("Арабская K", "arabic_k");
        Image arabL = new Image("Арабская L", "arabic_l");
        Image arabM = new Image("Арабская M", "arabic_m");
        Image arabN = new Image("Арабская N", "arabic_n");
        Image arabO = new Image("Арабская O", "arabic_o");
        Image arabP = new Image("Арабская P", "arabic_p");
        Image arabQ = new Image("Арабская Q", "arabic_q");
        Image arabR = new Image("Арабская R", "arabic_r");
        Image arabS = new Image("Арабская S", "arabic_s");
        Image arabT = new Image("Арабская T", "arabic_t");
        Image arabU = new Image("Арабская U", "arabic_u");
        Image arabV = new Image("Арабская V", "arabic_v");
        Image arabW = new Image("Арабская W", "arabic_w");
        Image arabX = new Image("Арабская X", "arabic_x");
        Image arabY = new Image("Арабская Y", "arabic_y");
        Image arabZ = new Image("Арабская Z", "arabic_z");
        list.add(arabA);
        list.add(arabB);
        list.add(arabC);
        list.add(arabD);
        list.add(arabE);
        list.add(arabF);
        list.add(arabG);
        list.add(arabH);
        list.add(arabI);
        list.add(arabJ);
        list.add(arabK);
        list.add(arabL);
        list.add(arabM);
        list.add(arabN);
        list.add(arabO);
        list.add(arabP);
        list.add(arabQ);
        list.add(arabR);
        list.add(arabS);
        list.add(arabT);
        list.add(arabU);
        list.add(arabV);
        list.add(arabW);
        list.add(arabX);
        list.add(arabY);
        list.add(arabZ);
        Image printedA = new Image("Печатная A", "printed_a");
        Image printedB = new Image("Печатная B", "printed_b");
        Image printedC = new Image("Печатная C", "printed_c");
        Image printedD = new Image("Печатная D", "printed_d");
        Image printedE = new Image("Печатная E", "printed_e");
        Image printedF = new Image("Печатная F", "printed_f");
        Image printedG = new Image("Печатная G", "printed_g");
        Image printedH = new Image("Печатная H", "printed_h");
        Image printedI = new Image("Печатная I", "printed_i");
        Image printedJ = new Image("Печатная J", "printed_j");
        Image printedK = new Image("Печатная K", "printed_k");
        Image printedL = new Image("Печатная L", "printed_l");
        Image printedM = new Image("Печатная M", "printed_m");
        Image printedN = new Image("Печатная N", "printed_n");
        Image printedO = new Image("Печатная O", "printed_o");
        Image printedP = new Image("Печатная P", "printed_p");
        Image printedQ = new Image("Печатная Q", "printed_q");
        Image printedR = new Image("Печатная R", "printed_r");
        Image printedS = new Image("Печатная S", "printed_s");
        Image printedT = new Image("Печатная T", "printed_t");
        Image printedU = new Image("Печатная U", "printed_u");
        Image printedV = new Image("Печатная V", "printed_v");
        Image printedW = new Image("Печатная W", "printed_w");
        Image printedX = new Image("Печатная X", "printed_x");
        Image printedY = new Image("Печатная Y", "printed_y");
        Image printedZ = new Image("Печатная Z", "printed_z");
        list.add(printedA);
        list.add(printedB);
        list.add(printedC);
        list.add(printedD);
        list.add(printedE);
        list.add(printedF);
        list.add(printedG);
        list.add(printedH);
        list.add(printedI);
        list.add(printedJ);
        list.add(printedK);
        list.add(printedL);
        list.add(printedM);
        list.add(printedN);
        list.add(printedO);
        list.add(printedP);
        list.add(printedQ);
        list.add(printedR);
        list.add(printedS);
        list.add(printedT);
        list.add(printedU);
        list.add(printedV);
        list.add(printedW);
        list.add(printedX);
        list.add(printedY);
        list.add(printedZ);
        return list;
    }

}
