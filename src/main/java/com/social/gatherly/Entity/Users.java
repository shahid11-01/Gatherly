package com.social.gatherly.Entity;


import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "user_name", columnDefinition = "CHAR(100)", nullable = false, length = 100)
    private String userName;

    @Column(name= "email", columnDefinition = "CHAR(100)", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name="password", columnDefinition = "CHAR(255)", nullable = false, length = 255)
    private String password;

    @CreationTimestamp
    @Column(name="created_at", columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(unique = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;





}
