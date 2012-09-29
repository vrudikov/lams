<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	
	<style media="screen,projection" type="text/css">
		div.growlUI { background: url(check48.png) no-repeat 10px 10px }
		div.growlUI h1, div.growlUI h2 {
			color: white; padding: 5px 5px 5px 0px; text-align: center;
		}
	</style>
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script language="JavaScript" type="text/JavaScript">
	
		var interval = "30000"; // = 30 seconds
		
		window.setInterval(
			function(){
				//fire onchange event for lams:textarea
				$("[id$=__lamstextarea]").change();
				//ajax form submit
				$('#learningForm').ajaxSubmit({
					url: "<c:url value='/learning.do?method=autoSaveAnswers&date='/>" + new Date().getTime(),
	                success: function() {
	                	$.growlUI('<fmt:message key="label.learning.draft.autosaved" />');
	                }
				});
	    	}, interval
	    );
	
		function submitMethod(actionMethod) {
			var submit = true;
			if (actionMethod != 'getPreviousQuestion') {
				jQuery(".text-area").each(function() {
					if (jQuery.trim($(this).val()) == "") {
						if (confirm("<fmt:message key="warning.empty.answers" />")) {
							doSubmit(actionMethod);
							return false;
						} else {
							this.focus();
							//return submit = false;
							return false;
						}
					}
				});
			}
			if (submit) {
				doSubmit(actionMethod);
			}
		}
		
		function doSubmit(actionMethod) {
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
	</script>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
	
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>	

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self"  styleId="learningForm">
			<c:choose>
				<c:when test="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}">
					<html:hidden property="method" value="getNextQuestion"/>
				</c:when>
				<c:otherwise>
					<html:hidden property="method" value="submitAnswersContent"/>
				</c:otherwise>
			</c:choose>
				
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="questionIndex" />
			<html:hidden property="totalQuestionCount" />

			<logic:messagesPresent>
				<p class="warning">
				  	<html:messages id="error" message="false"> 
            			<c:out value="${error}" escapeXml="false"/><BR> 
         			</html:messages> 
				</p>
			</logic:messagesPresent>

			<c:if test="${generalLearnerFlowDTO.activityOffline == 'true'}">
				<h3>
				   <fmt:message key="label.learning.forceOfflineMessage" />
				</h3>
			</c:if>

			<c:if test="${generalLearnerFlowDTO.activityOffline == 'false'}">
				<p>
				  <c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false" />
				</p>
				<c:choose>
					<c:when test="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}">

						<c:if test="${generalLearnerFlowDTO.totalQuestionCount != 1}">
							<c:if test="${generalLearnerFlowDTO.initialScreen == 'true'}">
								<p><fmt:message key="label.feedback.seq" />
									<c:out value="${generalLearnerFlowDTO.remainingQuestionCount}" />
									<fmt:message key="label.questions.simple" />
								</p>
							</c:if>
						</c:if>

						<c:if test="${generalLearnerFlowDTO.initialScreen != 'true'}">
							<p>
								<fmt:message key="label.questions.remaining" />
								<c:out value="${generalLearnerFlowDTO.remainingQuestionCount}" />
							</p>
						</c:if>

						<jsp:include page="/learning/mobile/SequentialAnswersContent.jsp" />
					</c:when>

					<c:otherwise>
						<c:if test="${generalLearnerFlowDTO.totalQuestionCount != 1}">
							<fmt:message key="label.feedback.combined" /> &nbsp <c:out
								value="${generalLearnerFlowDTO.remainingQuestionCount}" />
							<fmt:message key="label.questions.simple" />
						</c:if>						
						<jsp:include page="/learning/mobile/CombinedAnswersContent.jsp" />
					</c:otherwise>
				</c:choose>
			</c:if>

		</html:form>

</div>
</body>
</lams:html>
