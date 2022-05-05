package com.umbrella.android.data.db;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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

import com.umbrella.android.R;
import com.umbrella.android.ui.network.MainActivity;

public class DeleteActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText yearBox;
    private Button delButton;
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
        setContentView(R.layout.activity_delete);

        nameBox = findViewById(R.id.name);
        delButton = findViewById(R.id.deleteButton);
        userList = findViewById(R.id.list);
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        header = findViewById(R.id.header);
        userList = findViewById(R.id.list);
        Bundle extras = getIntent().getExtras();
        userList = findViewById(R.id.list);
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
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_NUMBER_HIDDEN,
                DatabaseHelper.COLUMN_NUMBER_LEARNING, DatabaseHelper.COLUMN_NUMBER_CYCLE, DatabaseHelper.COLUMN_NUMBER_ERROR};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        header.setText(" " + userCursor.getCount());
        userList.setAdapter(userAdapter);
    }

    public void delete(View view) {
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
        startActivity(intent);
    }

    private void goHome() {
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}