package com.interntrack.config;

import com.interntrack.model.ApplicationStatus;
import com.interntrack.model.Internship;
import com.interntrack.model.Offer;
import com.interntrack.model.Role;
import com.interntrack.model.User;
import com.interntrack.model.WorkMode;
import com.interntrack.repository.InternshipRepository;
import com.interntrack.repository.OfferRepository;
import com.interntrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final OfferRepository offerRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public DataSeeder(UserRepository userRepository,
                      InternshipRepository internshipRepository,
                      OfferRepository offerRepository,
                      PasswordEncoder passwordEncoder,
                      @Value("${interntrack.seed.admin-email}") String adminEmail,
                      @Value("${interntrack.seed.admin-password}") String adminPassword) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.offerRepository = offerRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedDemoData();
    }

    private void seedAdmin() {
        String email = adminEmail.trim().toLowerCase();
        if (!userRepository.existsByEmail(email)) {
            User admin = new User("InternTrack Admin", email, passwordEncoder.encode(adminPassword), Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Seeded admin account: " + email);
        }
    }

    private void seedDemoData() {
        if (userRepository.countByRole(Role.STUDENT) > 0 || internshipRepository.count() > 0) {
            return;
        }

        User aarav = student("Aarav Sharma", "aarav.sharma@interntrack.dev", "CSE", 2026, 8.4,
                "Java,Spring Boot,MySQL,REST");
        User diya = student("Diya Nair", "diya.nair@interntrack.dev", "IT", 2026, 8.9,
                "React,JavaScript,CSS,UI");
        User ishaan = student("Ishaan Verma", "ishaan.verma@interntrack.dev", "ECE", 2027, 7.8,
                "Python,SQL,Data Analysis");

        LocalDate today = LocalDate.now();

        Internship a1 = internship(aarav, "TechNova", "Backend Developer Intern", "Bengaluru",
                WorkMode.ONSITE, 25000, today.minusDays(5), today.plusDays(10), ApplicationStatus.APPLIED,
                "https://careers.technova.example/backend-intern");
        Internship a2 = internship(aarav, "CloudKart", "Java Developer Intern", "Remote",
                WorkMode.REMOTE, 20000, today.minusDays(9), today.plusDays(3), ApplicationStatus.SHORTLISTED,
                "https://cloudkart.example/jobs/java-intern");
        Internship d1 = internship(diya, "BrightLabs", "Frontend Developer Intern", "Pune",
                WorkMode.HYBRID, 22000, today.minusDays(2), today.plusDays(7), ApplicationStatus.APPLIED,
                "https://brightlabs.example/careers/frontend");
        Internship d2 = internship(diya, "PixelWorks", "UI/UX Intern", "Remote",
                WorkMode.REMOTE, 15000, today.minusDays(14), today.plusDays(21), ApplicationStatus.SELECTED,
                "https://pixelworks.example/intern/uiux");
        Internship i1 = internship(ishaan, "FinEdge", "Data Analyst Intern", "Mumbai",
                WorkMode.ONSITE, 18000, today.minusDays(20), today.minusDays(2), ApplicationStatus.APPLIED,
                "https://finedge.example/students");

        internshipRepository.save(a1);
        internshipRepository.save(a2);
        internshipRepository.save(d1);
        internshipRepository.save(d2);
        internshipRepository.save(i1);

        Offer offer = new Offer(diya, "PixelWorks", "UI/UX Intern");
        offer.setInternship(d2);
        offer.setStipend(15000);
        offer.setOfferDate(today.minusDays(1));
        offer.setJoiningDate(today.plusDays(30));
        offer.setResponseDeadline(today.plusDays(5));
        offer.setLetterLink("https://pixelworks.example/offers/diya.pdf");
        offer.setNotes("6-month internship with a pre-placement offer on good performance.");
        offerRepository.save(offer);

        System.out.println("Seeded 3 demo students with sample internships and an offer.");
        System.out.println("Demo student logins (password Student@123):");
        System.out.println("  aarav.sharma@interntrack.dev | diya.nair@interntrack.dev | ishaan.verma@interntrack.dev");
    }

    private User student(String name, String email, String branch, int year, double cgpa, String skills) {
        User u = new User(name, email, passwordEncoder.encode("Student@123"), Role.STUDENT);
        u.setCollege("National Institute of Technology");
        u.setBranch(branch);
        u.setGraduationYear(year);
        u.setCgpa(cgpa);
        u.setSkills(skills);
        return userRepository.save(u);
    }

    private Internship internship(User owner, String company, String role, String location, WorkMode mode,
                                  int stipend, LocalDate appliedOn, LocalDate deadline, ApplicationStatus status,
                                  String sourceLink) {
        Internship i = new Internship(owner, company, role, deadline);
        i.setLocation(location);
        i.setWorkMode(mode);
        i.setStipend(stipend);
        i.setApplicationDate(appliedOn);
        i.setStatus(status);
        i.setSourceLink(sourceLink);
        return i;
    }
}
