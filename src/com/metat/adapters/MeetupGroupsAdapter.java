package com.metat.adapters;

import com.example.metat.R;
import com.metat.models.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeetupGroupsAdapter extends BaseAdapter {
	private Context _context;
	private Group[] _groups;

    public MeetupGroupsAdapter(Context c, Group[] groups)
    {
    	_context = c;
    	_groups = groups;
    }

	public int getCount() {
		return _groups.length;
	}

	public Object getItem(int position) {
		return _groups[position];
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
    		contactLayout = (LinearLayout) vi.inflate(R.layout.meetup_group_list_item, parent, false); 
    	} 
    	else
    	{
    		contactLayout = (LinearLayout)convertView;
    	}

    	TextView contactNameTextView = (TextView)contactLayout.findViewById(R.id.group_name);
    	contactNameTextView.setText(_groups[position].getName());    		

    	TextView groupContactCountTextView = (TextView)contactLayout.findViewById(R.id.group_contact_count);
    	
    	if (_groups[position].getMemberCount() != 1)
    		groupContactCountTextView.setText(_groups[position].getMemberCount() + " " + _context.getString(R.string.people));   
    	else
    		groupContactCountTextView.setText(_groups[position].getMemberCount() + " " + _context.getString(R.string.person));  
    	
    	return contactLayout;
    }
}