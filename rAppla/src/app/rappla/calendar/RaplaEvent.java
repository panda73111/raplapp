package app.rappla.calendar;

import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("serial")
public class RaplaEvent implements Comparable<RaplaEvent>, Serializable
{
	private final String uid;
	private final Calendar startTime;
	private final Calendar endTime;
	private final String type;
	private final String title;
	private final String resources;
	private String language = null;
	private String course = null;
	private String changesAllowedBy = null;
	private int plannedHours = 0;
	private String note = null;
	private String createdBy = null;
	private String lastChangeBy = null;
	private String persons = null;

	public RaplaEvent(String uid, Calendar startTime,
			Calendar endTime, String type, String title, String resources)
	{
		this.uid = uid;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.title = title;
		this.resources = resources;
	}

	public static RaplaEvent FromEvent(Event e)
			throws MissingAttributeException
	{
		// all of these attributes are essential
		Uid uid = e.getUid();
		Date startTime = e.getStartDate();
		Date endTime = e.getEndDate();
		Categories categories = e.getCategories();
		Summary summary = e.getSummary();
		Location location = e.getLocation();

		if (uid == null)
			throw new MissingAttributeException("uid");
		if (startTime == null)
			throw new MissingAttributeException("startDate");
		if (endTime == null)
			throw new MissingAttributeException("endDate");
		if (categories == null)
			throw new MissingAttributeException("categories");
		if (summary == null)
			throw new MissingAttributeException("summary");
		if (location == null)
			throw new MissingAttributeException("location");

		RaplaEvent re = new RaplaEvent(uid.getValue(), startTime.toCalendar(), endTime.toCalendar(),
				categories.getValue(), summary.getValue(), location.getValue());

		return re;
	}
	
	public static RaplaEvent FromRecurringRaplaEvent(RaplaEvent e, Calendar newStartTime)
	{
		long duration = e.getDurationInMillis();
		Calendar newEndTime = Calendar.getInstance();
		newEndTime.setTimeInMillis(newStartTime.getTimeInMillis() + duration);
		
		RaplaEvent re = new RaplaEvent(e.uid, newStartTime, newEndTime, e.type, e.title, e.resources);
		re.changesAllowedBy = e.changesAllowedBy;
		re.course = e.course;
		re.createdBy = e.createdBy;
		re.language = e.language;
		re.lastChangeBy = e.lastChangeBy;
		re.note = e.note;
		re.persons = e.persons;
		re.plannedHours = e.plannedHours;
		return re;
	}

	public String getUid()
	{
		return uid;
	}
	public String getUniqueEventID()
	{
		return uid + startTime.getTimeInMillis();
	}

	public Calendar getStartTime()
	{
		return startTime;
	}

	public Calendar getEndTime()
	{
		return endTime;
	}

	public String getType()
	{
		return type;
	}

	public String getTitle()
	{
		return title;
	}
	public String getEventNameWithoutProfessor()
	{
		String 	eventName 					= getTitle();
		int 	separatorIndex				= eventName.indexOf("[");
		
		if(separatorIndex > 0)
			return eventName.substring(0, separatorIndex);
		else
			return eventName;
	}
	public String getProfessor()
	{
		String 	profName 					= getTitle();
		int 	separatorIndex				= profName.indexOf("[");
		
		if(separatorIndex > 0)
			return profName.substring(separatorIndex+1, profName.length()-1);
		else
			return profName;
	}
	
	public String getResources()
	{
		return resources;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setCourse(String course)
	{
		this.course = course;
	}

	public String getCourse()
	{
		return course;
	}

	public void setChangesAllowedBy(String changesAllowedBy)
	{
		this.changesAllowedBy = changesAllowedBy;
	}

	public String getChangesAllowedBy()
	{
		return changesAllowedBy;
	}

	public void setPlannedHours(int plannedHours)
	{
		this.plannedHours = plannedHours;
	}

	public int getPlannedHours()
	{
		return plannedHours;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getNote()
	{
		return note;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setLastChangeBy(String lastChangeBy)
	{
		this.lastChangeBy = lastChangeBy;
	}

	public String getLastChangeBy()
	{
		return lastChangeBy;
	}

	public void setPersons(String persons)
	{
		this.persons = persons;
	}

	public String getPersons()
	{
		return persons;
	}


	/**
	 * May be stupid, but it looks nicer in the Code.
	 * @return The Date the Event is scheduled on
	 */
	public Calendar getDate()
	{
		return getStartTime();
	}
	
	public long getDurationInMillis()
	{
		long startTime  	= getStartTime().getTimeInMillis();
		long endTime  		= getEndTime().getTimeInMillis();
		return endTime-startTime;
	}
	public long getDurationInSeconds()
	{
		return getDurationInMillis()/1000;
	}
	public long getDurationInMinutes()
	{
		return getDurationInSeconds()/60;
	}

	public long getTimeDifferenceInMinutes(Calendar referenceTime)
	{
		return getTimeDifferenceInMinutes(getStartTime(), referenceTime);
	}
	public long getTimeDifferenceInMinutes(RaplaEvent referenceTime)
	{
		return getTimeDifferenceInMinutes(referenceTime.getStartTime());
	}
	public static long getTimeDifferenceInMinutes(RaplaEvent baseTime, RaplaEvent referenceTime)
	{
		return baseTime.getTimeDifferenceInMinutes(referenceTime.getStartTime());
	}
	public static long getTimeDifferenceInMinutes(Calendar baseTime, Calendar referenceTime)
	{
		long timeDifference = (baseTime.getTimeInMillis() - referenceTime.getTimeInMillis())/1000/60;
		return Math.abs(timeDifference);
	}	
	
	@Override
	public int compareTo(RaplaEvent another)
	{
		return this.startTime.compareTo(another.startTime);
	}

    public int hashCode()
    {
        int hash = 0;

        if(uid!=null)
            hash += uid.hashCode();
        if(startTime!=null)
            hash += startTime.hashCode();
        if(endTime!=null)
            hash += endTime.hashCode();
        if(type!=null)
            hash += type.hashCode();
        if(title!=null)
            hash += title.hashCode();
        if(resources!=null)
            hash += resources.hashCode();
        if(language!=null)
            hash += language.hashCode();
        if(course!=null)
            hash += course.hashCode();
        if(changesAllowedBy!=null)
            hash += changesAllowedBy.hashCode();

        hash += plannedHours;

        if(note!=null)
            hash += note.hashCode();
        if(createdBy!=null)
            hash += createdBy.hashCode();
        if(lastChangeBy!=null)
            hash += lastChangeBy.hashCode();
        if(persons!=null)
            hash += persons.hashCode();

        return hash;
    }

}
