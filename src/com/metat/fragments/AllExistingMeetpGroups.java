package com.metat.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.metat.adapters.MeetupGroupsAdapter;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.main.GroupActivity;
import com.metat.main.MainActivity;
import com.metat.models.Contact;
import com.metat.models.Group;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AllExistingMeetpGroups extends ListFragment {
	private MeetupGroupsAdapter _meetupGroupsAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		bindMeetupGroupsAdapter();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent intent = new Intent(getActivity(), GroupActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.putExtra("groupId", ((Group)parent.getAdapter().getItem(position)).getMeetupId());

        		getActivity().startActivity(intent);	
		}});
	}
	
	private void bindMeetupGroupsAdapter()
	{
		GroupsDataAccess groupDataAccess = new GroupsDataAccess(getActivity());
		
		Map<String, Group> allUserGroups = new LinkedHashMap<String,Group>();
		
		for (Group group : groupDataAccess.getAllGroups())
		{
			allUserGroups.put(group.getMeetupId(), new Group(group.getMeetupId(), group.getName()));
		}
	
		for (Contact contact : MainActivity.AllContacts)
		{
			if (!allUserGroups.containsKey(contact.getGroupId()))
				allUserGroups.put(contact.getGroupId(), new Group(contact.getGroupId(), contact.getGroupName()));
			
			allUserGroups.get(contact.getGroupId()).addMemberCount();
		}
		
		ArrayList<Group> distinctGroups = new ArrayList<Group>();
		
		for(String key : allUserGroups.keySet())
		{
			distinctGroups.add(allUserGroups.get(key));
		}
		
		Collections.sort(distinctGroups);
		
		_meetupGroupsAdapter = new MeetupGroupsAdapter(getActivity(), distinctGroups.toArray(new Group[distinctGroups.size()]));
		
		setListAdapter(_meetupGroupsAdapter);
	}
}
