package com.federicovitale.spring_jwt_boilerplate.models.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.federicovitale.spring_jwt_boilerplate.models.entities.Address;
import com.federicovitale.spring_jwt_boilerplate.models.entities.UserPreference;
import com.federicovitale.spring_jwt_boilerplate.security.SignUpBody;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class User {
    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User() {}
    public User(SignUpBody body) {
        this.username = body.getUsername();
        this.firstName = body.getFirstName();
        this.lastName = body.getLastName();
        this.email = body.getEmail();
        this.setPasswordEncrypted(body.getPassword());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Please provide a username")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "Please provide a password")
    private String password;

    @Column(unique = true)
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotEmpty(message = "Please provide a first name")
    private String firstName;

    @NotEmpty(message = "Please provide a last name")
    private String lastName;


    private Boolean deleted = false;

    private Boolean active = false;
    private Boolean verified = false;
    private Boolean startedPasswordRecovery = false;

    @JsonIgnore
    private Date passwordRecoveryStart;

    private Timestamp lastLogin;

    @JsonIgnore
    private String resetToken;

    @JsonIgnore
    private String emailVerificationToken;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "id"})
    private UserPreference preferences;

    @ManyToMany
    @JoinTable(
            name = "user_address__rel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses;

    public void setPasswordEncrypted(String password) {
        this.password = passwordEncoder().encode(password);
    }
    public void encodePassword() {
        this.password = passwordEncoder().encode(this.password);
    }
}
