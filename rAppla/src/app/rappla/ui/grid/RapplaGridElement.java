package app.rappla.ui.grid;

import android.content.Context;
import android.widget.Button;
import app.rappla.R;
import app.rappla.calendar.RaplaEvent;
import app.rappla.ui.fragments.CalenderFragment;

public class RapplaGridElement
{
	protected static final int eventImage = R.drawable.event_glass;
	
	
	private Button eventButton;
	private RapplaGridElementLayout eventLayout;

	public RapplaGridElement(Context context, RaplaEvent raplaEvent, RapplaGridElementLayout layout)
	{
		eventButton = createEventButton(context, raplaEvent);
		eventLayout = layout;
	}

	public RapplaGridElement(Context context, RaplaEvent raplaEvent, int column, int offset, int length)
	{
		eventButton = createEventButton(context, raplaEvent);
		eventLayout = new RapplaGridElementLayout(column, offset, length);
	}

	public RapplaGridElement(Context context, RaplaEvent raplaEvent, int column)
	{
		eventButton = createEventButton(context, raplaEvent);
		eventLayout = createEventLayout(raplaEvent, column);
	}

	private Button createEventButton(Context context, RaplaEvent raplaEvent)
	{
		Button button = new Button(context);
		button.setOnClickListener(new EventClickListener(raplaEvent.getUid(), context));
		
		button.setBackgroundResource(eventImage);
		button.setText(raplaEvent.getTitle());
		return button;
	}

	private RapplaGridElementLayout createEventLayout(RaplaEvent raplaEvent, int column)
	{
		int eventLength = (int) (raplaEvent.getDurationInMinutes() / CalenderFragment.timeInterval);
		int eventOffset = (int) (raplaEvent.getTimeDifferenceInMinutes(CalenderFragment.getEarliestStart(raplaEvent.getDate())) / CalenderFragment.timeInterval);

		// Sorgt daf�r, dass kein Event l�nger als der Tag werden kann
		// TODO: Das geht noch sch�ner
		eventLength = (int) Math.min(CalenderFragment.getDayDuration() / CalenderFragment.timeInterval - eventOffset, eventLength);

		return new RapplaGridElementLayout(column, eventOffset, eventLength);
	}

	public Button getEventButton()
	{
		return eventButton;
	}

	public RapplaGridElementLayout getEventLayout()
	{
		return eventLayout;
	}
}