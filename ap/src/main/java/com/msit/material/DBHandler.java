package com.msit.material;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "profileManager";
    private static final String TABLE_NAME = "profile";
    private static final String KEY_NAME = "name";
    private static final String KEY_RING = "ring";
    private static final String KEY_MEDIA = "media";
    private static final String KEY_ALARM = "alarm";
    private final ArrayList<Profile> contact_list = new ArrayList<>();

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_NAME + " TEXT PRIMARY KEY," + KEY_RING + " TEXT,"
                + KEY_MEDIA + " TEXT," + KEY_ALARM + " TEXT" + ")";
        db.execSQL(CREATE_PROFILES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public void Add_Profile(Profile contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_RING, contact.getRing());
        values.put(KEY_MEDIA, contact.getMedia());
        values.put(KEY_ALARM, contact.getAlarm());
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    Profile Get_Profile(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_NAME,
                        KEY_RING, KEY_MEDIA, KEY_ALARM }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile contact = null;
        if (cursor != null) {
            contact = new Profile((cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        assert cursor != null;
        cursor.close();
        db.close();

        return contact;
    }

    public ArrayList<Profile> Get_Profiles() {
        try {
            contact_list.clear();

            String selectQuery = "SELECT  * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Profile contact = new Profile();
                    contact.setName(cursor.getString(0));
                    contact.setRing(cursor.getString(1));
                    contact.setMedia(cursor.getString(2));
                    contact.setAlarm(cursor.getString(3));
                    contact_list.add(contact);
                } while (cursor.moveToNext());
            }


            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e) {

            Log.e("all_Profiles", "" + e);
        }

        return contact_list;
    }

    public Cursor get_Volumes(String uname)
    {
        SQLiteDatabase sq= this.getReadableDatabase();
        String selection=KEY_NAME+" LIKE ?";
        String columns[]={KEY_RING,KEY_MEDIA,KEY_ALARM};
        String args[]={uname};

        Cursor cr= sq.query(TABLE_NAME,columns,selection,args,null,null,null);
        return  cr;
    }

    public int Update_Profile(Profile contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RING, contact.getRing());
        values.put(KEY_MEDIA, contact.getMedia());
        values.put(KEY_ALARM, contact.getAlarm());

        // updating row
        return db.update(TABLE_NAME, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(contact.getName()) });
    }

    public void Delete_Profile(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + " = ?",
                new String[]{String.valueOf(name)});
        db.close();
    }

   /* public int Get_Total_Profiles() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/
}
