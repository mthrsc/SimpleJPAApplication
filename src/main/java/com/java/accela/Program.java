/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.accela;

import com.sun.istack.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javax.persistence.RollbackException;

/**
 *
 * @author Matthieu Roscio
 */
public class Program {

    private String message = new String();
    private String input = new String();
    private boolean exit = false;
    private final Scanner scanner;
    private final DBOperations dbo;

    public Program() {
        scanner = new Scanner(System.in);
        dbo = new DBOperations();
    }

    public void compute() {
        while (!exit) {
            message = "\n\nChoose what operation to perform:\n"
                    + "1. Add Person \n"
                    + "2. Edit Person's first name and last name\n"
                    + "3. Delete Person\n"
                    + "4. Add Address to person \n"
                    + "5. Edit Person's Address \n"
                    + "6. Delete Person's Address \n"
                    + "7. Count Number of Persons\n"
                    + "8. List Persons\n"
                    + "q to quit\n\n";

            System.out.println(message);
            input = scanner.nextLine();
            input = cleanInput(input);

            switch (input) {
                case "1":
                    addPerson();
                    break;
                case "2":
                    findPerson("name");
                    break;
                case "3":
                    findPerson("deletePerson");
                    break;
                case "4":
                    findPerson("address");
                    break;
                case "5":
                    findPerson("address");
                    break;
                case "6":
                    findPerson("removeAddress");
                    break;
                case "7":
                    countEntries();
                    break;
                case "8":
                    printAll();
                    break;
                case "q":
                    exit = true;
                    break;

            }
        }
        dbo.closeEM();
    }

    private final void addPerson() {
        String[] fullName;
        long id;
        try {
            System.out.println("\n\n Add person:");
            System.out.println("Please enter new person first name and last name separated by a space. Eg. James Gosling");
            System.out.println("Enter q to return");
            input = scanner.nextLine();
            input = cleanInput(input);

            if (input.equalsIgnoreCase("q")) {
                return;
            }

            fullName = input.split("\\s+");
            Person p = new Person(fullName[0], fullName[1]);

            id = dbo.persistPerson(p);
            System.out.println("Person successfully saved with id: " + id);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input");
        }
    }

    private final void findPerson(String editType) {

        Person p = null;

        switch (editType) {
            case "name":
                System.out.println("\n\n Edit person's name:");
                if ((p = selectPerson()) != null) {
                    editName(p);
                }
                break;
            case "address":
                System.out.println("\n\n Edit person's address:");
                if ((p = selectPerson()) != null) {
                    editAddress(p);
                }
                break;
            case "removeAddress":
                System.out.println("\n\n Remove person's address:");
                if ((p = selectPerson()) != null) {
                    removeAddress(p);
                }
                break;
            case "deletePerson":
                System.out.println("\n\n Delete person:");
                if ((p = selectPerson()) != null) {
                    deletePerson(p);
                }
                break;
            default:
                return;
        }
    }

    private final void countEntries() {
        long count = dbo.countEntries();
        System.out.println("There is " + count + " persons in the \"person\" database");
    }

    private final void removeAddress(Person p) {
        System.out.println(p.toString());

        p.setStreet(null);
        p.setCity(null);
        p.setState(null);
        p.setPostalCode(null);

        dbo.persistPerson(p);
        System.out.println("Person updated: \n" + p.toString());
    }

    private final void editAddress(Person p) {
        System.out.println(p.toString());
        String street, city, state, postalCode = new String();
        while (true) {
            try {
                System.out.println("Please enter street (Press enter to keep current value):");
                System.out.println("Enter q to return");
                input = scanner.nextLine();
                input = cleanInput(input);

                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                street = input;

                System.out.println("Please enter city (Press enter to keep current value):");
                System.out.println("Enter q to return");
                input = scanner.nextLine();
                input = cleanInput(input);

                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                city = input;

                System.out.println("Please enter state (Press enter to keep current value):");
                System.out.println("Enter q to return");
                input = scanner.nextLine();
                input = cleanInput(input);

                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                state = input;

                System.out.println("Please enter postal code (Press enter to keep current value):");
                System.out.println("Enter q to return");
                input = scanner.nextLine();
                input = cleanInput(input);

                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                postalCode = input;

                if (!street.equals("")) {
                    p.setStreet(street);
                }
                if (!city.equals("")) {
                    p.setCity(city);
                }
                if (!state.equals("")) {
                    p.setState(state);
                }
                if (!postalCode.equals("")) {
                    p.setPostalCode(postalCode);
                }

                dbo.persistPerson(p);

                System.out.println("Person updated: \n" + p.toString());

                return;
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }

    private final void deletePerson(Person p) {
        System.out.println("\n\nAre you sure you want to delete person ? Y/N: \n" + p.toString());
        input = scanner.nextLine();
        input = cleanInput(input);
        try {
            if (input.equalsIgnoreCase("y")) {
                dbo.deletePerson(p.getId());
                System.out.println("Person deleted");
            } else {
                System.out.println("Delete cancelled");
            }
        } catch (RollbackException e) {
            System.out.println("Delete Error");
        }
    }

    private final void editName(Person p) {
        System.out.println(p.toString());
        while (true) {
            try {
                System.out.println("Enter new first name and last name. Eg. James Gosling");
                System.out.println("Enter q to return");
                input = scanner.nextLine();
                input = cleanInput(input);

                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                String[] fullName = input.split("\\s+");

                p.setFirstName(fullName[0]);
                p.setLastName(fullName[1]);
                dbo.persistPerson(p);
                return;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid input, name format is James Gosling");
            }
        }
    }

    private final Person selectPerson() {
        while (true) {
            System.out.println("1. Find person with id");
            System.out.println("2. Find person with first and last name");
            System.out.println("Enter q to return");
            input = scanner.nextLine();
            input = cleanInput(input);

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            if (input.equals("1")) {
                try {
                    long id = -1;
                    Person p = null;

                    System.out.println("\n\nFind person with id");
                    System.out.println("Please enter id:");
                    System.out.println("Enter q to return");

                    input = scanner.nextLine();
                    input = cleanInput(input);

                    if (input.equalsIgnoreCase("q")) {
                        break;
                    }

                    id = Long.parseLong(input);
                    p = dbo.retrieveWithID(id);

                    if (p == null) {
                        System.out.println("No matching ID");
                    } else {
                        return p;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, IDs are integer. Eg. 1, 2, 3, 4...");
                }
            } else if (input.equals("2")) {
                try {
                    System.out.println("Please enter person's first name and last name separated by a space. Eg. James Gosling");
                    System.out.println("Enter q to return");
                    input = scanner.nextLine();
                    input = cleanInput(input);

                    if (input.equalsIgnoreCase("q")) {
                        break;
                    }

                    String[] fullName = input.split("\\s+");

                    List personsList = dbo.retrieveWithFullName(fullName[0], fullName[1]);
                    if (personsList.size() == 0) {
                        System.out.println("No matching entry");
                        return null;
                    } else if (personsList.size() == 1) {
                        System.out.println(personsList.get(0).toString());
                        return (Person) personsList.get(0);
                    } else {
                        System.out.println("\n\nSeverals entry matching, please select with ID");
                        personsList.stream().forEach(System.out::println);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid input, name format is James Gosling");
                }
            }
        }
        return null;
    }

    private final void printAll() {
        try {
            List personsList = dbo.retrieveAll();
            personsList.stream().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private final String cleanInput(String input) {
        //Removing leading and trailing space, as well as possible extra space between words
        return input.replaceAll("\\s{2,}", " ").trim();
    }

}
