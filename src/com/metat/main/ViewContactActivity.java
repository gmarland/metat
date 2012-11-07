package com.metat.main;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.helpers.ImagesHelper;
import com.metat.models.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewContactActivity extends Activity {
	private Contact _contact;
	
	private ImageView _image;
	private TextView _meetupGroupName;
	private TextView _name;
	private TextView _email;
	private LinearLayout _emailContainer;
	private TextView _phone;
	private LinearLayout _phoneContainer;
	private TextView _notes;
	private LinearLayout _notesContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);
        
        Bundle extras = this.getIntent().getExtras();
        
        ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        _contact = contactDataAccess.getContact(extras.getLong("contactId"));
        
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
    	
    	_image = (ImageView) findViewById(R.id.image);
    	
    	if (_contact.getPhotoThumbnail().length > 0)
    	{
    		_image.setImageBitmap(ImagesHelper.ResampleImageFileToBitmap(_contact.getPhotoThumbnail(), ImagesHelper.THUMBNAIL_SIZE));
    	}
    	else
    	{
    		_image.setImageResource(R.drawable.profile_default);
    	}
    	
    	_meetupGroupName = (TextView) findViewById(R.id.meetup_group);
    	_meetupGroupName.setText(_contact.getGroupName());

    	_name = (TextView) findViewById(R.id.name);
    	_name.setText(_contact.getName());
    	
    	_email = (TextView) findViewById(R.id.email);
    	_email.setText(_contact.getEmail());
    	_emailContainer = (LinearLayout) findViewById(R.id.email_container);
    	if (_contact.getEmail().trim().length() == 0)
    		_emailContainer.setVisibility(View.GONE);
    	else
    		_emailContainer.setVisibility(View.VISIBLE);
    	
    	_phone = (TextView) findViewById(R.id.phone);
    	_phone.setText(_contact.getPhone());
    	_phoneContainer = (LinearLayout) findViewById(R.id.phone_container);
    	if (_contact.getPhone().trim().length() == 0)
    		_phoneContainer.setVisibility(View.GONE);
    	else
    		_phoneContainer.setVisibility(View.VISIBLE);
    	
    	_notes = (TextView) findViewById(R.id.notes);
    	_notes.setText(_contact.getNotes());
    	_notesContainer = (LinearLayout) findViewById(R.id.notes_container);
    	if (_contact.getNotes().trim().length() == 0)
    		_notesContainer.setVisibility(View.GONE);
    	else
    		_notesContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_contact_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
		Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);
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
    			cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			
    			getBaseContext().startActivity(cancelIntent);	
        		return true;
        	case R.id.edit:
        		Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		intent.putExtra("contactId", _contact.getId());

        		getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
    }
}