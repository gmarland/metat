package com.metat.webservices;

import junit.framework.Assert;

import com.metat.helpers.PreferencesHelper;
import com.metat.main.MainActivity;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

public class ClientWebservices {
	private static final String METAT_API_KEY = "mdefm4kojdu6bl0uset6g97umt";
	private static final String METAT_API_KEY_SECRET = "ac303nl4k4ks614f13fasbig35";

	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";
	
	public static final String MEETUP_REQUEST_TOKEN_URL = "https://api.meetup.com/oauth/request/";
	public static final String MEETUP_ACCESS_TOKEN_URL = "https://api.meetup.com/oauth/access/";
	public static final String MEETUP_AUTHORIZE_URL = "http://www.meetup.com/authenticate";

	public static final Uri CALLBACK_URI = Uri.parse("metat://authorized");
	
	public static Uri startMeetupAuthorization(Context context)
	{
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(METAT_API_KEY, METAT_API_KEY_SECRET);
		OAuthProvider provider = new CommonsHttpOAuthProvider(MEETUP_REQUEST_TOKEN_URL, MEETUP_ACCESS_TOKEN_URL, MEETUP_AUTHORIZE_URL);

		provider.setOAuth10a(true);

		SharedPreferences settings = context.getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);

		try {
			String authUrl = provider.retrieveRequestToken(consumer, "MetAt://authorized");

			SharedPreferences.Editor editor = settings.edit();
		
			editor.putString(REQUEST_TOKEN, consumer.getToken());
			editor.putString(REQUEST_SECRET, consumer.getTokenSecret());
			
			editor.commit();
			
			return Uri.parse(authUrl);
		} 
		catch (OAuthMessageSignerException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		} 
		catch (OAuthNotAuthorizedException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		} 
		catch (OAuthExpectationFailedException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		} 
		catch (OAuthCommunicationException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		}
		
		return null;
	}
	
	public static void completeMeetupAuthorization(Context context, Uri callbackUri)
	{
		OAuthConsumer consumer = new DefaultOAuthConsumer(METAT_API_KEY, METAT_API_KEY_SECRET);
		
		OAuthProvider provider = new DefaultOAuthProvider(
									MEETUP_REQUEST_TOKEN_URL,
									MEETUP_ACCESS_TOKEN_URL,
									MEETUP_AUTHORIZE_URL);

		provider.setOAuth10a(true);
		SharedPreferences settings = context.getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
		
		String token = settings.getString(REQUEST_TOKEN, null);
		String secret =settings.getString(REQUEST_SECRET, null);
		
		Intent i = new Intent(context, MainActivity.class);

		try {
			if(!(token == null || secret == null)) {
				consumer.setTokenWithSecret(token, secret);
			}
			
			String otoken = callbackUri.getQueryParameter(OAuth.OAUTH_TOKEN);
			String verifier = callbackUri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			Assert.assertEquals(otoken, consumer.getToken());

			provider.retrieveAccessToken(consumer, verifier);

			token = consumer.getToken();
			secret = consumer.getTokenSecret();

			SharedPreferences.Editor editor = settings.edit();

			Log.e("otoken", otoken);
			Log.e("token", token);
			
			editor.putString(USER_TOKEN, token);
			editor.putString(USER_SECRET, secret);
			
			editor.remove(REQUEST_TOKEN);
			editor.remove(REQUEST_SECRET);
				
			editor.commit();
			
			i.putExtra(USER_TOKEN, token);
			i.putExtra(USER_SECRET, secret);
		}
		catch (OAuthMessageSignerException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		}
		catch (OAuthNotAuthorizedException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		}
		catch (OAuthExpectationFailedException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		} 
		catch (OAuthCommunicationException ex) {
			Log.e("startMeetupAuthorization", Log.getStackTraceString(ex));
		}
	}
	  
	public static void getCurrentUser()
	{
	}
}
