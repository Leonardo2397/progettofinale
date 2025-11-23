package leonardoferrante.progettofinale.controller;

import leonardoferrante.progettofinale.DTO.BookingDto;
import leonardoferrante.progettofinale.entities.BookingStatus;
import leonardoferrante.progettofinale.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<BookingDto>> getBookingsByUser(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByUserEmail(email));
    }

    @GetMapping("/user/me")
    public ResponseEntity<List<BookingDto>> getMyBookings(org.springframework.security.core.Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(bookingService.getBookingsByUserEmail(email));
    }


    @GetMapping("/tour/{tourName}")
    public ResponseEntity<List<BookingDto>> getBookingsByTour(@PathVariable String tourName) {
        return ResponseEntity.ok(bookingService.getBookingsByTourName(tourName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingDto>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody BookingDto dto) {
        return ResponseEntity.ok(bookingService.updateBooking(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
