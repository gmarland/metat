package com.metat.models;

public class Contact {
	private long _id;
	private String _meetupId;
	private byte[] _photoThumbnail;
	private String _firstname;
	private String _lastname;
	private String _email;
	private String _phone;
	private String _notes;

	private String _groupId;
	private String _groupName;
	
	public Contact(long id, String meetupId, byte[] photoThumbnail, String firstname, String lastname, String groupId, String groupName)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_firstname = firstname;
		_lastname = lastname;

		_groupId = groupId;
		_groupName = groupName;
	}
	
	public Contact(long id, String meetupId, byte[] photoThumbnail, String firstname, String lastname, String email, String phone, String notes, String groupId, String groupName)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_firstname = firstname;
		_lastname = lastname;
		_email = email;
		_phone = phone;
		_notes = notes;
		
		_groupId = groupId;
		_groupName = groupName;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public String getMeetupId()
	{
		return _meetupId;
	}
	
	public byte[] getPhotoThumbnail()
	{
		return _photoThumbnail;
	}
	
	public String getFirstname()
	{
		return _firstname;
	}
	
	public String getLastName()
	{
		return _lastname;
	}
	
	public String getEmail()
	{
		return _email;
	}
	
	public String getPhone()
	{
		return _phone;
	}
	
	public String getNotes()
	{
		return _notes;
	}
	
	public String getGroupId()
	{
		return _groupId;
	}
	
	public String getGroupName()
	{
		return _groupName;
	}
}
