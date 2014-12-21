package com.example.cityrally.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	// Database Version
    private static int DATABASE_VERSION = 21;
 
    // Database Name
    private static final String DATABASE_NAME = "CheckPointDB";
 
    // Contacts table name
    private static final String TABLE_CHECKPOINT = "CheckPoint";
    
    // Contacts Table Columns names
    private static final String KEY_ID = "s";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_CHALLANGEID = "challangeID";
    private static final String KEY_CLUE = "clue";
    private static final String KEY_SOLVED = "solved";
    private static final String KEY_GIVEUP = "giveUp";
    
    
    
    public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
    
    // Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CHECKPOINT_TABLE = "CREATE TABLE " + TABLE_CHECKPOINT + 
				"("+KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " REAL," 
				+ KEY_LONGITUDE + " REAL," + KEY_CHALLANGEID + " TEXT," 
				+ KEY_CLUE + " TEXT," + KEY_SOLVED + " NUMERIC, " + KEY_GIVEUP + " NUMERIC" +")";
		db.execSQL(CREATE_CHECKPOINT_TABLE);
		System.out.println("Created DB");
	}	

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKPOINT);
		
		onCreate(db);
		
	}
	
	//CRUD
	
	// Adding new contact
	public void addCheckPoint(CheckPoint checkPoint) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(KEY_ID, checkPoint.getId());
	    values.put(KEY_LATITUDE, checkPoint.getLatitude());
	    values.put(KEY_LONGITUDE, checkPoint.getLongitude());
	    values.put(KEY_CHALLANGEID, checkPoint.getChallangeID());
	    values.put(KEY_CLUE, checkPoint.getClue());
	    values.put(KEY_SOLVED, checkPoint.isSolved());
	    values.put(KEY_GIVEUP, checkPoint.isGiveUp());
	    
	    // Inserting Row
	    db.insert(TABLE_CHECKPOINT, null, values);
	    db.close(); // Closing database connection
	   
	}
	 
	// Getting single contact
	public CheckPoint getCheckPoint(int id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_CHECKPOINT, new String[] { KEY_ID,
	            KEY_LATITUDE, KEY_LONGITUDE, KEY_CHALLANGEID, KEY_CLUE, KEY_SOLVED, KEY_GIVEUP }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    // Double.parseValue()
	    // contact.setID(Integer.parseInt(cursor.getString(0)));
	    int c_id = cursor.getInt(0);
	    //Double.parseDouble(cursor.getString(0));
	    double c_latitude = Double.parseDouble(cursor.getString(1));
	    //double c_longitude = cursor.getDouble(2);
	    double c_longitude = Double.parseDouble(cursor.getString(2));
	    String c_challangeID = cursor.getString(3);
	    String c_clue = cursor.getString(4);
	    boolean c_solved = ( cursor.getInt(5) == 1 );
	    boolean c_giveUp = ( cursor.getInt(6) == 1 );
	    
	    CheckPoint checkPoint = new CheckPoint(c_id, c_latitude, c_longitude, c_challangeID, c_clue, c_solved, c_giveUp);
	  	
	    return checkPoint;
	}
	 
	// Getting All Contacts
	public List<CheckPoint> getAllCheckPoint() {
		List<CheckPoint> checkPointList = new ArrayList<CheckPoint>();
		
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHECKPOINT + " ORDER BY " + KEY_ID + " ASC ";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckPoint checkPoint = new CheckPoint();
                checkPoint.setId(Integer.parseInt(cursor.getString(0)));
                //checkPoint.setLatitude(cursor.getInt(1));
                checkPoint.setLatitude(Double.parseDouble(cursor.getString(1)));
                //checkPoint.setLongitude(cursor.getInt(2));
                checkPoint.setLongitude(Double.parseDouble(cursor.getString(2)));
                checkPoint.setChallangeID(cursor.getString(3));
                checkPoint.setClue(cursor.getString(4));
                checkPoint.setSolved(cursor.getInt(5) == 1);
                checkPoint.setGiveUp(cursor.getInt(6) == 1);
                
                
                // Adding contact to list
                checkPointList.add(checkPoint);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return checkPointList;
	}
	 
	public List<String> getSolvedClues(boolean includeAll){
		
		List<String> clues = new ArrayList<String>();
		
        // Select All Query
        String selectQuery = "SELECT "+ KEY_CLUE + "," + KEY_SOLVED + "," + KEY_GIVEUP + " FROM " + TABLE_CHECKPOINT;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        if (cursor.moveToFirst()) {
            do {
                if ( cursor.getInt(1) == 1) {
                	if (includeAll) {
                		clues.add(cursor.getString(0));
                	} else {
                		if (cursor.getInt(2) == 0) {
                			clues.add(cursor.getString(0));
                		}
                	}
                }
            } while (cursor.moveToNext());
        }
        
		return clues;
	}
	
	//Get Solved Clues Count 
	public int getSolvedCluesCount() {
		return getSolvedClues(true).size();
	}


	// Getting checkpoint Count
	public int getCheckPointCount() {
		
		String countQuery = "SELECT * FROM " + TABLE_CHECKPOINT;
		
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
	}
	
	// Updating single contact
	public int updateCheckPoint(CheckPoint checkPoint) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, checkPoint.getLatitude());
	    values.put(KEY_LONGITUDE, checkPoint.getLongitude());
	    values.put(KEY_CHALLANGEID, checkPoint.getChallangeID());
	    values.put(KEY_CLUE, checkPoint.getClue());
	    values.put(KEY_SOLVED, checkPoint.isSolved());
	    values.put(KEY_GIVEUP, checkPoint.isGiveUp());
 
        // updating row
        return db.update(TABLE_CHECKPOINT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(checkPoint.getId()) });
	}
	 
	// Deleting single contact
	public void deleteCheckPoint(CheckPoint checkPoint) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		 
		db.delete(TABLE_CHECKPOINT, KEY_ID + " = ?",
	                new String[] { String.valueOf(checkPoint.getId()) });
	    db.close();
	}
	
	public boolean isCheckPointUnLocked(CheckPoint checkPoint) {
		boolean unLocked = false;
		
		List<CheckPoint> checkPoints = getAllCheckPoint();
		int n = checkPoints.size();
		for ( int i = 0; i < n; i++) {
			CheckPoint cn = checkPoints.get(i);
			if ( i == 0 && checkPoint.getId() == cn.getId() ) {
				unLocked = true;
			} else if ( i > 0 && checkPoints.get(i-1).isSolved() && checkPoint.getId() == cn.getId() ) {
				unLocked = true;
			}
		}
		
		return unLocked;
	}

}
