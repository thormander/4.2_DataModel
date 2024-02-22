package edu.baylor.cs.se.hibernate.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int rank;
    private String state;

    @Enumerated(EnumType.STRING)
    private TeamState teamState; 
    @ManyToMany
    @JoinTable(
            name = "TEAM_MEMBER",
            joinColumns = { @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID") }
    )
    private Set<Person> members = new HashSet<>();

    @ManyToOne
    private Contest contest;

    @ManyToOne
    private Person coach;

    // Self-loop for Clone
    @OneToOne
    @JoinColumn(name = "clone_of_id")
    private Team cloneOf;

    @OneToOne(mappedBy = "cloneOf")
    private Team clone;

    // Constructors, getters, and setters

    public Team() {
        
    }

    public Team(String name, int rank, String state) {
        this.name = name;
        this.rank = rank;
        this.state = state;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<Person> getMembers() {
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Person getCoach() {
        return coach;
    }

    public void setCoach(Person coach) {
        this.coach = coach;
        coach.getCoachedTeams().add(this);
    }

    public TeamState getTeamState() {
        return teamState;
    }

    public void setTeamState(TeamState teamState) {
        this.teamState = teamState;
    }

    public enum TeamState {
        ACCEPTED, PENDING, CANCELED
    }
}
