package leonardoferrante.progettofinale.repository;

import leonardoferrante.progettofinale.entities.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByGuide (User guide);
    List<Tour> findByDate (LocalDateTime date);
    List<Tour> findByDifficulty (String difficulty);
}