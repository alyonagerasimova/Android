package com.umbrella.android.data.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.umbrella.android.R;
import com.umbrella.android.ui.network.MainActivity;

public class DeleteActivity extends AppCompatActivity {

    EditText nameBox;
    EditText yearBox;
    Button delButton;
    Button saveButton;
    ListView userList;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = findViewById(R.id.name);
        delButton = findViewById(R.id.deleteButton);
        userList = findViewById(R.id.list);
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
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
        } else {
            // скрываем кнопку удаления
            //delButton.setVisibility(View.GONE);
        }
    }

    public void delete(View view) {
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        //goHome();
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