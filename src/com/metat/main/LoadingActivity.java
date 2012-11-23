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
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class LoadingActivity extends Activity {
	private OAuthConsumer _consumer;
	private OAuthProvider _provider;

	private String _userToken = "";
	
	private int _loadingCount = 0;
	public static boolean ContinueLoading = true;

	private TextView loadingLabel;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

        loadingLabel = (TextView) findViewById(R.id.loading_label);
        
        _loadingCount = 1;
        ContinueLoading = true;
        
		Thread thread = new Thread(new Runnable() { public void run() {
				try
				{
					if (ContinueLoading)
						_loadingAnimateHandler.sendMessage(new Message());
				}
				catch (Exception ex) {}
			}
		});
		
		thread.start();
		
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
		ContinueLoading = false;
		
		Intent cancelIntent = new Intent(getBaseContext(), MainActivity.class);
		cancelIntent.putExtra("selectedTab", MainActivity.TAB_MEETUPS);
		cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		getBaseContext().startActivity(cancelIntent);	
	}

    final Handler _loadingAnimateHandler = new Handler()
    {
		 @Override
		 public void handleMessage(Message msg) {
			 String loadingString = getBaseContext().getResources().getString(R.string.loading);
			 
			 for (int i=0; i<_loadingCount; i++)
			 {
				 loadingString += ".";
			 }

			 loadingLabel.setText(loadingString);
		        
			 if (_loadingCount < 3)
				 _loadingCount++;
			 else
				 _loadingCount = 0;
			 
			Thread thread = new Thread(new Runnable() { public void run() {
					try
					{
						Thread.sleep(500);

						if (ContinueLoading)
							_loadingAnimateHandler.sendMessage(new Message());
					}
					catch (Exception ex) {}
				}
			});
			
			thread.start();
		 }
    };
}
