package leonardoferrante.progettofinale.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "tours")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    private String difficulty;

    @Positive
    private double price;

    @Positive
    private int duration;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String startLocation;

    @NotBlank
    private String imageUrl;


    //relazione many to one con gli user(guida)
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    @JsonIgnore
    private User guide;

    //relazione oneToMany con booking
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();
}

