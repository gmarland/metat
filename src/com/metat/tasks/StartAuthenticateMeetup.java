package com.metat.tasks;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.metat.webservices.ClientWebservices;

public class StartAuthenticateMeetup extends AsyncTask<String, String, String>
{
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	
	private Activity _parentActivity;
	
	public StartAuthenticateMeetup(Activity activity, OAuthConsumer consumer, OAuthProvider provider)
	{
		_parentActivity = activity;

		_consumer = consumer;
		_provider = provider;
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