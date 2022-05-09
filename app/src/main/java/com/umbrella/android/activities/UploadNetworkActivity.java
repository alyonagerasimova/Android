package com.umbrella.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.umbrella.android.R;
import com.umbrella.android.data.db.SaveNetwork;
import com.umbrella.android.data.neuralNetwork.network.Network;

import java.util.ArrayList;
import java.util.List;

public class UploadNetworkActivity extends Activity {

    private EditText nameBox;
    private SaveNetwork databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private long userId = 0;
    private TextView header;
    private SimpleCursorAdapter userAdapter;
    private ListView listView;

    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_delete_network);
        List<Network> image_details = SaveNetwork.getListData();
        header = findViewById(R.id.header);
        listView = (ListView) findViewById(R.id.listView);

        databaseHelper = new SaveNetwork(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Network network = (Network) o;
                userId = id;
                if (extras != null) {
                    userId = extras.getLong("id");
                }
                // если 0, то добавление
                if (userId > 0) {
                    // получаем элемент по id из бд
                    userCursor = db.rawQuery("select * from " + SaveNetwork.TABLE + " where " +
                            SaveNetwork.COLUMN_NAME + "=?", new String[]{String.valueOf(userId)});
                    userCursor.moveToFirst();
                    nameBox.setText(userCursor.getString(1));
                    userCursor.close();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select * from " + SaveNetwork.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{SaveNetwork.COLUMN_NAME, SaveNetwork.COLUMN_NUMBER_HIDDEN,
                SaveNetwork.COLUMN_NUMBER_LEARNING, SaveNetwork.COLUMN_NUMBER_CYCLE, SaveNetwork.COLUMN_NUMBER_ERROR};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        header.setText("Найдено элементов: " + userCursor.getCount());
        listView.setAdapter(new CustomAdapterNetwork(this, userAdapter));
    }

}
