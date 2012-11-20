package com.metat.models;

public class MeetupContact {
	private long _id;
	private String _meetupId;
	private String _photoThumbnail;
	private String _name;
	private String _link;
	private String _meetupGroupId;

	private String _twitterId;
	private String _linkedInId;
	private String _facebookId;
	private String _tumblrId;
	private String _flickrId;

	public MeetupContact(long id, String meetupId, String photoThumbnail, String name, String link, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId, String meetupGroupId)
	{
		_id = id;
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;
		_link = link;

		_twitterId = twitterId;
		_linkedInId = linkedInId;
		_facebookId = facebookId;
		_tumblrId = tumblrId;
		_flickrId = flickrId;
		
		_meetupGroupId = meetupGroupId;
	}
	
	public MeetupContact(String meetupId, String photoThumbnail, String name, String twitterId, String linkedInId, String facebookId, String tumblrId, String flickrId, String meetupGroupId)
	{
		_meetupId = meetupId;
		_photoThumbnail = photoThumbnail;
		_name = name;

		_twitterId = twitterId;
		_linkedInId = linkedInId;
		_facebookId = facebookId;
		_tumblrId = tumblrId;
		_flickrId = flickrId;

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
	
	public String getTwitterId()
	{
		return _twitterId;
	}
	
	public void setTwitterId(String twitterId)
	{
		_twitterId = twitterId;
	}
	
	public String getLinkedInId()
	{
		return _linkedInId;
	}
	
	public void setLinkedInId(String linkedInId)
	{
		_linkedInId = linkedInId;
	}
	
	public String getFacebookId()
	{
		return _facebookId;
	}
	
	public void setFacebookId(String facebookId)
	{
		_facebookId = facebookId;
	}
	
	public String getTumblrId()
	{
		return _tumblrId;
	}
	
	public void setTumblrId(String tumblrId)
	{
		_tumblrId = tumblrId;
	}
	
	public String getFlickrId()
	{
		return _flickrId;
	}
	
	public void setFlickrId(String flickrId)
	{
		_flickrId = flickrId;
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
