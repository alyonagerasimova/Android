package com.umbrella.android.ui.network;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.umbrella.android.R;
import com.umbrella.android.data.Const;
import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.db.DatabaseHelper;
import com.umbrella.android.data.db.DeleteActivity;
import com.umbrella.android.data.db.UploadActivity;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.data.neuralNetwork.pictureService.Serialization;
import com.umbrella.android.databinding.ActivityNetworkBinding;
import com.umbrella.android.ui.network.map.MapsActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NetworkActivity extends AppCompatActivity {

    private Validation validation;
    private ActivityNetworkBinding binding;
    private EditText numberHiddenEditText;
    private EditText numberCycleEditText;
    private EditText learningRateEditText;
    private ImageView locationIcon;
    private EditText errorEditText;
    private Button imageButton;
    private Button recognizeButton;
    private Button trainButton;
    private String numberHidden;
    private String numberCycle;
    private String learningRate;
    private String error;
    private static final String IRPROP = "IRProp";
    private static final String RPROP = "RProp";
    private static final String BACKPROP = "BackProp";
    private Serialization serialization = null;

    public static void setFlagAlgorithm(String flagAlgorithm) {
        NetworkActivity.flagAlgorithm = flagAlgorithm;
    }

    private static String flagAlgorithm;

    public static int getFlagTrain() {
        return flagTrain;
    }

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

    public static ImageView getImageForView() {
        return imageForView;
    }

    private SQLiteDatabase mDb;
    private static ImageView imageForView;
    private static ImageView imageForRecognize;
    private RadioGroup radioGroup;
    private final int pick_image = 1;


    ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


    private void init() throws SQLException, ClassNotFoundException, IOException {
        numberHiddenEditText = binding.filedNumberHidden;
        numberCycleEditText = binding.filedNumberCycle;
        learningRateEditText = binding.filedCoeffStudy;
        errorEditText = binding.filedError;
        recognizeButton = binding.recognizeButton;
        radioGroup = binding.radioGroup;
        imageButton = binding.imageButtonUpload;
        imageForRecognize = binding.imageButton;
        trainButton = binding.buttonTrain;
        locationIcon = binding.locationIcon;
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainNetwork();
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
                    databaseHelper.saveNewNetwork(NetworkDataSource.getNetwork());

                    Toast.makeText(NetworkActivity.this, "Нейронная сеть сохранена", Toast.LENGTH_SHORT).show();
                    System.out.println("Все ок!");
                }
            }
        });
        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
                startActivity(intent);
                if (NetworkDataSource.getNetwork() != null) {
                    Toast.makeText(NetworkActivity.this, "Нейронная сеть загружена", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NetworkActivity.this, "Нейронная сеть создана", Toast.LENGTH_SHORT).show();
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
        binding.switchLenguige.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Locale locale = new Locale("value_en");
                    changeLocaleOnEnglish(locale);
                } else {
                    Locale locale = new Locale("value_ru");
                    changeLocaleOnRussian(locale);
                }
            }
        });
        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagTrain == 0) {
                    openSiteDialog();
                }
                TextView textView = findViewById(R.id.Answer);
                NetworkActivity.getImageForRecognize().setImageDrawable(serialization.readImageForTesting());
                NetworkActivity.getImageForRecognize().setScaleType(ImageView.ScaleType.FIT_XY);
                if (Network.getAnswer() != null) {
                    StringBuffer sb = new StringBuffer(textView.getText());
                    textView.setText(sb.delete(textView.getText().length() - 1, textView.getText().length()));
                    textView.setText(textView.getText() + " " + Network.getAnswer());
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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, pick_image);
            }
        });

        locationIcon.setOnClickListener(view -> {
            Intent intent = new Intent(NetworkActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }
    public void trainNetwork() {
        if (NetworkActivity.getFlagAlgorithm() == null || imageForRecognize == null
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
            //serialization.readImage(imageView.getTag().toString());
            flagTrain = 1;
            Toast.makeText(NetworkActivity.this, "Нейронная сеть обучена", Toast.LENGTH_SHORT).show();
            try {
                NetworkDataSource.initDataAndChooseAlgorithm();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view) {
        Intent intent = new Intent(this, DeleteActivity.class);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор

        if(db != null)
            db.close();
        if(userCursor != null)
            userCursor.close();
    }

    private void readInputValuesForNetwork() {
        numberHidden = numberHiddenEditText.getText().toString().trim();
        numberCycle = numberCycleEditText.getText().toString().trim();
        learningRate = learningRateEditText.getText().toString().trim();
        error = errorEditText.getText().toString().trim();
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("deprecation")
    private void changeLocaleOnEnglish(Locale locale) {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources()
                .updateConfiguration(configuration,
                        getBaseContext()
                                .getResources()
                                .getDisplayMetrics());
        setTitle(R.string.app_name);

        TextView tv = (TextView) findViewById(R.id.recognizeButton);
        tv.setText("Recognize a letter");
        tv = (TextView) findViewById(R.id.imageButtonUpload);
        tv.setText("Load image");
        tv = (TextView) findViewById(R.id.buttonSave);
        tv.setText("Save");
        tv = (TextView) findViewById(R.id.buttonDelete);
        tv.setText("Delete");
        tv = (TextView) findViewById(R.id.buttonUpload);
        tv.setText("Upload");
        tv = (TextView) findViewById(R.id.textViewFiledCoeffStudy);
        tv.setText("Learning Rate");
        tv = (TextView) findViewById(R.id.textViewFiledNumberHidden);
        tv.setText("Number of neurons in the hidden layer");
        tv = (TextView) findViewById(R.id.textViewFiledError);
        tv.setText("Error");
        tv = (TextView) findViewById(R.id.textViewFiledNumberCycle);
        tv.setText("Number of training cycles");
        tv = (TextView) findViewById(R.id.Answer);
        tv.setText("Network answer: ");
        tv = (TextView) findViewById(R.id.button_create_network);
        tv.setText("Create network");
        tv = (TextView) findViewById(R.id.TextViewAlgStudy);
        tv.setText("Learning algorithm");
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Neural network");
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("deprecation")
    private void changeLocaleOnRussian(Locale locale) {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources()
                .updateConfiguration(configuration,
                        getBaseContext()
                                .getResources()
                                .getDisplayMetrics());
        setTitle(R.string.app_name);

        TextView tv = (TextView) findViewById(R.id.recognizeButton);
        tv.setText("Распознать букву");
        tv = (TextView) findViewById(R.id.imageButtonUpload);
        tv.setText("Загрузить изображение");
        tv = (TextView) findViewById(R.id.buttonSave);
        tv.setText("Сохранить");
        tv = (TextView) findViewById(R.id.buttonDelete);
        tv.setText("Удалить");
        tv = (TextView) findViewById(R.id.buttonUpload);
        tv.setText("Загрузить");
        tv = (TextView) findViewById(R.id.textViewFiledCoeffStudy);
        tv.setText("Коэффициент обучения");
        tv = (TextView) findViewById(R.id.textViewFiledNumberHidden);
        tv.setText("Количество нейронов в скрытом слое");
        tv = (TextView) findViewById(R.id.textViewFiledError);
        tv.setText("Погрешность обучения");
        tv = (TextView) findViewById(R.id.textViewFiledNumberCycle);
        tv.setText("Количество циклов обучения");
        tv = (TextView) findViewById(R.id.Answer);
        tv.setText("Ответ сети: ");
        tv = (TextView) findViewById(R.id.button_create_network);
        tv.setText("Создать нейронную сеть");
        tv = (TextView) findViewById(R.id.TextViewAlgStudy);
        tv.setText("Алгоритм обучения");
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Нейронная сеть");
    }

    private void openSiteDialog() {
        SpannableString webaddress = null;
        if (NetworkActivity.getFlagAlgorithm() == null) {
            webaddress = new SpannableString(
                    "Выберите алгоритм обучения");
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if (imageForRecognize == null) {
            webaddress = new SpannableString(
                    "Загрузите изображение");
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if ((numberHidden.equals("") && numberCycle.equals("") && learningRate.equals("") && error.equals("")) ||
                (numberHidden.equals("") && numberCycle.equals("") && learningRate.equals("")) ||
                (numberHidden.equals("") && numberCycle.equals("") && error.equals("")) || (
                numberHidden.equals("") && numberCycle.equals("")) ||
                (numberHidden.equals("") || numberCycle.equals("") || learningRate.equals(""))) {
            webaddress = new SpannableString(
                    "Введите данные для создания нейронной сети");
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if (NetworkDataSource.getNetwork() == null) {
            webaddress = new SpannableString(
                    "Создайте нейронную сеть");
            Linkify.addLinks(webaddress, Linkify.ALL);
        } else if (flagTrain == 0) {
            webaddress = new SpannableString(
                    "Обучите нейронную сеть");
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
        return Validation.networkImage(imageView);
    }

}