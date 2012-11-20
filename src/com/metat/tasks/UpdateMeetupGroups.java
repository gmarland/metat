package com.metat.tasks;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.metat.dataaccess.ContactDataAccess;
import com.metat.dataaccess.GroupsDataAccess;
import com.metat.dataaccess.MeetupContactDataAccess;
import com.metat.main.MainActivity;
import com.metat.models.Group;
import com.metat.models.MeetupContact;
import com.metat.webservices.ClientWebservices;
import com.metat.webservices.ContactWebservices;
import com.metat.webservices.GroupWebservices;

public class UpdateMeetupGroups extends AsyncTask<String, String, String>
{
	private Activity _parentActivity;

	private OAuthConsumer _consumer;
	private OAuthProvider _provider;
	
	private String _meetupKey;
	private int _actionsPerformed;
	
	private long _selfId;
	private Group[] _retrievedMeetupGroups;
	
	public UpdateMeetupGroups(Activity activity, String meetupKey, OAuthConsumer consumer, OAuthProvider provider)
	{
		_parentActivity = activity;
		_meetupKey = meetupKey;
		_actionsPerformed = 0;

		_consumer = consumer;
		_provider = provider;
	}
	
	@Override
	protected String doInBackground(String... strings) {
		_selfId = ClientWebservices.getCurrentUser(_parentActivity,_meetupKey);

		if (_selfId >= 0)
		{
			_retrievedMeetupGroups = GroupWebservices.getAllGroups(_meetupKey, _selfId + "");
		}
		
		return "Complete";
	}
	
	@Override
    protected void onPostExecute(String result) {
		if ((_selfId == -2) && (MainActivity.AttemptReathorization))
		{
			MainActivity.AttemptReathorization = false;
	        StartAuthenticateMeetup startAuthenticateMeetupTask = new StartAuthenticateMeetup(_parentActivity, _consumer, _provider);
	        startAuthenticateMeetupTask.execute();
		}
		else
		{
			GroupsDataAccess groupsDataAccess = new GroupsDataAccess(_parentActivity);
			ContactDataAccess contactDataAccess = new ContactDataAccess(_parentActivity);
			
			if (_retrievedMeetupGroups.length > 0)
			{
				Group[] existingMeetupGroups = groupsDataAccess.getAllGroups();
				
				for (Group retrievedMeetupGroup : _retrievedMeetupGroups)
				{
					boolean onlineGroupFound = false;

					for (Group existingMeetupGroup : existingMeetupGroups)
					{
						if (retrievedMeetupGroup.getMeetupId().equals(existingMeetupGroup.getMeetupId()))
						{
							onlineGroupFound = true;
							
							if (!retrievedMeetupGroup.getName().trim().equals(existingMeetupGroup.getName().trim()))
							{
								groupsDataAccess.Update(retrievedMeetupGroup.getMeetupId(), retrievedMeetupGroup.getName().trim(), retrievedMeetupGroup.getLink().trim());
								contactDataAccess.UpdateGroupNames(retrievedMeetupGroup.getMeetupId(), retrievedMeetupGroup.getName().trim());
								_actionsPerformed++;
							}
						}
					}
					
					if (!onlineGroupFound)
					{
						groupsDataAccess.Insert(retrievedMeetupGroup);
						_actionsPerformed++;
					}
				}

				existingMeetupGroups = groupsDataAccess.getAllGroups();
				
				for (Group existingMeetupGroup : existingMeetupGroups)
				{
					boolean existingGroupFound = false;

					for (Group onlineMeetupGroup : _retrievedMeetupGroups)
					{
						if (existingMeetupGroup.getMeetupId().equals(onlineMeetupGroup.getMeetupId()))
							existingGroupFound = true;
					}

					if (!existingGroupFound)
					{
						groupsDataAccess.Delete(existingMeetupGroup.getMeetupId());
						_actionsPerformed++;
					}
				}
			}

			if (_actionsPerformed > 0)
			{
		        if (_parentActivity.getClass().equals(MainActivity.class))
		        {
		    		Intent broadcastRefreshComplete = new Intent();
		    		broadcastRefreshComplete.setAction("REFRESH_MEETUP_GROUPS");
		    		_parentActivity.sendBroadcast(broadcastRefreshComplete);
		        }
			}

			if (_retrievedMeetupGroups.length > 0)
			{
				Thread thread = new Thread(new Runnable() { public void run() {
					for (Group retrievedMeetupGroup : _retrievedMeetupGroups)
					{
						int contactActionCount = 0;
						
						ArrayList<MeetupContact> retrievedContacts = new ArrayList<MeetupContact>();
						retrievedContacts = ContactWebservices.getAllContacts(_meetupKey, retrievedMeetupGroup.getMeetupId());

						if (retrievedContacts.size() > 0)
						{
							MeetupContactDataAccess meetupContactDataAccess = new MeetupContactDataAccess(_parentActivity);
							 
							ArrayList<MeetupContact> existingMeetupContacts = meetupContactDataAccess.getAllMeetupContacts(retrievedMeetupGroup.getMeetupId());
							
							for (MeetupContact meetupContact : retrievedContacts)
							{
								boolean contactFound = false;

								for (int i=0; i<existingMeetupContacts.size(); i++)
								{
									if (meetupContact.getMeetupId().equals(existingMeetupContacts.get(i).getMeetupId()))
									{
										contactFound = true;
										
										String linkId = "";
										String twitterId = "";
										String linkedinId = "";
										String facebookId = "";
										String tumblrId = "";
										String flickrId = "";
										
										if (meetupContact.getLink() != null)
											linkId = meetupContact.getLink().trim();
										
										if (meetupContact.getTwitterId() != null)
											twitterId = meetupContact.getTwitterId().trim();
										
										if (meetupContact.getLinkedInId() != null)
											linkedinId = meetupContact.getLinkedInId().trim();
										
										if (meetupContact.getFacebookId() != null)
											facebookId = meetupContact.getFacebookId().trim();
										
										if (meetupContact.getTumblrId() != null)
											tumblrId = meetupContact.getTumblrId().trim();
										
										if (meetupContact.getFlickrId() != null)
											flickrId = meetupContact.getFlickrId().trim();
										
										if (((meetupContact.getName() != null) && (existingMeetupContacts.get(i).getName() != null) && (!meetupContact.getName().trim().equals(existingMeetupContacts.get(i).getName().trim()))) || 
												((existingMeetupContacts.get(i).getPhotoThumbnail() != null) && (existingMeetupContacts.get(i).getPhotoThumbnail() != null) && (!existingMeetupContacts.get(i).getPhotoThumbnail().trim().equals(existingMeetupContacts.get(i).getPhotoThumbnail().trim()))) || 
												((existingMeetupContacts.get(i).getLink() != null) && (meetupContactDataAccess != null) && (!linkId.equals(existingMeetupContacts.get(i).getLink().trim()))) || 
												((existingMeetupContacts.get(i).getTwitterId() != null) && (meetupContactDataAccess != null) && (!twitterId.equals(existingMeetupContacts.get(i).getTwitterId().trim()))) || 
												((existingMeetupContacts.get(i).getFacebookId() != null) && (meetupContactDataAccess != null) && (!facebookId.equals(existingMeetupContacts.get(i).getFacebookId().trim()))) || 
												((existingMeetupContacts.get(i).getFlickrId() != null) && (meetupContactDataAccess != null) && (!flickrId.equals(existingMeetupContacts.get(i).getFlickrId().trim()))) || 
												((existingMeetupContacts.get(i).getTumblrId() != null) && (meetupContactDataAccess != null) && (!tumblrId.equals(existingMeetupContacts.get(i).getTumblrId()))) || 
												((existingMeetupContacts.get(i).getLinkedInId() != null) && (meetupContactDataAccess != null) && (!linkedinId.equals(existingMeetupContacts.get(i).getLinkedInId().trim()))))
										{
											meetupContactDataAccess.Update(existingMeetupContacts.get(i).getMeetupId(), meetupContact.getPhotoThumbnail().trim(), meetupContact.getName().trim(), linkId, twitterId, linkedinId, facebookId, tumblrId, flickrId);
											existingMeetupContacts.get(i).setPhotoThumbnail(meetupContact.getPhotoThumbnail().trim());
											existingMeetupContacts.get(i).setName(meetupContact.getName().trim());
											existingMeetupContacts.get(i).setLink(linkId);
											existingMeetupContacts.get(i).setTwitterId(twitterId);
											existingMeetupContacts.get(i).setLinkedInId(linkedinId);
											existingMeetupContacts.get(i).setFacebookId(facebookId);
											existingMeetupContacts.get(i).setTumblrId(tumblrId);
											existingMeetupContacts.get(i).setFlickrId(flickrId);
											contactActionCount++;
										}
									}
								}
								
								if (!contactFound)
								{
									meetupContactDataAccess.Insert(meetupContact);
									existingMeetupContacts.add(meetupContact);
									contactActionCount++;
								}
							}
							
							for (MeetupContact existingMeetupContact : existingMeetupContacts)
							{
								boolean existingContactFound = false;

								for (MeetupContact meetupContact : retrievedContacts)
								{
									if (existingMeetupContact.getMeetupId().equals(meetupContact.getMeetupId()))
										existingContactFound = true;
								}

								if (!existingContactFound)
								{
									meetupContactDataAccess.Delete(existingMeetupContact.getMeetupId());
									contactActionCount++;
								}
							} 

							if (contactActionCount > 0)
							{
					    		Intent broadcastContactRefreshComplete = new Intent();
					    		broadcastContactRefreshComplete.setAction("REFRESH_MEETUP_CONTACTS");
	
					        	Bundle extras = new Bundle();
					        	extras.putString("meetupGroupId", retrievedMeetupGroup.getMeetupId());
					        	broadcastContactRefreshComplete.putExtras(extras);
					        	
					    		_parentActivity.sendBroadcast(broadcastContactRefreshComplete);
							}
						}
					}
				} });
				
				thread.start();
			}
		}
	}
}