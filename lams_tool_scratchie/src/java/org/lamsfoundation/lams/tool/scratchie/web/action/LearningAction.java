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

/* $Id$ */
package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieApplicationException;
import org.lamsfoundation.lams.tool.scratchie.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static IScratchieService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException,
	    ScratchieApplicationException {

	String param = mapping.getParameter();
	// -----------------------Scratchie Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("refreshQuestionList")) {
	    return refreshQuestionList(mapping, form, request, response);
	}
	if (param.equals("checkLeaderSubmittedNotebook")) {
	    return checkLeaderSubmittedNotebook(mapping, form, request, response);
	}
	if (param.equals("isAnswerCorrect")) {
	    return isAnswerCorrect(mapping, form, request, response);
	}
	if (param.equals("recordItemScratched")) {
	    return recordItemScratched(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("showResults")) {
	    return showResults(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(ScratchieConstants.ERROR);
    }

    /**
     * Read scratchie data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws ScratchieApplicationException
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	final Long toolSessionId = new Long(request.getParameter(ScratchieConstants.PARAM_TOOL_SESSION_ID));
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);
	// get back the scratchie and item list and display them on page
	final Scratchie scratchie = LearningAction.service.getScratchieBySessionId(toolSessionId);
	boolean isReflectOnActivity = scratchie.isReflectOnActivity();

	final ScratchieUser user;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // scratchieUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	final ScratchieUser groupLeader = (ScratchieUser) tryExecute(new Callable<Object>() {
	    @Override
	    public Object call() throws ScratchieApplicationException {
		return LearningAction.service.checkLeaderSelectToolForSessionLeader(user, toolSessionId);
	    }
	});

	// forwards to the leaderSelection page
	if ((groupLeader == null) && !mode.isTeacher()) {

	    // get group users and store it to request as DTO objects
	    List<ScratchieUser> groupUsers = LearningAction.service.getUsersBySession(toolSessionId);
	    List<User> groupUserDtos = new ArrayList<User>();
	    for (ScratchieUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(ScratchieConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(ScratchieConstants.PARAM_TOOL_SESSION_ID, toolSessionId);
	    request.setAttribute(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	    return mapping.findForward("waitforleader");
	}

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// get notebook entry
	NotebookEntry notebookEntry = null;
	if (isReflectOnActivity && (groupLeader != null)) {
	    notebookEntry = LearningAction.service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, groupLeader.getUserId().intValue());
	}
	String entryText = (notebookEntry == null) ? new String() : notebookEntry.getEntry();

	// basic information
	sessionMap.put(ScratchieConstants.ATTR_TITLE, scratchie.getTitle());
	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_INSTRUCTION, scratchie.getInstructions());
	sessionMap.put(ScratchieConstants.ATTR_USER_ID, user.getUserId());
	sessionMap.put(ScratchieConstants.ATTR_USER_UID, user.getUid());
	String groupLeaderName = groupLeader.getFirstName() + " " + groupLeader.getLastName();
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER_NAME, groupLeaderName);
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);
	boolean isUserFinished = (user != null) && user.isSessionFinished();
	sessionMap.put(ScratchieConstants.ATTR_USER_FINISHED, isUserFinished);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, isReflectOnActivity);
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_INSTRUCTION, scratchie.getReflectInstructions());
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (scratchie.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(
		toolSessionId, request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// check if there is submission deadline
	Date submissionDeadline = scratchie.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(ScratchieConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	// set scratched flag for display purpose
	Collection<ScratchieItem> items = LearningAction.service.getItemsWithIndicatedScratches(toolSessionId);

	// for teacher in monitoring display the number of attempt.
	if (mode.isTeacher()) {
	    LearningAction.service.getScratchesOrder(items, toolSessionId);
	}

	// calculate max score
	int maxScore = items.size() * 4;
	if (scratchie.isExtraPoint()) {
	    maxScore += items.size();
	}

	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	sessionMap.put(ScratchieConstants.ATTR_MAX_SCORE, maxScore);
	
	boolean isScratchingFinished = toolSession.isScratchingFinished();
	boolean isWaitingForLeaderToSubmitNotebook = isReflectOnActivity && (notebookEntry == null);
	boolean isShowResults = (isScratchingFinished && !isWaitingForLeaderToSubmitNotebook) && !mode.isTeacher();
	
	//show leader notebook page 
	if (isUserLeader && isScratchingFinished && isWaitingForLeaderToSubmitNotebook) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("newReflection"));
	    redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	    return redirect;
	    
	// show results page
	} else if (isShowResults) {
	
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("showResults"));
	    redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	    return redirect;
	    
	//show learning.jsp page
	} else {
	    sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, (Boolean) isScratchingFinished);
	    sessionMap.put(ScratchieConstants.ATTR_IS_WAITING_FOR_LEADER_TO_SUBMIT_NOTEBOOK,
		    (Boolean) isWaitingForLeaderToSubmitNotebook);
	    return mapping.findForward(ScratchieConstants.SUCCESS);
	}

    }

    /**
     * Refresh
     * 
     * @throws ScratchieApplicationException
     */
    private ActionForward refreshQuestionList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);

	// set scratched flag for display purpose
	Set<ScratchieItem> items = LearningAction.service.getItemsWithIndicatedScratches(toolSessionId);
	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);

	// refresh leadership status
	ScratchieUser user = this.getCurrentUser(toolSessionId);
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);

	// refresh ScratchingFinished status
	sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, toolSession.isScratchingFinished());
	
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }
    
    /**
     * Return whether leader still needs submit notebook.
     */
    private ActionForward checkLeaderSubmittedNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	initializeScratchieService();
	
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	boolean isReflectOnActivity = (Boolean) sessionMap.get(ScratchieConstants.ATTR_REFLECTION_ON);
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser groupLeader = toolSession.getGroupLeader();
	
	// get notebook entry
	NotebookEntry notebookEntry = null;
	if (isReflectOnActivity && (groupLeader != null)) {
	    notebookEntry = service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, groupLeader.getUserId().intValue());
	}
	boolean isWaitingForLeaderToSubmitNotebook = isReflectOnActivity && (notebookEntry == null);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(ScratchieConstants.ATTR_IS_WAITING_FOR_LEADER_TO_SUBMIT_NOTEBOOK, isWaitingForLeaderToSubmitNotebook);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);

	return null;
    }

    /**
     * Return whether scratchie answer is correct or not
     */
    private ActionForward isAnswerCorrect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	initializeScratchieService();

	Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));
	ScratchieAnswer answer = LearningAction.service.getScratchieAnswerByUid(answerUid);
	if (answer == null) {
	    return null;
	}

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(ScratchieConstants.ATTR_ANSWER_CORRECT, answer.isCorrect());
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);

	return null;
    }

    /**
     * Record in DB that leader has scratched specified answer.
     * 
     * @throws ScratchieApplicationException
     */
    private ActionForward recordItemScratched(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException, ScratchieApplicationException {
	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = this.getCurrentUser(toolSessionId);
	// only leader is allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	final Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));
	tryExecute(new Callable<Object>() {
	    @Override
	    public Object call() throws ScratchieApplicationException {
		LearningAction.service.recordItemScratched(toolSessionId, answerUid);
		return null;
	    }
	});
	return null;
    }

    /**
     * Displays results page. When leader gets to this page, scratchingFinished column is set to true for all users.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ScratchieApplicationException
     */
    private ActionForward showResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	boolean isReflectOnActivity = (Boolean) sessionMap.get(ScratchieConstants.ATTR_REFLECTION_ON);

	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	// in case of the leader (and if he hasn't done this when accessing notebook) we should let all other learners see Next Activity button
	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.setScratchingFinished(toolSessionId);
		    return null;
		}
	    });
	}

	// get updated score from ScratchieSession
	int score = toolSession.getMark();
	int maxScore = (Integer) sessionMap.get(ScratchieConstants.ATTR_MAX_SCORE);
	double percentage = (maxScore == 0) ? 0 : ((score * 100) / maxScore);
	request.setAttribute(ScratchieConstants.ATTR_SCORE, (int) percentage);

	// Create reflectList if reflection is enabled.
	if (isReflectOnActivity) {
	    List<ReflectDTO> reflections = LearningAction.service.getReflectionList(toolSession.getScratchie()
		    .getContentId());

	    // remove current session leader reflection
	    Iterator<ReflectDTO> refIterator = reflections.iterator();
	    while (refIterator.hasNext()) {
		ReflectDTO reflection = refIterator.next();
		if (toolSession.getSessionName().equals(reflection.getGroupName())) {
		    
		    //store for displaying purposes
		    String reflectEntry = StringEscapeUtils.unescapeJavaScript(reflection.getReflection());
		    sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, reflectEntry);
		    
		    //remove from list to display other groups' notebooks
		    refIterator.remove();
		    break;
		}
	    }

	    request.setAttribute(ScratchieConstants.ATTR_REFLECTIONS, reflections);
	}
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initializeScratchieService();
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	final Long userId = user.getUserID().longValue();

	String nextActivityUrl = null;

	try {
	    nextActivityUrl = (String) tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    return LearningAction.service.finishToolSession(toolSessionId, userId);
		}
	    });

	    request.setAttribute(ScratchieConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ScratchieApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ScratchieApplicationException 
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {

	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	ReflectionForm refForm = (ReflectionForm) form;
	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);
	
	// get the existing reflection entry
	NotebookEntry entry = LearningAction.service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}
	
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);

	// in case of the leader we should let all other learners see Next Activity button
	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.setScratchingFinished(toolSessionId);
		    return null;
		}
	    });
	}

	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database. Only leaders can submit reflections.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ScratchieApplicationException
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	ReflectionForm refForm = (ReflectionForm) form;
	final Integer userId = refForm.getUserID();
	final String entryText = refForm.getEntryText();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	final NotebookEntry entry = LearningAction.service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    ScratchieConstants.TOOL_SIGNATURE, userId, entryText);
		    return null;
		}
	    });
	} else {
	    // update existing entry
	    entry.setEntry(entryText);
	    entry.setLastModified(new Date());
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.updateEntry(entry);
		    return null;
		}
	    });
	}
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	return showResults(mapping, refForm, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    /**
     * Tries to execute supplied command. If it fails due to CannotAcquireLockException it tries again, and gives up
     * after 5 consecutive fails.
     */
    private Object tryExecute(Callable<Object> command) throws ScratchieApplicationException {
	final int MAX_TRANSACTION_RETRIES = 5;
	Object returnValue = null;

	for (int i = 0; i < MAX_TRANSACTION_RETRIES; i++) {
	    try {
		returnValue = command.call();
		break;
	    } catch (CannotAcquireLockException e) {
		if (i == (MAX_TRANSACTION_RETRIES - 1)) {
		    throw new ScratchieApplicationException(e);
		}
	    } catch (Exception e) {
		throw new ScratchieApplicationException(e);
	    }
	    LearningAction.log.warn("Transaction retry: " + (i + 1));
	}

	return returnValue;
    }

    private void initializeScratchieService() {
	if (LearningAction.service == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    LearningAction.service = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
    }

    private ScratchieUser getCurrentUser(Long sessionId) throws ScratchieApplicationException {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ScratchieUser scratchieUser = LearningAction.service.getUserByIDAndSession(user.getUserID().longValue(),
		sessionId);

	if (scratchieUser == null) {
	    ScratchieSession session = LearningAction.service.getScratchieSessionBySessionId(sessionId);
	    final ScratchieUser newScratchieUser = new ScratchieUser(user, session);
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.createUser(newScratchieUser);
		    return null;
		}
	    });

	    scratchieUser = newScratchieUser;
	}
	return scratchieUser;
    }

    private ScratchieUser getSpecifiedUser(Long sessionId, Integer userId) {
	ScratchieUser scratchieUser = LearningAction.service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (scratchieUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for scratchie activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return scratchieUser;
    }
}