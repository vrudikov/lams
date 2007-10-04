<%@ page import="java.util.*,
                java.util.Date,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.course.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.persist.navigation.CourseTocDbLoader,
                blackboard.db.*,
                blackboard.base.*,
                org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil,
                blackboard.platform.*,
                blackboard.platform.plugin.*"
	errorPage="/error.jsp"
%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">

<%
	String lsid = request.getParameter("lsid");
	String learnerUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + lsid;
	String monitorUrl = LamsSecurityUtil.generateRequestURL(ctx, "monitor") + "&lsid=" + lsid;
	String liveEditUrl = LamsSecurityUtil.generateRequestURL(ctx, "author");
	
//	 add port to the url if the port is in the blackboard url.
	int bbport = request.getServerPort();
	String bbportstr = bbport != 0 ? ":" + bbport : "";
	
	//String contentUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + learningSessionId;
	String updateGradesUrl = "\"" + request.getScheme()
									+ "://" +
									request.getServerName() + 
								    bbportstr +
									request.getContextPath() + 
									"/modules/updateGrades.jsp?lsid=" + lsid + 
									"&course_id=" + request.getParameter("course_id") +
									"&lineitem_id=" + request.getParameter("lineitem_id")
									+ "\"";
	

	String course_idstr = request.getParameter("course_id");	

	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	Id course_id = bbPm.generateId(Course.COURSE_DATA_TYPE, course_idstr);
	User sessionUser = ctx.getUser();
	Id sessionUserId = sessionUser.getId();
	

	//get the membership data to determine the User's Role
	CourseMembership courseMembership = null;
	CourseMembership.Role courseRole = null;
	CourseMembershipDbLoader sessionCourseMembershipLoader =
		(CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
	try 
	{  
		courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
		courseRole = courseMembership.getRole();
	} 
	catch (KeyNotFoundException e) 
	{
		// There is no membership record.
		e.printStackTrace();
		
	}
	catch (PersistenceException pe) 
	{
		// There is no membership record.
		pe.printStackTrace();
	}


	String instructorstr="hidden";
	if (courseRole.equals(CourseMembership.Role.INSTRUCTOR)||courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)) 
	{
		// instructor or assistant
		// can choose to redirect to monitor or learner
		instructorstr="button";
	}
	else if (!courseRole.equals(CourseMembership.Role.STUDENT))
	{

		response.sendRedirect("notAllowed.jsp");
	}
%>

<bbUI:docTemplate>
<head>
	<link type="text/css" rel="stylesheet" href="css/bb.css" />
	<script language="JavaScript" type="text/javascript">
		<!--
			var learnerWin = null;
			var monitorWin = null;
			var liveEditUrl= null;
			var learnerUrl = null;
			var monitorUrl = null;
			
			function back()
			{
				history.go(-1);
			}
			
			function openLearner()
			{
				learnerUrl = '<%=learnerUrl%>'; 
		    	if(learnerWin && learnerWin.open && !learnerWin.closed){
		            try {
		            	learnerWin.focus();
		            }catch(e){
		            	// popups blocked by a 3rd party
		            }
		        }
		        else{
		            try {
			            learnerWin = window.open(learnerUrl,'lWin','width=800,height=600,resizable=1');
			            learnerWin.focus();
			        }catch(e){
		            	// popups blocked by a 3rd party
		            }
		        }
			}
			
			function openMonitor()
			{
				monitorUrl = '<%=monitorUrl%>'; 
		    	if(monitorWin && monitorWin.open && !monitorWin.closed){
		    	
		            try {
		            monitorWin.focus();
		            }catch(e){
		            	// popups blocked by a 3rd party
		            }
		        }
		        else{
		            try {
		            monitorWin = window.open(monitorUrl,'aWin','width=800,height=600,resizable=1');
		            monitorWin.opener = self;
		            monitorWin.focus();
		            }catch(e){
		            	// popups blocked by a 3rd party
		            }
		            
		        }
			}
			
			function updateGrades()
			{
				window.location = <%=updateGradesUrl%>
			}
			
			function openAuthorForEditOnFly( learningDesignID )
			{				
				liveEditUrl= '<%=liveEditUrl%>' + '&layout=editonfly&learningDesignID=' + learningDesignID;
				if(monitorWin && !monitorWin.closed)
				{
					monitorWin.location = liveEditUrl;
				}
				else
				{
					monitorWin = window.open(liveEditUrl,'width=800,height=600,resizable');
					monitorWin.focus();
				}
			}
			
			
		//-->
</script>
</head>




<bbUI:breadcrumbBar handle="control_panel" isContent="true" >
    <bbUI:breadcrumb>LAMS Options</bbUI:breadcrumb>
</bbUI:breadcrumbBar>

<bbUI:titleBar iconUrl ="/images/ci/icons/bookopen_u.gif">LAMS Options</bbUI:titleBar>


<form name="workspace_form" id="workspace_form" method="post">
	<br>
	<b>Please Choose an Option</b>
	<br><br>
	&nbsp&nbsp&nbsp&nbsp
	<input type="button" class="button" name="OpenLearner" onClick="openLearner();" value="Open Lesson">
	<input type="<%=instructorstr%>" class="button" name="OpenMonitor" onClick="openMonitor();" value="Open Monitor">
	<input type="<%=instructorstr%>" class="button" name="UpdateGrades" onClick="updateGrades();" value="Update Progress">
	<input type="button" class="button" name="Cancel" onClick="back();" value="Cancel">

</form>
	
</bbUI:docTemplate>						  
</bbData:context>
