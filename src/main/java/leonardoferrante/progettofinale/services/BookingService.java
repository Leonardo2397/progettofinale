package leonardoferrante.progettofinale.services;

import leonardoferrante.progettofinale.DTO.BookingDto;
import leonardoferrante.progettofinale.entities.BookingStatus;
import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto dto);

    BookingDto getBookingById(Long id);

    List<BookingDto> getAllBookings();

    List<BookingDto> getBookingsByUserEmail(String email);

    List<BookingDto> getBookingsByTourName(String tourName);

    List<BookingDto> getBookingsByStatus(BookingStatus status);

    BookingDto updateBooking(Long id, BookingDto dto);

    void deleteBooking(Long id);
}
