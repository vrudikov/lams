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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/**
 * @hibernate.class
 */
public abstract class ComplexActivity extends Activity implements Serializable {


    protected ComplexActivityStrategy activityStrategy;
	/** persistent field */
	private Set activities;

	/** full constructor */
	public ComplexActivity(
			Long activityId,
			Integer id,
			String description,
			String title,
			Integer xcoord,
			Integer ycoord,
			Integer orderId,
			Boolean defineLater,
			Date createDateTime,
			LearningLibrary learningLibrary,
			Activity parentActivity,
			Activity libraryActivity,
			Integer parentUIID,
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId, 
			Transition transitionTo,
			Transition transitionFrom,
			String languageFile,
			Set activities) {
		super(activityId, id, description, title, xcoord, ycoord, orderId,
				defineLater, createDateTime, 
				learningLibrary, parentActivity, libraryActivity,parentUIID,learningDesign, grouping,
				activityTypeId, transitionTo,transitionFrom, languageFile);
		this.activities = activities;
	}

	/** default constructor */
	public ComplexActivity() {
		super();
	}

	/** minimal constructor */
	public ComplexActivity(
			Long activityId,
			Boolean defineLater,
			Date createDateTime,
			LearningLibrary learningLibrary,
			Activity parentActivity,
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId, 
			Transition transitionTo,
			Transition transitionFrom,Set activities) {
		super(activityId, defineLater, createDateTime, learningLibrary,
				parentActivity, learningDesign, grouping, activityTypeId,
				transitionTo,transitionFrom);
		this.activities = activities;
	}

	public String toString() {
		return new ToStringBuilder(this).append("activityId", getActivityId())
				.toString();
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan" sort="org.lamsfoundation.lams.learningdesign.ActivityOrderComparator"
	 * @hibernate.collection-key column="parent_activity_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.Activity"
	 *  
	 */
	public Set getActivities() {
		if(this.activities==null){
	        setActivities(new TreeSet(new ActivityOrderComparator()));
	    }	    
		return this.activities;
	}

	public void setActivities(Set activities) {
	    this.activities=activities;
	}
	public void addActivity(Activity activity){
		this.activities.add(activity);
	}
	
	/**
	 * Return the requested child activity based on the id.
	 * @param activityId the requested activity id.
	 * @return the child activity.
	 */
	public Activity getChildActivityById(long activityId)
	{
	    for(Iterator i = this.activities.iterator();i.hasNext();)
	    {
	        Activity child = (Activity)i.next();
	        if(child.getActivityId().longValue()==activityId)
	            return child;
	    }
	    return new NullActivity();
	}
    
	/**
	 * Delegate to activity strategy to check up the status of all children.
	 * 
     * @param learnerProgress the progress data that record what has been 
     * 						  completed
     * @return true if all children are completed.

	 */
	public boolean areChildrenCompleted(LearnerProgress learnerProgress)
	{
	    return activityStrategy.areChildrenCompleted(learnerProgress);
	}
	
	/**
	 * <p>Delegate to activity strategy to calculate what is the next activity
	 * within the parent activity.</p>
	 * 
	 * <b>Note:</b> The logic of what is the next activity here is progress
	 * 				enigne specific now. Please see the <code>ActivityStrategy</code>
	 * 				for details explaination of what is next.
	 * 
	 * @return the next activity within a parent activity
	 */
	public Activity getNextActivityByParent(Activity currentChild)
	{
	    return activityStrategy.getNextActivityByParent(this, currentChild);
	}	
	
	/** 
	 * Get all the tool activities in this activity. Called by Activity.getAllToolActivities().
	 * Recursively get tool activity from its children.
	 */
	protected void getToolActivitiesInActivity(SortedSet toolActivities) {
		
        for(Iterator i = this.getActivities().iterator();i.hasNext();) {
            Activity child = (Activity)i.next();
            child.getToolActivitiesInActivity(toolActivities);
        }
	    
	}
	
	public Set<AuthoringActivityDTO> getAuthoringActivityDTOSet()
	{
		Set<AuthoringActivityDTO> dtoSet = new TreeSet<AuthoringActivityDTO>(new ActivityDTOOrderComparator());
		dtoSet.add(new AuthoringActivityDTO(this)); //add parent activity
		
		//add the DTO for all child activities
		for(Iterator i = this.getActivities().iterator();i.hasNext();) {
            Activity child = (Activity)i.next();
            dtoSet.add(new AuthoringActivityDTO(child));
		}
		
		return dtoSet;
	}
}