package com.example.tms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static final String DATABASE_NAME = "TMS.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAMES = "NAMES";
    private static final String TABLE_CORP = "Corporations";
    private static final String TABLE_GAMES = "GAMES";

    private static final String COLUMN_ID = "id";
    //columns of NAMES table
    private static final String COLUMN_FIO = "FIO";
    //columns of Corporations table
    private static final String COLUMN_CORP = "Corp";
    //columns of GAMES table

    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_GAMENUM = "GameNum";
    private static final String COLUMN_NUMOFPLAYERS = "NumOfPlayers";
    private static final String COLUMN_POINTS = "POINTS";

    private static final int NUM_COLUMN_ID = 0;

    private SQLiteDatabase mDataBase;

    public DataBase(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }


    public long insertFIO(String fio) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FIO, fio);
        return mDataBase.insert(TABLE_NAMES, null, cv);
    }

    public long insertCorp(String corp) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_CORP, corp);
        return mDataBase.insert(TABLE_CORP, null, cv);
    }

    public long insertGames(String date, int gameNum, int points, String fio, String corp, int numberOfPlayers ) {

        ContentValues cv=new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_POINTS, points);
        cv.put(COLUMN_FIO, fio);
        cv.put(COLUMN_CORP, corp);
        cv.put(COLUMN_NUMOFPLAYERS, numberOfPlayers);
        cv.put(COLUMN_GAMENUM, gameNum);
        return mDataBase.insert(TABLE_GAMES, null, cv);
    }

public int update(Games games) {
    ContentValues cv=new ContentValues();
    cv.put(COLUMN_DATE, games.getDate());
    cv.put(COLUMN_POINTS, games.getPoints());
    cv.put(COLUMN_FIO, games.getFio());
    cv.put(COLUMN_CORP, games.getCorp());
    cv.put(COLUMN_NUMOFPLAYERS, games.getNumOfPlayers());
    cv.put(COLUMN_GAMENUM, games.getGameNum());
    return mDataBase.update(TABLE_GAMES, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(games.getId())});
}
    public int updateFio(Fio fio) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FIO, fio.getFio());
        return mDataBase.update(TABLE_NAMES, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(fio.getId())});
    }


    public void deleteAll() {
        mDataBase.delete(TABLE_GAMES, null, null);
    }

    public void deleteAllFio() {
        mDataBase.delete(TABLE_NAMES, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_GAMES, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
}
    public void deleteFio(long id) {
        mDataBase.delete(TABLE_NAMES, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

        public Games select(long id) {
            Cursor mCursor = mDataBase.query(TABLE_GAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

            mCursor.moveToFirst();
            String date = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_DATE));
            int gameNum = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_GAMENUM));
            int points = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_POINTS));
            int numberOfPlayers = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_NUMOFPLAYERS));
            String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
            String corp = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_CORP));
            return new Games(id,date, gameNum,points,numberOfPlayers,fio,corp);
        }

    public int selectCorpMaxPoints(String str) {
        Cursor mCursor = mDataBase.query(TABLE_GAMES, new String[]{"MAX("+COLUMN_POINTS+")"}, COLUMN_CORP + " = ?", new String[]{String.valueOf(str)}, null, null, null);

        mCursor.moveToFirst();
        int max = mCursor.getInt(0);
        return max;
    }
    public int selectCorpMaxPoints(String str, String dateFrom, String dateTo)  {
        int counter=0;
        Log.d("Alchemy","Поток SelectAll " + counter++);
        Cursor mCursor = mDataBase.query(TABLE_GAMES, new String[]{"MAX("+COLUMN_POINTS+")"}, COLUMN_CORP + " = ? AND "+COLUMN_DATE+" BETWEEN ? AND ?", new String[]{String.valueOf(str), dateFrom,dateTo}, null, null, null);
        mCursor.moveToFirst();
        int max = mCursor.getInt(0);
        return max;
    }

    public Map selectFioMaxPoints() {
        Cursor mCursor = mDataBase.query(TABLE_GAMES, new String[]{COLUMN_FIO,"MAX("+COLUMN_POINTS+")"}, null, null, COLUMN_FIO, null, null);
        Map<String, Integer> fioMax = new HashMap<String,Integer>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                fioMax.put(mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO)), mCursor.getInt(mCursor.getColumnIndexOrThrow("MAX("+COLUMN_POINTS+")")));
            } while (mCursor.moveToNext());
        } return fioMax;
    }
    public Map selectFioMaxPoints( String dateFrom, String dateTo) {
        int counter=0;
        Log.d("Alchemy","Поток SelectAll " + counter++);
        Cursor mCursor = mDataBase.query(TABLE_GAMES, new String[]{COLUMN_FIO,"MAX("+COLUMN_POINTS+")"}, COLUMN_DATE+" BETWEEN ? AND ?", new String[]{dateFrom,dateTo}, COLUMN_FIO, null, null);
        Map<String, Integer> fioMax = new HashMap<String,Integer>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                fioMax.put(mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO)), mCursor.getInt(mCursor.getColumnIndexOrThrow("MAX("+COLUMN_POINTS+")")));
            } while (mCursor.moveToNext());
        } return fioMax;
    }

    public Fio selectFio(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();
        String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
        return new Fio(id,fio);
    }

    public ArrayList<Games> selectAll() {
        int counter =0;
        Log.d("Alchemy","Поток SelectAll " + counter++);
        Cursor mCursor = mDataBase.query(TABLE_GAMES, null, null, null, null, null, null);
        ArrayList<Games> arr = new ArrayList<Games>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(COLUMN_ID));
                String date = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_DATE));
                int gameNum = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_GAMENUM));
                int points = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_POINTS));
                int numberOfPlayers = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_NUMOFPLAYERS));
                String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
                String corp = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_CORP));
                arr.add(new Games(id,date, gameNum,points,numberOfPlayers,fio,corp));
            } while (mCursor.moveToNext());
        }
        return arr;
    }
    public ArrayList<Fio> selectAllFio() {
        int counter =0;
        Log.d("Alchemy","Поток SelectAllFio " + counter++);
        Cursor mCursor = mDataBase.query(TABLE_NAMES, null, null, null, null, null, null);

        ArrayList<Fio> arr = new ArrayList<Fio>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
                arr.add(new Fio(id,fio));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String queryNames = "CREATE TABLE " + TABLE_NAMES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIO+" TEXT);";
            db.execSQL(queryNames);

            String queryCorp = "CREATE TABLE " + TABLE_CORP + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CORP+" TEXT);";
            db.execSQL(queryCorp);

            String queryGames = "CREATE TABLE " + TABLE_GAMES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE+" TEXT, " +
//                    "FOREIGN KEY (" + COLUMN_FIOID + ") REFERENCES "+ TABLE_NAMES + "(" + COLUMN_ID + ")," +
//                    COLUMN_CORPID+" TEXT, " +
                    COLUMN_GAMENUM+" INTEGER, " +
                    COLUMN_POINTS +" INTEGER, " +
                    COLUMN_NUMOFPLAYERS +" INTEGER, " +
                    COLUMN_FIO +" TEXT, " +
                    COLUMN_CORP+" TEXT);";
            db.execSQL(queryGames);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
            onCreate(db);
        }
    }

}
