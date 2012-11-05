package com.metat.dataaccess;

import java.util.ArrayList;

import com.metat.models.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactDataAccess {
	public static final String KEY_ID = "_id";
	public static final String KEY_MEEETUPID = "meetupId";
	public static final String KEY_THUMBNAIL = "thumbnail";
	public static final String KEY_FIRSTNAME = "firstname";
	public static final String KEY_LASTNAME = "lastname";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_GROUPMEETUPID = "groupMeetupId";
	public static final String KEY_GROUPNAME = "groupName";

	public static final String DATABASE_TABLE = "Contact";

	private DatabaseHelper _dbHelper;
	
	public ContactDataAccess(Context context) 
	{
		_dbHelper = DatabaseHelper.getInstance(context);
	}
	
	public Contact[] getAllContacts()
	{
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_FIRSTNAME, KEY_LASTNAME, KEY_GROUPMEETUPID, KEY_GROUPNAME }, null, null, null, null, KEY_FIRSTNAME + " ASC, " + KEY_LASTNAME + " ASC");

		if (contactsCursor != null)
		{
			ArrayList<Contact> contacts = new ArrayList<Contact>();

			if (contactsCursor.moveToFirst())
			{
				do
				{
					contacts.add(new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FIRSTNAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LASTNAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME))));
				}
				while (contactsCursor.moveToNext());
			}
			
			contactsCursor.close();
			
			return contacts.toArray(new Contact[contacts.size()]);
		}
		else
			return new Contact[0];
	}
	
	public Contact getContact(long id)
	{
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_FIRSTNAME, KEY_LASTNAME, KEY_EMAIL, KEY_PHONE, KEY_NOTES,KEY_GROUPMEETUPID, KEY_GROUPNAME }, KEY_ID + " = " + id, null, null, null, KEY_FIRSTNAME + " ASC, " + KEY_LASTNAME + " ASC");

		if (contactsCursor != null)
		{
			Contact contact = null;
			
			if (contactsCursor.moveToFirst())
			{
				do
				{
					contact = new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FIRSTNAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LASTNAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_EMAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_PHONE)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NOTES)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME)));
				}
				while (contactsCursor.moveToNext());
			}
			
			contactsCursor.close();
			
			return contact;
		}
		else
			return null;
	}
	
	public void Insert(String meetupId, byte[] thumbnail, String firstname, String lastname, String email, String phone, String notes, String groupId, String groupName)
	{
			ContentValues values = new ContentValues();
			values.put(KEY_MEEETUPID, meetupId);
			values.put(KEY_THUMBNAIL, thumbnail);
			values.put(KEY_FIRSTNAME, firstname);
			values.put(KEY_LASTNAME, lastname);
			values.put(KEY_EMAIL, email);
			values.put(KEY_PHONE, phone);
			values.put(KEY_NOTES, notes);
			values.put(KEY_GROUPMEETUPID, groupId);
			values.put(KEY_GROUPNAME, groupName);
			
			_dbHelper.getWritableDatabase().insert(DATABASE_TABLE, null, values);
	}
	
	public boolean updateNote(long id, byte[] thumbnail, String firstname, String lastname, String email, String phone, String notes, String groupId, String groupName)
	{
		ContentValues values = new ContentValues();
		if ((thumbnail != null) && (thumbnail.length > 0))
		{
			values.put(KEY_THUMBNAIL, thumbnail);
		}
		values.put(KEY_FIRSTNAME, firstname);
		values.put(KEY_LASTNAME, lastname);
		values.put(KEY_EMAIL, email);
		values.put(KEY_PHONE, phone);
		values.put(KEY_NOTES, notes);
		values.put(KEY_GROUPMEETUPID, groupId);
		values.put(KEY_GROUPNAME, groupName);
		
		return _dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_ID + " = " + id, null) > 0;
	}
	
	public void UpdateGroupNames(String meetupId, String name)
	{
		ContentValues ownerValues = new ContentValues();
		ownerValues.put(KEY_GROUPNAME, name);
		
		_dbHelper.getWritableDatabase().update(DATABASE_TABLE, ownerValues, KEY_GROUPMEETUPID + " = '" + meetupId + "'", null);
	}
	
	public boolean Delete(long id)
	{
		return (_dbHelper.getWritableDatabase().delete(DATABASE_TABLE, KEY_ID + " = " + id, null) > 0);
	}
}
