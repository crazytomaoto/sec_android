package com.hualianzb.sec.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hualianzb.sec.models.RememberEth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wty on 2017/8/30 0030.
 */

public class SearchDao {
    private SearchHelper helper;

    public SearchDao(Context context) {
        helper = new SearchHelper(context);
    }

    //获取所有数据
    public List<RememberEth> getAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS _ (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,mysearch TEXT)");
        List<RememberEth> list = new LinkedList<>();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from _", null);
            while (c.moveToNext()) {
                RememberEth search = new RememberEth();
                setValue(search, c);
                list.add(search);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeC(c);
            closeDB(db);
        }

        return list;
    }

    //插入数据
    public void insert(String str) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("insert into");
            db.execSQL("insert into search(mysearch) values(?)", new Object[]{str});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
    }

    public void delete() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from search");
        db.close();
    }

    private void setValue(RememberEth m, Cursor c) {
        m.setAddress(c.getString(c.getColumnIndex("address")));
        m.setPass(c.getString(c.getColumnIndex("pass")));
        m.setWalletName(c.getString(c.getColumnIndex("name")));
        m.setTips(c.getString(c.getColumnIndex("tips")));
        m.setMnemonics(c.getString(c.getColumnIndex("mnemonics")));
//        m.setWalletFile(c.getString(c.getColumnIndex("mysearch")));
//        m.setEcKeyPair(c.getString(c.getColumnIndex("mysearch")));
    }

    public void closeDB(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    public void closeC(Cursor c) {
        if (c != null) {
            c.close();
        }

    }
}
