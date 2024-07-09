package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Controller
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
        run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Show all contacts");
            System.out.println("2. Add new contact");
            System.out.println("3. Delete contact by email");
            System.out.println("4. Save contacts to file");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showAllContacts();
                    break;
                case "2":
                    addNewContact(scanner);
                    break;
                case "3":
                    deleteContactByEmail(scanner);
                    break;
                case "4":
                    saveContactsToFile();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void showAllContacts() {
        contactService.getContacts().forEach(System.out::println);
    }

    private void addNewContact(Scanner scanner) {
        System.out.print("Enter contact (fullName;phoneNumber;email): ");
        String line = scanner.nextLine();
        String[] parts = line.split(";");
        if (parts.length == 3) {
            contactService.addContact(new Contact(parts[0], parts[1], parts[2]));
        } else {
            System.out.println("Invalid format. Try again.");
        }
    }

    private void deleteContactByEmail(Scanner scanner) {
        System.out.print("Enter email of the contact to delete: ");
        String email = scanner.nextLine();
        contactService.removeContactByEmail(email);
    }

    private void saveContactsToFile() {
        String filePath = "src/main/resources/contacts.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Contact contact : contactService.getContacts()) {
                writer.write(formatContact(contact));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Fail from saving contacts to file: " + e.getMessage());
        }
    }

    private String formatContact(Contact contact) {
        StringBuilder formattedContact = new StringBuilder();

        // Format full name
        formattedContact.append(contact.getFullName() != null ? contact.getFullName() : "Нет имени");
        formattedContact.append(";");

        // Format phone number
        String phoneNumber = contact.getPhoneNumber();
        if (phoneNumber != null && (phoneNumber.matches("^\\+7\\d{10}$") || phoneNumber.matches("^8\\d{10}$"))) {
            formattedContact.append(phoneNumber);
        } else {
            formattedContact.append("Failed number");
        }
        formattedContact.append(";");

        // Format email
        formattedContact.append(contact.getEmail() != null ? contact.getEmail() : "not email");

        return formattedContact.toString();
    }
}
