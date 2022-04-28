package com.example.db_autor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainAd extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnClear;
    EditText etGrup, etNazv, etCena, etOcenka;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ad);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etNazv = findViewById(R.id.Nazv);
        etGrup = findViewById(R.id.Grup);
        etCena = findViewById(R.id.Cena);
        etOcenka = findViewById(R.id.Ocenka);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        etNazv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etNazv.setHint("");
            else
                etNazv.setHint("Nazv");
        });

        etGrup.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etGrup.setHint("");
            else
                etGrup.setHint("Grup");
        });

        etCena.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etCena.setHint("");
            else
                etCena.setHint("Cena");
        });

        etOcenka.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etOcenka.setHint("");
            else
                etOcenka.setHint("Ocenka");
        });
        UpdateTable();
    }

    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_PROD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAZV);
            int surnameIndex = cursor.getColumnIndex(DBHelper.KEY_GRUP);
            int discIndex = cursor.getColumnIndex(DBHelper.KEY_CENA);
            int markIndex = cursor.getColumnIndex(DBHelper.KEY_OCENKA);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputSurname = new TextView(this);
                params.weight = 3.0f;
                outputSurname.setLayoutParams(params);
                outputSurname.setText(cursor.getString(surnameIndex));
                dbOutputRow.addView(outputSurname);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputDisc = new TextView(this);
                params.weight = 3.0f;
                outputDisc.setLayoutParams(params);
                outputDisc.setText(cursor.getString(discIndex));
                dbOutputRow.addView(outputDisc);

                TextView outputMark = new TextView(this);
                params.weight = 3.0f;
                outputMark.setLayoutParams(params);
                outputMark.setText(cursor.getString(markIndex));
                dbOutputRow.addView(outputMark);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnAdd:
                String name = etNazv.getText().toString();
                String surname = etGrup.getText().toString();
                String disc = etCena.getText().toString();
                String mark = etOcenka.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_GRUP, surname);
                contentValues.put(DBHelper.KEY_NAZV, name);
                contentValues.put(DBHelper.KEY_CENA, disc);
                contentValues.put(DBHelper.KEY_OCENKA, mark);
                database.insert(DBHelper.TABLE_PROD, null, contentValues);
                UpdateTable();
                etNazv.setText("");
                etGrup.setText("");
                etCena.setText("");
                etOcenka.setText("");
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_PROD, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) view.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_PROD, DBHelper.KEY_ID+" = ?", new String[]{String.valueOf((view.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_PROD, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAZV);
                    int surnameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_GRUP);
                    int discIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CENA);
                    int markIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_OCENKA);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_GRUP, cursorUpdater.getString(surnameIndex));
                            contentValues.put(DBHelper.KEY_NAZV, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_CENA, cursorUpdater.getString(discIndex));
                            contentValues.put(DBHelper.KEY_OCENKA, cursorUpdater.getString(markIndex));
                            database.replace(DBHelper.TABLE_PROD, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        database.delete(DBHelper.TABLE_PROD, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();

                }
                cursorUpdater.close();
                break;

        }
    }
}