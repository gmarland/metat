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

	public static final String DATABASE_TABLE = "MeetupGroup";

	private DatabaseHelper _dbHelper;
	
	public GroupsDataAccess(Context context) 
	{
		_dbHelper = DatabaseHelper.getInstance(context);
	}
	
	public Group[] getAllGroups()
	{
		Cursor groupsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_NAME }, null, null, null, null, KEY_NAME + " ASC");

		if (groupsCursor != null)
		{
			ArrayList<Group> groups = new ArrayList<Group>();

			if (groupsCursor.moveToFirst())
			{
				do
				{
					Group group = new Group(groupsCursor.getLong(groupsCursor.getColumnIndex(KEY_ID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_MEEETUPID)), groupsCursor.getString(groupsCursor.getColumnIndex(KEY_NAME)));
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
	
	public void Insert(Group[] groups)
	{
		for (Group group : groups)
		{
			ContentValues values = new ContentValues();
			values.put(KEY_MEEETUPID, group.getMeetupId());
			values.put(KEY_NAME, group.getName());
			
			_dbHelper.getWritableDatabase().insert(DATABASE_TABLE, null, values);
		}
	}
	
	public boolean DeleteAll()
	{
		return (_dbHelper.getWritableDatabase().delete(DATABASE_TABLE, null, null) > 0);
	}
}
