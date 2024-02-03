package edu.baylor.cs.se.hibernate.services;

import edu.baylor.cs.se.hibernate.model.Person;
import edu.baylor.cs.se.hibernate.model.Team;
import edu.baylor.cs.se.hibernate.model.Contest;
import edu.baylor.cs.se.hibernate.services.PersonRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

//Spring annotations, feel free to ignore it
@Repository
@Transactional
public class SuperRepository {
    @PersistenceContext
    private EntityManager em;

    public void populate() {
       
        // Contest manager
        Person manager = new Person();
        manager.setName("Manager");
        manager.setEmail("manager@manager.com");
        manager.setBirthdate(new Date(-1000, 12, 15));
        manager.setUniversity("Manager University");
        em.persist(manager);
    
        Contest mainContest = createContest("Main Contest", 1000, new Date(), true, new Date(), new Date());
        em.persist(mainContest);
    
        Contest subContest = createContest("Sub Contest", 500, new Date(), true, new Date(), new Date());
        subContest.setParentContest(mainContest);
        subContest.getManagers().add(manager);
        em.persist(subContest);
    
        // Teams
        Team team1 = createTeam("Team 1", 1, "State 1");
        team1.setContest(subContest);
        Team team2 = createTeam("Team 2", 2, "State 2");
        team2.setContest(subContest);
        Team team3 = createTeam("Team 3", 3, "State 3");
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
    
    private Team createTeam(String name,int rank,String state){
        Team team = new Team();

        team.setName(name);
        team.setRank(rank);
        team.setState(state);

        em.persist(team);
        return team;
    }

    private Contest createContest(String name, int capacity,Date date, boolean registrationAllowed, Date registrationFrom, Date registrationTo){
        Contest contest = new Contest();
        
        contest.setName(name);
        contest.setCapacity(capacity);
        contest.setDate(date);
        contest.setRegistrationAllowed(registrationAllowed);
        contest.setRegistrationFrom(registrationFrom);
        contest.setRegistrationTo(registrationTo);

        em.persist(contest);
        return contest;
    }
 
}



/*
 * 
 *    @PersistenceContext
    private EntityManager em;

    

    public void populate(){
        Student student = createStudent("Joe");
        Student student2 = createStudent("John");
        Student student3 = createStudent("Bob");
        Student student4 = createStudent("Tim");
        Student student5 = createStudent("Jimmy");


        Teacher teacher = new Teacher();
        teacher.setFirstName("Bob");
        teacher.setLastName("Porter");
        teacher.setEmail("bob@porter.com");
        em.persist(teacher);

        Course course = new Course();
        course.setName("Software engineering");
        course.setTeacher(teacher);
        course.getStudents().add(student);
        course.getStudents().add(student3);
        course.getStudents().add(student4);
        em.persist(course);

        Course course2 = new Course();
        course2.setName("Boring class");
        course2.setTeacher(teacher);
        course2.getStudents().add(student);
        course2.getStudents().add(student5);
        em.persist(course2);
       
        //Do you know why this is not working????
        course.getStudents().add(student2); // have to do it on course as that is the main (recall student had the mapped)
        em.persist(course);

    }

    public List<Course> getCoursesBySize(){
        return em.createQuery("SELECT c FROM Course c WHERE c.students.size > 2 ").getResultList();
    }



    public List<Course> getCoursesByStudentName(String studentName){
        return em.createNamedQuery("Course.findCoursesByStudentName").setParameter("name",studentName).getResultList();
    }

    private Student createStudent(String name){
        Student student = new Student();
        student.setName(name);
        em.persist(student);
        return student;
    }
*/