package com.metat.tasks;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.metat.helpers.PreferencesHelper;
import com.metat.main.MainActivity;
import com.metat.webservices.ClientWebservices;

public class FinishAuthenticateMeetup extends AsyncTask<String, String, String>
{
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	
	private Activity _parentActivity;
	private Uri _meetupUri;
	
	public FinishAuthenticateMeetup(Activity activity, OAuthConsumer consumer, OAuthProvider provider, Uri uri)
	{
		_parentActivity = activity;
		
		_consumer = consumer;
		_provider = provider;
		
		_meetupUri = uri;

	}
	
	@Override
	protected String doInBackground(String... strings) {
		ClientWebservices.completeMeetupAuthorization(_parentActivity, _consumer, _provider, _meetupUri);
        
		return "";
	}
	
	@Override
    protected void onPostExecute(String result) {
		SharedPreferences settings =_parentActivity.getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
    	_parentActivity.getIntent().setData(null);
    	
        if ((settings.getString(PreferencesHelper.USER_TOKEN, null) != null) && (_parentActivity.getClass().equals(MainActivity.class)))
        {
        	((MainActivity)_parentActivity).setUserToken(settings.getString(PreferencesHelper.USER_TOKEN, ""));
        	((MainActivity)_parentActivity).resetMenuOptions();
        	((MainActivity)_parentActivity).refreshMeetupGroups();
        }
	}
}