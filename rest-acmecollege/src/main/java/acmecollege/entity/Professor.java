/***************************************************************************
 * File:  Professor.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")

/**
 * The persistent class for the professor database table.
 */
@Entity 
@Table(name = "professor") 
@NamedQuery(name = "Professor.findAll", query = "SELECT p FROM Professor p left join fetch p.courseRegistrations")
@AttributeOverride(name = "id", column = @Column(name = "professor_id"))

public class Professor extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Basic(optional = false)
	@Column(name = "department", nullable = false, length = 50)
	private String department;

	
	@Transient
	private String highestEducationalDegree; // Examples:  BS, MS, PhD, DPhil, MD, etc.
	
	@Transient
	private String specialization; // Examples:  Computer Science, Mathematics, Physics, etc.

	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "professor")
	
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public Professor() {
		super();
	}
	
	public Professor(String firstName, String lastName, String department, Set<CourseRegistration> courseRegistrations) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.courseRegistrations = courseRegistrations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHighestEducationalDegree() {
		return highestEducationalDegree;
	}

	public void setHighestEducationalDegree(String highestEducationalDegree) {
		this.highestEducationalDegree = highestEducationalDegree;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setProfessor(String firstName, String lastName, String department) {
		setFirstName(firstName);
		setLastName(lastName);
		setDepartment(department);
	}

	//Inherited hashCode/equals is sufficient for this entity class

}
