package com.doveazapp.SqliteManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Karthik on 6/27/2016.
 */
public class AddedCartDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Doveazcart";
    public static final int DATABASE_VERSION = 1;

    public AddedCartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    public static abstract class AddedCartEntry implements BaseColumns {
        public static final String TABLE = "cart";
        public static final String PROD_ID = "product_id";
        public static final String QUANTITY = "quantity";
    }

    public static final String CREATE = "CREATE TABLE " + AddedCartEntry.TABLE + "( " +
            AddedCartEntry._ID + " INTEGER PRIMARY KEY," +
            AddedCartEntry.PROD_ID + " TEXT," +
            AddedCartEntry.QUANTITY + " INTEGER )";

    public static final String DELETE = "DROP TABLE IF EXISTS " + AddedCartEntry.TABLE;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE);
        onCreate(db);
    }
}
