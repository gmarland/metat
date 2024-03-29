package com.metat.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import com.metat.contacts.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.dataaccess.MeetupContactDataAccess;
import com.metat.models.Contact;
import com.metat.models.Group;
import com.metat.models.MeetupContact;
import com.metat.models.NavigationSource;
import com.metat.helpers.NoDefaultSpinner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity implements TextWatcher {
	private NavigationSource _navigationSource = NavigationSource.AllContacts;

	private String _sourceGroupId = "";
	private long _sourceContactId = -1;
	
	private Group[] _groups;
	private ArrayAdapter<Group> _meetupGroupsAdapter;
	private ArrayAdapter<MeetupContact> _meetupGroupContactsAdapter;
	
	private static ArrayList<MeetupContact> _contacts = new ArrayList<MeetupContact>();	

	private IntentFilter _intentFilter;
	
	private NoDefaultSpinner _meetupGroupSelect;
	private AutoCompleteTextView _name;
	private EditText _website;
	private EditText _email;
	private EditText _phone;
	private EditText _notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey("navigationSource"))
        	_navigationSource = (NavigationSource)extras.get("navigationSource");

        if (extras.containsKey("groupId"))
        	_sourceGroupId = extras.getString("groupId");

        if (extras.containsKey("contactId"))
        	_sourceContactId = extras.getLong("contactId");

        _contacts = new ArrayList<MeetupContact>();

    	GroupsDataAccess groupsDataAccess = new GroupsDataAccess(this);
    	_groups = groupsDataAccess.getAllGroups();
    	
    	_name = (AutoCompleteTextView) findViewById(R.id.name);
    	_name.addTextChangedListener(this);
    	_name.setThreshold(2);
		_meetupGroupContactsAdapter = new ArrayAdapter<MeetupContact>(this, R.layout.contacts_spinner_style, _contacts);
		_name.setAdapter(_meetupGroupContactsAdapter);
    	
		_website = (EditText) findViewById(R.id.website);
    	_email = (EditText) findViewById(R.id.email);
    	_phone = (EditText) findViewById(R.id.phone);
    	_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    	_notes = (EditText) findViewById(R.id.notes);

        if (_groups.length > 0)
        {
	    	int groupSelectinedIndex = 0;
	    	
	    	for (int i=0; i<_groups.length; i++)
	    	{
	    		if (_groups[i].getMeetupId().equals(_sourceGroupId))
	    		{                                                                                                                                                                                                                                                 
	    			groupSelectinedIndex=i;
	    		}
	    	}
	    	
	    	_meetupGroupsAdapter = new ArrayAdapter<Group>(this, R.layout.groups_spinner_style, _groups);
	    	
	    	_meetupGroupSelect = (NoDefaultSpinner) findViewById(R.id.meetup_group_select);
	    	_meetupGroupSelect.setAdapter(_meetupGroupsAdapter);
	    	_meetupGroupSelect.setSelection(groupSelectinedIndex);
	    	_meetupGroupSelect.setVisibility(View.VISIBLE);
	    	_meetupGroupSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					_sourceGroupId = _groups[position].getMeetupId();
					
		        	MeetupContactDataAccess meetupContactDataAccess = new MeetupContactDataAccess(getBaseContext());
					_contacts = meetupContactDataAccess.getAllMeetupContacts(_sourceGroupId);
						
					_meetupGroupContactsAdapter.clear();
					_meetupGroupContactsAdapter.addAll(_contacts);
					_meetupGroupContactsAdapter.notifyDataSetChanged();
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
	    	
        	MeetupContactDataAccess meetupContactDataAccess = new MeetupContactDataAccess(this);
			_contacts = meetupContactDataAccess.getAllMeetupContacts(_groups[groupSelectinedIndex].getMeetupId());
				
			_meetupGroupContactsAdapter.clear();
			_meetupGroupContactsAdapter.addAll(_contacts);
			_meetupGroupContactsAdapter.notifyDataSetChanged();

			if (_sourceContactId != -1)
			{
				MeetupContact meetupContact = meetupContactDataAccess.getMeetupContact(_sourceContactId);
				
				if (meetupContact != null)
				{
					_name.setText(meetupContact.getName());
			    	_notes.setText(meetupContact.getBio());
			    	_website.requestFocus();
				}
			}
        }
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();

        _intentFilter = new IntentFilter();
        _intentFilter.addAction("REFRESH_MEETUP_CONTACTS");
        
        registerReceiver(_intentReceiver, _intentFilter);
	}

    @Override 
    protected void onPause() 
    { 
        unregisterReceiver(_intentReceiver); 
        super.onPause(); 
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
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
		case AllGroups:
			cancelIntent = new Intent(getBaseContext(), MainActivity.class);
			cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
			break;
		case GroupContacts:
			if (_sourceGroupId.trim().length() > 0)
			{
				cancelIntent = new Intent(getBaseContext(), GroupActivity.class);
				cancelIntent.putExtra("groupId", _sourceGroupId);
			}
			else
			{
				cancelIntent = new Intent(getBaseContext(), MainActivity.class);
				cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
			}
			break;
		case ViewMeetupContact:
			cancelIntent = new Intent(getBaseContext(), ViewMeetupContactActivity.class);
			cancelIntent.putExtra("contactId", _sourceContactId);
			cancelIntent.putExtra("navigationSource", NavigationSource.GroupContacts);
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
        	case R.id.cancel:
				Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);

				switch (_navigationSource)
				{
				case AllContacts:
					cancelIntent = new Intent(getBaseContext(), MainActivity.class);
					cancelIntent.putExtra("selectedTab", MainActivity.TAB_CONTACTS);
					break;
				case AllGroups:
					cancelIntent = new Intent(getBaseContext(), MainActivity.class);
					cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
					break;
				case GroupContacts:
					if (_sourceGroupId.trim().length() > 0)
					{
						cancelIntent = new Intent(getBaseContext(), GroupActivity.class);
						cancelIntent.putExtra("groupId", _sourceGroupId);
					}
					else
					{
						cancelIntent = new Intent(getBaseContext(), MainActivity.class);
						cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
					}
					break;
				case ViewMeetupContact:
					cancelIntent = new Intent(getBaseContext(), ViewMeetupContactActivity.class);
					cancelIntent.putExtra("contactId", _sourceContactId);
					cancelIntent.putExtra("navigationSource", NavigationSource.GroupContacts);
					break;
				}
				
				cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(cancelIntent);	
        		return true;
        	case R.id.save:
        		if (_name.getText().toString().length() > 0)
        		{
	        		String meetupId = "";
	        		String name = _name.getText().toString();
	        		String contactPhotoThumbnailLocation = "";
	        		String website = _website.getText().toString(); 
	        		String link = "";
	        		String twitterId = "";
	        		String linkedInId = "";
	        		String facebookId = "";
	        		String tumblrId = "";
	        		String flickrId = "";
	        		
	        		for (int i=0; i<_contacts.size(); i++)
	        		{
	        			if ((_contacts.get(i).getName().trim().toLowerCase()).equals(_name.getText().toString().trim().toLowerCase()))
	        			{
		        			meetupId = _contacts.get(i).getMeetupId();
		        			name = _contacts.get(i).getName();
		        			contactPhotoThumbnailLocation = _contacts.get(i).getPhotoThumbnail();
		        			link = _contacts.get(i).getLink();
			        		twitterId = _contacts.get(i).getTwitterId();
			        		linkedInId = _contacts.get(i).getLinkedInId();
			        		facebookId = _contacts.get(i).getFacebookId();
			        		tumblrId = _contacts.get(i).getTumblrId();
			        		flickrId = _contacts.get(i).getFlickrId();
	        				break;
	        			}
	        		}
	        		
	        		ContactDataAccess contactDataAccess = new ContactDataAccess(this);
	        		contactDataAccess.Insert(meetupId, new byte[0], name, website, _email.getText().toString(), _phone.getText().toString(), _notes.getText().toString(), link, twitterId, linkedInId, facebookId, tumblrId, flickrId, ((Group)_meetupGroupSelect.getSelectedItem()).getMeetupId(), ((Group)_meetupGroupSelect.getSelectedItem()).getName());

	        		if (contactPhotoThumbnailLocation.trim().length() > 0)
	        		{
		        		DownloadImageTask downloadImageTask = new DownloadImageTask(this, meetupId, contactPhotoThumbnailLocation);
		        		downloadImageTask.execute();
	        		}
	        		
	        		Contact contact = contactDataAccess.getContact(meetupId);

	        		Intent intent = new Intent(getBaseContext(), ViewContactActivity.class);
	        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		intent.putExtra("contactId", contact.getId());

	        		getBaseContext().startActivity(intent);	
					
	        		return true;
        		}
        		else
        		{
        			Toast.makeText(this, R.string.you_need_to_provide_a_name, Toast.LENGTH_SHORT).show();
        		}
        }
        
        return false;
    }

	private class DownloadImageTask extends AsyncTask<String, String, String>
	{
		private Context _context;
		private String _meetupId;
		private String _imageLocation;
		
		private byte[] _downloadedImage = new byte[0];
		
		public DownloadImageTask(Context context, String meetupId, String imageLocation)
		{
			_context = context;
			_meetupId = meetupId;
			_imageLocation = imageLocation;
		}
		
		@Override
		protected String doInBackground(String... strings) {
			URL imageUrl;
			URLConnection imageConnection;
			InputStream inputStream;
			
			String[] urlParts = _imageLocation.split("/");
			
			if (urlParts.length == 0)
			{
				return "Aborted";
			}
			
			if (!urlParts[urlParts.length-1].contains("."))
			{
				return "Aborted";
			}
			
			try
			{
				imageUrl = new URL(_imageLocation);
			}
			catch (Exception ex)
			{
				return "Aborted";
			}
			
			try
			{
				imageConnection = imageUrl.openConnection();
				inputStream = imageConnection.getInputStream();
			}
			catch (IOException ex)
			{
				return "Aborted";
			}
			
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
		
			int current = 0;

			try
			{
                while ((current = bis.read()) != -1) 
                {
                    baf.append((byte) current);
                }
			}
			catch (IOException ex)
			{
				return "Aborted";
			}
			
			if (baf.isEmpty())
			{
				return "Aborted";
			}
			
			_downloadedImage = baf.toByteArray();
			
			try
			{
				bis.close();
			}
			catch (Exception ex)
			{
				bis = null;
			}
			
			try
			{
				inputStream.close();
			}
			catch (Exception ex)
			{
				inputStream = null;
			}
			
			baf.clear();
			imageConnection = null;
			baf = null;

			return "Complete";
		}
		
		@Override
        protected void onPostExecute(String result) {
			ContactDataAccess contactDataAccess = new ContactDataAccess(_context);
			contactDataAccess.updateImage(_meetupId, _downloadedImage);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	private BroadcastReceiver _intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			
			if (extras.containsKey("meetupGroupId")) {
				if(extras.getString("meetupGroupId").trim().equals(_sourceGroupId.trim())) {
					MeetupContactDataAccess meetupContactDataAccess = new MeetupContactDataAccess(getBaseContext());
					_contacts = meetupContactDataAccess.getAllMeetupContacts(_sourceGroupId);
						
					_meetupGroupContactsAdapter.clear();
					_meetupGroupContactsAdapter.addAll(_contacts);
					_meetupGroupContactsAdapter.notifyDataSetChanged();
				}
			}
		}
	};
}