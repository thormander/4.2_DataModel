package edu.baylor.cs.se.hibernate.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PERSON")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    private String university;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams = new HashSet<>();

    @OneToOne(mappedBy = "coach")
    private Team coachedTeam;

    @ManyToMany(mappedBy = "managers")
    private Set<Contest> managedContests = new HashSet<>();

    // Constructors, getters, and setters

    public Person() {
        
    }

    public Person(String name, String email, Date birthdate, String university) {
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.university = university;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Team getCoachedTeam() {
        return coachedTeam;
    }

    public void setCoachedTeam(Team coachedTeam) {
        this.coachedTeam = coachedTeam;
    }

    public Set<Contest> getManagedContests() {
        return managedContests;
    }

    public void setManagedContests(Set<Contest> managedContests) {
        this.managedContests = managedContests;
    }
}
