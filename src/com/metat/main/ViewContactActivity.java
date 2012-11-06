package com.metat.main;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.models.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewContactActivity extends Activity {
	private Contact _contact;
	
	private TextView _meetupGroupName;
	private TextView _name;
	private TextView _email;
	private TextView _phone;
	private TextView _notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);
        
        Bundle extras = this.getIntent().getExtras();
        
        ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        _contact = contactDataAccess.getContact(extras.getLong("contactId"));
        
    	setTitle("< " + getResources().getString(R.string.contact));
    	
    	_meetupGroupName = (TextView) findViewById(R.id.meetup_group);
    	_meetupGroupName.setText(_contact.getGroupName());
    	
    	_name = (TextView) findViewById(R.id.name);
    	_name.setText(_contact.getName());
    	_email = (TextView) findViewById(R.id.email);
    	_email.setText(_contact.getEmail());
    	_phone = (TextView) findViewById(R.id.phone);
    	_phone.setText(_contact.getPhone());
    	_notes = (TextView) findViewById(R.id.notes);
    	_notes.setText(_contact.getNotes());
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