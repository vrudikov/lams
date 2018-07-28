<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup" /></title>
	
	<lams:css/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" rel="stylesheet" />
	<style>
		.form-control-students {
			display: initial;
			width: 43%;
		}
		.form-control-my-details {
			display: initial;
			width: 49%;		
		}
		#your-students .form-group {
			display: flex;
		}
		
		#portrait-button {
			margin-left: -25px;
		}
		.help-block {
			margin-top: 0;
		}
	</style>

	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript">
		$.validator.addMethod("charactersNotAllowedName", function(value) {
			return /^[^<>^*@%$]*$/.test(value)
		});
		$.validator.addMethod("notEqualTo", function(value, element, param) {
			return this.optional(element) || value != param;
		}, "Please specify a different (non-default) value");
	
		$(function() {
			// Setup form validation 
			$("#DemoSignupForm").validate({
				debug : true,
				errorClass : 'help-block',
				//  validation rules
				rules : {
					lastName : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					email : {
						required: true
					},
					country : {
						required: true,
						notEqualTo: "0"
					},
					lastName1 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName1 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					lastName2 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName2 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					lastName3 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName3 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					lastName4 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName4 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					lastName5 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName5 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					lastName6 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					},
					firstName6 : {
						required : true,
						maxlength : 255,
						charactersNotAllowedName : true
					}
				},

				// Specify the validation error messages
				messages : {
					lastName : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					country: {
						required: "<fmt:message key='error.country.required'/>",
						notEqualTo: "<fmt:message key='error.country.required'/>"
					},
					lastName1 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName1 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					lastName2 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName2 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					lastName3 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName3 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					lastName4 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName4 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					lastName5 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName5 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					},
					lastName6 : {
						charactersNotAllowedName : "<fmt:message key='error.lastname.invalid.characters'/>"
					},
					firstName6 : {
						charactersNotAllowedName : "<fmt:message key='error.firstname.invalid.characters'/>"
					}
				},
				submitHandler : function(form) {
					form.submit();
				},
				invalidHandler : function(){
					$('#submitButton').button('reset');
				}
			});
		});
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="admin">
		<div class="page-header">
			<p class="text-center">
				<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg"
					alt="LAMS - Learning Activity Management System" width="200px"></img>
			</p>  

			<c:if test="${not empty error}">
				<lams:Alert type="danger" id="errors" close="false">
					<c:out value="${error}" />
				</lams:Alert>
			</c:if>
		</div>
		
	<form id="DemoSignupForm" name="DemoSignupForm" method="post" action="/lams/signup/demoSignup.do" novalidate="novalidate"  autocomplete="off">
		<c:set var="org.apache.struts.taglib.html.BEAN"  value="${DemoSignupForm}" />
		<html:hidden property="method" value="completeSignup" />
		
			<div class="row">
				<div class="col-md-6 col-md-offset-3">
					<div class="panel with-nav-tabs panel-default">
						<div class="panel-heading" style="height: 51px">
							<fmt:message key="label.your.details" />
						</div>
						<div class="panel-body">
						
						<div class="col-xs-2">
							<img class="portrait-sm portrait-round" src="/lams/images/css/john-doe-portrait.jpg" alt="">
							<!-- <br>
							<a id="portrait-button" class="btn btn-sm"
							    href="#" onclick="javascript:showMyPortraitDialog(); return false;">
								<fmt:message key="label.change.portrait" />
							</a> -->
						</div>
						
						<div class="col-xs-10">
							<div class="form-group">
		            	
								<input type="text" name="firstName" size="40" maxlength="255"
									class="form-control form-control-my-details" placeholder="<fmt:message key="signup.first.name" />"/>
								<html:errors property="firstName" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<input type="text" name="lastName" size="40" maxlength="255"
									class="form-control form-control-my-details" placeholder="<fmt:message key="signup.last.name" />"/>
								<html:errors property="lastName" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<c:set var="isEmailDisabled">${not empty DemoSignupForm.map.email}</c:set>
								<html:text property="email" styleId="email" size="40" maxlength="255" styleClass="form-control" 
										disabled="${isEmailDisabled}"/>
								<html:errors property="email" />
								<span style="display: none;'" class="email error"><fmt:message
										key="error.email.invalid.format" /></span>
							</div>
							
							<div class="form-group">
								<html:select property="localeId" styleClass="form-control">
									<c:forEach items="${locales}" var="locale">
										<html:option value="${locale.localeId}">
											<c:out value="${locale.description}" />
										</html:option>
									</c:forEach>
								</html:select>
							</div>
							
							<div class="form-group">
								<html:select property="country" styleClass="form-control">
									<html:option value="0">
										<fmt:message key="label.select.country" />
									</html:option>
									
									<c:forEach items="${countryCodes}" var="countryCode">
										<html:option value="${countryCode.key}">
											${countryCode.value}
										</html:option>
									</c:forEach>
								</html:select>
								<html:errors property="country" />
							</div>
						</div>
						</div>
					</div>
		
					<div class="panel with-nav-tabs panel-default">
						<div class="panel-heading" style="height: 51px">
							<fmt:message key="label.your.students" />
						</div>
						
						<div id="your-students" class="panel-body">
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName1" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName1" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName1" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName1" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName2" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName2" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName2" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName2" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName3" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName3" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName3" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName3" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName4" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName4" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName4" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName4" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName5" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName5" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName5" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName5" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
							
							<div class="form-group">
								<lams:Portrait userId="2"/>
							
								<html:text property="firstName6" size="40" maxlength="255"
									styleClass="form-control form-control-students loffset10 roffset10" />
								<html:errors property="firstName6" />
								<span style="display: none;'" class="first error"><fmt:message
										key="error.firstname.invalid.characters" /></span>

								<html:text property="lastName6" size="40" maxlength="255"
									styleClass="form-control form-control-students" />
								<html:errors property="lastName6" />
								<span style="display: none;'" class="last error"><fmt:message
										key="error.lastname.invalid.characters" /></span>
							</div>
						</div>
					</div>
				
					<div class="form-group" align="right">
						<button id="submitButton"
								class="btn btn-sm btn-default voffset5"
							    data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='login.submit' /></span>"
							    onClick="javascript:$(this).button('loading');$('#email').prop('disabled', false);$('#DemoSignupForm').submit()"
						>
							<fmt:message key="label.create.account" />
						</button>
					</div>
				
				</div>
			</div>	
		
		</form>
	</lams:Page>
</body>
</lams:html>
