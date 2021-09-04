/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.tests;

import com.java.accela.DBOperations;
import com.java.accela.Person;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Matthieu Roscio
 */
@Disabled
public class UnitTest {

    long id;

    @AfterAll
    public static void close() {
        DBOperations dbo = new DBOperations();
        dbo.closeEM();
    }

    @Test
    @DisplayName("Add, retrieve(ID) and delete person")
    void addDeletePerson() {
        DBOperations dbo = new DBOperations();
        long id = -1;

        assertTrue((id = dbo.persistPerson(new Person("Junit", "Person"))) > -1);

        Person p1 = dbo.retrieveWithID(id);

        assertNotNull(p1);

        Person p2 = new Person();
        dbo.deletePerson(id);
        p2 = dbo.retrieveWithID(id);

        assertNull(p2);
    }

    @Test
    @DisplayName("Count entries")
    void count() {
        DBOperations dbo = new DBOperations();

        long count = dbo.countEntries();

        assertTrue(count > 0);
    }

    @Test
    @DisplayName("Count entries")
    void retrieveAll() {
        DBOperations dbo = new DBOperations();
        dbo.persistPerson(new Person("Junit", "Person2"));
        List all = dbo.retrieveAll();
        assertFalse(all.isEmpty());
    }

    @Test
    @DisplayName("Count entries")
    void retrieveWithName() {
        DBOperations dbo = new DBOperations();

        Person p1 = new Person("Junit", "Person3");
        dbo.persistPerson(p1);
        List persons = dbo.retrieveWithFullName("Junit", "Person3");

        assertFalse(persons.isEmpty());
    }

}
