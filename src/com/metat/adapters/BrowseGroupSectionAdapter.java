package com.metat.adapters;

import com.metat.contacts.R;
import com.metat.helpers.ImageDownloader;
import com.metat.models.MeetupContact;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class BrowseGroupSectionAdapter extends BaseAdapter {
	public ImageDownloader GroupImageDownloader;
	
	private Context _context;
	private MeetupContact[] _contacts;

    public BrowseGroupSectionAdapter(Context context, MeetupContact[] contacts)
    {
    	_context = context;
    	_contacts = contacts;

    	GroupImageDownloader = new ImageDownloader(context);
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
    	GroupImageDownloader.DisplayImage(_contacts[position].getMeetupId(), _contacts[position].getPhotoThumbnail(), imageView);

    	TextView contactNameTextView = (TextView)contactLayout.findViewById(R.id.group_contact_name);
    	contactNameTextView.setText(_contacts[position].getName());    
    	
    	return contactLayout;
    }
}