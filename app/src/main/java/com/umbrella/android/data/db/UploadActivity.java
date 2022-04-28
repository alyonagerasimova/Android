package com.umbrella.android.data.db;

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
import com.umbrella.android.ui.network.NetworkActivity;

public class UploadActivity extends AppCompatActivity {

    EditText nameBox;
    EditText yearBox;
    Button uploadButton;
    Button saveButton;
    ListView userList;
    DatabaseHelper databaseHelper;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;
    TextView header;
    SimpleCursorAdapter userAdapter;

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

    public void upload(View view) {
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        int nameIndex = userCursor.getColumnIndexOrThrow("name");
        String name = userCursor.getString(nameIndex);
        nameIndex = userCursor.getColumnIndexOrThrow("numberHidden");
        int numberHidden = userCursor.getInt(nameIndex);
        nameIndex = userCursor.getColumnIndexOrThrow("numberCycle");
        int numberCycle = userCursor.getInt(nameIndex);
        nameIndex = userCursor.getColumnIndexOrThrow("numberLearning");
        double numberLearning = userCursor.getDouble(nameIndex);
        nameIndex = userCursor.getColumnIndexOrThrow("numberError");
        double numberError = userCursor.getDouble(nameIndex);
        Intent intent = new Intent(getApplicationContext(), NetworkActivity.class);
        startActivity(intent);
    }

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
        header.setText("Найдено элементов: " + userCursor.getCount());
        userList.setAdapter(userAdapter);
    }
}
