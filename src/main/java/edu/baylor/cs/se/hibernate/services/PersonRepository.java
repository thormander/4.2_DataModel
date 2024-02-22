package edu.baylor.cs.se.hibernate.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.baylor.cs.se.hibernate.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;



@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}

