package com.metat.adapters;

import com.metat.contacts.R;
import com.metat.models.Contact;
import com.metat.helpers.ImageLoader;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class GroupContactsSectionAdapter extends BaseAdapter {
	public ImageLoader GroupImageLoader;
	
	private Context _context;
	private Contact[] _contacts;

    public GroupContactsSectionAdapter(Context context, Contact[] contacts)
    {
    	_context = context;
    	_contacts = contacts;

    	GroupImageLoader = new ImageLoader(context);
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

    public View getView(final int position, View convertView, ViewGroup parent)
    {
    	TableLayout contactLayout;

    	if (convertView == null) 
    	{ 
    		LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    		contactLayout = (TableLayout) vi.inflate(R.layout.group_contact_list_item, parent, false); 
    	} 
    	else
    	{
    		contactLayout = (TableLayout)convertView;
    	}

		ImageView imageView = (ImageView)contactLayout.findViewById(R.id.group_contact_image);
		
    	imageView.setTag(_contacts[position].getMeetupId());
    	imageView.setImageBitmap(BitmapFactory.decodeResource(_context.getResources(), R.drawable.group_profile_default));
    	GroupImageLoader.DisplayImage(_contacts[position].getId(), _contacts[position].getMeetupId(), imageView);

    	TextView contactNameTextView = (TextView)contactLayout.findViewById(R.id.group_contact_name);
    	contactNameTextView.setText(_contacts[position].getName());    
    	
    	return contactLayout;
    }
}