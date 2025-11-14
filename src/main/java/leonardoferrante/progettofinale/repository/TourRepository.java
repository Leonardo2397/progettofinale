package leonardoferrante.progettofinale.repository;

import leonardoferrante.progettofinale.entities.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import leonardoferrante.progettofinale.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Optional<Tour> findByName(String name);
    List<Tour> findByGuide (User guide);
    List<Tour> findByDate (LocalDateTime date);
    List<Tour> findByDifficulty (String difficulty);
}