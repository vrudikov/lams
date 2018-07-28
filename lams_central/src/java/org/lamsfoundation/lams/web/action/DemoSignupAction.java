package org.lamsfoundation.lams.web.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.signup.service.ISignupService;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DemoSignupAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(DemoSignupAction.class);
    private static ISignupService signupService = null;
    private static ITimezoneService timezoneService = null;
    private static MessageService messageService = null;
    private static IUserManagementService userManagementService;
    private static IMonitoringService monitoringService;

    private static List<SupportedLocale> locales = new ArrayList<>();
    private final static Long[] DEFAULT_LEARNING_DESIGNS = { 1l, 2l, 3l };
    private final static String[] LAST_NAMES = { "Hanson", "George", "Hernandez", "Drake", "Vargas", "Knight", "Hicks",
	    "Bailey", "Collins", "Chambers", "Rogers", "Bryant", "Day", "Sanders", "Weaver", "Romero", "Osborne",
	    "Reid", "Matthews", "Dennis", "Barker", "Mullins", "Nash", "Perry", "Sparks", "Phelps", "Potter", "Pena",
	    "Hall", "Patrick", "Andrews", "Allison", "Brock", "Walsh", "Sims", "Haynes", "Bass", "Clayton",
	    "Williamson", "Riley", "Payne", "Spencer", "Mendez", "Martinez", "Allen", "Rivera", "Ellis", "Horton",
	    "Campbell", "Wolfe", "Howard", "Bell", "Burton", "Price", "Wise", "Poole", "Stanley", "Cummings", "Ruiz",
	    "Watkins", "Simon", "Santos", "Ryan", "Klein", "Conner", "Graham", "Mcgee", "Beck", "Norton", "Walters",
	    "Sharp", "Mendoza", "Castro", "Stephens", "Tate", "Adkins", "Luna", "Palmer", "Page", "Rodriquez",
	    "Figueroa", "Jordan", "Munoz", "Lloyd", "Bryan", "Swanson", "Lucas", "Carter", "Burgess", "Patterson",
	    "Moody", "Wagner", "Floyd", "Mills", "Reed", "Holmes", "Marshall", "Curtis", "Keller", "Stokes", "Parker",
	    "Roberts", "Brady", "Marsh", "Walker", "Morgan", "Fox", "Scott", "Jackson", "Frazier", "Carroll", "Vaughn",
	    "Soto", "Watson", "Kennedy", "Rodgers", "Carson", "Herrera", "Garner", "Sandoval", "Nelson", "Mcguire",
	    "Ramos", "Hale", "Crawford", "Harmon", "Delgado", "Diaz", "Saunders", "Willis", "Lewis", "Barnett",
	    "Schneider", "Wong", "Owen", "Gibson", "Graves", "Shaw", "Jennings", "Hodges", "Little", "Erickson",
	    "Bates", "Stewart", "Meyer", "Reynolds", "Hill", "Berry", "Cross", "Norris", "Nichols", "Wallace",
	    "Copeland", "Barrett", "Rose", "Steele", "Houston", "Richardson", "Dixon", "Murray", "Sutton", "Wood",
	    "Anderson", "Ramsey", "Lamb", "Hoffman", "Clarke", "Colon", "Craig", "Franklin", "Powers", "Rios", "Powell",
	    "Estrada", "Gardner", "Chapman", "Lyons", "Shelton", "Valdez", "Blair", "Ortega", "Maxwell", "Weber",
	    "Norman", "Thomas", "Miles", "Roy", "Holloway", "Moore", "Barnes", "Freeman", "Lopez", "Fuller", "Hart",
	    "Castillo", "Cox", "Welch", "Fletcher", "Wells", "Wright", "Mcdonald", "Mack", "Boone", "Tran", "Gilbert",
	    "Stevens", "Cole", "Hardy", "Mccarthy", "King", "Wade", "Benson", "Long", "Carr", "Park", "Fields",
	    "Flowers", "Hunter", "Griffith", "Huff", "Warren", "Perkins", "Aguilar", "Collier", "Salazar", "Patton",
	    "Goodman", "Rodriguez", "Cooper", "Ray", "Porter", "Olson", "Adams", "Bush", "Gordon", "Vasquez", "Guzman",
	    "Medina", "Armstrong", "Jones", "Coleman", "Reese", "Santiago", "Caldwell", "Padilla", "Robbins", "Cortez",
	    "Barton", "Hubbard", "Glover", "Holt", "Harper", "Harrison", "Myers", "Phillips", "Robinson", "Morrison",
	    "West", "Mcbride", "Smith", "Daniels", "Ward", "Mccormick", "Ford", "Roberson", "Gross", "Webb", "Gonzalez",
	    "Wheeler", "Sherman", "Love", "Lane", "Baker", "Lowe", "French", "Schwartz", "Gill", "Wilkins", "Paul",
	    "Ferguson", "Hopkins", "Owens", "Blake", "Cunningham", "Thornton", "Torres", "Jefferson", "Gray", "Casey",
	    "Mcdaniel", "Nguyen", "Peters", "Nunez", "Pierce", "Lawson", "Bradley", "Gomez", "Alexander", "Bowers",
	    "Gutierrez", "Ramirez", "Turner", "Reyes", "Reeves", "Brown", "Kelley", "Richards", "Briggs", "Ortiz",
	    "Neal", "Barber", "Silva", "Hampton", "Pittman", "Garcia", "Terry", "Baldwin", "Hamilton", "Russell",
	    "Williams", "Schultz", "Cannon", "Grant", "Townsend", "Summers", "Miller", "Ingram", "Burns", "Kim",
	    "Bennett", "Cruz", "Murphy", "Washington", "Tyler", "Bowman", "Burke", "Bridges", "Jacobs", "Carlson",
	    "Leonard", "Greene", "Farmer", "Goodwin", "Maldonado", "Larson", "Arnold", "Holland", "Logan", "Evans",
	    "Byrd", "Carpenter", "Watts", "Harris", "Schmidt", "Cain", "Morton", "Hogan", "Rhodes", "Morris",
	    "Mckenzie", "Frank", "Underwood", "Jimenez", "Daniel", "Sullivan", };
    private final static String[] MALE_NAMES = { "Elias", "Wilfred", "Jared", "Timmy", "Antonio", "Clint", "Carl",
	    "Elmer", "Emanuel", "Ronnie", "Bill", "Dustin", "John", "Wm", "Duane", "Pat", "Cody", "Harold", "Derek",
	    "Louis", "Willard", "Courtney", "Lawrence", "Jordan", "Timothy", "Jacob", "Norman", "Luther", "Lloyd",
	    "Wallace", "Jonathan", "Vincent", "Darrin", "Andy", "Jamie", "Shawn", "Pedro", "Orlando", "Fred",
	    "Francisco", "Marlon", "Phil", "Virgil", "Geoffrey", "Marshall", "Roberto", "Arthur", "Clarence", "Alan",
	    "Alonzo", "Javier", "Kent", "Lionel", "Chester", "Martin", "Myron", "Mathew", "Alexander", "Zachary",
	    "Benny", "Victor", "Randolph", "Ignacio", "Ross", "Noel", "Francis", "Alejandro", "Dwight", "Byron",
	    "Caleb", "Marty", "Julio", "Sheldon", "Gilbert", "Tyler", "Irvin", "Scott", "Lucas", "Hugo", "Dave", "Rudy",
	    "Vernon", "Bobby", "Ernest", "Eduardo", "Albert", "Keith", "Kirk", "Chris", "Matt", "Lamar", "Seth", "Wade",
	    "Levi", "Joel", "Gabriel", "Steve", "Todd", "Charlie", "Brent", "Andrew", "Ramiro", "Rodney", "Corey",
	    "Robin", "James", "Bruce", "Leland", "Maurice", "Garry", "Steven", "Warren", "Oliver", "Gregg", "Damon",
	    "Cameron", "Brandon", "Tyrone", "Richard", "Horace", "Matthew", "Joshua", "Neal", "Rogelio", "Homer", "Lee",
	    "Donnie", "Peter", "Gregory", "Gerald", "Troy", "Winston", "Leroy", "Joe", "Clyde", "Saul", "Curtis",
	    "Enrique", "Larry", "Roosevelt", "Jerry", "Leonard", "Jermaine", "Tony", "Terry", "Jason", "Felix", "Edwin",
	    "Brendan", "Randy", "Gordon", "Arnold", "Simon", "Roy", "Santiago", "Joseph", "Pablo", "Kenneth", "Marion",
	    "Rodolfo", "Willie", "Walter", "Gilberto", "Ted", "Roman", "Alberto", "Bradford", "Sidney", "Earnest",
	    "Josh", "Loren", "Darrell", "Carlton", "Cory", "Stanley", "Adam", "Earl", "Frederick", "Clifford", "Dale",
	    "Rex", "Drew", "Nicholas", "Woodrow", "Rick", "Jesse", "Dewey", "Terrence", "Conrad", "Luis", "Lewis",
	    "Miguel", "Laurence", "Barry", "Evan", "Ramon", "Glenn", "Milton", "Jimmie", "Shane", "Rudolph", "Randal",
	    "Franklin", "Jeffrey", "Kurt", "Ricardo", "Kristopher", "Rene", "Kelvin", "Colin", "Manuel", "Jessie",
	    "Kevin", "Merle", "Spencer", "Daryl", "Ivan", "Dominic", "Austin", "Christian", "Ervin", "Robert", "Ed",
	    "Andre", "Billy", "Santos", "Otis", "Emilio", "Bryant", "Jeremiah", "Oscar", "Grant", "Danny", "Toby",
	    "Kim", "Terrance", "Emmett", "Ismael", "Phillip", "Erik", "Sammy", "Alfonso" };
    private final static String[] FEMALE_NAMES = { "Robyn", "Dorothy", "Olga", "Lorene", "Traci", "Jeannette", "Gloria",
	    "Bethany", "Elisa", "Dianna", "Frances", "Lorraine", "Sara", "Susie", "Mary", "Audrey", "Chelsea", "Becky",
	    "Rachael", "Shelly", "Miriam", "Lynda", "Jean", "Sonya", "Lee", "Lindsay", "Nichole", "Geraldine",
	    "Rosemarie", "Ebony", "Juanita", "Kathryn", "Brooke", "Ora", "Antonia", "Pauline", "Mable", "Jackie",
	    "Jody", "Gayle", "Felicia", "Linda", "Alberta", "Olivia", "Barbara", "Robin", "Lucia", "Sabrina", "Leah",
	    "Edna", "Estelle", "Tanya", "Olive", "Cristina", "June", "Roxanne", "Agnes", "Sheryl", "Mona", "Lorena",
	    "Eva", "Martha", "Heather", "Celia", "Theresa", "Mae", "Katherine", "Teresa", "Melanie", "Marian",
	    "Adrienne", "Karen", "Joyce", "Kayla", "Leigh", "Ashley", "Mamie", "Meredith", "Rebecca", "Geneva", "Leona",
	    "Jeannie", "Carla", "Marsha", "Claudia", "Yvonne", "Darla", "Gretchen", "Christie", "Debbie", "Della",
	    "Edith", "Elizabeth", "Jessie", "Crystal", "Anne", "Lynn", "Stacy", "Faye", "Laura", "Juana", "May",
	    "Marianne", "Lucy", "Sadie", "Faith", "Nicole", "Kerry", "Amy", "Mattie", "Norma", "Beth", "Lynette",
	    "Camille", "Ginger", "Shirley", "Tami", "Fannie", "Opal", "Marie", "Lela", "Belinda", "Joanna", "Vickie",
	    "Lora", "Ethel", "Lisa", "Anna", "Mabel", "Brandi", "Holly", "Jodi", "Christine", "Jo", "Jane", "Margarita",
	    "Viola", "Veronica", "Hope", "Shawna", "Emily", "Dixie", "Vera", "Molly", "Candice", "Yolanda", "Erma",
	    "Ruby", "Muriel", "Alexandra", "Irma", "Samantha", "Alicia", "Cheryl", "Maureen", "Winifred", "Alyssa",
	    "Bernice", "Kathleen", "April", "Patsy", "Florence", "Ramona", "Amelia", "Cora", "Peggy", "Nora", "Irene",
	    "Mildred", "Kristin", "Antoinette", "Lola", "Krystal", "Silvia", "Vicky", "Jacquelyn", "Elsa", "Judy",
	    "Amber", "Shelia", "Johanna", "Jenna", "Janet", "Carrie", "Beulah", "Daisy", "Marguerite", "Lula",
	    "Lillian", "Genevieve", "Lynne", "Delia", "Victoria", "Maryann", "Angelica", "Kara", "Nettie", "Laurie",
	    "Flora", "Inez", "Rachel", "Gwendolyn", "Julia", "Rose", "Pat", "Rochelle", "Verna", "Bertha", "Iris",
	    "Nellie", "Sharon", "Ann", "Janice", "Cathy", "Gina", "Joan", "Elaine", "Renee", "Casey", "Bernadette",
	    "Rita", "Rosie", "Lila", "Latoya", "Valerie", "Paula", "Lillie", "Priscilla", "Patty", "Maria", "Margaret",
	    "Jamie", "Deanna", "Catherine", "Delores", "Katie", "Mandy", "Julie", "Diana", "Rosa", "Sandy", "Eunice", };

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (signupService == null || timezoneService == null || messageService == null
		|| userManagementService == null || monitoringService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    signupService = (ISignupService) wac.getBean("signupService");
	    timezoneService = (ITimezoneService) wac.getBean("timezoneService");
	    messageService = (MessageService) wac.getBean("centralMessageService");
	    userManagementService = (IUserManagementService) wac.getBean("userManagementService");
	    monitoringService = (IMonitoringService) wac.getBean("monitoringService");

	    //hardcoded list of supported locales
	    locales.add((SupportedLocale) userManagementService.findById(SupportedLocale.class, 1));
	    locales.add((SupportedLocale) userManagementService.findById(SupportedLocale.class, 2));
	    locales.add((SupportedLocale) userManagementService.findById(SupportedLocale.class, 8));
	    locales.add((SupportedLocale) userManagementService.findById(SupportedLocale.class, 17));
	    Collections.sort(locales);
	}

	// no context and unsubmitted form means it's the initial request
	return mapping.findForward("signup");
    }

    public ActionForward verifyEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm signupForm = (DynaActionForm) form;

	// validation
	ActionMessages errors = validateSignup(signupForm);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return mapping.findForward("signup");
	}

	User user = new User();
	String email = signupForm.getString("email");
	user.setLogin(email);
	user.setEmail(email);
	user.setTimeZone(timezoneService.getServerTimezone().getTimezoneId());
	String salt = HashUtil.salt();
	user.setSalt(salt);
	user.setPassword(HashUtil.sha256(signupForm.getString("password"), salt));
	user.setEmailVerified(false);
	user.setDisabledFlag(true);
	user.setTheme(userManagementService.getDefaultTheme());
	AuthenticationMethod authMethod = (AuthenticationMethod) userManagementService.findById(AuthenticationMethod.class, AuthenticationMethod.DB);
	user.setAuthenticationMethod(authMethod);
	user.setCreateDate(new Date());
	userManagementService.saveUser(user);

	//send verification email
	try {
	    String hash = HashUtil.sha256(user.getEmail(), user.getSalt());
	    String link = Configuration.get(ConfigurationKeys.SERVER_URL) + "signup/demoSignup.do?method=confirmEmail&login="
		    + URLEncoder.encode(user.getLogin(), "UTF-8") + "&hash=" + hash;
	    String subject = messageService.getMessage("signup.email.verify.subject");
	    String body = messageService.getMessage("signup.email.verify.body", new Object[] { link });
	    boolean isHtmlFormat = true;
	    Emailer.sendFromSupportEmail(subject, user.getEmail(), body, isHtmlFormat);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}
	
	request.setAttribute("email", user.getEmail());
	request.setAttribute("isVerificationSent", true);
	return mapping.findForward("emailVerificationResult");
    }

    private ActionMessages validateSignup(DynaActionForm signupForm) {
	ActionMessages errors = new ActionMessages();

	//password validation
	if (StringUtils.isBlank(signupForm.getString("password"))) {
	    errors.add("password", new ActionMessage("error.password.blank"));
	} else if (!StringUtils.equals(signupForm.getString("password"), signupForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("error.passwords.unequal"));
	} else if (!ValidationUtil.isPasswordValueValid(signupForm.getString("password"),
		signupForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("label.password.restrictions"));
	}

	//user email validation
	String userEmail = (signupForm.get("email") == null) ? null : (String) signupForm.get("email");
	if (StringUtils.isBlank(userEmail)) {
	    errors.add("email", new ActionMessage("error.email.blank"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errors.add("email", new ActionMessage("error.email.invalid.format"));
	}

	return errors;
    }
    
    /**
     * Checks whether entered email is unique in a DB. It is used for validation issues during sign up.
     */
    public ActionForward isEmailUnique(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, IOException, JSONException {
	
	// check user doesn't already exist
	String email = WebUtil.readStrParam(request, "email");
	User user = userManagementService.getUserByLogin(email);
	boolean isUnique = (user == null);
	
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("isUnique", isUnique);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(jsonObject);
	return null;
    }

    public ActionForward completeSignup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm signupForm = (DynaActionForm) form;

	//update user properties with the ones from the form
	String login = signupForm.getString("email");
	User user = signupService.getUserByLogin(login);
	String firstName = signupForm.getString("firstName");
	user.setFirstName(firstName);
	user.setLastName(signupForm.getString("lastName"));
	user.setCountry(signupForm.getString("country"));
	SupportedLocale locale = (SupportedLocale) userManagementService.findById(SupportedLocale.class,
		(Integer) signupForm.get("localeId"));
	user.setLocale(locale);
	userManagementService.saveUser(user);
	
	//create user's group
	Organisation org = new Organisation();
	org.setName(firstName + " Course demo");
	org.setDescription("Auto generated by LAMS");
	org.setOrganisationState(
		(OrganisationState) userManagementService.findById(OrganisationState.class, OrganisationState.ACTIVE));
	org.setEnableCourseNotifications(true);
	org.setParentOrganisation(userManagementService.getRootOrganisation());
	org.setOrganisationType((OrganisationType) userManagementService.findById(OrganisationType.class,
		OrganisationType.COURSE_TYPE));
	org.setChildOrganisations(new HashSet());
	userManagementService.saveOrganisation(org, user.getUserId());

	//add user roles
	ArrayList<String> rolesList = new ArrayList<String>();
	rolesList.add(Role.ROLE_LEARNER.toString());
	rolesList.add(Role.ROLE_MONITOR.toString());
	rolesList.add(Role.ROLE_AUTHOR.toString());
	rolesList.add(Role.ROLE_GROUP_MANAGER.toString());
	userManagementService.setRolesForUserOrganisation(user, org.getOrganisationId(), rolesList);

	//create initial 6 students
	List<User> learnerListPlusTeacher = new LinkedList<User>();
	ArrayList<String> studentsCredentials = new ArrayList<String>();
	int loginIncrement = 0;
	for (int i = 1; i <= 6; i++) {
	    //come up with the new login name
	    loginIncrement++;
	    while (userManagementService.getUserByLogin(loginIncrement+login) != null) {
		loginIncrement++;
	    }
	    String studentLogin = loginIncrement+login;
	    
	    User student = new User();
	    student.setLogin(studentLogin);
	    student.setFirstName(signupForm.getString("firstName" + i));
	    student.setLastName(signupForm.getString("lastName" + i));
	    student.setDisabledFlag(false);
	    student.setCreateDate(new Date());
//	    student.setEmail(userForm.getString("email"));
	    student.setCountry(signupForm.getString("country"));
	    student.setTimeZone(user.getTimeZone());
	    student.setLocale(locale);
	    student.setTheme(userManagementService.getDefaultTheme());
	    AuthenticationMethod authMethod = (AuthenticationMethod) userManagementService
		    .findById(AuthenticationMethod.class, AuthenticationMethod.DB);
	    student.setAuthenticationMethod(authMethod);
	    String salt = HashUtil.salt();
	    student.setSalt(salt);
	    String studentPassword = HashUtil.sha1("" + new Date().getTime()).substring(0, 6);
	    student.setPassword(HashUtil.sha256(studentPassword, salt));
	    userManagementService.saveUser(student);

	    //add user roles
	    ArrayList<String> studentRoles = new ArrayList<String>();
	    studentRoles.add(Role.ROLE_LEARNER.toString());
	    userManagementService.setRolesForUserOrganisation(student, org.getOrganisationId(), studentRoles);
	    
	    studentsCredentials.add(studentLogin);
	    studentsCredentials.add(studentPassword);
	    learnerListPlusTeacher.add(student);
	}
	learnerListPlusTeacher.add(user);
	
	//instantiate default lessons
	for (Long learningDesignId : DEFAULT_LEARNING_DESIGNS) {
	    String lessonName = null;
	    String description = null;
	    Boolean enableLessonIntro = false;
	    Boolean displayDesignImage = false;
	    Boolean learnerPresenceAvailable = false;
	    Boolean learnerImAvailable = false;
	    Boolean liveEditEnabled = true;
	    Boolean enableNotifications = false;
	    Boolean forceLearnerRestart = false;
	    Boolean allowLearnerRestart = false;
	    Boolean gradebookOnComplete = false;
	    Integer numberDaysToLessonFinish = null;
	    Long precedingLessonId = null;
	    Lesson lesson = monitoringService.initializeLesson(lessonName, description, learningDesignId,
		    org.getOrganisationId(), user.getUserId(), null, enableLessonIntro, displayDesignImage,
		    learnerPresenceAvailable, learnerImAvailable, liveEditEnabled, enableNotifications,
		    forceLearnerRestart, allowLearnerRestart, gradebookOnComplete, numberDaysToLessonFinish,
		    precedingLessonId);
	    Long lessonId = lesson.getLessonId();

	    // Create Lesson class
	    String learnerGroupName = "learnerGroupName";
	    String staffGroupName = "staffGroupName";
	    List<User> staffList = new LinkedList<User>();
	    staffList.add(user);
	    monitoringService.createLessonClassForLesson(lessonId, org, learnerGroupName, learnerListPlusTeacher,
		    staffGroupName, staffList, user.getUserId());

	    monitoringService.startLesson(lessonId, user.getUserId());
	}
	    
	//send welcome email
	try {
	    //add required label parameters
	    studentsCredentials.add(0, Configuration.get(ConfigurationKeys.SERVER_URL));
	    studentsCredentials.add(0, user.getLogin());
	    
	    String subject = messageService.getMessage("signup.email.welcome.subject");
	    String body = messageService.getMessage("demo.signup.email.welcome.body", studentsCredentials.toArray());
	    boolean isHtmlFormat = true;
	    Emailer.sendFromSupportEmail(subject, user.getEmail(), body, isHtmlFormat);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}
	
	request.setAttribute("isAccountActivated", true);
	return mapping.findForward("emailVerificationResult");
    }

    /**
     * Checks whether incoming hash is the same as the expected hash for email verification
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public ActionForward confirmEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
	String login = WebUtil.readStrParam(request, "login", false);
	String hash = WebUtil.readStrParam(request, "hash", false);
	User user = userManagementService.getUserByLogin(login);

	//check whether user is already activated
	if (user.getDisabledFlag().booleanValue()) {
	    boolean verified = signupService.emailVerify(login, hash);
	    if (verified) {
		prepareAddLearnersPage(mapping, form, request);
		return mapping.findForward("addLearners");

	    } else {
		request.setAttribute("isVerificationFailed", true);
		return mapping.findForward("emailVerificationResult");
	    }
	    
	} else {
	    request.setAttribute("isAccountActivated", true);
	    return mapping.findForward("emailVerificationResult");
	}

    }
    
    private void prepareAddLearnersPage(ActionMapping mapping, ActionForm form, HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException {
	String login = WebUtil.readStrParam(request, "login");
	User user = userManagementService.getUserByLogin(login);

	DynaActionForm userForm = (DynaActionForm) form;
	userForm.set("email", user.getEmail());

	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(true));

	    Random rand = new Random(System.currentTimeMillis());
	for (int i = 1; i <= 6; i++) {
	    //pick a random element
	    
	    String lastName = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
	    String firstName = (i % 2) == 0 ? MALE_NAMES[rand.nextInt(MALE_NAMES.length)]
		    : FEMALE_NAMES[rand.nextInt(FEMALE_NAMES.length)];
	    
	    userForm.set("lastName" + i, lastName);
	    userForm.set("firstName" + i, firstName);
	}
	
	userForm.set("localeId", 1);
	request.setAttribute("locales", locales);
    }
}
