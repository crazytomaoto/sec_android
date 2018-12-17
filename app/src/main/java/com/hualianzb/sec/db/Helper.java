package com.hualianzb.sec.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Date:2018/8/17
 * auther:wangtianyun
 * describe:
 */
public class Helper extends SQLiteOpenHelper {
    private static final String DB_NAME = "wallet.db"; //数据库名称

    private static final int VERSION = 1; //数据库版本

    public Helper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //  钱包
        db.execSQL("create table if not exists _wallete_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "walletName TEXT," +
                "address TEXT," +
                "pass TEXT,mnemonics TEXT,tips TEXT,ecKeyPair ECKeyPair,walletFile WalletFile)");

        //  市
        db.execSQL("create table if not exists _ecKeyPair_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "privateKey TEXT," +
                "mark TEXT," +
                "pcode TEXT," +
                "code TEXT)");

        //  县
        db.execSQL("create table if not exists _couny_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "codeName TEXT," +
                "mark TEXT," +
                "pcode TEXT," +
                "code TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
