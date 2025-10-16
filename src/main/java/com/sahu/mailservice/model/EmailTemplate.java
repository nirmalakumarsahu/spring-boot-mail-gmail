package com.sahu.mailservice.model;

import com.sahu.mailservice.constant.EmailTemplateType;
import com.sahu.mailservice.model.convert.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "email_templates")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String subject;
    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private EmailTemplateType type;

    @Convert(converter = StringListConverter.class)
    @Column(name = "placeholders", columnDefinition = "TEXT")
    private List<String> placeholders;
    private Boolean placeholderExists;
    private Boolean active;
}
