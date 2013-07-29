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
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;

public class Organisation implements Serializable, Comparable {

    private static final long serialVersionUID = -6742443056151585129L;

    /** identifier field */
    private Integer organisationId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String code;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Organisation parentOrganisation;

    /** persistent field */
    private Date createDate;

    /** persistent field */
    private User createdBy;

    /** persistent field */
    private Workspace workspace;

    /** persistent field */
    private OrganisationType organisationType;

    /** persistent field */
    private Set userOrganisations;

    /** persistent field */
    private Set childOrganisations;

    /** persistent field */
    private Set lessons;

    private OrganisationState organisationState;

    private SupportedLocale locale;

    /** persistent field */
    private Boolean courseAdminCanAddNewUsers;

    /** persistent field */
    private Boolean courseAdminCanBrowseAllUsers;

    /** persistent field */
    private Boolean courseAdminCanChangeStatusOfCourse;

    /** persistent field */
    private Boolean courseAdminCanCreateGuestAccounts;

    /** persistent field */
    private Boolean enableCourseNotifications;

    /** persistent field */
    private Boolean enableGradebookForMonitors;

    /** persistent field */
    private Boolean enableGradebookForLearners;
    
    /** persistent field */
    private Boolean enableSingleActivityLessons;

    /** persistent field */
    private Date archivedDate;

    private String orderedLessonIds;

    /** full constructor */
    public Organisation(String name, String description, Organisation parentOrganisation, Date createDate,
	    User createdBy, Workspace workspace, OrganisationType organisationType, Set userOrganisations,
	    Set childOrganisations, Set lessons, Boolean courseAdminCanAddNewUsers,
	    Boolean courseAdminCanBrowseAllUsers, Boolean courseAdminCanChangeStatusOfCourse,
	    Boolean courseAdminCanCreateGuestAccounts, Boolean enableCourseNotifications, String orderedLessonIds,
	    Boolean enableGradebookForLearners, Boolean enableGradebookForMonitors) {
	this.name = name;
	this.description = description;
	this.parentOrganisation = parentOrganisation;
	this.createDate = createDate;
	this.createdBy = createdBy;
	this.workspace = workspace;
	this.organisationType = organisationType;
	this.userOrganisations = userOrganisations;
	this.childOrganisations = childOrganisations;
	this.lessons = lessons;
	this.courseAdminCanAddNewUsers = courseAdminCanAddNewUsers;
	this.courseAdminCanBrowseAllUsers = courseAdminCanBrowseAllUsers;
	this.courseAdminCanChangeStatusOfCourse = courseAdminCanChangeStatusOfCourse;
	this.courseAdminCanCreateGuestAccounts = courseAdminCanCreateGuestAccounts;
	this.enableCourseNotifications = enableCourseNotifications;
	this.orderedLessonIds = orderedLessonIds;
	this.enableGradebookForLearners = enableGradebookForLearners;
	this.enableGradebookForMonitors = enableGradebookForMonitors;
    }

    /** default constructor */
    public Organisation() {
	this.courseAdminCanAddNewUsers = Boolean.FALSE;
	this.courseAdminCanBrowseAllUsers = Boolean.FALSE;
	this.courseAdminCanChangeStatusOfCourse = Boolean.FALSE;
	this.courseAdminCanCreateGuestAccounts = Boolean.FALSE;
	this.enableCourseNotifications = Boolean.FALSE;
	this.enableGradebookForLearners = Boolean.FALSE;
	this.enableGradebookForMonitors = Boolean.FALSE;
    }

    /** minimal constructor */
    public Organisation(Date createDate, User createdBy, Workspace workspace, OrganisationType organisationType,
	    Set userOrganisations, Set lessons) {
	this.createDate = createDate;
	this.createdBy = createdBy;
	this.workspace = workspace;
	this.organisationType = organisationType;
	this.userOrganisations = userOrganisations;
	this.lessons = lessons;

	// mandatory fields in the db
	this.courseAdminCanAddNewUsers = Boolean.FALSE;
	this.courseAdminCanBrowseAllUsers = Boolean.FALSE;
	this.courseAdminCanChangeStatusOfCourse = Boolean.FALSE;
	this.courseAdminCanCreateGuestAccounts = Boolean.FALSE;
	this.enableCourseNotifications = Boolean.FALSE;
	this.enableGradebookForLearners = Boolean.FALSE;
	this.enableGradebookForMonitors = Boolean.FALSE;
    }

    public Organisation(String name, String description, Date createDate, User createdBy,
	    OrganisationType organisationType) {
	super();
	this.name = name;
	this.description = description;
	this.createDate = createDate;
	this.createdBy = createdBy;
	this.organisationType = organisationType;
    }

    public Integer getOrganisationId() {
	return this.organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Organisation getParentOrganisation() {
	return this.parentOrganisation;
    }

    public void setParentOrganisation(Organisation parentOrganisation) {
	this.parentOrganisation = parentOrganisation;
    }

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public User getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(User createdBy) {
	this.createdBy = createdBy;
    }

    public Workspace getWorkspace() {
	return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
	this.workspace = workspace;
    }

    public OrganisationType getOrganisationType() {
	return this.organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
	this.organisationType = organisationType;
    }

    public Set getUserOrganisations() {
	return this.userOrganisations;
    }

    public void setUserOrganisations(Set userOrganisations) {
	this.userOrganisations = userOrganisations;
    }

    public Set getChildOrganisations() {
	return childOrganisations;
    }

    public void setChildOrganisations(Set childOrganisations) {
	this.childOrganisations = childOrganisations;
    }

    public Set getLessons() {
	return this.lessons;
    }

    public void setLessons(Set lessons) {
	this.lessons = lessons;
    }

    public OrganisationState getOrganisationState() {
	return this.organisationState;
    }

    public void setOrganisationState(OrganisationState organisationState) {
	this.organisationState = organisationState;
    }

    public Boolean getCourseAdminCanAddNewUsers() {
	return this.courseAdminCanAddNewUsers;
    }

    public void setCourseAdminCanAddNewUsers(Boolean courseAdminCanAddNewUsers) {
	this.courseAdminCanAddNewUsers = courseAdminCanAddNewUsers;
    }

    public Boolean getCourseAdminCanBrowseAllUsers() {
	return this.courseAdminCanBrowseAllUsers;
    }

    public void setCourseAdminCanBrowseAllUsers(Boolean courseAdminCanBrowseAllUsers) {
	this.courseAdminCanBrowseAllUsers = courseAdminCanBrowseAllUsers;
    }

    public Boolean getCourseAdminCanChangeStatusOfCourse() {
	return this.courseAdminCanChangeStatusOfCourse;
    }

    public void setCourseAdminCanChangeStatusOfCourse(Boolean courseAdminCanChangeStatusOfCourse) {
	this.courseAdminCanChangeStatusOfCourse = courseAdminCanChangeStatusOfCourse;
    }

    public Boolean getCourseAdminCanCreateGuestAccounts() {
	return this.courseAdminCanCreateGuestAccounts;
    }

    public void setCourseAdminCanCreateGuestAccounts(Boolean courseAdminCanCreateGuestAccounts) {
	this.courseAdminCanCreateGuestAccounts = courseAdminCanCreateGuestAccounts;
    }

    public Boolean getEnableCourseNotifications() {
	return this.enableCourseNotifications;
    }

    public void setEnableCourseNotifications(Boolean enableCourseNotifications) {
	this.enableCourseNotifications = enableCourseNotifications;
    }

    public String toString() {
	return new ToStringBuilder(this).append("organisationId", getOrganisationId()).toString();
    }

    public boolean equals(Object other) {
	if (!(other instanceof Organisation))
	    return false;
	Organisation castOther = (Organisation) other;
	return new EqualsBuilder().append(this.getOrganisationId(), castOther.getOrganisationId()).isEquals();
    }

    public int hashCode() {
	return new HashCodeBuilder().append(getOrganisationId()).toHashCode();
    }

    public OrganisationDTO getOrganisationDTO() {
	return new OrganisationDTO(this);
    }

    public SupportedLocale getLocale() {
	return locale;
    }

    public void setLocale(SupportedLocale locale) {
	this.locale = locale;
    }

    public int compareTo(Object o) {
	return name.compareToIgnoreCase(((Organisation) o).getName());
    }

    public Date getArchivedDate() {
	return this.archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
	this.archivedDate = archivedDate;
    }

    public String getOrderedLessonIds() {
	return orderedLessonIds;
    }

    public void setOrderedLessonIds(String orderedLessonIds) {
	this.orderedLessonIds = orderedLessonIds;
    }

    public Boolean getEnableGradebookForMonitors() {
	return enableGradebookForMonitors;
    }

    public void setEnableGradebookForMonitors(Boolean enableGradebookForMonitors) {
	this.enableGradebookForMonitors = enableGradebookForMonitors;
    }

    public Boolean getEnableGradebookForLearners() {
	return enableGradebookForLearners;
    }

    public void setEnableGradebookForLearners(Boolean enableGradebookForLearners) {
	this.enableGradebookForLearners = enableGradebookForLearners;
    }

    public Boolean getEnableSingleActivityLessons() {
        return enableSingleActivityLessons;
    }

    public void setEnableSingleActivityLessons(Boolean enableSingleActivityLessons) {
        this.enableSingleActivityLessons = enableSingleActivityLessons;
    }
}