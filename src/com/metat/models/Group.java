package com.metat.models;

public class Group {
	private long _id;
	private String _meetupId;
	private String _name;
	
	public Group(String meetupId, String name)
	{
		_id = -1;
		_meetupId = meetupId;
		_name = name;
	}
	
	public Group(long id, String meetupId, String name)
	{
		_id = id;
		_meetupId = meetupId;
		_name = name;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public String getMeetupId()
	{
		return _meetupId;
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
