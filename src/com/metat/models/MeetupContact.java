package com.metat.models;

public class MeetupContact {
	private long _id;
	private String _meetupId;
	private String _photoThumbnail;
	private String _name;
	private String _link;
	private String _meetupGroupId;

	public MeetupContact(long id, String meetupId, String photoThumbnail, String name, String link, String meetupGroupId)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
		_link = link;
		_meetupGroupId = meetupGroupId;
	}
	
	public MeetupContact(String meetupId, String photoThumbnail, String name, String meetupGroupId)
	{
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
		_meetupGroupId = meetupGroupId;
	}

	public MeetupContact(String meetupId, String name, String meetupGroupId)
	{
		_meetupId = meetupId;
		_name = name;
		_meetupGroupId = meetupGroupId;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public String getMeetupId()
	{
		return _meetupId;
	}
	
	public String getPhotoThumbnail()
	{
		return _photoThumbnail;
	}
	
	public void setPhotoThumbnail(String photoThumbnail)
	{
		_photoThumbnail = photoThumbnail;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public String getLink()
	{
		return _link;
	}
	
	public void setLink(String link)
	{
		_link = link;
	}
	
	public String getMeetupGroupId()
	{
		return _meetupGroupId;
	}
	
    @Override
    public String toString() {
        return _name;
    }
}
