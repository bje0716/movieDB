package com.grapefruit.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.grapefruit.movie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private myDBHelper myDBHelper;
    private SQLiteDatabase sql;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        setSupportActionBar(binding.toolbar);

        myDBHelper = new myDBHelper(this);
    }

    public void init(View view) {
        sql = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(sql, 1, 2);
        sql.close();
    }

    public void insert(View view) {
        sql = myDBHelper.getWritableDatabase();
        sql.execSQL("INSERT INTO groupTBL VALUES ( '"
                + binding.movieTitle.getText().toString() + "', '"
                + binding.productYear.getText().toString() + "', '"
                + binding.director.getText().toString() + "');");
        sql.close();

        binding.movieTitle.setText("");
        binding.director.setText("");
        binding.productYear.setText("");

        Toast.makeText(this, "입력됨", Toast.LENGTH_SHORT).show();
    }

    public void select(View view) {
        sql = myDBHelper.getReadableDatabase();
        cursor = sql.rawQuery("SELECT * FROM groupTBL;", null);

        String strTitle = "영화 제목" + "\r\n" + "ㅡㅡㅡㅡ" + "\r\n";
        String strYear = "제작년도" + "\r\n" + "ㅡㅡㅡㅡ" + "\r\n";
        String strDirector = "감독" + "\r\n" + "ㅡㅡㅡㅡ" + "\r\n";

        while (cursor.moveToNext()) {
            strTitle += cursor.getString(0) + "\r\n";
            strYear += cursor.getString(1) + "\r\n";
            strDirector += cursor.getString(2) + "\r\n";
        }

        binding.resultTitle.setText(strTitle);
        binding.resultYear.setText(strYear);
        binding.resultDirector.setText(strDirector);

        cursor.close();
        sql.close();
    }

    public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL (title CHAR(20), director CHAR(20), year INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }
}
