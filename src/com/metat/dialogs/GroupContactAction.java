package com.metat.dialogs;

import com.example.metat.R;
import com.metat.main.GroupActivity;
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

public class GroupContactAction extends DialogFragment {
	private Context _context;
	private Contact _contact;

	private TextView _contactActionHeader;
	private TextView _editBtn;
	private TextView _deleteBtn;
	
	public static GroupContactAction newInstance(long contactId) {
		GroupContactAction contactAction = new GroupContactAction();

        Bundle args = new Bundle();
        args.putLong("contactId", contactId);
        contactAction.setArguments(args);

        return contactAction;
    }
	
	public GroupContactAction() {
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
		
        View view = inflater.inflate(R.layout.contact_action, container);
		
		ContactDataAccess contactDataAccess = new ContactDataAccess(_context);
		_contact = contactDataAccess.getContact(getArguments().getLong("contactId"));
		
		_contactActionHeader = (TextView) view.findViewById(R.id.contact_action_header);
		_contactActionHeader.setText(_contact.getName());
		
		_editBtn = (TextView) view.findViewById(R.id.edit_contact_btn);
		_editBtn.setOnClickListener(editContactButtonListener);
		
		_deleteBtn = (TextView) view.findViewById(R.id.delete_contact_btn);
		_deleteBtn.setOnClickListener(deleteContactButtonListener);
		
        return view;
	}
	
    private Button.OnClickListener editContactButtonListener = new Button.OnClickListener() 
    {
 		public void onClick(View v) {
 			((GroupActivity)_context).editContactSelected(_contact.getId());
 		}
     };
  	
     private Button.OnClickListener deleteContactButtonListener = new Button.OnClickListener() 
     {
  		public void onClick(View v) {
 			//getDialog().dismiss();
 			((GroupActivity)_context).deleteContactSelected(_contact.getId());
  		}
     };
}
