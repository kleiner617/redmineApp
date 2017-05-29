package com.ippon.redmineapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FixedVersion.
 */
@Entity
@Table(name = "fixed_version")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FixedVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "fixedVersion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Issue> issues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FixedVersion name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public FixedVersion issues(Set<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public FixedVersion addIssue(Issue issue) {
        issues.add(issue);
        issue.setFixedVersion(this);
        return this;
    }

    public FixedVersion removeIssue(Issue issue) {
        issues.remove(issue);
        issue.setFixedVersion(null);
        return this;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FixedVersion fixedVersion = (FixedVersion) o;
        if(fixedVersion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fixedVersion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FixedVersion{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
