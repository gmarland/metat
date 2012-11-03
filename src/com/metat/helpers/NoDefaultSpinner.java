package com.metat.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class NoDefaultSpinner extends Spinner {
	public NoDefaultSpinner(Context context)
	{ 
		super(context);
	}

	public NoDefaultSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public NoDefaultSpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setSelection(int position, boolean animate)
	{
		boolean sameSelected = position == getSelectedItemPosition();
		super.setSelection(position, animate);
	  
		if (sameSelected) {
		    getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
	    }
	}
	
	@Override public void setSelection(int position)
	{
		boolean sameSelected = position == getSelectedItemPosition();
		super.setSelection(position);
		if (sameSelected) {
			if (getOnItemSelectedListener() != null) {
				getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
			}
		}
	}
}
