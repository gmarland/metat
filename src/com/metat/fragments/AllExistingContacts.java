package com.metat.fragments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.metat.adapters.ContactsAdapter;
import com.metat.adapters.ContactsSectionAdapter;
import com.metat.main.MainActivity;
import com.metat.models.Contact;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class AllExistingContacts extends ListFragment {
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		bindContactsAdapter();

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() { 

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				((MainActivity)getActivity()).showContactActionDialog(((Contact)parent.getAdapter().getItem(position)).getId());
				
				return false;
			} 
	    });
	}
	
	public void bindContactsAdapter()
	{
		ContactsAdapter contactsAdapter = new ContactsAdapter(getActivity());
		
		Map<String,List<Contact>> contactSections = new LinkedHashMap<String,List<Contact>>();
	
		for (Contact contact : MainActivity.AllContacts)
		{
			if (!contactSections.containsKey(contact.getName().substring(0, 1).toUpperCase()))
				contactSections.put(contact.getName().substring(0, 1).toUpperCase(), new ArrayList<Contact>());
			
			contactSections.get(contact.getName().substring(0, 1).toUpperCase()).add(contact);
		}
		
		for (String headerIndex : contactSections.keySet())
		{
			contactsAdapter.addSection(headerIndex, new ContactsSectionAdapter(getActivity(), contactSections.get(headerIndex).toArray(new Contact[contactSections.get(headerIndex).size()])));
		}
		
		setListAdapter(contactsAdapter);
	}
}
