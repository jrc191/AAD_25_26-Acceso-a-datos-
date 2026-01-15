package com.jramcon398.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents the profile of a student.
 */

@Data
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String phone;
}