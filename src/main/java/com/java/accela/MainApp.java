/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.accela;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Matthieu Roscio
 */
public class MainApp {

    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("pers");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public static void main(String[] args) {

        Program p = new Program();
        p.compute();
//        Person p1 = new Person("al", "capone");
//        p1.setId(99);
//
//        tx.begin();
//        em.persist(p1);
//        tx.commit();
//        em.close();

    }
}
