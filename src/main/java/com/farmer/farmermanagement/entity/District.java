package com.farmer.farmermanagement.entity;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Table(name = "districts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String name;
 
    // Many districts belong to one state
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
}
 