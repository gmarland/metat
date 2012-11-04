package com.metat.main;

import com.example.metat.R;
import com.metat.webservices.ClientWebservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StartAuthenticateMeetupTask startAuthenticateMeetupTask = new StartAuthenticateMeetupTask(this);
        startAuthenticateMeetupTask.execute();
        
        _contactSortingTabs = (TabHost) findViewById(R.id.contact_sorting_tabs);
        _contactSortingTabs.setOnTabChangedListener(this); 

        _contactSortingTabs.setup();
        
        _contactSortingTabs.addTab(newTab(TAB_CONTACTS, R.string.contacts, R.id.sort_by_contact_container));
        _contactSortingTabs.addTab(newTab(TAB_MEETUPS, R.string.meetups, R.id.sort_by_group_container));
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		Uri uri = getIntent().getData();
		
		if (uri != null && ClientWebservices.CALLBACK_URI.getScheme().equals(uri.getScheme())) {
			FinishAuthenticateMeetupTask finishAuthenticateMeetupTask = new FinishAuthenticateMeetupTask(this, uri);
			finishAuthenticateMeetupTask.execute();
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case R.id.add_contact:
				Intent intent = new Intent(getBaseContext(), AddContactActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
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
		// TODO Auto-generated method stub
		
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
				Uri authenticationUri = ClientWebservices.startMeetupAuthorization(_parentActivity.getBaseContext());
				
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
			Intent i = _parentActivity.getIntent();

			ClientWebservices.completeMeetupAuthorization(_parentActivity, _meetupUri);
			
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
}
