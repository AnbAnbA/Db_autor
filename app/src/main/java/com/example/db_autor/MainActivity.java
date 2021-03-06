package com.example.db_autor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnEnter, btnSign;
    EditText username, password;

    DBHelper dbHelper;
    SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(this);


        btnSign = (Button) findViewById(R.id.btnSign);
        btnSign.setOnClickListener(this);


        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                username.setHint("");
            else
                username.setHint("Username");
        });
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                password.setHint("");
            else
                password.setHint("Password");
        });

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_LOGIN, "admin");
        contentValues.put(DBHelper.KEY_PASSWORD, "admin");

        database.insert(DBHelper.TABLE_USERS, null, contentValues);
    }


    public void onFocusChanged(View view, boolean b){
        switch(view.getId()){
            case R.id.etUsername:
                if(b){
                    username.setHint("");
                }
                else{
                    username.setHint("Username");
                }
                break;
            case R.id.etPassword:
                if(b){
                    password.setHint("");
                }
                else{
                    password.setHint("Password");
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnEnter:
                Cursor loginCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                boolean logged = false;
                if (loginCursor.moveToFirst()) {

                    int usernameIndex = loginCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passwordIndex = loginCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do {
                        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                            startActivity(new Intent(this, MainAd.class));
                            logged = true;
                            break;
                        }
                        if(username.getText().toString().equals(loginCursor.getString(usernameIndex)) && password.getText().toString().equals(loginCursor.getString(passwordIndex))){
                            startActivity(new Intent(this, MainP.class));
                            logged = true;
                            break;
                        }
                    } while (loginCursor.moveToNext());
                }
                loginCursor.close();
                if (!logged)
                    Toast.makeText(this, "?????????????????? ???????????????????? ???????????? ?? ???????????? ???? ???????? ??????????????", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSign:
                Cursor signCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                boolean finded = false;
                if (signCursor.moveToFirst()) {
                    int usernameIndex = signCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    do {
                        if (username.getText().toString().equals(signCursor.getString(usernameIndex))) {
                            Toast.makeText(this, "???????????????? ???????? ?????????? ?????? ??????????????????????????????", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    } while (signCursor.moveToNext());
                }
                if (!finded) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN, username.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, password.getText().toString());
                    database.insert(DBHelper.TABLE_USERS, null, contentValues);
                    Toast.makeText(this, "???? ?????????????? ????????????????????????????????????!", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;

        }
    }
}