package com.metat.main;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.dialogs.GroupContactAction;
import com.metat.dialogs.GroupContactDeleteConfirm;
import com.metat.helpers.ConnectionHelper;
import com.metat.helpers.PreferencesHelper;
import com.metat.models.Contact;
import com.metat.models.Group;
import com.metat.fragments.AllExistingGroupContacts;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class GroupActivity extends Activity {
	private String _userToken;
	
	public static Group SelectedGroup = null;
	public static Contact[] AllContacts = new Contact[0];
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle extras = getIntent().getExtras();
        
        GroupsDataAccess groupdDataAccess = new GroupsDataAccess(this);
        SelectedGroup = groupdDataAccess.getGroup(extras.getString("groupId"));
        
    	setTitle(" " + SelectedGroup.getName());
        
    	if (ConnectionHelper.isNetworkAvailable(getBaseContext()))
    	{
	        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
			
	        if (settings.getString(PreferencesHelper.USER_TOKEN, null) != null)
	        {
	        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
	        }
    	}
    	
    	ContactDataAccess contactDataAccess = new ContactDataAccess(this);
    	AllContacts = contactDataAccess.getAllContacts(SelectedGroup.getMeetupId());

		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		Fragment allContactsFragment;

		if (savedInstanceState != null)
		{
			allContactsFragment = getFragmentManager().findFragmentByTag("AllExistingGroupContacts");
		}
		else
		{
			allContactsFragment = new AllExistingGroupContacts();
			Bundle allContactsSelectedArgs = new Bundle();
			allContactsFragment.setArguments(allContactsSelectedArgs);
			transaction.add(R.id.contacts_container, allContactsFragment, "AllExistingGroupContacts");
		}

		transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	if (_userToken.trim().length() > 0)
    		getMenuInflater().inflate(R.menu.group_menu, menu);
    	else
    		getMenuInflater().inflate(R.menu.group_menu_static, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();

    	if (_userToken.trim().length() > 0)
    		getMenuInflater().inflate(R.menu.group_menu, menu);
    	else
    		getMenuInflater().inflate(R.menu.group_menu_static, menu);

        return super.onPrepareOptionsMenu(menu);
    }
    
    public void resetMenuOptions()
    {
    	this.invalidateOptionsMenu();
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
        	case R.id.add_contact:
				Intent intent = new Intent(getBaseContext(), AddContactActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
    }
    
    public void showContactActionDialog(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("groupContactActionDialog");
        if (prev != null) {
        	fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        GroupContactAction contactAction = GroupContactAction.newInstance(contactId);
        if (contactAction.getDialog() != null) {
        	contactAction.getDialog().setCancelable(true);
        	contactAction.getDialog().setCanceledOnTouchOutside(true);
        }
        
        contactAction.show(fragmentTransaction, "groupContactActionDialog");
    }
    
    public void editContactSelected(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevContactActionDialog = getFragmentManager().findFragmentByTag("groupContactActionDialog");
        if (prevContactActionDialog != null) {
            ((GroupContactAction)prevContactActionDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevContactActionDialog);
        }
        
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

		Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("contactId", contactId);

		getBaseContext().startActivity(intent);	
    }
    
    public void deleteContactSelected(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevContactActionDialog = getFragmentManager().findFragmentByTag("groupContactActionDialog");
        if (prevContactActionDialog != null) {
            ((GroupContactAction)prevContactActionDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevContactActionDialog);
        }
        
        Fragment prevDeleteConfirmDialog = getFragmentManager().findFragmentByTag("groupDeleteConfirmDialog");
        if (prevDeleteConfirmDialog != null) {
        	fragmentTransaction.remove(prevDeleteConfirmDialog);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        
        fragmentTransaction = getFragmentManager().beginTransaction();
        
        GroupContactDeleteConfirm contactDeleteConfirm = GroupContactDeleteConfirm.newInstance(contactId);
        if (contactDeleteConfirm.getDialog() != null) {
        	contactDeleteConfirm.getDialog().setCancelable(true);
        	contactDeleteConfirm.getDialog().setCanceledOnTouchOutside(true);
        }
        contactDeleteConfirm.show(fragmentTransaction, "groupDeleteConfirmDialog");
    }
    
    public void deleteContactConfirmed(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevDeleteConfirmDialog = getFragmentManager().findFragmentByTag("groupDeleteConfirmDialog");
        if (prevDeleteConfirmDialog != null) {
            ((GroupContactDeleteConfirm)prevDeleteConfirmDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevDeleteConfirmDialog);
        }

        fragmentTransaction.commit();
        
        ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        contactDataAccess.Delete(contactId);

    	AllContacts = contactDataAccess.getAllContacts(SelectedGroup.getMeetupId());
        
        fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment allExistingContacts = getFragmentManager().findFragmentByTag("AllExistingGroupContacts");
        if (allExistingContacts != null)
        {
        	((AllExistingGroupContacts)allExistingContacts).bindContactsAdapter();
        }
    }
}
