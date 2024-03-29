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
	public static final String KEY_NAME = "name";
	public static final String KEY_WEBSITE = "website";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_LINK = "link";
	public static final String KEY_FACEBOOK_LINK = "facebookLink";
	public static final String KEY_TWITTER_LINK = "twitterLink";
	public static final String KEY_FLICKR_LINK = "flickrLink";
	public static final String KEY_LINKEDIN_LINK = "linkedInLink";
	public static final String KEY_TUMBLR_LINK = "tumblrLink";
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
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_NAME, KEY_LINK, KEY_GROUPMEETUPID, KEY_GROUPNAME }, null, null, null, null, KEY_NAME + " ASC");

		if (contactsCursor != null)
		{
			ArrayList<Contact> contacts = new ArrayList<Contact>();

			if (contactsCursor.moveToFirst())
			{
				do
				{
					contacts.add(new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME))));
				}
				while (contactsCursor.moveToNext());
			}
			
			contactsCursor.close();
			
			return contacts.toArray(new Contact[contacts.size()]);
		}
		else
			return new Contact[0];
	}
	
	public Contact[] getAllContacts(String groupMeetupId)
	{
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_NAME, KEY_LINK, KEY_GROUPMEETUPID, KEY_GROUPNAME }, KEY_GROUPMEETUPID + " = '" + groupMeetupId + "'", null, null, null, KEY_NAME + " ASC");

		if (contactsCursor != null)
		{
			ArrayList<Contact> contacts = new ArrayList<Contact>();

			if (contactsCursor.moveToFirst())
			{
				do
				{
					contacts.add(new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME))));
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
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_NAME, KEY_WEBSITE, KEY_EMAIL, KEY_PHONE, KEY_NOTES, KEY_LINK, KEY_FACEBOOK_LINK, KEY_TWITTER_LINK, KEY_FLICKR_LINK, KEY_LINKEDIN_LINK, KEY_TUMBLR_LINK, KEY_GROUPMEETUPID, KEY_GROUPNAME }, KEY_ID + " = " + id, null, null, null, KEY_NAME + " ASC");

		if (contactsCursor != null)
		{
			Contact contact = null;
			
			if (contactsCursor.moveToFirst())
			{
				do
				{
					contact = new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_WEBSITE)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_EMAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_PHONE)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NOTES)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_TWITTER_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINKEDIN_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FACEBOOK_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_TUMBLR_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FLICKR_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME)));
				}
				while (contactsCursor.moveToNext());
			}
			
			contactsCursor.close();
			
			return contact;
		}
		else
			return null;
	}

	public Contact getContact(String meetupId)
	{
		Cursor contactsCursor= _dbHelper.getReadableDatabase().query(DATABASE_TABLE, new String[] { KEY_ID, KEY_MEEETUPID, KEY_THUMBNAIL, KEY_NAME, KEY_WEBSITE, KEY_EMAIL, KEY_PHONE, KEY_NOTES, KEY_LINK, KEY_FACEBOOK_LINK, KEY_TWITTER_LINK, KEY_FLICKR_LINK, KEY_LINKEDIN_LINK, KEY_TUMBLR_LINK, KEY_GROUPMEETUPID, KEY_GROUPNAME }, KEY_MEEETUPID + " = '" + meetupId + "'", null, null, null, KEY_NAME + " ASC");

		if (contactsCursor != null)
		{
			Contact contact = null;
			
			if (contactsCursor.moveToFirst())
			{
				do
				{
					contact = new Contact(contactsCursor.getLong(contactsCursor.getColumnIndex(KEY_ID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_MEEETUPID)), contactsCursor.getBlob(contactsCursor.getColumnIndex(KEY_THUMBNAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NAME)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_WEBSITE)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_EMAIL)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_PHONE)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_NOTES)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_TWITTER_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_LINKEDIN_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FACEBOOK_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_TUMBLR_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_FLICKR_LINK)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPMEETUPID)), contactsCursor.getString(contactsCursor.getColumnIndex(KEY_GROUPNAME)));
				}
				while (contactsCursor.moveToNext());
			}
			
			contactsCursor.close();
			
			return contact;
		}
		else
			return null;
	}
	
	public void Insert(String meetupId, byte[] thumbnail, String name, String website, String email, String phone, String notes, String link, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId, String groupId, String groupName)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_MEEETUPID, meetupId);
		values.put(KEY_THUMBNAIL, thumbnail);
		values.put(KEY_NAME, name);
		values.put(KEY_WEBSITE, website);
		values.put(KEY_EMAIL, email);
		values.put(KEY_PHONE, phone);
		values.put(KEY_NOTES, notes);
		values.put(KEY_LINK, link);
		values.put(KEY_FACEBOOK_LINK,  facebookId);
		values.put(KEY_TWITTER_LINK, twitterId);
		values.put(KEY_FLICKR_LINK, flickrId);
		values.put(KEY_LINKEDIN_LINK, linkedInId);
		values.put(KEY_TUMBLR_LINK, tumblrId);
		values.put(KEY_GROUPMEETUPID, groupId);
		values.put(KEY_GROUPNAME, groupName);
		
		_dbHelper.getWritableDatabase().insert(DATABASE_TABLE, null, values);
	}
	
	public void updateImage(String meetupId, byte[] thumbnail)
	{
		ContentValues values = new ContentValues();
		if ((thumbnail != null) && (thumbnail.length > 0))
		{
			values.put(KEY_THUMBNAIL, thumbnail);
			_dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_MEEETUPID + " = '" + meetupId + "'", null);
		}
	}
	
	public boolean updateContact(long id, String meetupId, byte[] thumbnail, String name, String website, String email, String phone, String notes, String link, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_MEEETUPID, meetupId);
		if ((thumbnail != null) && (thumbnail.length > 0))
		{
			values.put(KEY_THUMBNAIL, thumbnail);
		}
		values.put(KEY_NAME, name);
		values.put(KEY_WEBSITE, website);
		values.put(KEY_EMAIL, email);
		values.put(KEY_PHONE, phone);
		values.put(KEY_NOTES, notes);
		values.put(KEY_LINK, link);
		values.put(KEY_FACEBOOK_LINK,  facebookId);
		values.put(KEY_TWITTER_LINK, twitterId);
		values.put(KEY_FLICKR_LINK, flickrId);
		values.put(KEY_LINKEDIN_LINK, linkedInId);
		values.put(KEY_TUMBLR_LINK, tumblrId);
		
		return _dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_ID + " = " + id, null) > 0;
	}
	
	public boolean updateContact(String meetupId, String link, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_LINK, link);
		values.put(KEY_FACEBOOK_LINK,  facebookId);
		values.put(KEY_TWITTER_LINK, twitterId);
		values.put(KEY_FLICKR_LINK, flickrId);
		values.put(KEY_LINKEDIN_LINK, linkedInId);
		values.put(KEY_TUMBLR_LINK, tumblrId);
		
		return _dbHelper.getWritableDatabase().update(DATABASE_TABLE, values, KEY_MEEETUPID + " = '" + meetupId + "'", null) > 0;
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
