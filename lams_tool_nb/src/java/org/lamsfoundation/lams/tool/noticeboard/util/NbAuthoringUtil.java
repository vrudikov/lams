/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.noticeboard.util;

import javax.servlet.http.HttpServletRequest;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

/**
 * @author mtruong
 *
 */
public class NbAuthoringUtil {

	 private NbAuthoringUtil() {}
	 
	 /**
	  * Converts the request parameter <code>toolContentId</code>, from a string to a Long
	  * @author mtruong
	  */

	 	public static Long convertToLong(String toolContentId)
	 	{
	 		Long contentId = new Long(Long.parseLong(toolContentId));
	 		return contentId;
	 	}
	 	
	 /*
	  * Not needed anymore, using ActionForm to persist the values
	  */
	 	public static void cleanSession(HttpServletRequest request)
	 	{
	 	    request.getSession().removeAttribute(NoticeboardConstants.TOOL_CONTENT_ID);
	 	    request.getSession().removeAttribute(NoticeboardConstants.RICH_TEXT_CONTENT);
	 	    request.getSession().removeAttribute(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN);
	 	    request.getSession().removeAttribute(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN);
	 	}
}
