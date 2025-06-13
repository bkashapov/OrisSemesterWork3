package ru.itis.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;
}
