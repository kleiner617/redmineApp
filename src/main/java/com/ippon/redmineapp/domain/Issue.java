package com.ippon.redmineapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "done_ratio")
    private String doneRatio;

    @Column(name = "estimated_hours")
    private String estimatedHours;

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    @Column(name = "story_points")
    private String storyPoints;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Tracker tracker;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Priority priority;

    @ManyToOne
    private Author author;

    @ManyToOne
    private FixedVersion fixedVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Issue subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public Issue description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Issue startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public String getDoneRatio() {
        return doneRatio;
    }

    public Issue doneRatio(String doneRatio) {
        this.doneRatio = doneRatio;
        return this;
    }

    public void setDoneRatio(String doneRatio) {
        this.doneRatio = doneRatio;
    }

    public String getEstimatedHours() {
        return estimatedHours;
    }

    public Issue estimatedHours(String estimatedHours) {
        this.estimatedHours = estimatedHours;
        return this;
    }

    public void setEstimatedHours(String estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public Issue createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getUpdatedOn() {
        return updatedOn;
    }

    public Issue updatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getStoryPoints() {
        return storyPoints;
    }

    public Issue storyPoints(String storyPoints) {
        this.storyPoints = storyPoints;
        return this;
    }

    public void setStoryPoints(String storyPoints) {
        this.storyPoints = storyPoints;
    }

    public Project getProject() {
        return project;
    }

    public Issue project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public Issue tracker(Tracker tracker) {
        this.tracker = tracker;
        return this;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public Status getStatus() {
        return status;
    }

    public Issue status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Issue priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Author getAuthor() {
        return author;
    }

    public Issue author(Author author) {
        this.author = author;
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public FixedVersion getFixedVersion() {
        return fixedVersion;
    }

    public Issue fixedVersion(FixedVersion fixedVersion) {
        this.fixedVersion = fixedVersion;
        return this;
    }

    public void setFixedVersion(FixedVersion fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Issue issue = (Issue) o;
        if(issue.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, issue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Issue{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", description='" + description + "'" +
            ", startDate='" + startDate + "'" +
            ", doneRatio='" + doneRatio + "'" +
            ", estimatedHours='" + estimatedHours + "'" +
            ", createdOn='" + createdOn + "'" +
            ", updatedOn='" + updatedOn + "'" +
            ", storyPoints='" + storyPoints + "'" +
            '}';
    }
}
