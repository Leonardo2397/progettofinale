package leonardoferrante.progettofinale.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourDto {
    private Long id;
    private String name;
    private String description;
    private String difficulty;
    private double price;
    private int duration;
    private LocalDateTime date;
    private String startLocation;
    private String imageUrl;
    private String guideEmail;
}