package com.metat.dataaccess;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.metat.dataaccess.DatabaseHelper;
import com.metat.models.Group;

public class GroupsDataAccess {
	public static final String KEY_ID = "_id";
	public static final String KEY_MEEETUPID = "meetupId";
	public static final String KEY_NAME = "name";
	public static final String KEY_LINK = "link";

	public static final String DATABASE_TABLE = "MeetupGroup";

	private DatabaseHelper _dbHelper;
	
	public GroupsDataAccess(Context context) 
	{
		_dbHelper = DatabaseHelper.getInstance(context);
	}
	
	public Group getGroup(String id)
	{
		Cursor groupsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_NAME, KEY_LINK }, KEY_MEEETUPID + " = '" + id + "'", null, null, null, KEY_NAME + " ASC");

		if (groupsCursor != null)
		{
			Group group = null;
			
			if (groupsCursor.moveToFirst())
			{
				do
				{
					group = new Group(groupsCursor.getLong(groupsCursor.getColumnIndex(KEY_ID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_MEEETUPID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_NAME)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_LINK)));
				}
				while (groupsCursor.moveToNext());
			}
			
			groupsCursor.close();
			
			return group;
		}
		else
			return null;
	}
	
	public Group[] getAllGroups()
	{
		Cursor groupsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_NAME, KEY_LINK }, null, null, null, null, KEY_NAME + " ASC");

		if (groupsCursor != null)
		{
			ArrayList<Group> groups = new ArrayList<Group>();

			if (groupsCursor.moveToFirst())
			{
				do
				{
					Group group = new Group(groupsCursor.getLong(groupsCursor.getColumnIndex(KEY_ID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_MEEETUPID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_NAME)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_LINK)));
					groups.add(group);
				}
				while (groupsCursor.moveToNext());
			}
			
			groupsCursor.close();
			

			return groups.toArray(new Group[groups.size()]);
		}
		else
			return new Group[0];
	}
	
	public void Insert(Group group)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_MEEETUPID, group.getMeetupId());
		values.put(KEY_NAME, group.getName());
		values.put(KEY_LINK, group.getLink());
		
		_dbHelper.getWritableDatabase().insert(DATABASE_TABLE, null, values);
	}
	
	public void Update(String meetupId, String name, String link)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_LINK, link);
		
		_dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_MEEETUPID + " = '" + meetupId + "'", null);
	}
	
	public boolean Delete(String meetupId)
	{
		return (_dbHelper.getWritableDatabase().delete(DATABASE_TABLE, KEY_MEEETUPID + " = '" + meetupId + "'", null) > 0);
	}
}
