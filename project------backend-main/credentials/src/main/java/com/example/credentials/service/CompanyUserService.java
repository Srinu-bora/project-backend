package com.example.credentials.service;

import com.example.credentials.model.CompanyUser;
import com.example.credentials.repository.CompanyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserRepository companyUserRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register the company user
    public String registerCompany(CompanyUser companyUser) {
        // Generate a unique 4-digit company ID
        companyUser.setCompanyId(generateUniqueCompanyId());
        companyUser.setPassword(passwordEncoder.encode(companyUser.getPassword()));
        companyUserRepository.save(companyUser);
        return "Company registered successfully!";
    }

    // Authenticate user
    public boolean authenticate(String username, String rawPassword) {
        CompanyUser companyUser = companyUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        return passwordEncoder.matches(rawPassword, companyUser.getPassword());
    }

    // Get all companies
    public List<CompanyUser> getAllCompanies() {
        return companyUserRepository.findAll();
    }

    // Get company by ID
    public CompanyUser getCompanyById(Long id) {
        return companyUserRepository.findById(id).orElse(null);
    }

    // Update company by ID
    public boolean updateCompany(Long id, CompanyUser updatedCompany) {
        Optional<CompanyUser> optionalCompanyUser = companyUserRepository.findById(id);
        if (optionalCompanyUser.isPresent()) {
            CompanyUser existingCompany = optionalCompanyUser.get();
            existingCompany.setCompanyName(updatedCompany.getCompanyName());
            existingCompany.setOwnerName(updatedCompany.getOwnerName());
            existingCompany.setLocation(updatedCompany.getLocation());
            existingCompany.setAddress(updatedCompany.getAddress());
            existingCompany.setPhoneNumber(updatedCompany.getPhoneNumber());
            companyUserRepository.save(existingCompany);
            return true;
        }
        return false;
    }

    // Delete company by ID
    public boolean deleteCompany(Long id) {
        if (companyUserRepository.existsById(id)) {
            companyUserRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Generate unique 4-digit company ID
    private String generateUniqueCompanyId() {
        // Loop until a unique company ID is found
        String companyId;
        do {
            companyId = String.format("%04d", (int) (Math.random() * 10000));
        } while (companyUserRepository.existsByCompanyId(companyId));  // Ensure it's unique
        return companyId;
    }
    
}
