package edu.baylor.cs.se.hibernate.controller;

import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.model.Contest;
import edu.baylor.cs.se.hibernate.services.PersonRepository;
import edu.baylor.cs.se.hibernate.services.SuperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


//Ignore this as it is Spring and not Java EE (Jax-RS) controller
@RestController
public class MyController {

    private SuperRepository superRepository;

    //you should generally favour constructor/setter injection over field injection
    @Autowired
    public MyController(SuperRepository superRepository, PersonRepository personRepository){
        this.superRepository = superRepository;
    }

    @RequestMapping(value = "/populate", method = RequestMethod.POST)
    public ResponseEntity populate(){
        superRepository.populate();
        return new ResponseEntity(HttpStatus.OK);
    }

    // Task 1
    // passing in data through JSON body in postman
    @PostMapping(value = "/contestregistration/{contestId}")
    public ResponseEntity<Team> registerTeamToContest(@PathVariable Long contestId, @RequestBody Team team) {
        Team registeredTeam = superRepository.registerTeamToContest(contestId, team);
        return new ResponseEntity<>(registeredTeam, HttpStatus.CREATED);
    }

    // Task 2
    // passing in data through JSON body in postman
    @PostMapping(value = "/editTeam/{teamId}")
    public ResponseEntity<Team> editTeam(@PathVariable Long teamId, @RequestBody Team newTeamEdit) {
        Team existingTeam = superRepository.findTeamById(teamId);
        
        existingTeam.setName(newTeamEdit.getName());
        existingTeam.setRank(newTeamEdit.getRank());
        existingTeam.setState(newTeamEdit.getState());
        existingTeam.setTeamState(newTeamEdit.getTeamState());
        
        Team editedTeam = superRepository.editTeam(existingTeam);
        
        return new ResponseEntity<>(editedTeam, HttpStatus.OK);
    }
    
    // passing in data through JSON body in postman
    @PostMapping(value = "/editContest/{contestId}")
    public ResponseEntity<Contest> editContest(@PathVariable Long contestId, @RequestBody Contest newContestEdit) {
        Contest existingContest = superRepository.findContestById(contestId);
        
        existingContest.setName(newContestEdit.getName());
        existingContest.setCapacity(newContestEdit.getCapacity());
        existingContest.setDate(newContestEdit.getDate());
        
        Contest editedContest = superRepository.updateContest(existingContest);
        
        return new ResponseEntity<>(editedContest, HttpStatus.OK);
    }

    @PostMapping(value = "/setEditable/{contestId}")
    public ResponseEntity<Contest> setEditable(@PathVariable Long contestId) {
        Contest contest = superRepository.findContestById(contestId);

        contest.setEditable(true);
        Contest updatedContest = superRepository.updateContest(contest);

        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    @PostMapping(value = "/setReadOnly/{contestId}")
    public ResponseEntity<Contest> setReadOnly(@PathVariable Long contestId) {

        Contest contest = superRepository.findContestById(contestId);
        contest.setEditable(false);
        Contest updatedContest = superRepository.updateContest(contest);

        return new ResponseEntity<>(updatedContest, HttpStatus.OK);
    }

    // Task 3
    // we pass in the contest that will be 'super contest' and the team that gets promoted
    @PostMapping(value = "/promote/{contestId}/{teamId}")
    public ResponseEntity<?> promoteTeamToContest(@PathVariable Long contestId, @PathVariable Long teamId) {
        Contest contest = superRepository.findContestById(contestId);
        if (contest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
        if (contest.getTeams().size() >= contest.getCapacity()) {
            System.out.println("Not enough capacity");
            return new ResponseEntity<>( HttpStatus.FORBIDDEN);
        }
    
        Team originalTeam = superRepository.findTeamById(teamId);
        if (originalTeam == null) {
            System.out.println("Team not there");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
        if (originalTeam.getCoach() == null || originalTeam.getMembers().size() != 3) {
            System.out.println("No coach or not enough members");
            return new ResponseEntity<>( HttpStatus.FORBIDDEN); 
        }
    
        if (originalTeam.getMembers().stream().anyMatch(member -> member.getTeams().stream().anyMatch(team -> team.getContest().equals(contest)))) {
            System.out.println("Someone is part of another team");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    
        Team clonedTeam = cloneTeam(originalTeam);
        clonedTeam.setContest(contest);
        contest.getTeams().add(clonedTeam);
        Team savedTeam = superRepository.saveTeam(clonedTeam);
    
        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
    }
    
    private Team cloneTeam(Team originalTeam) {
        Team clonedTeam = new Team();
        clonedTeam.setId(null); // Setting id null
    
        // Clone team details
        clonedTeam.setName(originalTeam.getName());
        clonedTeam.setRank(originalTeam.getRank());
        clonedTeam.setState(originalTeam.getState());
        clonedTeam.setTeamState(originalTeam.getTeamState());
        clonedTeam.setCoach(originalTeam.getCoach()); 
        clonedTeam.setMembers(new HashSet<>(originalTeam.getMembers()));
        return clonedTeam;
    }
    
    
    

}