package com.sahu.mailservice.repository;

import com.sahu.mailservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    boolean existsByNameAndActiveTrue(String name);
}
