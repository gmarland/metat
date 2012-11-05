package com.metat.models;

public class MeetupContact {
	private String _meetupId;
	private String _photoThumbnail;
	private String _name;
	
	public MeetupContact(String meetupId, String photoThumbnail, String name)
	{
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
	}

	public MeetupContact(String meetupId, String name)
	{
		_meetupId = meetupId;
		_name = name;
	}
	
	public String getMeetupId()
	{
		return _meetupId;
	}
	
	public String getPhotoThumbnail()
	{
		return _photoThumbnail;
	}
	
	public String getName()
	{
		return _name;
	}
	
    @Override
    public String toString() {
        return _name;
    }
}
