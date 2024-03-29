package com.metat.fragments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.metat.adapters.BrowseGroupAdapter;
import com.metat.adapters.BrowseGroupSectionAdapter;
import com.metat.contacts.R;
import com.metat.dataaccess.MeetupContactDataAccess;
import com.metat.main.ViewMeetupContactActivity;
import com.metat.models.MeetupContact;
import com.metat.models.NavigationSource;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.AdapterView.OnItemClickListener;

public class BrowseGroupContacts extends ListFragment {
	private String _groupId;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		Bundle args = null;

		if (savedInstanceState != null)
			args = savedInstanceState;
		else if (getArguments() != null) 
			args = this.getArguments();
		
		if ((args != null) && (getArguments().containsKey("groupId")))
			_groupId = args.getString("groupId");
		
		bindContactsAdapter();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent intent = new Intent(getActivity(), ViewMeetupContactActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.putExtra("contactId", ((MeetupContact)parent.getAdapter().getItem(position)).getId());
        		intent.putExtra("navigationSource", NavigationSource.GroupContacts);

        		getActivity().startActivity(intent);	
		}});
		
		getListView().setFastScrollEnabled(true);
	}
	
	public void bindContactsAdapter()
	{
		MeetupContactDataAccess meetupDataAccess = new MeetupContactDataAccess(getActivity());
		List<MeetupContact> meetupContacts = meetupDataAccess.getAllMeetupContacts(_groupId);
		
		BrowseGroupAdapter groupAdapter = new BrowseGroupAdapter(getActivity());
		
		Map<String,List<MeetupContact>> contactSections = new LinkedHashMap<String,List<MeetupContact>>();

		for (MeetupContact contact : meetupContacts)
		{
			if (!contactSections.containsKey(contact.getName().substring(0, 1).toUpperCase()))
				contactSections.put(contact.getName().substring(0, 1).toUpperCase(), new ArrayList<MeetupContact>());
			
			contactSections.get(contact.getName().substring(0, 1).toUpperCase()).add(contact);
		}
		
		for (String headerIndex : contactSections.keySet())
		{
			BrowseGroupSectionAdapter browseGroupSectionAdapter = new BrowseGroupSectionAdapter(getActivity(), contactSections.get(headerIndex).toArray(new MeetupContact[contactSections.get(headerIndex).size()]));
			groupAdapter.addSection(headerIndex, browseGroupSectionAdapter);
		}
		
		setListAdapter(groupAdapter);
	}
	
	public void onResume()
	{
		super.onResume();
		bindContactsAdapter();
	}
	
	public void onPause()
	{
		super.onPause();
		clearAllImages();
	}
	
	public void clearAllImages()
	{
		if (getListView() != null)
		{
			for (int i=0; i<getListView().getChildCount(); i++)
			{
				if (getListView().getChildAt(i).getClass().equals(TableLayout.class))
				{
					TableLayout imageLayout = (TableLayout)getListView().getChildAt(i);
					ImageView imageView = (ImageView)imageLayout.findViewById(R.id.group_contact_image);
					
			        if (imageView != null)
			        { 
			            if ((imageView.getDrawable() != null) && (!((BitmapDrawable)imageView.getDrawable()).getBitmap().isRecycled()))
			            {
			            	((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle(); 
			            	imageView.setBackgroundResource(0); 
			            }
			        } 

	            	imageView = null;
			        imageLayout = null;
				}
			}
	
			if (getListAdapter() != null)
			{
				((BrowseGroupAdapter)getListAdapter()).stopAdapterLoaders();
				setListAdapter(null);
			}
			
			System.gc();
		}
	}
}
