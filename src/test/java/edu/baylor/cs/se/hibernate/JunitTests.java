package edu.baylor.cs.se.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.baylor.cs.se.hibernate.model.Contest;
import edu.baylor.cs.se.hibernate.model.Person;
import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.model.Team.TeamState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
public class JunitTests {

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        populate();
    }

    // Task 2
    @Test 
    public void testSelectAllTeams() {
        @SuppressWarnings("unchecked")
        List<Team> teams = em.createNativeQuery("SELECT * FROM TEAM", Team.class).getResultList();
        
        System.out.println("----------------");
        System.out.println("Task 2");
        System.out.println("All Teams:");
        teams.forEach(team -> System.out.println(team.getName() + " | " + team.getState()));
    
        assertTrue(teams.size() == 3, "Fetch 3 teams");
        System.out.println("----------------");
    }
    

    // Task 3
    @Test
    public void groupPersonsByAgeUsingNativeQuery() {
        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createNativeQuery(
                "SELECT YEAR(CURRENT_DATE()) - YEAR(BIRTHDATE) AS AGE, COUNT(*) AS COUNT " +
                "FROM PERSON " + 
                "GROUP BY AGE")
                .getResultList();
    
        if (results.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("----------------");
            System.out.println("Task 3");
            results.forEach(result -> {
                Integer age = ((Number) result[0]).intValue();
                Long count = ((Number) result[1]).longValue(); 
                System.out.println("Age: " + age + " | Count: " + count);
            });
            System.out.println("----------------");
        }
        
    }

    // Task 4
    @Test
    public void testContestOccupancyVsCapacity() {
        Object[] mainContestResult = (Object[]) em.createNativeQuery(
                "SELECT COUNT(t.id) AS OCCUPANCY, c.CAPACITY " +
                "FROM CONTEST c " +
                "LEFT JOIN TEAM t ON c.ID = t.CONTEST_ID " +
                "WHERE c.NAME = :name " +
                "GROUP BY c.CAPACITY")
                .setParameter("name", "Main Contest")
                .getSingleResult();
    
        int mainContestOccupancy = ((Number) mainContestResult[0]).intValue();
        int mainContestTotalCapacity = ((Number) mainContestResult[1]).intValue();
        int mainContestCapacityLeft = mainContestTotalCapacity - mainContestOccupancy;
        
        System.out.println("----------------");
        System.out.println("Task 4");
        System.out.println("Main Contest: Occupancy = " + mainContestOccupancy + ", Capacity = " + mainContestTotalCapacity);
        System.out.println("Capacity Left for Main Contest: " + mainContestCapacityLeft);
    
        // Do for sub contest also
        Object[] subContestResult = (Object[]) em.createNativeQuery(
                "SELECT COUNT(t.id) AS OCCUPANCY, c.CAPACITY " +
                "FROM CONTEST c " +
                "LEFT JOIN TEAM t ON c.ID = t.CONTEST_ID " +
                "WHERE c.NAME = :name " +
                "GROUP BY c.CAPACITY")
                .setParameter("name", "Sub Contest")
                .getSingleResult();
    
        int subContestOccupancy = ((Number) subContestResult[0]).intValue();
        int subContestTotalCapacity = ((Number) subContestResult[1]).intValue();
        int subContestCapacityLeft = subContestTotalCapacity - subContestOccupancy;
        
        System.out.println("Sub Contest: Occupancy = " + subContestOccupancy + ", Capacity = " + subContestTotalCapacity);
        System.out.println("Capacity Left for Sub Contest: " + subContestCapacityLeft);
        
        assertEquals(0, mainContestOccupancy, "Main Contest occupancy should be 0.");
        assertEquals(3, subContestOccupancy, "Sub Contest occupancy should be 3.");
        System.out.println("----------------");
    }

    public void populate() {
       
        // Contest manager
        Person manager = new Person();
        manager.setName("Manager");
        manager.setEmail("manager@manager.com");
        manager.setBirthdate(new Date(-1000, 12, 15));
        manager.setUniversity("Manager University");
        em.persist(manager);
    
        Contest mainContest = createContest("Main Contest", 1000, new Date(), true);
        em.persist(mainContest);
    
        Contest subContest = createContest("Sub Contest", 500, new Date(), true);
        subContest.setParentContest(mainContest);
        subContest.getManagers().add(manager);
        em.persist(subContest);
    
        // Teams
        Team team1 = createTeam("Team 1", 1, "State 1", TeamState.ACCEPTED);
        team1.setContest(subContest);
        Team team2 = createTeam("Team 2", 2, "State 2", TeamState.PENDING);
        team2.setContest(subContest);
        Team team3 = createTeam("Team 3", 3, "State 3", TeamState.PENDING);
        team3.setContest(subContest);
    
        // Coaches
        Person coach1 = createPerson("Coach 1", "coach1@gmail.com", new Date(25, 12, 15), "University 1");
        team1.setCoach(coach1);
        Person coach2 = createPerson("Coach 2", "coach2@gmail.com", new Date(25, 12, 15), "University 2");
        team2.setCoach(coach2);
        Person coach3 = createPerson("Coach 3", "coach3@gmail.com", new Date(25, 12, 15), "University 3");
        team3.setCoach(coach3);


        // Players
        team1.getMembers().add(createPerson("Player 1 Team 1", "player1team1@gmail.com",new Date(5, 12, 15), "University 1"));
        team1.getMembers().add(createPerson("Player 2 Team 1", "player2team1@gmail.com",new Date(5, 12, 15), "University 1"));
        team1.getMembers().add(createPerson("Player 3 Team 1", "player3team1@gmail.com", new Date(5, 12, 15), "University 1"));
    
        team2.getMembers().add(createPerson("Player 1 Team 2", "player1team2@gmail.com", new Date(100, 12, 15), "University 2"));
        team2.getMembers().add(createPerson("Player 2 Team 2", "player2team2@gmail.com", new Date(100, 12, 15), "University 2"));
        team2.getMembers().add(createPerson("Player 3 Team 2", "player3team2@gmail.com", new Date(100, 12, 15), "University 2"));
    
        team3.getMembers().add(createPerson("Player 1 Team 3", "player1team3@gmail.com", new Date(50, 12, 15), "University 3"));
        team3.getMembers().add(createPerson("Player 2 Team 3", "player2team3@gmail.com", new Date(50, 12, 15), "University 3"));
        team3.getMembers().add(createPerson("Player 3 Team 3", "player3team3@gmail.com", new Date(50, 12, 15), "University 3"));
    
        // Persist 
        em.persist(team1);
        em.persist(team2);
        em.persist(team3);
    }
    
    private Person createPerson(String name,String email,Date birthdate,String university){
        Person person = new Person();

        person.setName(name);
        person.setEmail(email);
        person.setBirthdate(birthdate);
        person.setUniversity(university);

        em.persist(person);
        return person;
    }
    
    private Team createTeam(String name,int rank,String state,TeamState state2){
        Team team = new Team();

        team.setName(name);
        team.setRank(rank);
        team.setState(state);
        team.setTeamState(state2);

        em.persist(team);
        return team;
    }

    private Contest createContest(String name, int capacity,Date date,Boolean editable){
        Contest contest = new Contest();
        
        contest.setName(name);
        contest.setCapacity(capacity);
        contest.setDate(date);
        contest.setEditable(editable);

        em.persist(contest);
        return contest;
    }


}


