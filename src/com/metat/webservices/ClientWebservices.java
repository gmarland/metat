package com.metat.webservices;

import junit.framework.Assert;

import com.metat.helpers.PreferencesHelper;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

public class ClientWebservices {
	public static final String METAT_API_KEY = "mdefm4kojdu6bl0uset6g97umt";
	public static final String METAT_API_KEY_SECRET = "ac303nl4k4ks614f13fasbig35";
	
	public static final String MEETUP_REQUEST_TOKEN_URL = "https://api.meetup.com/oauth/request/";
	public static final String MEETUP_ACCESS_TOKEN_URL = "https://api.meetup.com/oauth/access/";
	public static final String MEETUP_AUTHORIZE_URL = "http://www.meetup.com/authenticate";

	public static final Uri CALLBACK_URI = Uri.parse("metat://authorized");
	
	public static Uri startMeetupAuthorization(Context context, OAuthConsumer consumer, OAuthProvider provider)
	{
		provider.setOAuth10a(true);

		SharedPreferences settings = context.getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);

		try {
			String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URI.toString());

			SharedPreferences.Editor editor = settings.edit();
		
			editor.putString(PreferencesHelper.REQUEST_TOKEN, consumer.getToken());
			editor.putString(PreferencesHelper.REQUEST_SECRET, consumer.getTokenSecret());
			
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
	
	public static void completeMeetupAuthorization(Context context, OAuthConsumer consumer, OAuthProvider provider, Uri callbackUri)
	{
		provider.setOAuth10a(true);
		SharedPreferences settings = context.getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
		
		String token = settings.getString(PreferencesHelper.REQUEST_TOKEN, null);
		String secret = settings.getString(PreferencesHelper.REQUEST_SECRET, null);

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
			
			editor.putString(PreferencesHelper.USER_TOKEN, token);
			editor.putString(PreferencesHelper.USER_SECRET, secret);
			
			editor.remove(PreferencesHelper.REQUEST_TOKEN);
			editor.remove(PreferencesHelper.REQUEST_SECRET);
				
			editor.commit();
		}
		catch (OAuthMessageSignerException ex) {
			Log.e("completeMeetupAuthorization", Log.getStackTraceString(ex));
		}
		catch (OAuthNotAuthorizedException ex) {
			Log.e("completeMeetupAuthorization", Log.getStackTraceString(ex));
		}
		catch (OAuthExpectationFailedException ex) {
			Log.e("completeMeetupAuthorization", Log.getStackTraceString(ex));
		} 
		catch (OAuthCommunicationException ex) {
			Log.e("completeMeetupAuthorization", Log.getStackTraceString(ex));
		}
	}
	  
	public static void getCurrentUser()
	{
	}
}
