package com.metat.main;

import com.metat.contacts.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.helpers.ImagesHelper;
import com.metat.models.Contact;
import com.metat.models.NavigationSource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewContactActivity extends Activity {
	private Contact _contact;
	private NavigationSource _navigationSource = NavigationSource.AllContacts;
	
	private ImageView _contactImage;
	private TextView _name;
	private TextView _meetupGroupName;

	private ImageView _twitterImageView;
	private ImageView _facebookImageView;
	private ImageView _flickrImageView;
	private ImageView _tumblrImageView;
	private ImageView _linkedInImageView;

	private LinearLayout _websiteContainer;
	private TextView _website;
	private LinearLayout _phoneContainer;
	private TextView _phone;
	private LinearLayout _emailContainer;
	private TextView _email;
	private LinearLayout _notesContainer;
	private TextView _notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);
        
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        
        Bundle extras = getIntent().getExtras();

        if (extras.containsKey("navigationSource"))
        	_navigationSource = (NavigationSource)extras.get("navigationSource");
        
        ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        _contact = contactDataAccess.getContact(extras.getLong("contactId"));

        _contactImage = (ImageView) findViewById(R.id.contact_image);
    	
    	if (_contact.getPhotoThumbnail().length > 0)
    	{
    		_contactImage.setVisibility(View.VISIBLE);
    		_contactImage.setImageBitmap(ImagesHelper.ImageFileToBitmap(_contact.getPhotoThumbnail()));
    	}
    	else
    	{
    		_contactImage.setVisibility(View.GONE);
    	}
    	
    	_meetupGroupName = (TextView) findViewById(R.id.meetup_group);
    	_meetupGroupName.setText(_contact.getGroupName());

    	_name = (TextView) findViewById(R.id.name);
    	_name.setText(_contact.getName());

    	_twitterImageView = (ImageView) findViewById(R.id.twitter_link);
    	if ((_contact.getTwitterId() != null) && (_contact.getTwitterId().trim().length() > 0))
    	{
    		_twitterImageView.setVisibility(View.VISIBLE);
    		_twitterImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + _contact.getTwitterId().replaceAll("@", "")));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getBaseContext().startActivity(browserIntent);
				}
    			
    		});
    	}
    	else
    	{
    		_twitterImageView.setVisibility(View.GONE);
    	}
    	
    	_facebookImageView = (ImageView) findViewById(R.id.facebook_link);
    	if ((_contact.getFacebookId() != null) && (_contact.getFacebookId().trim().length() > 0))
    	{
    		_facebookImageView.setVisibility(View.VISIBLE);
    		_facebookImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/profile.php?id=" + _contact.getFacebookId()));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getBaseContext().startActivity(browserIntent);
				}
    			
    		});
    	}
    	else
    	{
    		_facebookImageView.setVisibility(View.GONE);
    	}
    	
    	_flickrImageView = (ImageView) findViewById(R.id.flickr_link);
    	if ((_contact.getFlickrId() != null) && (_contact.getFlickrId().trim().length() > 0))
    	{
    		_flickrImageView.setVisibility(View.VISIBLE);
    		_flickrImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_contact.getFlickrId()));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getBaseContext().startActivity(browserIntent);
				}
    			
    		});
    	}
    	else
    	{
    		_flickrImageView.setVisibility(View.GONE);
    	}
    	
    	_tumblrImageView = (ImageView) findViewById(R.id.tumblr_link);
    	if ((_contact.getTumblrId() != null) && (_contact.getTumblrId().trim().length() > 0))
    	{
    		_tumblrImageView.setVisibility(View.VISIBLE);
    		_tumblrImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_contact.getTumblrId()));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getBaseContext().startActivity(browserIntent);
				}
    			
    		});
    	}
    	else
    	{
    		_tumblrImageView.setVisibility(View.GONE);
    	}
    	
    	_linkedInImageView = (ImageView) findViewById(R.id.linkedin_link);
    	if ((_contact.getLinkedInId() != null) && (_contact.getLinkedInId().trim().length() > 0))
    	{
    		_linkedInImageView.setVisibility(View.VISIBLE);
    		_linkedInImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_contact.getLinkedInId()));
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getBaseContext().startActivity(browserIntent);
				}
    			
    		});
    	}
    	else
    	{
    		_linkedInImageView.setVisibility(View.GONE);
    	}
    	
    	int contactDetailCount = 0;

    	if (_contact.getWebsite().trim().length() > 0)
    		contactDetailCount++;

    	if (_contact.getPhone().trim().length() > 0)
    		contactDetailCount++;

    	if (_contact.getEmail().trim().length() > 0)
    		contactDetailCount++;

    	if (_contact.getNotes().trim().length() > 0)
    		contactDetailCount++;
    	
    	int contentDetailDisplayed = 0;

    	_websiteContainer = (LinearLayout) findViewById(R.id.website_container);
    	_websiteContainer.setOnClickListener(_websiteButtonListener);
    	
    	_website = (TextView) findViewById(R.id.website);
    	_website.setText(_contact.getWebsite());
    	
    	if (_contact.getWebsite().trim().length() == 0)
    	{
    		_websiteContainer.setVisibility(View.GONE);
    	}
    	else
    	{
    		_websiteContainer.setVisibility(View.VISIBLE);
    		
    		if (contactDetailCount == 1)
    			_websiteContainer.setBackgroundResource(R.drawable.contact_detail_full);
    		else
    			_websiteContainer.setBackgroundResource(R.drawable.contact_detail_top);
    		
    		contentDetailDisplayed++;
    	}

    	_phoneContainer = (LinearLayout) findViewById(R.id.phone_container);
    	_phoneContainer.setOnClickListener(_phoneButtonListener);
    	
    	_phone = (TextView) findViewById(R.id.phone);
    	_phone.setText(_contact.getPhone());
    	
    	if (_contact.getPhone().trim().length() == 0)
    	{
    		_phoneContainer.setVisibility(View.GONE);
    	}
    	else
    	{
    		_phoneContainer.setVisibility(View.VISIBLE);

    		if (contactDetailCount == 1)
    			_phoneContainer.setBackgroundResource(R.drawable.contact_detail_full);
    		else if (contentDetailDisplayed == 0)
    			_phoneContainer.setBackgroundResource(R.drawable.contact_detail_top);
    		else if (contentDetailDisplayed == contactDetailCount)
    			_phoneContainer.setBackgroundResource(R.drawable.contact_detail_bottom);
    		else
    			_phoneContainer.setBackgroundResource(R.drawable.contact_detail_middle);
    		
    		contentDetailDisplayed++;
    	}

    	_emailContainer = (LinearLayout) findViewById(R.id.email_container);
    	_emailContainer.setOnClickListener(_emailButtonListener);
    	
    	_email = (TextView) findViewById(R.id.email);
    	_email.setText(_contact.getEmail());
    	
    	if (_contact.getEmail().trim().length() == 0)
    	{
    		_emailContainer.setVisibility(View.GONE);
    	}
    	else
    	{
    		_emailContainer.setVisibility(View.VISIBLE);

    		if (contactDetailCount == 1)
    			_emailContainer.setBackgroundResource(R.drawable.contact_detail_full);
    		else if (contentDetailDisplayed == 0)
    			_emailContainer.setBackgroundResource(R.drawable.contact_detail_top);
    		else if (contentDetailDisplayed == contactDetailCount)
    			_emailContainer.setBackgroundResource(R.drawable.contact_detail_bottom);
    		else
    			_emailContainer.setBackgroundResource(R.drawable.contact_detail_middle);

    		contentDetailDisplayed++;
    	}

    	_notesContainer = (LinearLayout) findViewById(R.id.notes_container);
    	_notes = (TextView) findViewById(R.id.notes);
    	_notes.setText(_contact.getNotes());
    	
    	if (_contact.getNotes().trim().length() == 0)
    	{
    		_notesContainer.setVisibility(View.GONE);
    	}
    	else
    	{
    		_notesContainer.setVisibility(View.VISIBLE);

    		if (contactDetailCount == 1)
    			_notesContainer.setBackgroundResource(R.drawable.contact_detail_full);
    		else
    			_notesContainer.setBackgroundResource(R.drawable.contact_detail_bottom);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_contact_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
		Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);

		switch (_navigationSource)
		{
		case AllContacts:
			cancelIntent = new Intent(getBaseContext(), MainActivity.class);
			cancelIntent.putExtra("selectedTab", MainActivity.TAB_CONTACTS);
			break;
		case GroupContacts:
			cancelIntent = new Intent(getBaseContext(), GroupActivity.class);
			cancelIntent.putExtra("groupId", _contact.getGroupId());
			break;
		}
		
		cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		getBaseContext().startActivity(cancelIntent);	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
    		case R.id.back:
    			Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);

    			switch (_navigationSource)
    			{
    			case AllContacts:
    				cancelIntent = new Intent(getBaseContext(), MainActivity.class);
    				cancelIntent.putExtra("selectedTab", MainActivity.TAB_CONTACTS);
    				break;
    			case GroupContacts:
    				cancelIntent = new Intent(getBaseContext(), GroupActivity.class);
    				cancelIntent.putExtra("groupId", _contact.getGroupId());
    				break;
    			}
    			
    			cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			
    			getBaseContext().startActivity(cancelIntent);	
        		return true;
        	case R.id.edit:
        		Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.putExtra("contactId", _contact.getId());
        		intent.putExtra("navigationSource", NavigationSource.ViewContact);

        		getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
    }

    private Button.OnClickListener _websiteButtonListener = new Button.OnClickListener() 
    {
		public void onClick(View v) {
			
		}
    };

    private Button.OnClickListener _emailButtonListener = new Button.OnClickListener() 
    {
		public void onClick(View v) {
    		String emailList[] = { _contact.getEmail() };  

    		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    		
    		emailIntent.setType("plain/text"); 
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);
			
    		startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_using)));
		}
    };

    private Button.OnClickListener _phoneButtonListener = new Button.OnClickListener() 
    {
		public void onClick(View v) {
			String uri = "tel:" + _contact.getPhone();
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse(uri));
			startActivity(intent);
		}
    };
}