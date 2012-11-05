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

import com.metat.models.Group;

public class GroupWebservices {
	private static final String GROUP_CONTAINER = "results";
	private static final String GROUP_ID = "id";
	private static final String GROUP_NAME = "name";

	private static final String GET_ALL_GROUPS = "https://api.meetup.com/2/groups?access_token={key}&member_id={member_id}&only=" + GROUP_ID + "," + GROUP_NAME;

	
	public static Group[] getAllGroups(String meetupKey, String memberId)
	{
		HttpResponse response = null;
		StatusLine statusLine = null;
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
		    response = httpclient.execute(new HttpGet(GET_ALL_GROUPS.replace("{key}", meetupKey).replace("{member_id}", memberId)));
		    statusLine = response.getStatusLine();
		}
		catch (Exception ex)
		{
			Log.e("getAllGroups()", Log.getStackTraceString(ex));
			return new Group[0];
		}


	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        
	        try {
	        	response.getEntity().writeTo(out);
		        out.close();
	        }
	        catch (Exception ex) {
				Log.e("getAllGroups()", Log.getStackTraceString(ex));
				return new Group[0];
	        }
	        
	        String responseString = out.toString();
	        
	        ArrayList<Group> groups = new ArrayList<Group>();

	        try {
				JSONObject json = new JSONObject(responseString);
				JSONArray allGroups = json.getJSONArray(GROUP_CONTAINER);

			    for(int i = 0; i < allGroups.length(); i++){
			        JSONObject group = allGroups.getJSONObject(i);
			        groups.add(new Group(group.getString(GROUP_ID), group.getString(GROUP_NAME)));
			    }
			    
			    return groups.toArray(new Group[groups.size()]);
			}
	        catch (JSONException ex) {
				Log.e("getAllGroups()", Log.getStackTraceString(ex));
				return new Group[0];
			}
	    }
	    else {
	        try {
	        	response.getEntity().getContent().close();
	        }
	        catch (Exception ex) {
				Log.e("getAllGroups()", Log.getStackTraceString(ex));
	        }

			return new Group[0];
	    }
	}
}
