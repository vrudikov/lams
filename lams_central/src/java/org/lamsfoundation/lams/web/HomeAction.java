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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * this is an action where all lams client environments launch. initial
 * configuration of the individual environment setting is done here.
 * 
 * @struts:action path="/home" validate="false" parameter="method"
 * @struts:action-forward name="sysadmin" path="/sysadmin.jsp"
 * @struts:action-forward name="learner" path="/learner.jsp"
 * @struts:action-forward name="author" path="/author.jsp"
 * @struts:action-forward name="monitorLesson" path="/monitorLesson.jsp"
 * @struts:action-forward name="addLesson" path="/addLesson.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 * @struts:action-forward name="passwordChange" path=".passwordChange"
 * @struts:action-forward name="index" path="/index.jsp"
 * 
 */
public class HomeAction extends DispatchAction {

    private static Logger log = Logger.getLogger(HomeAction.class);

    private static IUserManagementService service;
    private static ILessonService lessonService;

    private IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return getService().getUserByLogin(dto.getLogin());
    }

    /**
     * request for sysadmin environment
     */
    public ActionForward sysadmin(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {

	try {
	    log.debug("request sysadmin");
	    int orgId = new Integer(req.getParameter("orgId")).intValue();
	    UserDTO user = getUser();
	    if (user == null) {
		log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else if (getService().isUserInRole(user.getUserID(), orgId, Role.SYSADMIN)) {
		log.debug("user is sysadmin");
		return mapping.findForward("sysadmin");
	    } else {
		log.error("User " + user.getLogin()
			+ " tried to get sysadmin screen but isn't sysadmin in organisation: " + orgId);
		return displayMessage(mapping, req, "error.authorisation");
	    }

	} catch (Exception e) {
	    log.error("Failed to load sysadmin", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for learner environment
     */
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	try {
	    log.debug("request learner");

	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    String mode = WebUtil.readStrParam(req, AttributeNames.PARAM_MODE, true);

	    UserDTO user = getUser();
	    if (user == null) {
		log.error("learner: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
		if (lesson == null || !lesson.isLessonStarted()) {
		    return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
		}

		if (lesson.getLessonClass() == null
			|| !lesson.getLessonClass().getLearners().contains(getRealUser(user))) {
		    log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
		}

		if (mode != null)
		    req.setAttribute(AttributeNames.PARAM_MODE, mode);

		req.setAttribute(AttributeNames.PARAM_EXPORT_PORTFOLIO_ENABLED,
			lesson.getLearnerExportAvailable() != null ? lesson.getLearnerExportAvailable() : Boolean.TRUE);
		req.setAttribute(AttributeNames.PARAM_PRESENCE_ENABLED, lesson.getLearnerPresenceAvailable());
		req.setAttribute(AttributeNames.PARAM_PRESENCE_IM_ENABLED, lesson.getLearnerImAvailable());
		req.setAttribute(AttributeNames.PARAM_TITLE, lesson.getLessonName());
		req.setAttribute(AttributeNames.PARAM_CREATE_DATE_TIME, lesson.getCreateDateTime());
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);
		String presenceUrl = Configuration.get(ConfigurationKeys.XMPP_DOMAIN);
		req.setAttribute("presenceUrl", presenceUrl);
		req.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
		return mapping.findForward("learner");
	    }

	} catch (Exception e) {
	    log.error("Failed to load learner", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for author environment
     */
    public ActionForward author(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	try {
	    log.debug("request author");
	    UserDTO user = getUser();
	    if (user == null) {
		log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Long learningDesignID = null;
		String layout = null;
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);

		String requestSrc = req.getParameter("requestSrc");
		String notifyCloseURL = req.getParameter("notifyCloseURL");
		String customCSV = req.getParameter(AttributeNames.PARAM_CUSTOM_CSV);
		String extLmsId = req.getParameter(AttributeNames.PARAM_EXT_LMS_ID);

		if (req.getParameter("learningDesignID") != null)
		    learningDesignID = WebUtil.readLongParam(req, "learningDesignID");

		if (req.getParameter("layout") != null)
		    layout = WebUtil.readStrParam(req, "layout");

		if (layout != null)
		    req.setAttribute("layout", layout);

		if (req.getParameter("learningDesignID") != null)
		    learningDesignID = WebUtil.readLongParam(req, "learningDesignID");

		if (learningDesignID != null)
		    req.setAttribute("learningDesignID", learningDesignID);

		req.setAttribute("requestSrc", requestSrc);
		req.setAttribute("notifyCloseURL", notifyCloseURL);
		req.setAttribute(AttributeNames.PARAM_CUSTOM_CSV, customCSV);
		req.setAttribute(AttributeNames.PARAM_EXT_LMS_ID, extLmsId);

		return mapping.findForward("author");
	    }

	} catch (Exception e) {
	    log.error("Failed to load author", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for monitor environment
     */
    public ActionForward monitorLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {

	try {
	    log.debug("request monitorLesson");
	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
		if (lesson == null) {
		    log.error("monitorLesson: Lesson " + lessonId + " does not exist. Unable to monitor lesson");
		    return mapping.findForward("error");
		}

		if (lesson.getLessonClass() == null || !lesson.getLessonClass().isStaffMember(getRealUser(user))) {
		    log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
		}

		log.debug("user is staff");
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);
		req.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
		return mapping.findForward("monitorLesson");
	    }
	} catch (Exception e) {
	    log.error("Failed to load monitor lesson", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for add lesson wizard
     */
    public ActionForward addLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {

	try {
	    log.debug("request addLesson");
	    
	    Integer courseId = WebUtil.readIntParam(req, AttributeNames.PARAM_COURSE_ID, false);
	    Integer classId = WebUtil.readIntParam(req, AttributeNames.PARAM_CLASS_ID, true);
	    
	    UserDTO user = getUser();
	    if (user == null) {
		log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Integer orgId = classId != null ? classId : courseId;
		if (getService().isUserInRole(user.getUserID(), orgId, Role.MONITOR)
			|| getService().isUserInRole(user.getUserID(), orgId, Role.GROUP_MANAGER)) {
		    log.debug("user is staff");
		    String orgName = ((Organisation) getService().findById(Organisation.class, orgId)).getName();
		    
		    req.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, orgId);
		    req.setAttribute(AttributeNames.PARAM_ORGANISATION_NAME, orgName);
		   
		    return mapping.findForward("addLesson");
		} else {
		    log.error("User " + user.getLogin()
			    + " tried to get staff screen but isn't staff in organisation: " + orgId);
		    return displayMessage(mapping, req, "error.authorisation");
		}
	    }

	} catch (Exception e) {
	    log.error("Failed to load add lesson", e);
	    return mapping.findForward("error");
	}
    }

    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	UserDTO userDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	if (userDTO.getLoggedIntoLamsCommunity() != null && userDTO.getLoggedIntoLamsCommunity()) {
	    log.debug("Need to log out user from lamscoomunity");
	    req.getSession().invalidate();

	    //clear system shared session.
	    SessionManager.getSession().invalidate();
	    
	    // redirect to lamscommunity logout servlet to log out.
	    String url = "http://lamscommunity.org/register/logout?return_url=";
	    url += URLEncoder.encode(Configuration.get(ConfigurationKeys.SERVER_URL), "UTF8");
	    res.sendRedirect(url);
	    return null;
	    
	} else {
	    req.getSession().invalidate();

	    //clear system shared session.
	    SessionManager.getSession().invalidate();

	    return mapping.findForward("index");
	}
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }
}