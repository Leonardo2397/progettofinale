package leonardoferrante.progettofinale.repository;

import leonardoferrante.progettofinale.entities.Booking;
import leonardoferrante.progettofinale.entities.BookingStatus;
import leonardoferrante.progettofinale.entities.Tour;
import leonardoferrante.progettofinale.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByTour(Tour tour);
    List<Booking> findByStatus(BookingStatus status);
}
