package leonardoferrante.progettofinale.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime bookingDate;
    private LocalDateTime tourDate;
    private int participants;
    private String status;
    private String email;
    private String tourName;
}