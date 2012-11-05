package com.metat.fragments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.metat.adapters.MeetupGroupsAdapter;
import com.metat.main.MainActivity;
import com.metat.models.Contact;
import com.metat.models.Group;

import android.app.ListFragment;
import android.os.Bundle;

public class AllExistingMeetpGroups extends ListFragment {
	private MeetupGroupsAdapter _meetupGroupsAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		bindMeetupGroupsAdapter();
	}
	
	private void bindMeetupGroupsAdapter()
	{
		Map<String, Group> allGroups = new LinkedHashMap<String,Group>();
	
		for (Contact contact : MainActivity.AllContacts)
		{
			if (!allGroups.containsKey(contact.getGroupId()))
				allGroups.put(contact.getGroupId(), new Group(contact.getGroupId(), contact.getGroupName()));
		}
		
		ArrayList<Group> distinctGroups = new ArrayList<Group>();
		
		for(String key : allGroups.keySet())
		{
			distinctGroups.add(allGroups.get(key));
		}
		
		_meetupGroupsAdapter = new MeetupGroupsAdapter(getActivity(), distinctGroups.toArray(new Group[distinctGroups.size()]));
		
		setListAdapter(_meetupGroupsAdapter);
	}
}
