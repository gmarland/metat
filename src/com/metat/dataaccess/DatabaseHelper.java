package com.metat.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper 
{
	public static final String DATABASE_NAME = "MetAt";
	public static final int DATABASE_VERSION = 1;

	private Context _context;
	private static DatabaseHelper _dbHelper = null;
	
	private String GROUP_TABLE_CREATE = "CREATE TABLE MeetupGroup (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										"meetupId TEXT NULL, " +
										"name TEXT NOT NULL)";

	private String CONTACT_TABLE_CREATE = "CREATE TABLE Contact (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										"meetupId TEXT NULL, " + 
										"thumbnail BLOB NOT NULL, " + 
										"firstname TEXT NOT NULL, " + 
										"lastname TEXT NULL, " +
										"email TEXT NULL, " +
										"phone TEXT NULL, " +
										"notes TEXT NULL, " +
										"groupMeetupId TEXT NULL, " +
										"groupName TEXT NOT NULL)";
	
    private DatabaseHelper(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) 
    {
		db.execSQL(GROUP_TABLE_CREATE);
		db.execSQL(CONTACT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
    	for (int i=oldVersion+1; i<= newVersion; i++)
    	{
    	}
    }
    
    public Context getContext()
    {
    	return _context;
    }
    
    public static DatabaseHelper getInstance(Context ctx)
    {
    	if (_dbHelper == null)
    		_dbHelper = new DatabaseHelper(ctx.getApplicationContext());

    	return _dbHelper;  
    }
    
    public static void closeConnection()
    {
    	if (_dbHelper != null)
    	{
    		try
    		{
		    	_dbHelper.close();
		    	_dbHelper = null;
    		}
    		catch(Exception ex) {}
	    }
    }
}
