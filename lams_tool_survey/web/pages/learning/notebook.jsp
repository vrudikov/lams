<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</head>
<body class="stripes">

		<c:set var="sessionMapID" value="${param.sessionMapID}"/>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		
		<html:form action="/learning/submitReflection" method="post">
			<html:hidden property="userID" />
			<html:hidden property="sessionMapID"/>
			
			<div id="content">
			<h1>
				${sessionMap.title}
			</h1>
				<table>
					<tr>
						<td>
							<%@ include file="/common/messages.jsp"%>
						</td>
					</tr>
					<tr>
						<td>
							<lams:out value="${sessionMap.reflectInstructions}"/>
						</td>
					</tr>
		
					<tr>
						<td>
							<lams:STRUTS-textarea cols="60" rows="8" property="entryText"/>
						</td>
					</tr>
		
					<tr>
						<td class="right-buttons">
							<html:submit styleClass="button">
								<fmt:message key="label.finish"/>
							</html:submit>
						</td>
					</tr>
				</table>
			</div>
		</html:form>
		
		<div id="footer">
		</div>
		<!--closes footer-->

</body>
</html:html>
