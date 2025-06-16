package ru.itis.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class Skill {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Double rating;

    private Integer ratingCount;

    @Column(length = 600)
    private String description;

    private String imageFilename = "/images/default_skill.jpg";

    private Boolean isArchived = false;
}
