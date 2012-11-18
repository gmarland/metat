package com.metat.main;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import com.metat.contacts.R;
import com.metat.helpers.ConnectionHelper;
import com.metat.helpers.PreferencesHelper;
import com.metat.tasks.UpdateMeetupGroups;
import com.metat.webservices.ClientWebservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LoadingActivity extends Activity {
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;

	private String _userToken = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

    	_consumer = new CommonsHttpOAuthConsumer(ClientWebservices.METAT_API_KEY, ClientWebservices.METAT_API_KEY_SECRET);
    	_provider = new CommonsHttpOAuthProvider(ClientWebservices.MEETUP_REQUEST_TOKEN_URL, ClientWebservices.MEETUP_ACCESS_TOKEN_URL, ClientWebservices.MEETUP_AUTHORIZE_URL);

        SharedPreferences settings = getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
		
        if (settings.getString(PreferencesHelper.USER_TOKEN, null) != null)
        {
	    	if (ConnectionHelper.isNetworkAvailable(getBaseContext()))
	    	{
	        	_userToken = settings.getString(PreferencesHelper.USER_TOKEN, "");
	        	refreshMeetupGroups();
	        	
	        }
    	}
        
        continueToMain();
    }

	public void refreshMeetupGroups()
	{
		UpdateMeetupGroups updateMeetupGroupsTask = new UpdateMeetupGroups(this, _userToken, _consumer, _provider);
		updateMeetupGroupsTask.execute();
	}
	
	public void continueToMain()
	{
		Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);
		cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
		cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		getBaseContext().startActivity(cancelIntent);	
	}
}
