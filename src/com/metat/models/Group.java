package com.metat.models;

public class Group implements Comparable<Group> {
	private long _id;
	private String _meetupId;
	private String _name;
	private String _link;
	
	private int _memberCount = 0;
	
	public Group(String meetupId, String name)
	{
		_id = -1;
		_meetupId = meetupId;
		_name = name;
	}
	
	public Group(long id, String meetupId, String name, String link)
	{
		_id = id;
		_meetupId = meetupId;
		_name = name;
		_link = link;
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
	
	public void addMemberCount()
	{
		_memberCount++;
	}
	
	public int getMemberCount()
	{
		return _memberCount;
	}
	
	public String getLink()
	{
		return _link;
	}
	
    @Override
    public String toString() {
        return _name;
    }

	public int compareTo(Group compareGroup) {
		return (compareGroup.getName().toLowerCase().compareTo(_name.toLowerCase())*-1);
	}
}
