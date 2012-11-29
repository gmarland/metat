package com.metat.fragments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.metat.adapters.GroupContactsAdapter;
import com.metat.adapters.GroupContactsSectionAdapter;
import com.metat.contacts.R;
import com.metat.main.GroupActivity;
import com.metat.main.ViewContactActivity;
import com.metat.models.Contact;
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
import android.widget.AdapterView.OnItemLongClickListener;

public class AllExistingGroupContacts extends ListFragment {
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		bindContactsAdapter();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent intent = new Intent(getActivity(), ViewContactActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.putExtra("contactId", ((Contact)parent.getAdapter().getItem(position)).getId());
        		intent.putExtra("groupId", ((Contact)parent.getAdapter().getItem(position)).getGroupId());
        		intent.putExtra("navigationSource", NavigationSource.GroupContacts);

        		getActivity().startActivity(intent);	
		}});
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() { 

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				((GroupActivity)getActivity()).showContactActionDialog(((Contact)parent.getAdapter().getItem(position)).getId());
				
				return false;
			} 
	    });

		getListView().setFastScrollEnabled(true);
	}
	
	public void bindContactsAdapter()
	{
		GroupContactsAdapter contactsAdapter = new GroupContactsAdapter(getActivity());
		
		Map<String,List<Contact>> contactSections = new LinkedHashMap<String,List<Contact>>();

		for (Contact contact : GroupActivity.AllContacts)
		{
			if (!contactSections.containsKey(contact.getName().substring(0, 1).toUpperCase()))
				contactSections.put(contact.getName().substring(0, 1).toUpperCase(), new ArrayList<Contact>());
			
			contactSections.get(contact.getName().substring(0, 1).toUpperCase()).add(contact);
		}
		
		for (String headerIndex : contactSections.keySet())
		{
			GroupContactsSectionAdapter contactsSectionAdapter = new GroupContactsSectionAdapter(getActivity(), contactSections.get(headerIndex).toArray(new Contact[contactSections.get(headerIndex).size()]));
			contactsAdapter.addSection(headerIndex, contactsSectionAdapter);
		}
		
		setListAdapter(contactsAdapter);
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
				((GroupContactsAdapter)getListAdapter()).stopAdapterLoaders();
				setListAdapter(null);
			}
			
			System.gc();
		}
	}
}
