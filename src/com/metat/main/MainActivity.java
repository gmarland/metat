package com.metat.main;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.dialogs.ContactAction;
import com.metat.dialogs.ContactDeleteConfirm;
import com.metat.helpers.ConnectionHelper;
import com.metat.helpers.PreferencesHelper;
import com.metat.models.Contact;
import com.metat.models.Group;
import com.metat.models.NavigationSource;
import com.metat.webservices.ClientWebservices;
import com.metat.webservices.GroupWebservices;
import com.metat.fragments.AllExistingContacts;
import com.metat.fragments.AllExistingMeetpGroups;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements OnTabChangeListener {
    public static final String TAB_CONTACTS = "contacts";
    public static final String TAB_MEETUPS = "meetups";
    
	private TabHost _contactSortingTabs;
	
	private String _userToken = "";
	private static boolean _attemptReathorization = true;
	
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	
	public static Contact[] AllContacts = new Contact[0];
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    	_consumer = new CommonsHttpOAuthConsumer(ClientWebservices.METAT_API_KEY, ClientWebservices.METAT_API_KEY_SECRET);
    	_provider = new CommonsHttpOAuthProvider(ClientWebservices.MEETUP_REQUEST_TOKEN_URL, ClientWebservices.MEETUP_ACCESS_TOKEN_URL, ClientWebservices.MEETUP_AUTHORIZE_URL);
    	
    	if (ConnectionHelper.isNetworkAvailable(getBaseContext()))
    	{
	        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
			
	        if (settings.getString(PreferencesHelper.USER_TOKEN, null) == null)
	        {
		        StartAuthenticateMeetupTask startAuthenticateMeetupTask = new StartAuthenticateMeetupTask(this);
		        startAuthenticateMeetupTask.execute();
	        }
	        else
	        {
	        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
	        	refreshMeetupGroups();
	        }
    	}
    	
    	ContactDataAccess contactDataAccess = new ContactDataAccess(this);
    	AllContacts = contactDataAccess.getAllContacts();
        
        _contactSortingTabs = (TabHost) findViewById(R.id.contact_sorting_tabs);
        _contactSortingTabs.setOnTabChangedListener(this); 
        _contactSortingTabs.setup();

        _contactSortingTabs.addTab(newTab(TAB_MEETUPS, R.string.meetups, R.id.meetup_groups_container));
        _contactSortingTabs.addTab(newTab(TAB_CONTACTS, R.string.contacts, R.id.contacts_container));
        
        Bundle extras = getIntent().getExtras();

        if ((extras != null) && (extras.containsKey("selectedTab")))
            _contactSortingTabs.setCurrentTabByTag(extras.getString("selectedTab"));
        else
        	_contactSortingTabs.setCurrentTabByTag(TAB_MEETUPS);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		Fragment allContactsFragment;

		if (savedInstanceState != null)
		{
			allContactsFragment = getFragmentManager().findFragmentByTag("AllExistingContacts");
		}
		else
		{
			allContactsFragment = new AllExistingContacts();
			Bundle allContactsSelectedArgs = new Bundle();
			allContactsFragment.setArguments(allContactsSelectedArgs);
			transaction.add(R.id.contacts_container, allContactsFragment, "AllExistingContacts");
		}

		Fragment allMeetupsFragment;

		if (savedInstanceState != null)
		{
			allMeetupsFragment = getFragmentManager().findFragmentByTag("AllExistingMeetups");
		}
		else
		{
			allMeetupsFragment = new AllExistingMeetpGroups();
			Bundle allContactsSelectedArgs = new Bundle();
			allContactsFragment.setArguments(allContactsSelectedArgs);
			transaction.add(R.id.meetup_groups_container, allMeetupsFragment, "AllExistingMeetups");
		}

		transaction.commit();
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		
		Uri uri = getIntent().getData();
		
		if ((uri != null) && (ClientWebservices.CALLBACK_URI.getScheme().equals(uri.getScheme())))
		{
			FinishAuthenticateMeetupTask finishAuthenticateMeetupTask = new FinishAuthenticateMeetupTask(this, uri);
			finishAuthenticateMeetupTask.execute();
		}
		else
		{
	    	if (ConnectionHelper.isNetworkAvailable(getBaseContext()))
	    	{
		        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
		        
		        if (settings.getString(PreferencesHelper.USER_TOKEN, null) == null)
		        {
			        StartAuthenticateMeetupTask startAuthenticateMeetupTask = new StartAuthenticateMeetupTask(this);
			        startAuthenticateMeetupTask.execute();
		        }
	    	}
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	if (_userToken.trim().length() > 0)
    		getMenuInflater().inflate(R.menu.main_menu, menu);
    	else
    		getMenuInflater().inflate(R.menu.main_menu_static, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();

    	if (_userToken.trim().length() > 0)
    		getMenuInflater().inflate(R.menu.main_menu, menu);
    	else
    		getMenuInflater().inflate(R.menu.main_menu_static, menu);

        return super.onPrepareOptionsMenu(menu);
    }
    
    public void resetMenuOptions()
    {
    	this.invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case R.id.add_contact:
				Intent intent = new Intent(getBaseContext(), AddContactActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				if (_contactSortingTabs.getCurrentTabTag().trim().toLowerCase().equals("meetups"))
				{
	        		intent.putExtra("navigationSource", NavigationSource.AllGroups);
				}
				else if(_contactSortingTabs.getCurrentTabTag().trim().toLowerCase().equals("contacts"))
				{
	        		intent.putExtra("navigationSource", NavigationSource.AllContacts);
				}
				
				getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
    }
    
    public void showContactActionDialog(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("contactActionDialog");
        if (prev != null) {
        	fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        ContactAction contactAction = ContactAction.newInstance(contactId);
        if (contactAction.getDialog() != null) {
        	contactAction.getDialog().setCancelable(true);
        	contactAction.getDialog().setCanceledOnTouchOutside(true);
        }
        
        contactAction.show(fragmentTransaction, "contactActionDialog");
    }
    
    public void editContactSelected(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevContactActionDialog = getFragmentManager().findFragmentByTag("contactActionDialog");
        if (prevContactActionDialog != null) {
            ((ContactAction)prevContactActionDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevContactActionDialog);
        }
        
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

		Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("contactId", contactId);
		intent.putExtra("navigationSource", NavigationSource.AllContacts);

		getBaseContext().startActivity(intent);	
    }
    
    public void deleteContactSelected(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevContactActionDialog = getFragmentManager().findFragmentByTag("contactActionDialog");
        if (prevContactActionDialog != null) {
            ((ContactAction)prevContactActionDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevContactActionDialog);
        }
        
        Fragment prevDeleteConfirmDialog = getFragmentManager().findFragmentByTag("deleteConfirmDialog");
        if (prevDeleteConfirmDialog != null) {
        	fragmentTransaction.remove(prevDeleteConfirmDialog);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        
        fragmentTransaction = getFragmentManager().beginTransaction();
        
        ContactDeleteConfirm contactDeleteConfirm = ContactDeleteConfirm.newInstance(contactId);
        if (contactDeleteConfirm.getDialog() != null) {
        	contactDeleteConfirm.getDialog().setCancelable(true);
        	contactDeleteConfirm.getDialog().setCanceledOnTouchOutside(true);
        }
        contactDeleteConfirm.show(fragmentTransaction, "deleteConfirmDialog");
    }
    
    public void deleteContactConfirmed(long contactId)
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    	
        Fragment prevDeleteConfirmDialog = getFragmentManager().findFragmentByTag("deleteConfirmDialog");
        if (prevDeleteConfirmDialog != null) {
            ((ContactDeleteConfirm)prevDeleteConfirmDialog).getDialog().dismiss();
        	fragmentTransaction.remove(prevDeleteConfirmDialog);
        }

        fragmentTransaction.commit();
        
        ContactDataAccess contactDataAccess = new ContactDataAccess(this);
        contactDataAccess.Delete(contactId);

    	AllContacts = contactDataAccess.getAllContacts();
        
        fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment allExistingContacts = getFragmentManager().findFragmentByTag("AllExistingContacts");
        if (allExistingContacts != null)
        {
        	((AllExistingContacts)allExistingContacts).bindContactsAdapter();
        }
    }

    // Private Methods
    private TabSpec newTab(String tag, int label, int tabContentId) 
    {        
    	View indicator = LayoutInflater.from(this).inflate(R.layout.contact_sorting_tab, null);
    	((TextView) indicator.findViewById(R.id.sorting_lbl)).setText(label);
    	TabSpec tabSpec = _contactSortingTabs.newTabSpec(tag);
    	tabSpec.setIndicator(indicator);
    	tabSpec.setContent(tabContentId);

    	return tabSpec; 
    }

	@Override
	public void onTabChanged(String arg0) {
	}
	
	public void refreshMeetupGroups()
	{
		UpdateMeetupGroupsTask updateMeetupGroupsTask = new UpdateMeetupGroupsTask(this, _userToken);
		updateMeetupGroupsTask.execute();
	}
	
	private class StartAuthenticateMeetupTask extends AsyncTask<String, String, String>
	{
		private Activity _parentActivity;
		
		public StartAuthenticateMeetupTask(Activity activity)
		{
			_parentActivity = activity;
		}
		
		@Override
		protected String doInBackground(String... strings) {
			Intent i = _parentActivity.getIntent();
			
			if (i.getData() == null) {
				Uri authenticationUri = ClientWebservices.startMeetupAuthorization(_parentActivity.getBaseContext(), _consumer, _provider);
				
				if (authenticationUri != null)
					return authenticationUri.toString();
				else
				{
					return "";
				}
			}
			else
				return "";
		}
		
		@Override
        protected void onPostExecute(String result) {
			if (result.trim().length() > 0)
			{
				_parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result)));
			}
		}
	}
	
	private class FinishAuthenticateMeetupTask extends AsyncTask<String, String, String>
	{
		private Activity _parentActivity;
		private Uri _meetupUri;
		
		public FinishAuthenticateMeetupTask(Activity activity, Uri uri)
		{
			_parentActivity = activity;
			_meetupUri = uri;
		}
		
		@Override
		protected String doInBackground(String... strings) {
			ClientWebservices.completeMeetupAuthorization(_parentActivity, _consumer, _provider, _meetupUri);
	        
			return "";
		}
		
		@Override
        protected void onPostExecute(String result) {
			SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
        	getIntent().setData(null);
        	
	        if (settings.getString(PreferencesHelper.USER_TOKEN, null) != null)
	        {
	        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
	        	((MainActivity)_parentActivity).resetMenuOptions();
	        	((MainActivity)_parentActivity).refreshMeetupGroups();
	        }
		}
	}
	
	private class UpdateMeetupGroupsTask extends AsyncTask<String, String, String>
	{
		private Activity _parentActivity;
		private String _meetupKey;
		
		private long _selfId;
		
		public UpdateMeetupGroupsTask(Activity activity, String meetupKey)
		{
			_parentActivity = activity;
			_meetupKey = meetupKey;
		}
		
		@Override
		protected String doInBackground(String... strings) {
			_selfId = ClientWebservices.getCurrentUser(_parentActivity,_meetupKey);

			if (_selfId >= 0)
			{
				GroupsDataAccess groupsDataAccess = new GroupsDataAccess(_parentActivity);
				ContactDataAccess contactDataAccess = new ContactDataAccess(_parentActivity);
				
				Group[] onlineMeetupGroups = GroupWebservices.getAllGroups(_meetupKey, _selfId + "");
				
				if (onlineMeetupGroups.length > 0)
				{
					Group[] existingMeetupGroups = groupsDataAccess.getAllGroups();
					
					for (Group onlineMeetupGroup : onlineMeetupGroups)
					{
						boolean onlineGroupFound = false;
	
						for (Group existingMeetupGroup : existingMeetupGroups)
						{
							if (onlineMeetupGroup.getMeetupId().equals(existingMeetupGroup.getMeetupId()))
							{
								onlineGroupFound = true;
								
								if (!onlineMeetupGroup.getName().trim().equals(existingMeetupGroup.getName().trim()))
								{
									groupsDataAccess.Update(onlineMeetupGroup.getMeetupId(), onlineMeetupGroup.getName().trim());
									contactDataAccess.UpdateGroupNames(onlineMeetupGroup.getMeetupId(), onlineMeetupGroup.getName().trim());
								}
							}
						}
						
						if (!onlineGroupFound)
						{
							groupsDataAccess.Insert(onlineMeetupGroup);
						}
					}
	
					existingMeetupGroups = groupsDataAccess.getAllGroups();
					
					for (Group existingMeetupGroup : existingMeetupGroups)
					{
						boolean existingGroupFound = false;
	
						for (Group onlineMeetupGroup : onlineMeetupGroups)
						{
							if (existingMeetupGroup.getMeetupId().equals(onlineMeetupGroup.getMeetupId()))
								existingGroupFound = true;
						}
	
						if (!existingGroupFound)
						{
							groupsDataAccess.Delete(existingMeetupGroup.getMeetupId());
						}
					}
				}
			}
			
			
			return "Complete";
		}
		
		@Override
        protected void onPostExecute(String result) {
			if ((_selfId == -2) && (_attemptReathorization))
			{
				_attemptReathorization = false;
				StartAuthenticateMeetupTask startAuthenticateMeetupTask = new StartAuthenticateMeetupTask(_parentActivity);
				startAuthenticateMeetupTask.execute();
			}
		}
	}
}
