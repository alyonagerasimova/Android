package com.umbrella.android.data.db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umbrella.android.R;
import com.umbrella.android.data.NetworkDataSource;
import com.umbrella.android.data.neuralNetwork.network.Network;
import com.umbrella.android.ui.network.NetworkActivity;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class UploadActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText yearBox;
    private Button uploadButton;
    private Button saveButton;
    private ListView userList;
    private DatabaseHelper databaseHelper;
    private DatabaseHelper sqlHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private long userId = 0;
    private TextView header;
    private SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activuty_upload);

        nameBox = findViewById(R.id.name);
        uploadButton = findViewById(R.id.upload);
        header = findViewById(R.id.header);
        userList = findViewById(R.id.list);
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();
        databaseHelper = new DatabaseHelper(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userId = id;
                if (extras != null) {
                    userId = extras.getLong("id");
                }
                // если 0, то добавление
                if (userId > 0) {
                    // получаем элемент по id из бд
                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                            DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
                    userCursor.moveToFirst();
                    nameBox.setText(userCursor.getString(1));
                    userCursor.close();
                }
            }
        });
    }

    @SuppressLint("Range")
    public void upload(View view) throws SQLException {
        String name = "";
        int numberHidden = 0;
        double learningRate = 0.0;
        int numberCycle = 0;
        double error = 0.0;

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        numberHidden = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER_HIDDEN));
        learningRate = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER_LEARNING));
        numberCycle = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER_CYCLE));
        error = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER_ERROR));
        byte[] byteHiddenValues = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_OUTPUT_VALUES));
        byte[] byteOutputValues = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_OUTPUT_VALUES));
        byte[] byteLineBias = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_LINE_BIAS));
        byte[] byteStolbBias = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOLB_BIAS));
        byte[] byteLineWeights0 = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_LINE_WEIGHTS0));
        byte[] byteStolbWeight0= cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOLB_WEIGHTS0));
        byte[] byteLineWeights1 = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_LINE_WEIGHTS1));
        byte[] byteStolbWeight1 = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOLB_WEIGHTS1));

        ByteBuffer bb = ByteBuffer.wrap(byteHiddenValues);
        double[] doublesHidden = new double[byteHiddenValues.length / 8];
        for (int i = 0; i < doublesHidden.length; i++) {
            doublesHidden[i] = bb.getDouble();
        }
        ByteBuffer bbb = ByteBuffer.wrap(byteOutputValues);
        double[] doublesOutput = new double[byteOutputValues.length / 8];
        for (int i = 0; i < doublesOutput.length; i++) {
            doublesOutput[i] = bbb.getDouble();
        }
        ByteBuffer bbbb = ByteBuffer.wrap(byteLineBias);
        double[] lineBias = new double[byteLineBias.length / 8];
        for (int i = 0; i < lineBias.length; i++) {
            lineBias[i] = bbbb.getDouble();
        }
        ByteBuffer bbbbb = ByteBuffer.wrap(byteStolbBias);
        double[] stolbBias = new double[byteStolbBias.length / 8];
        for (int i = 0; i < stolbBias.length; i++) {
            stolbBias[i] = bbbbb.getDouble();
        }
        double[][] bias = new double[numberHidden][];
        bias[0] = lineBias;
        bias[1] = stolbBias;


        ByteBuffer bbbbbb = ByteBuffer.wrap(byteLineWeights0);
        double[] lineWeight0 = new double[byteLineWeights0.length / 8];
        for (int i = 0; i < lineWeight0.length; i++) {
            lineWeight0[i] = bbbbbb.getDouble();
        }
        ByteBuffer bbbbbbb = ByteBuffer.wrap(byteStolbWeight0);
        double[] stolbWeight0 = new double[byteStolbWeight0.length / 8];
        for (int i = 0; i < stolbWeight0.length; i++) {
            stolbWeight0[i] = bbbbbbb.getDouble();
        }
        ByteBuffer bbbbbbbb = ByteBuffer.wrap(byteLineWeights1);
        double[] lineWeight1 = new double[byteLineWeights1.length / 8];
        for (int i = 0; i < lineWeight1.length; i++) {
            lineWeight1[i] = bbbbbbbb.getDouble();
        }
        ByteBuffer bbbbbbbbb = ByteBuffer.wrap(byteStolbWeight1);
        double[] stolbWeight1 = new double[byteStolbWeight1.length / 8];
        for (int i = 0; i < stolbWeight1.length; i++) {
            stolbWeight1[i] = bbbbbbbbb.getDouble();
        }
        double[][][] weights = new double[2][][];
        weights[0] = new double[400][doublesHidden.length];
        weights[1] = new double[doublesHidden.length][doublesOutput.length];

        weights[0][0] = lineWeight0;
        weights[0][1] = stolbWeight0;
        weights[1][0] = lineWeight1;
        weights[1][1] = stolbWeight1;
        Network network;
        network = new Network();
        network.setNumberHiddenNeurons(numberHidden);
        network.setLearningRate(learningRate);
        network.setNumberCycles(numberCycle);
        network.setError(error);
        network.setHiddenValues(doublesHidden);
        network.setOutputValues(doublesOutput);
        network.setBias(bias);
        network.setWeights(weights);
        NetworkDataSource.setNetwork(network);
        Intent intent = new Intent(this, NetworkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_NUMBER_HIDDEN,
                DatabaseHelper.COLUMN_NUMBER_LEARNING, DatabaseHelper.COLUMN_NUMBER_CYCLE, DatabaseHelper.COLUMN_NUMBER_ERROR};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        header.setText(" " + userCursor.getCount());
        userList.setAdapter(userAdapter);
    }
}
