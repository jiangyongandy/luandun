package com.jy.xinlangweibo.models.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import static com.jy.xinlangweibo.dao.DaoMaster.createAllTables;
import static com.jy.xinlangweibo.dao.DaoMaster.dropAllTables;

/**
 * Created by JIANG on 2016/11/13.
 */

public class LuanDuanDBHelper extends DatabaseOpenHelper {
//    1-2-3 statusBean 增加localId
    public static final int SCHEMA_VERSION = 3;

    public LuanDuanDBHelper(Context context, String name) {
        super(context, name, SCHEMA_VERSION);
    }

    public LuanDuanDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(Database db) {
        Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
        createAllTables(db, false);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        dropAllTables(db, true);
        onCreate(db);
//        for (int version = oldVersion + 1; version <= currentVersion; version++) {
//            upgradeTo(sqLiteDatabase, version);
//        }
    }

//    private void upgradeTo(SQLiteDatabase db, int version) {
//        switch (version) {
//            case 1:
//                createEmoticonsTable(db);
//                break;
//            default:
//                throw new IllegalStateException("Don't know how to upgrade to " + version);
//        }
//    }

}
