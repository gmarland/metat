package com.metat.webservices;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.metat.models.MeetupContact;

public class ContactWebservices {
	private static final String MEMBER_CONTAINER = "results";
	private static final String MEMBER_ID = "id";
	private static final String MEMBER_NAME = "name";
	private static final String MEMBER_PHOTO_CONTAINER = "photo";
	private static final String MEMBER_PHOTO_URL = "photo_link";
	private static final String MEMBER_OTHER_SERVICES = "other_services";
	private static final String MEMBER_TWITTER = "twitter";
	private static final String MEMBER_LINKEDIN = "linkedin";
	private static final String MEMBER_FACEBOOK = "facebook";
	private static final String MEMBER_TUMBLR = "tumblr";
    private static final String MEMBER_FLICKR = "flickr";
	private static final String MEMBER_OTHER_SERVICES_IDENTIFIER = "identifier";

	private static final String GET_ALL_CONTACTS = "https://api.meetup.com/2/members?access_token={key}&group_id={group_id}&only=" + MEMBER_ID + "," + MEMBER_NAME + "," + MEMBER_PHOTO_CONTAINER + "," + MEMBER_PHOTO_URL + "," + MEMBER_OTHER_SERVICES;


	public static ArrayList<MeetupContact> getAllContacts(String meetupKey, String groupId)
	{
		HttpResponse response = null;
		StatusLine statusLine = null;
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
		    response = httpclient.execute(new HttpGet(GET_ALL_CONTACTS.replace("{key}", meetupKey).replace("{group_id}", groupId)));
		    statusLine = response.getStatusLine();
		}
		catch (Exception ex)
		{
			Log.e("getAllContacts()", Log.getStackTraceString(ex));
			return new ArrayList<MeetupContact>();
		}

	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        
	        try {
	        	response.getEntity().writeTo(out);
		        out.close();
	        }
	        catch (Exception ex) {
				Log.e("getAllContacts()", Log.getStackTraceString(ex));
				return new ArrayList<MeetupContact>();
	        }
	        
	        String responseString = out.toString();

	        ArrayList<MeetupContact> contacts = new ArrayList<MeetupContact>();

	        try {
				JSONObject json = new JSONObject(responseString);
				JSONArray allContacts = json.getJSONArray(MEMBER_CONTAINER);

			    for(int i = 0; i < allContacts.length(); i++){
			        JSONObject contact = allContacts.getJSONObject(i);
			        
			        String photoUrl = "";
			        String twitterId = "";
			        String linkedInId = "";
			        String facebookId = "";
			        String tumblrId = "";
			        String flickrId = "";
			        
			        if (contact.has(MEMBER_PHOTO_CONTAINER))
			        {
				        JSONObject photoContainer = contact.getJSONObject(MEMBER_PHOTO_CONTAINER);
			        	photoUrl = photoContainer.getString(MEMBER_PHOTO_URL);
			        }

			        if (contact.has(MEMBER_OTHER_SERVICES))
			        {
				        JSONObject otherServices = contact.getJSONObject(MEMBER_OTHER_SERVICES);

				        if (otherServices.has(MEMBER_TWITTER))
				        {
					        JSONObject twitterObj = otherServices.getJSONObject(MEMBER_TWITTER);
					        twitterId = twitterObj.getString(MEMBER_OTHER_SERVICES_IDENTIFIER);
				        }

				        if (otherServices.has(MEMBER_LINKEDIN))
				        {
					        JSONObject linkedinObj = otherServices.getJSONObject(MEMBER_LINKEDIN);
					        linkedInId = linkedinObj.getString(MEMBER_OTHER_SERVICES_IDENTIFIER);
				        }

				        if (otherServices.has(MEMBER_FACEBOOK))
				        {
					        JSONObject facebookObj = otherServices.getJSONObject(MEMBER_FACEBOOK);
					        facebookId = facebookObj.getString(MEMBER_OTHER_SERVICES_IDENTIFIER);
				        }

				        if (otherServices.has(MEMBER_TUMBLR))
				        {
					        JSONObject tumblrObj = otherServices.getJSONObject(MEMBER_TUMBLR);
					        tumblrId = tumblrObj.getString(MEMBER_OTHER_SERVICES_IDENTIFIER);
				        }

				        if (otherServices.has(MEMBER_FLICKR))
				        {
					        JSONObject flickrObj = otherServices.getJSONObject(MEMBER_FLICKR);
					        flickrId = flickrObj.getString(MEMBER_OTHER_SERVICES_IDENTIFIER);
				        }
			        }
			        
			        contacts.add(new MeetupContact(contact.getString(MEMBER_ID), photoUrl, contact.getString(MEMBER_NAME), twitterId, linkedInId, facebookId, tumblrId, flickrId, groupId));
			    }
			    
			    return contacts;
			}
	        catch (JSONException ex) {
				Log.e("getAllContacts()", Log.getStackTraceString(ex));
				return new ArrayList<MeetupContact>();
			}
	    }
	    else {
	        try {
	        	response.getEntity().getContent().close();
	        }
	        catch (Exception ex) {
				Log.e("getAllContacts()", ex.getMessage());
	        }

			return new ArrayList<MeetupContact>();
	    }
	}
}
