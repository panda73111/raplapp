package RapplaGrid;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Space;
import app.rappla.R;

public class RapplaGrid extends GridLayout
{
	public final static String elementPrefix = "EP#";
	public final static String coordinateSeperator = "#";

	private HashMap<View, RapplaGridElementLayout> allElementLayouts;
	private ArrayList<View> nonSpaceViews;


	public RapplaGrid(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		allElementLayouts = new HashMap<View, RapplaGridElementLayout>();
		nonSpaceViews= new ArrayList<View>();
		fillWithSpaces(context);
	}


	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			setBackgroundResource(R.drawable.wooden_background_landscape);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			setBackgroundResource(R.drawable.wooden_background_potrait);
		}
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		for (int i = 0; i < getChildCount(); i++)
		{
			View view 			= getChildAt(i);
			applyLayout(view, allElementLayouts.get(view));
		}
		
	}


	public View getElementAt(int x, int y)
	{
		return findViewWithTag(elementPrefix + x + coordinateSeperator + y);
	}

	public boolean addElement(RapplaGridElement element)
	{
		RapplaGridElementLayout layout = element.getEventLayout();
		return addElementAt(element.getEventButton(), layout.xCoordinate, layout.yCoordinate, layout.rowSpan);
	}
	public boolean addElementAt(View element, int x, int y)
	{
		return addElementAt(element, x, y, 0);
	}
	public boolean addElementAt(View element, int x, int y, int rowSpan)
	{
		if(allElementLayouts.containsKey(element))	// Sollte das Element schon 
			return false;							// hinzugefügt worden sein
		
		if(rowSpan<1) rowSpan=1;					// Rowspan auf Minimum 1 setzen
													// TODO: auf Maximum achten
		
		if(!(element instanceof Space))
			nonSpaceViews.add(element);
		
		RapplaGridElementLayout rapplaLayout 	= new RapplaGridElementLayout(x, y, rowSpan);
		allElementLayouts.put(element, rapplaLayout);
		applyLayout(element, rapplaLayout);
		
		addView(element);
		return true;
	}
	public boolean removeElement(View view)
	{
		if(!allElementLayouts.containsKey(view))
			return false;
		removeView(view);
		allElementLayouts.remove(view);
		nonSpaceViews.remove(view);
		return true;
	}
	
	/**
	 * This wont remove the Spaces!
	 */
	public void removeAllElements()
	{
		while(nonSpaceViews.size()>0)
			removeElement(nonSpaceViews.get(0));

	}
	
	private void fillWithSpaces(Context context)
	{
		for (int x = 0; x < getColumnCount(); x++)
		{
			for (int y = 0; y < getRowCount(); y++)
			{
				Space s = new Space(context);
				addElementAt(s, x, y);
			}
		}
	}

	private void applyLayout(View view, RapplaGridElementLayout layout)
	{
		if(layout==null)
			return;
		applyLayout(view, layout.xCoordinate, layout.yCoordinate, layout.rowSpan);
	}
	private void applyLayout(View view, int x, int y, int rowSpan)
	{
		// Try to get old Layout
		GridLayout.LayoutParams gParams = (LayoutParams) view.getLayoutParams();
		// If failed, create a new one
		if(gParams==null) 
			gParams = new GridLayout.LayoutParams();
		
		gParams.setGravity(Gravity.FILL_VERTICAL);
		gParams.width 		= getWidth() / getColumnCount();
		gParams.height 		= getHeight() / getRowCount() * rowSpan;
		gParams.rowSpec 	= GridLayout.spec(y, rowSpan);
		gParams.columnSpec 	= GridLayout.spec(x, 1);

		view.setLayoutParams(gParams);
	}
}
