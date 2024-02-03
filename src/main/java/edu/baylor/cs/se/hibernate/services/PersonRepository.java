package edu.baylor.cs.se.hibernate.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;



@Repository
public class PersonRepository {
    private EntityManager entityManager;

    public PersonRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Object[]> groupByAgeAndCount() {
        String sql = "SELECT YEAR(CURRENT_DATE) - YEAR(p.birthdate) AS age, COUNT(*) " +
                     "FROM PERSON p " +
                     "GROUP BY YEAR(CURRENT_DATE) - YEAR(p.birthdate)";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
