package com.metat.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper 
{
	public static final String DATABASE_NAME = "MetAt";
	public static final int DATABASE_VERSION = 3;

	private Context _context;
	private static DatabaseHelper _dbHelper = null;
	
	private String GROUP_TABLE_CREATE = "CREATE TABLE MeetupGroup (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										"meetupId TEXT NULL, " +
										"name TEXT NOT NULL, " +
										"link TEXT NULL)";
	
	private String MEETUP_CONTACT_TABLE_CREATE = "CREATE TABLE MeetupContact (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										"groupMeetupId TEXT NULL, " +
										"meetupId TEXT NOT NULL, " +
										"name TEXT NOT NULL, " +
										"bio TEXT NOT NULL, " +
										"photoUrl NULL, " +
										"link TEXT NULL," +
										"facebookLink TEXT NULL," +
										"twitterLink TEXT NULL," +
										"flickrLink TEXT NULL," +
										"linkedInLink TEXT NULL," +
										"tumblrLink TEXT NULL)";

	private String CONTACT_TABLE_CREATE = "CREATE TABLE Contact (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										"meetupId TEXT NULL, " + 
										"thumbnail BLOB NOT NULL, " + 
										"name TEXT NOT NULL, " + 
										"website TEXT NOT NULL, " + 
										"email TEXT NULL, " +
										"phone TEXT NULL, " +
										"notes TEXT NULL, " +
										"link TEXT NULL," +
										"facebookLink TEXT NULL," +
										"twitterLink TEXT NULL," +
										"flickrLink TEXT NULL," +
										"linkedInLink TEXT NULL," +
										"tumblrLink TEXT NULL, " +
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
		db.execSQL(MEETUP_CONTACT_TABLE_CREATE);
		db.execSQL(CONTACT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
    	for (int i=oldVersion+1; i<= newVersion; i++)
    	{
    		if (oldVersion < 2)
    		{
    			db.execSQL(MEETUP_CONTACT_TABLE_CREATE);
    			db.execSQL("ALTER TABLE MeetupGroup ADD COLUMN link TEXT NULL;");
    			db.execSQL("ALTER TABLE Contact ADD COLUMN link TEXT NULL;");
    			db.execSQL("ALTER TABLE Contact ADD COLUMN groupLink TEXT NULL;");
    		}
    		
    		if (oldVersion < 3)
    		{
    			db.execSQL("ALTER TABLE MeetupContact ADD COLUMN bio TEXT NULL;");
    		}
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
