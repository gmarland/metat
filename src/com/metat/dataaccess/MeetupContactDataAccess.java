package com.metat.dataaccess;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.metat.models.MeetupContact;

public class MeetupContactDataAccess {
	public static final String KEY_ID = "_id";
	public static final String KEY_MEEETUPID = "meetupId";
	public static final String KEY_PHOTOURL = "photoUrl";
	public static final String KEY_NAME = "name";
	public static final String KEY_LINK = "link";
	public static final String KEY_GROUPMEETUPID = "groupMeetupId";

	public static final String DATABASE_TABLE = "MeetupContact";

	private DatabaseHelper _dbHelper;
	
	public MeetupContactDataAccess(Context context) 
	{
		_dbHelper = DatabaseHelper.getInstance(context);
	}
	
	public MeetupContact getMeetupContact(String id)
	{
		Cursor contactCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_PHOTOURL, KEY_NAME, KEY_LINK, KEY_GROUPMEETUPID }, KEY_MEEETUPID + " = '" + id + "'", null, null, null, KEY_NAME + " ASC");

		if (contactCursor != null)
		{
			MeetupContact meetupContact = null;
			
			if (contactCursor.moveToFirst())
			{
				do
				{
					meetupContact = new MeetupContact(contactCursor.getLong(contactCursor.getColumnIndex(KEY_ID)), contactCursor.getString(contactCursor.getColumnIndex(KEY_MEEETUPID)), contactCursor.getString(contactCursor.getColumnIndex(KEY_PHOTOURL)), contactCursor.getString(contactCursor.getColumnIndex(KEY_NAME)), contactCursor.getString(contactCursor.getColumnIndex(KEY_LINK)),  contactCursor.getString(contactCursor.getColumnIndex(KEY_GROUPMEETUPID)));
				}
				while (contactCursor.moveToNext());
			}
			
			contactCursor.close();
			
			return meetupContact;
		}
		else
			return null;
	}
	
	public ArrayList<MeetupContact> getAllMeetupContacts(String groupMeetupId)
	{
		Cursor contactCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_NAME, KEY_PHOTOURL, KEY_LINK, KEY_GROUPMEETUPID }, KEY_GROUPMEETUPID + " = '" + groupMeetupId + "'", null, null, null, KEY_NAME + " ASC");

		if (contactCursor != null)
		{
			ArrayList<MeetupContact> meetupContacts = new ArrayList<MeetupContact>();

			if (contactCursor.moveToFirst())
			{
				do
				{
					MeetupContact meetupContact = new MeetupContact(contactCursor.getLong(contactCursor.getColumnIndex(KEY_ID)), contactCursor.getString(contactCursor.getColumnIndex(KEY_MEEETUPID)), contactCursor.getString(contactCursor.getColumnIndex(KEY_PHOTOURL)), contactCursor.getString(contactCursor.getColumnIndex(KEY_NAME)), contactCursor.getString(contactCursor.getColumnIndex(KEY_LINK)),  contactCursor.getString(contactCursor.getColumnIndex(KEY_GROUPMEETUPID)));
					meetupContacts.add(meetupContact);
				}
				while (contactCursor.moveToNext());
			}
			
			contactCursor.close();
			

			return meetupContacts;
		}
		else
			return new ArrayList<MeetupContact>();
	}
	
	public void Insert(MeetupContact meetupContact)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_MEEETUPID, meetupContact.getMeetupId());
		values.put(KEY_PHOTOURL, meetupContact.getPhotoThumbnail());
		values.put(KEY_NAME, meetupContact.getName());
		values.put(KEY_LINK, meetupContact.getLink());
		values.put(KEY_GROUPMEETUPID, meetupContact.getMeetupGroupId());
		
		_dbHelper.getWritableDatabase().insert(DATABASE_TABLE, null, values);
	}
	
	public void Update(String meetupId, String photoUrl, String name, String link)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_PHOTOURL, photoUrl);
		values.put(KEY_NAME, name);
		values.put(KEY_LINK, link);
		
		_dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_MEEETUPID + " = '" + meetupId + "'", null);
	}
	
	public boolean Delete(String meetupId)
	{
		return (_dbHelper.getWritableDatabase().delete(DATABASE_TABLE, KEY_MEEETUPID + " = '" + meetupId + "'", null) > 0);
	}
}
