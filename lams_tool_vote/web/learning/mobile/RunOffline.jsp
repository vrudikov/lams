<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" />
	</title>

	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) 
		{
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>

</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<fmt:message key="activity.title" />
		</h1>
	</div><!-- /header -->

	<html:form action="/learning?validate=false" method="POST">
		<html:hidden property="dispatch" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="userID" />

		<div data-role="content">
			<c:choose>
				<c:when test="${empty submissionDeadline}">
					<p>
						<fmt:message key="label.learning.forceOfflineMessage" />
					</p>
				</c:when>
				<c:otherwise>
					<div class="warning">
						<fmt:message key="authoring.info.teacher.set.restriction" >
							<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
						</fmt:message>
					</div>
				</c:otherwise>														
			</c:choose>
		</div>
	</html:form>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
				<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true'}">
				      <a href="#nogo" name="endLearning" id="finishButton" 
				      onclick="javascript:submitMethod('learnerFinished');return false" data-role="button" data-icon="arrow-r" data-theme="b">
					<span class="nextActivity"><fmt:message key="label.finished" /></span>
				      </a>
				</c:if>

				<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}">
					<button name="forwardtoReflection"
						onclick="javascript:submitMethod('forwardtoReflection');"  data-icon="arrow-r" data-theme="b">
						<fmt:message key="label.continue" />
					</button>
				</c:if>
		</span>
	</div><!-- /footer -->

</div>
</body>
</lams:html>








