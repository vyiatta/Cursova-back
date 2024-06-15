package com.example.project.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;

@Service
public class EmailController {

    private Properties prop;
    private final String username = "cleanoraorders@gmail.com";
    private final String password = "xubhpggdlckqxhmj";


    @PostConstruct
    public void postInitialization() {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
    }


    public void sendEmail() {
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("meyervikulov@gmail.com"));
            message.setSubject("Welcome to Cleanora: Let's Begin Your Dry Cleaning Journey");

            // Read HTML content from the file
            StringBuilder htmlContent = new StringBuilder();
            ClassPathResource resource = new ClassPathResource("welcome.html");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    htmlContent.append(line);
                }
            }

            // Set the HTML content in the email
            message.setContent(htmlContent.toString(), "text/html");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
    }

