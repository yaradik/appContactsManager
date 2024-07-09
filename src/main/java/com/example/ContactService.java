package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private List<Contact> contacts = new ArrayList<>();

    @Value("classpath:contacts.txt")
    private org.springframework.core.io.Resource contactsResource;

    @PostConstruct
    @Profile("init")
    public void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(contactsResource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                contacts.add(new Contact(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContactByEmail(String email) {
        contacts.removeIf(contact -> contact.getEmail().equals(email));
    }
}
