<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
		<c:set var="questionType" value="2"	scope="request" />
		
		<style type="text/css">
			label { width: 10em; float: left; }
			label.error { float: none; color: red; padding-left: .5em; vertical-align: top; font-weight: bold; font-style: italic;}
			em { font-weight: bold; padding-right: 1em; vertical-align: top; }
		</style>

		<script type="text/javascript">
			var questionType = ${questionType};
			var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
		   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
    	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
    	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentoption.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.validate.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.form.js'/>"></script>
  	    <script><!--
			$(document).ready(function(){
				
		    	$("#assessmentQuestionForm").validate({
		    		rules: {
		    			title: "required",
		    			defaultGrade: {
		    			      required: true,
		    			      digits: true
		    			},
		    			penaltyFactor: {
		    			      required: true,
		    			      number: true
		    			},
		    			fake: {
		    				required: function(element) {
		    		        	return $("input[name^=optionQuestion]:filled").length < 1;
			    		    }
	    			    }
		    		},
		    		messages: {
		    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			defaultGrade: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
		    			},
		    			penaltyFactor: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				number: "<fmt:message key='label.authoring.choice.enter.float'/>"
		    			}
		    		},
		    		debug: true,
     			    submitHandler: function(form) {
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#question").val(FCKeditorAPI.GetInstance("question").GetXHTML());
		    			$("#generalFeedback").val(FCKeditorAPI.GetInstance("generalFeedback").GetXHTML());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#questionListArea'), 
		    		   		success: afterRatingSubmit  // post-submit callback
		    		    }; 				
		    		    				
		    			$('#assessmentQuestionForm').ajaxSubmit(options);
		    		}
		  		});
			});
    		// post-submit callback 
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox()
    			self.parent.tb_remove();
    		}    
  		--></script>
		
		
	</lams:head>
	
	<body class="stripes">
		<div id="content" >	
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="assessmentQuestionForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<html:hidden property="questionIndex" />
				<html:hidden property="feedbackOnCorrect" />
				<html:hidden property="feedbackOnPartiallyCorrect" />
				<html:hidden property="feedbackOnIncorrect" />
				<html:hidden property="questionIndex" />
	
				<h2 class="no-space-left">
					<fmt:message key="label.authoring.basic.type.matching.pairs" />
				</h2>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.name" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="title" size="55" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:FCKEditor id="question" value="${formBean.question}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:FCKEditor>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="defaultGrade" size="3" />
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.penalty.factor" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="penaltyFactor" size="3" />
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.general.feedback" />
				</div>
				<lams:FCKEditor id="generalFeedback" value="${formBean.generalFeedback}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:FCKEditor>				
	
				<div class="field-name space-top" >
					<fmt:message key="label.authoring.basic.shuffle.the.choices" />
				</div>
				<html:checkbox property="shuffle" styleId="shuffleTheChoices" styleClass="noBorder" />
				
				<br><br>
				<input type="hidden" name="fake" id="fake">
				<div class="field-name space-top">
					<fmt:message key="label.authoring.matching.pairs.matching.pairs" />
				</div>				
				<label for="fake" class="error" style="display: none;"><fmt:message key='label.authoring.matching.pairs.error.one.matching.pair'/></label>
			</html:form>
			
			<!-- Options -->
			<form id="optionForm" name="optionForm">
				<%@ include file="optionlist.jsp"%>
				<a href="javascript:;" onclick="addOption();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
					<fmt:message key="label.authoring.matching.pairs.add.matching.pair" /> 
				</a>
			</form>
			
			<br><br><br>
			<lams:ImgButtonWrapper>
				<a href="#" onclick="$('#assessmentQuestionForm').submit();" class="button-add-item">
					<fmt:message key="label.authoring.matching.pairs.add.matching.pairs" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" class="button space-left">
					<fmt:message key="label.cancel" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
