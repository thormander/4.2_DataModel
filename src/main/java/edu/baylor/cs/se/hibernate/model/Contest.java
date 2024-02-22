package edu.baylor.cs.se.hibernate.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "CONTEST")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean editable;

    private int capacity;

    @Temporal(TemporalType.DATE)
    private Date date;


    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Team> teams = new HashSet<>();

    // Self-loop for preliminary round
    @ManyToOne
    @JoinColumn(name = "parent_contest_id")
    private Contest parentContest;
    @OneToMany(mappedBy = "parentContest", cascade = CascadeType.ALL)
    private Set<Contest> subContests = new HashSet<>();

    // Many-to-Many relationship with Person for managing contests
    @ManyToMany
    @JoinTable(
        name = "contest_manager",
        joinColumns = @JoinColumn(name = "contest_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> managers = new HashSet<>();

    // Constructors, getters, and setters

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    // contest stuff

    public Contest getParentContest() {
        return parentContest;
    }

    public void setParentContest(Contest parentContest) {
        this.parentContest = parentContest;
    }

    public Set<Contest> getSubContests() {
        return subContests;
    }

    public void setSubContests(Set<Contest> subContests) {
        this.subContests = subContests;
    }

    public Set<Person> getManagers() {
        return managers;
    }

    public void setManagers(Set<Person> managers) {
        this.managers = managers;
    }
}
