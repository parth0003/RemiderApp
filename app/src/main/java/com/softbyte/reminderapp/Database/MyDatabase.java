package com.softbyte.reminderapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase(Context context) {
        super(context, "Reminder App", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table ReminderApp (eventid LONG,name TEXT,fromdate TEXT,todate String,fromtime TEXT,totime String,location TEXT,remind TEXT,note TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert_data(Long eventid, String name, String fromdate, String todate, String fromtime, String totime, String location, String remind, String note) {
        Log.d("qry", "Inserted qry");
        String qry = "insert into ReminderApp values('" + eventid + "','" + name + "','"+ fromdate +"','" + todate + "','"+ fromtime +"','" + totime + "','"+ location +"','" + remind + "','"+ note +"')";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(qry);
        Log.d("qry", "Inserted qry");
    }

    public Cursor view_data() {
        String qry = "select * from ReminderApp";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(qry, null);
        return cur;
    }

    public Cursor viewdata(Long eventid) {
        String qry = "select * from ReminderApp where eventid='" + eventid + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(qry, null);
        return cur;
    }

    public void delete(Long n) {
        String qry = "delete from ReminderApp where eventid='" + n + "'";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(qry);
    }

    public void update(String n, String sd, String st, String ed, String et, String l, String rt, String no, Long a) {
        String qry = "UPDATE ReminderApp set `name`='" + n + "',`fromdate`='" + sd + "',`fromtime`='" + st + "',`todate`='" + ed + "',`totime`='" + et + "',`location`='" + l + "',`remind`='" + rt + "',`note`='" + no + "' where `eventid`='" + a + "'";
        Log.d("qury", qry);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(qry);
    }
}
