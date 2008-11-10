<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script type="text/javascript">
	   <%-- user for  rsrcresourceitem.js --%>
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
       var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
	</script>
		<script type="text/javascript"
			src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>

	</lams:head>
	<body class="tabpart">

		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post"
			styleId="resourceItemForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="instructionList" id="instructionList" />
			<input type="hidden" name="itemType" id="itemType" value="4" />
			<html:hidden property="itemIndex" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.learning.object" />
			</h2>
			
			<div class="field-name">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<html:text property="title" size="55" tabindex="1" />

			<%--  Remove description in as LDEV-617
							<tr>
								<td width="130px">
									<fmt:message key="label.authoring.basic.resource.description.input" />
								</td>
								<td>
									<lams:STRUTS-textarea rows="5" cols="55" tabindex="2" property="description" />
								</td>
							</tr>
							--%>
			
			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.zip.file.input" />
			</div>

			<c:set var="itemAttachment"
				value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div id="itemAttachmentArea">
				<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
			</div>

		</html:form>
		<!-- Instructions -->

		<%@ include file="instructions.jsp"%>

		
		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitResourceItem()" class="button-add-item"><fmt:message
					key="label.authoring.basic.add.learning.object" /> </a>
			<a href="javascript:;" onclick="cancelResourceItem()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		<br />
	</body>
</lams:html>
