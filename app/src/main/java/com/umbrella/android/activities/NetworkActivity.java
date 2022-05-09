package com.umbrella.android.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.umbrella.android.R;
import com.umbrella.android.data.Const;
import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.db.SaveNetwork;
import com.umbrella.android.data.db.DeleteNetworkActivity;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;
import com.umbrella.android.databinding.ActivityNetworkBinding;
import com.umbrella.android.ui.network.NetworkFormState;
import com.umbrella.android.ui.network.NetworkViewModelFactory;
import com.umbrella.android.ui.network.Validation;
import com.umbrella.android.ui.network.map.MapActivity;
import com.yandex.mapkit.MapKitFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NetworkActivity extends AppCompatActivity {

    private ImageView locationIcon;
    private Validation validation;
    private ActivityNetworkBinding binding;
    private EditText numberHiddenEditText;
    private EditText numberCycleEditText;
    private EditText learningRateEditText;
    private EditText errorEditText;
    private Button imageButton;
    private static Button recognizeButton;

    public static void setNumberHidden(String numberHidden) {
        NetworkActivity.numberHidden = numberHidden;
    }

    private static String numberHidden;

    public static void setNumberCycle(String numberCycle) {
        NetworkActivity.numberCycle = numberCycle;
    }

    private static String numberCycle;

    public static void setLearningRate(String learningRate) {
        NetworkActivity.learningRate = learningRate;
    }

    private static String learningRate;

    public static void setError(String error) {
        NetworkActivity.error = error;
    }

    private static String error;
    private static final String IRPROP = "IRProp";
    private static final String RPROP = "RProp";
    private static final String BACKPROP = "BackProp";
    private Serialization serialization = null;

    private static String flagAlgorithm;

    private static int flagTrain = 0;

    public static String getFlagAlgorithm() {
        return flagAlgorithm;
    }

    public static List<double[]> getPatterns() {
        return patterns;
    }

    private static List<double[]> patterns = new ArrayList<>();

    public static ImageView getImageForRecognize() {
        return imageForRecognize;
    }

    private static ImageView imageForRecognize = null;

    private static ImageView imageForView = null;
    private RadioGroup radioGroup;
    private final int pick_image = 1;

    private SaveNetwork databaseHelper;

    private void init() throws SQLException, ClassNotFoundException, IOException {
        numberHiddenEditText = binding.filedNumberHidden;
        numberCycleEditText = binding.filedNumberCycle;
        learningRateEditText = binding.filedCoeffStudy;
        errorEditText = binding.filedError;
        recognizeButton = binding.recognizeButton;
        radioGroup = binding.radioGroup;
        imageButton = binding.imageButtonUpload;
        imageForView = binding.imageButton;
        imageForRecognize = binding.imageButton;
        locationIcon = binding.locationIcon;
        databaseHelper = new SaveNetwork(getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String MAPKIT_API_KEY = "5ec3a1f0-9379-4338-8cdd-676426193383";
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        serialization = new Serialization(this);
        binding = ActivityNetworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        validation = new ViewModelProvider(this, new NetworkViewModelFactory())
                .get(Validation.class);
        try {
            init();
        } catch (SQLException | ClassNotFoundException | IOException throwables) {
            throwables.printStackTrace();
        }
        if (ListImagesActivity.getDrawable() != null) {
            imageForView.setImageDrawable(ListImagesActivity.getDrawable());
            imageForView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (ListImagesActivity.getDrawableForTrain() != null) {
            imageForRecognize.setImageDrawable(ListImagesActivity.getDrawableForTrain());
            imageForRecognize.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if(numberHidden != null|| learningRate != null|| numberCycle != null || error != null){
            numberHiddenEditText.setText(numberHidden);
            learningRateEditText.setText(learningRate);
            numberCycleEditText.setText(numberCycle);
            errorEditText.setText(error);
        }
        Validation.getLoginFormState().observe(this, new Observer<NetworkFormState>() {
            @Override
            public void onChanged(@Nullable NetworkFormState networkFormState) {
                if (networkFormState == null) {
                    return;
                }
                recognizeButton.setEnabled(networkFormState.isDataValid());
                if (networkFormState.getNumberHiddenError() != null) {
                    numberHiddenEditText.setError(getString(networkFormState.getNumberHiddenError()));
                }
                if (networkFormState.getNumberCycleError() != null) {
                    numberCycleEditText.setError(getString(networkFormState.getNumberCycleError()));
                }
                if (networkFormState.getLearningRate() != null) {
                    learningRateEditText.setError(getString(networkFormState.getLearningRate()));
                }
                if (networkFormState.getError() != null) {
                    errorEditText.setError(getString(networkFormState.getError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                validation.NetworkDataChanged(numberHiddenEditText.getText().toString().trim(),
                        numberCycleEditText.getText().toString().trim(),
                        learningRateEditText.getText().toString().trim(),
                        errorEditText.getText().toString().trim());
            }
        };

        numberHiddenEditText.addTextChangedListener(afterTextChangedListener);
        learningRateEditText.addTextChangedListener(afterTextChangedListener);
        numberCycleEditText.addTextChangedListener(afterTextChangedListener);
        errorEditText.addTextChangedListener(afterTextChangedListener);

        binding.buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
            }
        });


        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkDataSource.getNetwork() == null) {
                    openSiteDialog();
                } else {
                    NetworkDataSource.getNetwork().setNumberHiddenNeurons(Integer.parseInt(numberHidden));
                    NetworkDataSource.getNetwork().setLearningRate(Double.parseDouble(learningRate));
                    if (numberCycle != null && !numberCycle.equals(""))
                        NetworkDataSource.getNetwork().setNumberCycles(Integer.parseInt(numberCycle));
                    if (error != null && !error.equals("")) {
                        NetworkDataSource.getNetwork().setError(Double.parseDouble(error));
                    }
                    UploadNetworkActivity uploadNetworkActivity = new UploadNetworkActivity();
                    databaseHelper.saveNewNetwork(NetworkDataSource.getNetwork());

                    Toast.makeText(NetworkActivity.this, R.string.network_saved, Toast.LENGTH_SHORT).show();
                    System.out.println("Все ок!");
                }
            }
        });
        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeleteNetworkActivity.class);
                startActivity(intent);
                if (NetworkDataSource.getNetwork() != null) {
                    Toast.makeText(NetworkActivity.this, R.string.network_loaded, Toast.LENGTH_SHORT).show();
                    numberHiddenEditText.setText(String.valueOf(NetworkDataSource.getNetwork().getNumberHiddenNeurons()));
                    learningRateEditText.setText(String.valueOf(NetworkDataSource.getNetwork().getLearningRateFactor()));
                    if (NetworkDataSource.getNetwork().getNumberCycles() != 0) {
                        numberCycleEditText.setText(String.valueOf(NetworkDataSource.getNetwork().getNumberCycles()));
                    }
                    if (NetworkDataSource.getNetwork().getError() != 0.0) {
                        errorEditText.setText(String.valueOf(NetworkDataSource.getNetwork().getError()));
                    }
                }
            }
        });
        binding.buttonCreateNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readInputValuesForNetwork();
                if ((!numberHidden.equals("") && !numberCycle.equals("") && !learningRate.equals("") && !error.equals("")) ||
                        (!numberHidden.equals("") && !numberCycle.equals("") && !learningRate.equals("")) ||
                        (!numberHidden.equals("") && !numberCycle.equals("") && !error.equals("")) || (
                        !numberHidden.equals("") && !numberCycle.equals("")) ||
                        (!numberHidden.equals("") || !numberCycle.equals("") || !learningRate.equals(""))) {
                    NetworkDataSource networkDataSource = new NetworkDataSource();
                    networkDataSource.network(numberHidden, numberCycle, learningRate, error);

                    Toast.makeText(NetworkActivity.this, R.string.network_created, Toast.LENGTH_SHORT).show();
                } else {
                    openSiteDialog();
                }
            }
        });

        numberHiddenEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validation.network(numberHiddenEditText.getText().toString().trim(),
                            numberCycleEditText.getText().toString().trim(),
                            learningRateEditText.getText().toString().trim(),
                            errorEditText.getText().toString().trim());
                } else {
                    return false;
                }
                return true;
            }
        });
        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isImage(imageForView)) {
                    openSiteDialog();
                }else {
                    TextView textView = findViewById(R.id.Answer);
                    trainNetwork();
                    if (Network.getAnswer() != null) {
                        StringBuffer sb = new StringBuffer(textView.getText());
                        textView.setText(sb.delete(textView.getText().length() - 1, textView.getText().length()));
                        textView.setText(textView.getText() + " " + Network.getAnswer());
                    }
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioButtonIpprop:
                        flagAlgorithm = IRPROP;
                        break;
                    case R.id.radioButtonBackProp:
                        flagAlgorithm = BACKPROP;
                        break;
                    case R.id.radioButtonRprop:
                        flagAlgorithm = RPROP;
                        break;
                    default:
                        break;
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListImagesActivity.class);
                startActivity(intent);
            }
        });

        locationIcon.setOnClickListener(view -> {
            Intent intent = new Intent(NetworkActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }

    public void trainNetwork() {
        if (NetworkActivity.getFlagAlgorithm() == null || imageForView == null
                || NetworkDataSource.getNetwork() == null) {
            openSiteDialog();
        } else {
            readInputValuesForNetwork();
            String str = "1";
            for (int i = 0; i < Const.NUMBER_OUTPUT_NEURONS; i++) {
                try {
                    double[] temp = serialization.readValuesForTraining(Const.ARRAY_LETTERS[i]);
                    if (temp != null) {
                        patterns.add(i, temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < Const.NUMBER_OUTPUT_NEURONS; i++) {
                try {
                    double[] temp = serialization.readValuesForTraining(Const.ARRAY_LETTERS[i] + str);
                    if (temp != null) {
                        patterns.add(i, temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //   serialization.readImageForView();
            //serialization.readValuesForTraining();
            flagTrain = 1;
            try {
                NetworkDataSource.initDataAndChooseAlgorithm();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
//            Toast.makeText(NetworkActivity.this, "Нейронная сеть обучена", Toast.LENGTH_SHORT).show();
        }
    }

    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view) {
        Intent intent = new Intent(this, DeleteNetworkActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void readInputValuesForNetwork() {
        numberHidden = numberHiddenEditText.getText().toString().trim();
        numberCycle = numberCycleEditText.getText().toString().trim();
        learningRate = learningRateEditText.getText().toString().trim();
        error = errorEditText.getText().toString().trim();
    }

    private void openSiteDialog() {
        SpannableString webaddress = null;
        if (NetworkActivity.getFlagAlgorithm() == null) {
            webaddress = new SpannableString(
                    getString(R.string.algorithm_null));
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if (imageForView != null) {
            webaddress = new SpannableString(
                    getString(R.string.image_null));
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if ((numberHidden.equals("") && numberCycle.equals("") && learningRate.equals("") && error.equals("")) ||
                (numberHidden.equals("") && numberCycle.equals("") && learningRate.equals("")) ||
                (numberHidden.equals("") && numberCycle.equals("") && error.equals("")) || (
                numberHidden.equals("") && numberCycle.equals("")) ||
                (numberHidden.equals("") || numberCycle.equals("") || learningRate.equals(""))) {
            webaddress = new SpannableString(
                    getString(R.string.incomplete_data));
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if (NetworkDataSource.getNetwork() == null) {
            webaddress = new SpannableString(
                    getString(R.string.network_null));
            Linkify.addLinks(webaddress, Linkify.ALL);
        }
        final AlertDialog aboutDialog = new AlertDialog.Builder(
                NetworkActivity.this).setMessage(webaddress)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

        aboutDialog.show();

        ((TextView) aboutDialog.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageForView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public boolean isImage(ImageView imageView) {
        return Validation.isNetworkImage(imageView);
    }

}