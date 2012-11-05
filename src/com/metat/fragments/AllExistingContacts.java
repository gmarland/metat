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

public class AllExistingContacts extends ListFragment {
	private ContactsAdapter _contactsAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		bindContactsAdapter();
	}
	
	private void bindContactsAdapter()
	{
		_contactsAdapter = new ContactsAdapter(getActivity());
		
		Map<String,List<Contact>> contactSections = new LinkedHashMap<String,List<Contact>>();
	
		for (Contact contact : MainActivity.AllContacts)
		{
			if (!contactSections.containsKey(contact.getFirstname().substring(0, 1).toUpperCase()))
				contactSections.put(contact.getFirstname().substring(0, 1).toUpperCase(), new ArrayList<Contact>());
			
			contactSections.get(contact.getFirstname().substring(0, 1).toUpperCase()).add(contact);
		}
		
		for (String headerIndex : contactSections.keySet())
		{
			_contactsAdapter.addSection(headerIndex, new ContactsSectionAdapter(getActivity(), contactSections.get(headerIndex).toArray(new Contact[contactSections.get(headerIndex).size()])));
		}
		
		setListAdapter(_contactsAdapter);
	}
}
