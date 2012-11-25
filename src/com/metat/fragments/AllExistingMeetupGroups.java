package com.metat.fragments;

import com.metat.adapters.MeetupGroupsAdapter;
import com.metat.main.GroupActivity;
import com.metat.main.MainActivity;
import com.metat.models.Group;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AllExistingMeetupGroups extends ListFragment {
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
	
	public void bindMeetupGroupsAdapter()
	{
		_meetupGroupsAdapter = new MeetupGroupsAdapter(getActivity(), MainActivity.AllGroups);
		
		setListAdapter(_meetupGroupsAdapter);
	}
}
