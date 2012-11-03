package com.metat.main;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.models.Group;
import com.metat.models.MeetupContact;
import com.metat.helpers.NoDefaultSpinner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class AddContactActivity extends Activity {
	private Group[] _groups;
	private ArrayAdapter<Group> _meetupGroupsAdapter;
	
	private MeetupContact[] _contacts = new MeetupContact[0];
	
	private NoDefaultSpinner _meetupGroupSelect;
	private AutoCompleteTextView _name;
	private EditText _email;
	private EditText _phone;
	private EditText _notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        
    	setTitle(" " + getResources().getString(R.string.add));
    	
    	GroupsDataAccess groupsDataAccess = new GroupsDataAccess(this);
    	_groups = groupsDataAccess.getAllGroups();
    	
    	_meetupGroupsAdapter = new ArrayAdapter<Group>(this, R.layout.groups_spinner_style, _groups);
    	
    	_meetupGroupSelect = (NoDefaultSpinner) findViewById(R.id.meetup_group_select);
    	_meetupGroupSelect.setAdapter(_meetupGroupsAdapter);

    	_name = (AutoCompleteTextView) findViewById(R.id.name);
    	_email = (EditText) findViewById(R.id.email);
    	_phone = (EditText) findViewById(R.id.phone);
    	_notes = (EditText) findViewById(R.id.notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case android.R.id.home:
				Intent homeIntent = new Intent(getBaseContext(), MainActivity.class);
				homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(homeIntent);	
        		return true;
        	case R.id.cancel:
				Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);
				cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(cancelIntent);	
        		return true;
        	case R.id.save:
        		int selectedIndex = -1;
        		
        		for (int i=0; i<_contacts.length; i++)
        		{
        			if ((_contacts[i].getName().trim().toLowerCase()).equals(_name.getText().toString().trim().toLowerCase()))
        			{
        				selectedIndex = i;
        				break;
        			}
        		}
        		
        		String meetupId = "";
        		String firstname = "";
        		String lastname = "";
        		
        		if (selectedIndex != -1) {
        			meetupId = _contacts[selectedIndex].getMeetupId();
        		}
        			
        		firstname = _name.getText().toString().substring(0,_name.getText().toString().indexOf(" "));
        		lastname = _name.getText().toString().substring(_name.getText().toString().indexOf(" "));
        		
        		ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        		contactDataAccess.Insert(meetupId, new byte[0], firstname.trim(), lastname.trim(), _email.getText().toString(), _phone.getText().toString(), _notes.getText().toString(), ((Group)_meetupGroupSelect.getSelectedItem()).getMeetupId(), ((Group)_meetupGroupSelect.getSelectedItem()).getName());
        		
				Intent returnIntent = new Intent(getBaseContext(), MainActivity.class);
				returnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(returnIntent);	
				
        		return true;
        }
        
        return false;
    }
}