package com.metat.main;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import com.metat.helpers.PreferencesHelper;
import com.metat.contacts.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.dialogs.ContactAction;
import com.metat.dialogs.ContactDeleteConfirm;
import com.metat.dialogs.WelcomeMessage;
import com.metat.helpers.ConnectionHelper;
import com.metat.models.Contact;
import com.metat.models.NavigationSource;
import com.metat.tasks.FinishAuthenticateMeetup;
import com.metat.tasks.StartAuthenticateMeetup;
import com.metat.tasks.UpdateMeetupGroups;
import com.metat.webservices.ClientWebservices;
import com.metat.fragments.AllExistingContacts;
import com.metat.fragments.AllExistingMeetpGroups;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements OnTabChangeListener {
    public static final String TAB_CONTACTS = "contacts";
    public static final String TAB_MEETUPS = "meetups";
    
	private TabHost _contactSortingTabs;
	private LinearLayout _meetupLoginLayout;
	
	private String _userToken = "";
	public static boolean AttemptReathorization = true;
	
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	private IntentFilter _intentFilter;
	
	public static Contact[] AllContacts = new Contact[0];
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

    	_consumer = new CommonsHttpOAuthConsumer(ClientWebservices.METAT_API_KEY, ClientWebservices.METAT_API_KEY_SECRET);
    	_provider = new CommonsHttpOAuthProvider(ClientWebservices.MEETUP_REQUEST_TOKEN_URL, ClientWebservices.MEETUP_ACCESS_TOKEN_URL, ClientWebservices.MEETUP_AUTHORIZE_URL);

    	_meetupLoginLayout = (LinearLayout)findViewById(R.id.MeetupLoginContainer);
    	_meetupLoginLayout.setOnClickListener(_loginButtonListener);
    	
        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
		
        if (settings.getString(PreferencesHelper.USER_TOKEN, null) != null)
        {
	    	if (ConnectionHelper.isNetworkAvailable(getBaseContext()))
	    	{
	        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
	        }
    	}
        else
        {
            if (settings.getBoolean(PreferencesHelper.SHOW_WELCOME, true))
            {
            	showWelcomeDialog();
            }
        }
        

    	ContactDataAccess contactDataAccess = new ContactDataAccess(this);
    	AllContacts = contactDataAccess.getAllContacts();
        
        _contactSortingTabs = (TabHost)findViewById(R.id.contact_sorting_tabs);
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

        _intentFilter = new IntentFilter();
        _intentFilter.addAction("REFRESH_MEETUP_GROUPS");
        
        registerReceiver(_intentReceiver, _intentFilter);
        
		Uri uri = getIntent().getData();
		
		if ((uri != null) && (ClientWebservices.CALLBACK_URI.getScheme().equals(uri.getScheme())))
		{
			FinishAuthenticateMeetup finishAuthenticateMeetupTask = new FinishAuthenticateMeetup(this, _consumer, _provider, uri);
			finishAuthenticateMeetupTask.execute();
		}
	}

    @Override 
    protected void onPause() 
    { 
        unregisterReceiver(_intentReceiver); 
        super.onPause(); 
    } 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	if (_userToken.trim().length() > 0)
    	{
    		getMenuInflater().inflate(R.menu.main_menu, menu);
	    	_meetupLoginLayout.setVisibility(View.GONE);
    	}
    	else
    	{
    		getMenuInflater().inflate(R.menu.main_menu_static, menu);
	    	_meetupLoginLayout.setVisibility(View.VISIBLE);
    	}
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();

    	if (_userToken.trim().length() > 0)
    	{
    		getMenuInflater().inflate(R.menu.main_menu, menu);
	    	_meetupLoginLayout.setVisibility(View.GONE);
    	}
    	else
    	{
    		getMenuInflater().inflate(R.menu.main_menu_static, menu);
	    	_meetupLoginLayout.setVisibility(View.VISIBLE);
    	}

        return super.onPrepareOptionsMenu(menu);
    }
    
    public void resetMenuOptions()
    {
    	this.invalidateOptionsMenu();
    }
    
    public void setUserToken(String userToken)
    {
    	_userToken = userToken;
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
        	case R.id.send_feedback:
	    		String emailList[] = { PreferencesHelper.FEEDBACK_EMAIL };  

	    		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	    		
	    		emailIntent.setType("plain/text"); 
    			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);
    			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.met_at_feedback));
    			
	    		startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_feedback_using)));
				return true;
        	case R.id.logout_meetup:
    	        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
    			SharedPreferences.Editor editor = settings.edit();
    			
    			editor.remove(PreferencesHelper.USER_TOKEN);
    			editor.remove(PreferencesHelper.USER_SECRET);
    			
    			editor.commit();
    			
    			_userToken = "";
    			
    			resetMenuOptions();
				return true;
        }
        
        return false;
    }
    
    public void showWelcomeDialog()
    {
    	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("welcomeMessageDialog");
        if (prev != null) {
        	fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        WelcomeMessage welcomeMessage = WelcomeMessage.newInstance();
        
        if (welcomeMessage.getDialog() != null) {
        	welcomeMessage.getDialog().setCancelable(false);
        	welcomeMessage.getDialog().setCanceledOnTouchOutside(false);
        }
        
        welcomeMessage.show(fragmentTransaction, "welcomeMessageDialog");
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

	@Override
	public void onTabChanged(String arg0) {
		updateTabStyles();
	}
	
	public void refreshMeetupGroups()
	{
		UpdateMeetupGroups updateMeetupGroupsTask = new UpdateMeetupGroups(this, _userToken, _consumer, _provider);
		updateMeetupGroupsTask.execute();
	}
	
	public void reloadMeetupGroups()
	{
		Fragment allMeetupsFragment = getFragmentManager().findFragmentByTag("AllExistingMeetups");

		if (allMeetupsFragment != null)
		{
			((AllExistingMeetpGroups)allMeetupsFragment).bindMeetupGroupsAdapter();
		}
	}
	
    public void logIntoMeetup()
    {
        StartAuthenticateMeetup startAuthenticateMeetupTask = new StartAuthenticateMeetup(this, _consumer, _provider);
        startAuthenticateMeetupTask.execute();
    }

    /// Private Methods
    
    private TabSpec newTab(String tag, int label, int tabContentId) 
    {        
    	View indicator = LayoutInflater.from(this).inflate(R.layout.contact_sorting_tab, null);
    	((TextView) indicator.findViewById(R.id.sorting_lbl)).setText(label);
    	TabSpec tabSpec = _contactSortingTabs.newTabSpec(tag);
    	tabSpec.setIndicator(indicator);
    	tabSpec.setContent(tabContentId);

    	return tabSpec; 
    }
	
	private void updateTabStyles()
	{
		for (int i=0; i<_contactSortingTabs.getTabWidget().getChildCount(); i++)
		{
			_contactSortingTabs.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.contacts_tab_inactive));
		}

		_contactSortingTabs.getTabWidget().getChildAt(_contactSortingTabs.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.contacts_tab_selected));
	}
    
    private Button.OnClickListener _loginButtonListener = new Button.OnClickListener() 
    {
		public void onClick(View v) {
			logIntoMeetup();
		}
    };

	private BroadcastReceiver _intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			reloadMeetupGroups();
		}
	};
}
