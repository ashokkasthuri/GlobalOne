package com.ustglobal.myustapp.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ustglobal.myustapp.Dashboard.LinkedInProfileOBJ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal.
 */

public class Database extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LinkedIn";

    // Contacts table name
    private static final String TABLE_PROFILE = "profile";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL_ID = "email_id";
    private static final String KEY_PICTURE_URL = "picture_url";
    private static final String KEY_LINKEDIN_ID = "linkedin_id";
    private static final String KEY_PUBLIC_PROFILE_URL = "public_profile_url";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PROFILE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FULL_NAME + " TEXT," + KEY_EMAIL_ID + " TEXT," + KEY_LINKEDIN_ID + " TEXT," + KEY_PICTURE_URL + " TEXT," + KEY_PUBLIC_PROFILE_URL + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }


    public void addContact(LinkedInProfileOBJ profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME, profile.getName());
        values.put(KEY_EMAIL_ID, profile.getEmail_id());
        values.put(KEY_LINKEDIN_ID, profile.getLinkedin_id());
        values.put(KEY_PICTURE_URL, profile.getPicture_url());
        values.put(KEY_PUBLIC_PROFILE_URL, profile.getPublic_profile());

        // Inserting Row
        db.insert(TABLE_PROFILE, null, values);
        db.close(); // Closing database connection
    }


    public LinkedInProfileOBJ getProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE, new String[]{KEY_ID, KEY_FULL_NAME, KEY_EMAIL_ID, KEY_PICTURE_URL, KEY_LINKEDIN_ID, KEY_PUBLIC_PROFILE_URL}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        LinkedInProfileOBJ profile = new LinkedInProfileOBJ(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return contact
        return profile;
    }


    public List<LinkedInProfileOBJ> getAllProfiles() {

        List<LinkedInProfileOBJ> profile = new ArrayList<LinkedInProfileOBJ>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LinkedInProfileOBJ contact = new LinkedInProfileOBJ();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setEmail_id(cursor.getString(2));
                contact.setPicture_url(cursor.getString(3));
                contact.setLinkedin_id(cursor.getString(4));
                contact.setPublic_profile(cursor.getString(5));
                // Adding contact to list
                profile.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return profile;
    }


    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PROFILE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public int update(LinkedInProfileOBJ profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULL_NAME, profile.getName());
        values.put(KEY_EMAIL_ID, profile.getEmail_id());
        values.put(KEY_LINKEDIN_ID, profile.getLinkedin_id());
        values.put(KEY_PICTURE_URL, profile.getPicture_url());
        values.put(KEY_PUBLIC_PROFILE_URL, profile.getPublic_profile());

        // updating row
        return db.update(TABLE_PROFILE, values, KEY_ID + " = ?", new String[]{String.valueOf(profile.getId())});
    }


    public void delete(LinkedInProfileOBJ profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, KEY_ID + " = ?",
                new String[]{String.valueOf(profile.getId())});
        db.close();
    }
}
