package com.example.credentials.controller;

import com.example.credentials.dto.LoginRequest;
import com.example.credentials.model.CompanyUser;
import com.example.credentials.service.CompanyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "http://localhost:3000")  // Modify as needed for your front-end
public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;

    // Register a new company
    @PostMapping("/register")
    public ResponseEntity<String> registerCompany(@RequestBody CompanyUser companyUser) {
        companyUser.setCompanyId(generateUniqueCompanyId());
        String response = companyUserService.registerCompany(companyUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   
@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    boolean authenticated = companyUserService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    if (authenticated) {
        return ResponseEntity.ok("Login successful!");
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}


    // Get all companies
    @GetMapping
    public List<CompanyUser> getAllCompanies() {
        return companyUserService.getAllCompanies();
    }

    // Get a company by ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyUser> getCompanyById(@PathVariable Long id) {
        CompanyUser companyUser = companyUserService.getCompanyById(id);
        return companyUser != null ? ResponseEntity.ok(companyUser) : ResponseEntity.notFound().build();
    }

    // Update a company by ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody CompanyUser updatedCompany) {
        boolean isUpdated = companyUserService.updateCompany(id, updatedCompany);
        return isUpdated ? ResponseEntity.ok("Company updated successfully!") 
                         : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
    }

    // Delete a company by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean isDeleted = companyUserService.deleteCompany(id);
        return isDeleted ? ResponseEntity.ok("Company deleted successfully!") 
                         : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
    }

    private String generateUniqueCompanyId() {
        // Implement unique 4-digit ID generation logic here
        return String.format("%04d", (int) (Math.random() * 10000));
    }
}
