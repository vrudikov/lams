<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 		if (mode == "learner") {
			// if this is learner mode, then we add this validation see (LDEV-1319)
		
			if (document.learningForm.entryText.value == "") {
				
				// if the input is blank, then we further inquire to make sure it is correct
				if (confirm("<fmt:message>message.learner.blank.input</fmt:message>"))  {
					// if correct, submit form
					return true;
				} else {
					// otherwise, focus on the text area
					document.learningForm.entryText.focus();
					document.getElementById("finishButton").disabled = false;
					return false;      
				}
			} else {
				// there was something on the form, so submit the form
				return true;
			}
		}
	}

-->
</script>

<div id="content">
	<h1>
		${contentDTO.title}
	</h1>

	<p>
		${contentDTO.instructions}
	</p>

	<html:link href="${conferenceURL}" target="_blank">
		Open Conference ///
	</html:link>	


	<c:if test="${contentDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	&nbsp;

	<html:form action="/learning" method="post"
		onsubmit="return validateForm();">
		<html:hidden property="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />

		<c:set var="lrnForm"
			value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">

				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message key="button.finish" />
					</html:submit>
				</div>

			</c:when>

			<c:otherwise>
				<lams:out value="${lrnForm.entryText}" />
			</c:otherwise>
		</c:choose>

	</html:form>
</div>
