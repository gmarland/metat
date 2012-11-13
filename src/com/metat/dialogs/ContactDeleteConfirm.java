package com.metat.dialogs;

import com.metat.contacts.R;
import com.metat.main.MainActivity;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.models.Contact;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ContactDeleteConfirm extends DialogFragment {
	private Context _context;
	private Contact _contact;

	private TextView _deleteConfirmHeader;
	private TextView _confirmedBtn;
	private TextView _cancelledBtn;
	
	public static ContactDeleteConfirm newInstance(long contactId) {
		ContactDeleteConfirm contactAction = new ContactDeleteConfirm();

        Bundle args = new Bundle();
        args.putLong("contactId", contactId);
        contactAction.setArguments(args);

        return contactAction;
    }
	
	public ContactDeleteConfirm() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		_context = getActivity();
		
        View view = inflater.inflate(R.layout.contact_delete_confirm, container);
		
		ContactDataAccess contactDataAccess = new ContactDataAccess(_context);
		_contact = contactDataAccess.getContact(getArguments().getLong("contactId"));
		
		_deleteConfirmHeader = (TextView) view.findViewById(R.id.contact_delete_confirm_header);
		_deleteConfirmHeader.setText(_contact.getName());
		
		_confirmedBtn = (TextView) view.findViewById(R.id.confirmed_btn);
		_confirmedBtn.setOnClickListener(deleteConfirmedButtonListener);
		
		_cancelledBtn = (TextView) view.findViewById(R.id.cancelled_btn);
		_cancelledBtn.setOnClickListener(deleteCancelledButtonListener);
		
        return view;
	}
 	
    private Button.OnClickListener deleteConfirmedButtonListener = new Button.OnClickListener() 
    {
 		public void onClick(View v) {
 			((MainActivity)_context).deleteContactConfirmed(_contact.getId());
 			getDialog().dismiss();
 		}
     };
  	
     private Button.OnClickListener deleteCancelledButtonListener = new Button.OnClickListener() 
     {
  		public void onClick(View v) {
 			getDialog().dismiss();
  		}
     };
}
