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

	private static final String GET_ALL_CONTACTS = "https://api.meetup.com/2/members?access_token={key}&group_id={group_id}&only=" + MEMBER_ID + "," + MEMBER_NAME + "," + MEMBER_PHOTO_CONTAINER + "." + MEMBER_PHOTO_URL;


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
			        if (contact.has(MEMBER_PHOTO_CONTAINER))
			        {
				        JSONObject photoContainer = contact.getJSONObject(MEMBER_PHOTO_CONTAINER);
				        contacts.add(new MeetupContact(contact.getString(MEMBER_ID), photoContainer.getString(MEMBER_PHOTO_URL), contact.getString(MEMBER_NAME), groupId));
			        }
			        else
			        {
				        contacts.add(new MeetupContact(contact.getString(MEMBER_ID), "", contact.getString(MEMBER_NAME), groupId));
			        }
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
