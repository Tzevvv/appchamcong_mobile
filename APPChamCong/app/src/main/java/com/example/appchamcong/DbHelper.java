package com.example.appchamcong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;

    //CLASS TABLE
    private static final String CLASS_TABLE_NAME="CLASS_TABLE";
    public static final String C_ID="CID";
    public static final String CLASS_NAME_KEY="CLASS_NAME";
    public static final String SUBJECT_NAME_KEY="SUBJECT_NAME";

    private static final String CREATE_CLASS_TABLE=
            "CREATE TABLE " + CLASS_TABLE_NAME + "(" +
                        C_ID+" INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                        CLASS_NAME_KEY + " TEXT ," +
                        SUBJECT_NAME_KEY+" TEXT ," +
                        "UNIQUE (" + CLASS_NAME_KEY + "," + SUBJECT_NAME_KEY +")"+");";

    private static final String DROP_CLASS_TABLE="DROP TABLE IF EXISTS "+CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE="SELECT * FROM "+CLASS_TABLE_NAME;


    //STUDENT TABLE.
    private static final String STUDENT_TABLE_NAME="STUDENTTB";
    public static final String S_ID="SID";
    public static final String STUDENT_NAME_KEY="STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY="ROLL";

    private static final String CREATE_STUDENT_TABLE=
            "CREATE TABLE "+STUDENT_TABLE_NAME+"("+
                    S_ID+" INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                    C_ID+" INTEGER not null, "+
                    STUDENT_NAME_KEY+" TEXT,"+
                    STUDENT_ROLL_KEY+" INTEGER,"+
                    "FOREIGN KEY("+C_ID+") REFERENCES "+ CLASS_TABLE_NAME+ "("+C_ID+")"+
                    ");";
    private static final String DROP_STUDENT_TABLE="DROP TABLE IF EXISTS "+ STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE="SELECT * FROM "+ STUDENT_TABLE_NAME;



    //STATUS TABLE
    private static final String STATUS_TABLE_NAME="STATUS_TABLE";
    public static final String STATUS_ID="STATUS_ID";
    public static final String DATE_KEY="STATUS_DATE";
    public static final String STATUS_KEY="STATUS";

    private static final String CREATE_STATUS_TABLE=
            "CREATE TABLE "+STATUS_TABLE_NAME+
                    "("+
                    STATUS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                    S_ID+" INTEGER not null, "+
                    C_ID+" INTEGER not null,  "+
                    DATE_KEY+" DATE not null,"+
                    STATUS_KEY+" TEXT not null,"+
                    "UNIQUE("+ S_ID+","+DATE_KEY+")"+
//                    "FOREIGN KEY("+S_ID+") REFERENCES "+ STUDENT_TABLE_NAME+ "("+S_ID+"),"+
//                    "FOREIGN KEY("+C_ID+") REFERENCES "+ CLASS_TABLE_NAME+ "("+C_ID+")"+
                    ");";

    private static final String DROP_STATUS_TABLE="DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE="SELECT * FROM "+STATUS_TABLE_NAME;



    public DbHelper(@Nullable Context context) {super(context,"sinhvienAttendancess.db", null,VERSION);}


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

   //CLASS
    long addClass(String className,String subjectName){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);
        return database.insert(CLASS_TABLE_NAME,null,values);
    }

    Cursor getClassTable(){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.rawQuery(SELECT_CLASS_TABLE,null);
    }

    int deleteClass(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.delete(CLASS_TABLE_NAME,C_ID+"=?",new String[]{String.valueOf(cid)});

    }

    long updateClass(long cid,String className,String subjectName){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);

        return database.update(CLASS_TABLE_NAME, values,C_ID+"=?",new String[]{String.valueOf(cid)});
    }


//    STUDENT
    long addStudent(long scid,int roll,String Name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(C_ID,scid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,Name);
        return database.insert(STUDENT_TABLE_NAME,null,values);
    }
    Cursor getStudentTable(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
//        return database.rawQuery(SELECT_STUDENT_TABLE,null);
        return database.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
    }
    int deletStudent(long sid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long updateStudent(long sid,String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(STUDENT_NAME_KEY,name);
        return database.update(STUDENT_TABLE_NAME, values,S_ID+"=?",new String[]{String.valueOf(sid)});
    }

    //STATUS.
    long addStatus(long sid,long cid,String date ,String status){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();values.put(S_ID,sid);
        values.put(C_ID,cid);
        values.put(DATE_KEY,date);
        values.put(STATUS_KEY,status);
        return database.insert(STATUS_TABLE_NAME,null,values);
    }
    long updateStatust(long sid,String date,String status){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause=DATE_KEY+"='"+date+"' AND "+S_ID+"='"+sid+"'";
        return database.update(STATUS_TABLE_NAME, values,whereClause,null);
    }

    String getStatus (long sid,String date){
        SQLiteDatabase database=this.getReadableDatabase();
        String whereClause =DATE_KEY+"='"+date+"' AND "+S_ID+"='"+sid+"'";
        Cursor cursor =database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if (cursor.moveToFirst()){
            String status=cursor.getString(cursor.getColumnIndex(STATUS_KEY));
            return status;
        }else {
            String status=null;
            return status;
        }
    }


}
