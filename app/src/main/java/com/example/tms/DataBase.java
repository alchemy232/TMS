package com.example.tms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

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
    private static final String COLUMN_FIOID = "FIOID";
    private static final String COLUMN_CORPID = "CORPID";
    private static final String COLUMN_POINTS = "POINTS";

    private static final int NUM_COLUMN_DATE = 1;
    private static final int NUM_COLUMN_GAMENUM = 2;
    private static final int NUM_COLUMN_NUMOFPLAYERS = 4;
    private static final int NUM_COLUMN_FIO = 5;
    private static final int NUM_COLUMN_CORP = 6;
    private static final int NUM_COLUMN_POINTS = 3;


    private static final int NUM_COLUMN_ID = 0;
//    private static final int NUM_COLUMN_TEAMHOME = 1;
//    private static final int NUM_COLUMN_TEAMGUAST = 2;
//    private static final int NUM_COLUMN_GOALSHOME = 3;
//    private static final int NUM_COLUMN_GOALSGUEST = 4;

    private SQLiteDatabase mDataBase;

    public DataBase(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

//    public long insert(String fio,String teamguest,int goalshouse,int goalsguest) {
//        ContentValues cv=new ContentValues();
//        cv.put(COLUMN_TEAMHOME, teamhouse);
//        cv.put(COLUMN_TEAMGUAST, teamguest);
//        cv.put(COLUMN_GOALSHOME, goalshouse);
//        cv.put(COLUMN_GOALSGUAST,goalsguest);
//        return mDataBase.insert(TABLE_NAMES, null, cv);
//    }

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

//    public int update(Matches md) {
//        ContentValues cv=new ContentValues();
//        cv.put(COLUMN_TEAMHOME, md.getTeamhouse());
//        cv.put(COLUMN_TEAMGUAST, md.getTeamguest());
//        cv.put(COLUMN_GOALSHOME, md.getGoalshouse());
//        cv.put(COLUMN_GOALSGUAST,md.getGoalsguest());
//        return mDataBase.update(TABLE_NAMES, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(md.getId())});
//    }
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

//    public void delete(long id) {
//        mDataBase.delete(TABLE_NAMES, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
//    }
    public void delete(long id) {
        mDataBase.delete(TABLE_GAMES, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
}


//    public Matches select(long id) {
//        Cursor mCursor = mDataBase.query(TABLE_NAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
//
//        mCursor.moveToFirst();
//        String TeamHome = mCursor.getString(NUM_COLUMN_TEAMHOME);
//        String TeamGuest = mCursor.getString(NUM_COLUMN_TEAMGUAST);
//        int GoalsHome = mCursor.getInt(NUM_COLUMN_GOALSHOME);
//        int GoalsGuest=mCursor.getInt(NUM_COLUMN_GOALSGUEST);
//        return new Matches(id, TeamHome, TeamGuest, GoalsHome,GoalsGuest);
//    }
        public Games select(long id) {
            Cursor mCursor = mDataBase.query(TABLE_GAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

            mCursor.moveToFirst();
            String date = mCursor.getString(NUM_COLUMN_DATE);
            int gameNum = mCursor.getInt(NUM_COLUMN_GAMENUM);
            int points = mCursor.getInt(NUM_COLUMN_POINTS);
            int numberOfPlayers = mCursor.getInt(NUM_COLUMN_NUMOFPLAYERS);
            String fio = mCursor.getString(NUM_COLUMN_FIO);
            String corp = mCursor.getString(NUM_COLUMN_CORP);
            return new Games(id,date, gameNum,points,numberOfPlayers,fio,corp);
        }
    public Fio selectFio(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAMES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();
        String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
        return new Fio(id,fio);
    }

    public ArrayList<Games> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_GAMES, null, null, null, null, null, null);

        ArrayList<Games> arr = new ArrayList<Games>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String date = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_DATE));
                int gameNum = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_GAMENUM));
                int points = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_POINTS));
                int numberOfPlayers = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_POINTS));
                String fio = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_FIO));
                String corp = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_CORP));
                arr.add(new Games(id,date, gameNum,points,numberOfPlayers,fio,corp));
            } while (mCursor.moveToNext());
        }
        return arr;
    }
    public ArrayList<Fio> selectAllFio() {
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