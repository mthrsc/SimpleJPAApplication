/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.accela;

/**
 *
 * @author Matthieu Roscio
 */
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DBOperations {

    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("pers");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public DBOperations() {
    }

    public final long persistPerson(Person p) {
        long id = -1;
        tx.begin();
        em.persist(p);
        tx.commit();
        return p.getId();
    }

    public final Person retrieveWithID(long id) {
        Person p = new Person();
        tx.begin();
        p = em.find(Person.class, id);
        tx.commit();

        return p;
    }

    public final List retrieveWithFullName(String firstName, String lastName) {
        String queryText = "SELECT p FROM Person p WHERE p.firstName = :firstName and p.lastName = :lastName";
        Query query = em.createQuery(queryText);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        return query.getResultList();
    }

    public final List retrieveAll() {
        Query query = em.createQuery("SELECT p FROM Person p");
        return query.getResultList();
    }

    public final void deletePerson(long id) {
        tx.begin();
        Person p = em.find(Person.class, id);
        em.remove(p);
        tx.commit();
    }

    public final long countEntries() {
        Query query = em.createQuery("SELECT count(p) FROM Person p");
        long result = (long) query.getSingleResult();
        return result;
    }

    public final void closeEM() {
        em.close();
    }
}
