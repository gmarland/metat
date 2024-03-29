package com.metat.dialogs;

import com.metat.contacts.R;
import com.metat.main.MainActivity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class LoadingContacts extends DialogFragment {
	
	public static LoadingContacts newInstance() {
		LoadingContacts loadingContacts = new LoadingContacts();

        return loadingContacts;
    }
	
	public LoadingContacts() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View view = inflater.inflate(R.layout.loading_contacts, container);

		LinearLayout loadingProgressBarContainer = (LinearLayout) view.findViewById(R.id.loading_contacts_progressBar);
    	ProgressBar progressBar = new ProgressBar(getActivity());
    	loadingProgressBarContainer.addView(progressBar);

    	getDialog().setCancelable(true);
    	getDialog().setCanceledOnTouchOutside(true);
        
        return view;
	}
	
	public void onDismiss (DialogInterface dialog)
	{
		MainActivity.LoggingIn = false;
	}
}
