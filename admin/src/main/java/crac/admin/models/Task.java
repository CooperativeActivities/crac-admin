package crac.admin.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Task {
	
	@Id
	private Long id;
	
	private String name;

	private String description;

	private String location;
	
	private Date startTime;
	
	private Date endTime;
	
	private int urgency;
	
	private int amountOfVolunteers;
	
	private String feedback;
	
	private Long superProjectId;

	public Task(String name, String description, String location, Date startTime, Date endTime,
			int urgency, int amountOfVolunteers, String feedback) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.urgency = urgency;
		this.amountOfVolunteers = amountOfVolunteers;
		this.feedback = feedback;
	}
	
	public Task(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getUrgency() {
		return urgency;
	}

	public void setUrgency(int urgency) {
		this.urgency = urgency;
	}

	public int getAmountOfVolunteers() {
		return amountOfVolunteers;
	}

	public void setAmountOfVolunteers(int amountOfVolunteers) {
		this.amountOfVolunteers = amountOfVolunteers;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


	public Long getSuperProjectId() {
		return superProjectId;
	}

	public void setSuperProjectId(long superProjectId) {
		this.superProjectId = superProjectId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	
	
}
