package com.metat.adapters;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.metat.contacts.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

public class GroupContactsAdapter extends BaseAdapter implements SectionIndexer {
	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
	public final ArrayAdapter<String> headers;
	
	public final static int TYPE_SECTION_HEADER = 0;

    private HashMap<String, Integer> _alphaIndexer;  
	
	public GroupContactsAdapter(Context context) {
		headers = new ArrayAdapter<String>(context, R.layout.contact_list_header);
		_alphaIndexer = new HashMap<String, Integer>();  
	}
	
	public void addSection(String section, Adapter adapter) {
		this.headers.add(section);
		this.sections.put(section, adapter);
		
		int sectionCount = 0;

		for (int i=0; i<headers.getCount()-1; i++)
		{
			sectionCount += sections.get(headers.getItem(i)).getCount();
		}
		
		_alphaIndexer.put(section, sectionCount);
	}
	
	public Object getItem(int position) {
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section 
			if(position == 0) return section;
			if(position < size) return adapter.getItem(position - 1);
	
			// otherwise jump into next section
			position -= size;
		}
		return null;
	}
	
	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for(Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		return total;
	}
	
	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for(Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}
	
	public int getItemViewType(int position) {
		int type = 1;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section 
			if(position == 0) return TYPE_SECTION_HEADER;
			if(position < size) return type + adapter.getItemViewType(position - 1);
	
			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}
	
	public boolean areAllItemsSelectable() {
		return false;
	}
	
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}
	
	public void stopAdapterLoaders()
	{
		for(String section : this.sections.keySet()) {
			GroupContactsSectionAdapter adapter = (GroupContactsSectionAdapter)sections.get(section);
			adapter.GroupImageLoader = null;
			adapter = null;
			sections.put(section, null);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;
			
			// check if position inside this section 
			if(position == 0) return headers.getView(sectionnum, convertView, parent);
			if(position < size) return adapter.getView(position - 1, convertView, parent);
	
			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getPositionForSection(int position) {
		return _alphaIndexer.get(headers.getItem(position));
	}

	@Override
	public int getSectionForPosition(int position) {
		int sectionCount = 0;

		for (int i=0; i<headers.getCount()-1; i++)
		{
			sectionCount += sections.get(headers.getItem(i)).getCount();
			
			if (sectionCount > position)
				return i;
		}
		
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sectionHeaders = new String[headers.getCount()];
		
		for (int i=0; i<headers.getCount(); i++)
		{
			sectionHeaders[i] = headers.getItem(i);
		}
		
		return sectionHeaders;
	}
}
