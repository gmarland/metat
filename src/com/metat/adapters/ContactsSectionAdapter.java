package com.metat.adapters;

import com.example.metat.R;
import com.metat.models.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsSectionAdapter extends BaseAdapter {
	private Context _context;
	private Contact[] _contacts;

    public ContactsSectionAdapter(Context c, Contact[] contacts)
    {
    	_context = c;
    	_contacts = contacts;
    }

	public int getCount() {
		return _contacts.length;
	}

	public Object getItem(int position) {
		return _contacts[position];
	}

	public long getItemId(int itemId) {
		return itemId;
	}

    public View getView(int position, View convertView, ViewGroup parent)
    {
    	LinearLayout contactLayout;

    	if (convertView == null) 
    	{ 
    		LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    		contactLayout = (LinearLayout) vi.inflate(R.layout.contact_list_item, parent, false); 
    	} 
    	else
    	{
    		contactLayout = (LinearLayout)convertView;
    	}

    	TextView contactNameTextView = (TextView)contactLayout.findViewById(R.id.contact_name);
    	contactNameTextView.setText(_contacts[position].getName());    

    	TextView contactMetAtTextView = (TextView)contactLayout.findViewById(R.id.contact_meetup_group);
    	contactMetAtTextView.setText(_context.getString(R.string.met_at) + " " + _contacts[position].getGroupName());
    	
    	return contactLayout;
    }
}