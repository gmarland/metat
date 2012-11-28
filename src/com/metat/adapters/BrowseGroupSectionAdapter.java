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
		ImageView twitterImageView = (ImageView) contactLayout.findViewById(R.id.twitter_link);
		ImageView facebookImageView = (ImageView) contactLayout.findViewById(R.id.facebook_link);
		ImageView flickrImageView = (ImageView) contactLayout.findViewById(R.id.flickr_link);
		ImageView tumblrImageView = (ImageView) contactLayout.findViewById(R.id.tumblr_link);
		ImageView linkedInImageView = (ImageView) contactLayout.findViewById(R.id.linkedin_link);
		
    	imageView.setTag(_contacts[position].getMeetupId());
    	imageView.setImageBitmap(BitmapFactory.decodeResource(_context.getResources(), R.drawable.group_profile_default));
    	GroupImageDownloader.DisplayImage(_contacts[position].getMeetupId(), _contacts[position].getPhotoThumbnail(), imageView);

    	TextView contactNameTextView = (TextView)contactLayout.findViewById(R.id.group_contact_name);
    	contactNameTextView.setText(_contacts[position].getName());    

    	if ((_contacts[position].getTwitterId() != null) && (_contacts[position].getTwitterId().trim().length() > 0))
    	{
    		twitterImageView.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		twitterImageView.setVisibility(View.GONE);
    	}
    	
    	if ((_contacts[position].getFacebookId() != null) && (_contacts[position].getFacebookId().trim().length() > 0))
    	{
    		facebookImageView.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		facebookImageView.setVisibility(View.GONE);
    	}
    	
    	if ((_contacts[position].getFlickrId() != null) && (_contacts[position].getFlickrId().trim().length() > 0))
    	{
    		flickrImageView.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		flickrImageView.setVisibility(View.GONE);
    	}
    	
    	if ((_contacts[position].getTumblrId() != null) && (_contacts[position].getTumblrId().trim().length() > 0))
    	{
    		tumblrImageView.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		tumblrImageView.setVisibility(View.GONE);
    	}
    	
    	if ((_contacts[position].getLinkedInId() != null) && (_contacts[position].getLinkedInId().trim().length() > 0))
    	{
    		linkedInImageView.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		linkedInImageView.setVisibility(View.GONE);
    	}
    	
    	return contactLayout;
    }
}