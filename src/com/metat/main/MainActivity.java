package com.metat.main;

import com.example.metat.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements OnTabChangeListener {
    public static final String TAB_CONTACTS = "contacts";
    public static final String TAB_MEETUPS = "meetups";

	private TabHost _contactSortingTabs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        _contactSortingTabs = (TabHost) findViewById(R.id.contact_sorting_tabs);
        _contactSortingTabs.setOnTabChangedListener(this); 

        _contactSortingTabs.setup();
        
        _contactSortingTabs.addTab(newTab(TAB_CONTACTS, R.string.contacts, R.id.sort_by_contact_container));
        _contactSortingTabs.addTab(newTab(TAB_MEETUPS, R.string.meetups, R.id.sort_by_group_container));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case R.id.add_contact:
				Intent intent = new Intent(getBaseContext(), AddContactActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getBaseContext().startActivity(intent);	
        		return true;
        }
        
        return false;
    }

    // Private Methods
    private TabSpec newTab(String tag, int label, int tabContentId) 
    {        
    	View indicator = LayoutInflater.from(this).inflate(R.layout.contact_sorting_tab, null);
    	((TextView) indicator.findViewById(R.id.sorting_lbl)).setText(label);
    	TabSpec tabSpec = _contactSortingTabs.newTabSpec(tag);
    	tabSpec.setIndicator(indicator);
    	tabSpec.setContent(tabContentId);

    	return tabSpec; 
    }

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
