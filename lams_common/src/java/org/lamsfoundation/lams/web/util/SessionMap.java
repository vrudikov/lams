/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.web.util;

/* $Id$ */

import java.util.HashMap;

public class SessionMap extends HashMap {

	private static final long serialVersionUID = -4702185515740658324L;

	private static long counter = (long) 0;

	private String sessionID;

	public SessionMap() {
		long count = getCount();
		this.sessionID = "sessionMapID-" + count;
		this.put("sessionID", "sessionMapID-" + count);
	}

	private long getCount() {
		synchronized (SessionMap.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}