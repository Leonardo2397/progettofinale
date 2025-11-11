package leonardoferrante.progettofinale.services.impl;

import jakarta.persistence.EntityNotFoundException;
import leonardoferrante.progettofinale.DTO.BookingDto;
import leonardoferrante.progettofinale.entities.*;
import leonardoferrante.progettofinale.repository.BookingRepository;
import leonardoferrante.progettofinale.repository.TourRepository;
import leonardoferrante.progettofinale.repository.UserRepository;
import leonardoferrante.progettofinale.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public BookingDto createBooking(BookingDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Tour tour = tourRepository.findByName(dto.getTourName())
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato"));

        Booking booking = Booking.builder()
                .bookingDate(dto.getBookingDate())
                .tourDate(dto.getTourDate())
                .participants(dto.getParticipants())
                .status(BookingStatus.valueOf(dto.getStatus()))
                .user(user)
                .tour(tour)
                .build();

        bookingRepository.save(booking);
        return mapToDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
        return mapToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByTourName(String tourName) {
        Tour tour = tourRepository.findByName(tourName)
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato"));

        return bookingRepository.findByTour(tour)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        booking.setTourDate(dto.getTourDate());
        booking.setParticipants(dto.getParticipants());
        booking.setStatus(BookingStatus.valueOf(dto.getStatus()));

        bookingRepository.save(booking);
        return mapToDto(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Prenotazione non trovata");
        }
        bookingRepository.deleteById(id);
    }

    private BookingDto mapToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getBookingDate(),
                booking.getTourDate(),
                booking.getParticipants(),
                booking.getStatus().name(),
                booking.getUser().getEmail(),
                booking.getTour().getName()
        );
    }
}

