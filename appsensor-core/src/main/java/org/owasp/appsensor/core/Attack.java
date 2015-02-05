package org.owasp.appsensor.core;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.owasp.appsensor.core.util.DateUtils;

/**
 * An attack can be added to the system in one of two ways: 
 * <ol>
 * 		<li>Analysis is performed by the event analysis engine and determines an attack has occurred</li>
 * 		<li>Analysis is performed by an external system (ie. WAF) and added to the system.</li>
 * </ol>
 * 
 * The key difference between an {@link Event} and an {@link Attack} is that an {@link Event}
 * is "suspicous" whereas an {@link Attack} has been determined to be "malicious" by some analysis.
 *
 * @author John Melton (jtmelton@gmail.com) http://www.jtmelton.com/
 */
@Entity
public class Attack implements Serializable {

	private static final long serialVersionUID = 7231666413877649836L;

	@Id
	@Column
	@GeneratedValue
	private Integer id;
	
	/** User who triggered the attack, could be anonymous user */
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	/** Detection Point that was triggered */
	@ManyToOne(cascade = CascadeType.ALL)
	private DetectionPoint detectionPoint;
	
	/** When the attack occurred */
	@Column
	private String timestamp;

	/** 
	 * Identifier label for the system that detected the attack. 
	 * This will be either the client application, or possibly an external 
	 * detection system, such as syslog, a WAF, network IDS, etc.  */
	@Column
	private String detectionSystemId; 
	
	/** 
	 * The resource being requested when the attack was triggered, which can be used 
     * later to block requests to a given function. 
     */
	@ManyToOne(cascade = CascadeType.ALL)
    private Resource resource;
	
    public Attack () { }

    public Attack (User user, DetectionPoint detectionPoint, String detectionSystemId) {
		this(user, detectionPoint, DateUtils.getCurrentTimestampAsString(), detectionSystemId);
	}
	
	public Attack (User user, DetectionPoint detectionPoint, String timestamp, String detectionSystemId) {
		setUser(user);
		setDetectionPoint(detectionPoint);
		setTimestamp(timestamp);
		setDetectionSystemId(detectionSystemId);
	}
	
	public Attack (User user, DetectionPoint detectionPoint, String timestamp, String detectionSystemId, Resource resource) {
		setUser(user);
		setDetectionPoint(detectionPoint);
		setTimestamp(timestamp);
		setDetectionSystemId(detectionSystemId);
		setResource(resource);
	}
	
	public Attack (Event event) {
		setUser(event.getUser());
		setDetectionPoint(event.getDetectionPoint());
		setTimestamp(event.getTimestamp());
		setDetectionSystemId(event.getDetectionSystemId());
		setResource(event.getResource());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public Attack setUser(User user) {
		this.user = user;
		return this;
	}
	
	public DetectionPoint getDetectionPoint() {
		return detectionPoint;
	}

	public Attack setDetectionPoint(DetectionPoint detectionPoint) {
		this.detectionPoint = detectionPoint;
		return this;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Attack setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	public String getDetectionSystemId() {
		return detectionSystemId;
	}

	public Attack setDetectionSystemId(String detectionSystemId) {
		this.detectionSystemId = detectionSystemId;
		return this;
	}

	public Resource getResource() {
		return resource;
	}

	public Attack setResource(Resource resource) {
		this.resource = resource;
		return this;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).
				append(user).
				append(detectionPoint).
				append(timestamp).
				append(detectionSystemId).
				append(resource).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Attack other = (Attack) obj;
		
		return new EqualsBuilder().
				append(user, other.getUser()).
				append(detectionPoint, other.getDetectionPoint()).
				append(timestamp, other.getTimestamp()).
				append(detectionSystemId, other.getDetectionSystemId()).
				append(resource, other.getResource()).
				isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).
			       append("user", user).
			       append("detectionPoint", detectionPoint).
			       append("timestamp", timestamp).
			       append("detectionSystemId", detectionSystemId).
			       append("resource", resource).
			       toString();
	}

}
