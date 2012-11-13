package com.metat.dialogs;

import com.metat.contacts.R;
import com.metat.helpers.PreferencesHelper;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeMessage extends DialogFragment {
	private TextView _alertMessageHeader;
	private TextView _alertMessageButton;
	private LinearLayout _dontShowAgainContainer;
	private Button _alertButtonClose;
	
	public static WelcomeMessage newInstance() {
        return new WelcomeMessage();
    }
	
	public WelcomeMessage() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setStyle(STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_message, container);
		
		_alertMessageHeader = (TextView) view.findViewById(R.id.alert_header);
		_alertMessageHeader.setText(R.string.welcome_to_met_at);
		
		_alertMessageButton = (TextView) view.findViewById(R.id.alert_body);
		_alertMessageButton.setText(R.string.welcome_message);
		
		_dontShowAgainContainer = (LinearLayout) view.findViewById(R.id.dont_show_again_container);
		_dontShowAgainContainer.setVisibility(View.GONE);
		
		_alertButtonClose = (Button) view.findViewById(R.id.alert_close_btn);
		_alertButtonClose.setText(R.string.close);
		_alertButtonClose.setOnClickListener(_doneButtonListener);

    	getDialog().setCancelable(false);
    	getDialog().setCanceledOnTouchOutside(false);

        return view;
	}
    
	private Button.OnClickListener _doneButtonListener = new Button.OnClickListener() 
	{
		public void onClick(View v){
	        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PreferencesHelper.MEEUP_PREFS, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();

			editor.putBoolean(PreferencesHelper.SHOW_WELCOME, false);
			
			editor.commit();
			
			WelcomeMessage.this.dismiss();
	   	}
	};
}
