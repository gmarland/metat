package com.metat.models;

public class Contact {
	private long _id;
	private String _meetupId;
	private byte[] _photoThumbnail;
	private String _name;
	private String _email;
	private String _phone;
	private String _notes;
	private String _link;

	private String _twitterId;
	private String _linkedInId;
	private String _facebookId;
	private String _tumblrId;
	private String _flickrId;

	private String _groupId;
	private String _groupName;
	
	public Contact(long id, String meetupId, byte[] photoThumbnail, String name, String link, String groupId, String groupName)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
		_link = link;

		_groupId = groupId;
		_groupName = groupName;
	}
	
	public Contact(long id, String meetupId, byte[] photoThumbnail, String name, String email, String phone, String notes, String link, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId, String groupId, String groupName)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
		_email = email;
		_phone = phone;
		_notes = notes;
		_link = link;

		_twitterId = twitterId;
		_linkedInId = linkedInId;
		_facebookId = facebookId;
		_tumblrId = tumblrId;
		_flickrId = flickrId;
		
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
	
	public String getName()
	{
		return _name;
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
	
	public String getLink()
	{
		return _link;
	}
	
	public String getTwitterId()
	{
		return _twitterId;
	}
	
	public String getLinkedInId()
	{
		return _linkedInId;
	}
	
	public String getFacebookId()
	{
		return _facebookId;
	}
	
	public String getTumblrId()
	{
		return _tumblrId;
	}
	
	public String getFlickrId()
	{
		return _flickrId;
	}
	
	public String getGroupId()
	{
		return _groupId;
	}
	
	public String getGroupName()
	{
		return _groupName;
	}
	
    @Override
    public String toString() {
        return _name;
    }
}
