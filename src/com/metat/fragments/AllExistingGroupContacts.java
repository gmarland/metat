package com.metat.fragments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.metat.adapters.ContactsAdapter;
import com.metat.adapters.ContactsSectionAdapter;
import com.metat.main.GroupActivity;
import com.metat.main.ViewContactActivity;
import com.metat.models.Contact;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        		getActivity().startActivity(intent);	
		}});
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() { 

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				((GroupActivity)getActivity()).showContactActionDialog(((Contact)parent.getAdapter().getItem(position)).getId());
				
				return false;
			} 
	    });
	}
	
	public void bindContactsAdapter()
	{
		ContactsAdapter contactsAdapter = new ContactsAdapter(getActivity());
		
		Map<String,List<Contact>> contactSections = new LinkedHashMap<String,List<Contact>>();

		for (Contact contact : GroupActivity.AllContacts)
		{
			if (!contactSections.containsKey(contact.getName().substring(0, 1).toUpperCase()))
				contactSections.put(contact.getName().substring(0, 1).toUpperCase(), new ArrayList<Contact>());
			
			contactSections.get(contact.getName().substring(0, 1).toUpperCase()).add(contact);
		}
		
		for (String headerIndex : contactSections.keySet())
		{
			ContactsSectionAdapter contactsSectionAdapter = new ContactsSectionAdapter(getActivity(), contactSections.get(headerIndex).toArray(new Contact[contactSections.get(headerIndex).size()]));
			contactsAdapter.addSection(headerIndex, contactsSectionAdapter);
		}
		
		setListAdapter(contactsAdapter);
	}
}
