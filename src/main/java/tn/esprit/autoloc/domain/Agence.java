package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;

    private String ville;

    private String adresse;

    private String telephone;

    // N Agences <-> N Vehicules (côté propriétaire)
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "agence_vehicule",
            joinColumns = @JoinColumn(name = "agence_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicule_id")
    )
    private List<Vehicule> vehicules = new ArrayList<>();

    // 1 Agence -> N Employes
    @JsonIgnore
    @OneToMany(
            mappedBy = "agence",
            fetch = FetchType.LAZY
    )
    private List<Employe> employes = new ArrayList<>();
}
