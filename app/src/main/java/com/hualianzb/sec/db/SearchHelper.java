package com.hualianzb.sec.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class SearchHelper extends SQLiteOpenHelper {

    private final static int VERSION = 1;
    private final static String DATABASENAME = "search_db";

    public SearchHelper(Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.print("--->>>   onCreate");
        db.execSQL("CREATE TABLE IF NOT EXISTS search (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mysearch TEXT)");
        System.out.print("--->>>   onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
