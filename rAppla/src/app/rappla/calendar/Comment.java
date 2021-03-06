/*
 * Copyright (C) 2005-2006 Craig Knudsen and other authors
 * (see AUTHORS for a complete list)
 *
 * JavaCalTools is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the GNU Lesser General Public License is included in the Wine
 * distribution in the file COPYING.LIB. If you did not receive this copy,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA.
 */

package app.rappla.calendar;

import java.util.Locale;

/**
 * iCalendar Comment class - This object represents a comment and corresponds to
 * the COMMENT iCalendar property.
 * 
 * @version $Id: Comment.java,v 1.1 2007/05/11 13:55:08 cknudsen Exp $
 * @author Craig Knudsen, craig@k5n.us
 */
public class Comment extends Property
{
	/** Alternate representation URI */
	public String altrep = null;
	/** Language specification */
	public String language = null;

	/**
	 * Constructor
	 */
	public Comment()
	{
		super("COMMENT", "");
	}

	/**
	 * Constructor
	 * 
	 * @param icalStr
	 *            One or more lines of iCalendar that specifies an event/todo
	 *            comment
	 * @param parseMode
	 *            PARSE_STRICT or PARSE_LOOSE
	 */
	public Comment(String icalStr) throws ParseException
	{
		super(icalStr);

		for (int i = 0; i < attributeList.size(); i++)
		{
			Attribute a = attributeAt(i);
			String aname = a.name.toUpperCase(Locale.ENGLISH);
			if (aname.equals("ALTREP"))
			{
				altrep = a.value;
			} else if (aname.equals("LANGUAGE"))
			{
				language = a.value;
			}
		}
	}

}
