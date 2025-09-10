package com.garja.Garja.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // same as User.id

    private String department; // example field specific to admin

      @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
