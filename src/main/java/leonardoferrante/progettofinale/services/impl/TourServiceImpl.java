package leonardoferrante.progettofinale.services.impl;

import jakarta.persistence.EntityNotFoundException;
import leonardoferrante.progettofinale.DTO.TourDto;
import leonardoferrante.progettofinale.entities.Tour;
import leonardoferrante.progettofinale.entities.User;
import leonardoferrante.progettofinale.repository.TourRepository;
import leonardoferrante.progettofinale.repository.UserRepository;
import leonardoferrante.progettofinale.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TourDto createTour(TourDto dto) {
        User guide = userRepository.findByEmail(dto.getGuideEmail())
                .orElseThrow(() -> new EntityNotFoundException("Guida non trovata"));

        Tour tour = Tour.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .difficulty(dto.getDifficulty())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .date(dto.getDate())
                .startLocation(dto.getStartLocation())
                .imageUrl(dto.getImageUrl())
                .guide(guide)
                .build();

        tourRepository.save(tour);
        return mapToDto(tour);
    }

    @Override
    public TourDto getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato"));

        return mapToDto(tour);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourDto updateTour(Long id, TourDto dto) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato"));

        User guide = userRepository.findByEmail(dto.getGuideEmail())
                .orElseThrow(() -> new EntityNotFoundException("Guida non trovata"));

        tour.setName(dto.getName());
        tour.setDescription(dto.getDescription());
        tour.setDifficulty(dto.getDifficulty());
        tour.setPrice(dto.getPrice());
        tour.setDuration(dto.getDuration());
        tour.setDate(dto.getDate());
        tour.setStartLocation(dto.getStartLocation());
        tour.setImageUrl(dto.getImageUrl());
        tour.setGuide(guide);

        tourRepository.save(tour);
        return mapToDto(tour);
    }

    @Override
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour non trovato");
        }
        tourRepository.deleteById(id);
    }

    @Override
    public List<TourDto> getToursByDifficulty(String difficulty) {
        return tourRepository.findByDifficulty(difficulty)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TourDto> getToursByDate(LocalDateTime date) {
        return tourRepository.findByDate(date)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TourDto> getToursByGuideEmail(String email) {
        User guide = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Guida non trovata"));

        return tourRepository.findByGuide(guide)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourDto getTourByName(String name) {
        Tour tour = tourRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato"));

        return mapToDto(tour);
    }

    private TourDto mapToDto(Tour tour) {
        return TourDto.builder()
                .id(tour.getId())
                .name(tour.getName())
                .description(tour.getDescription())
                .difficulty(tour.getDifficulty())
                .price(tour.getPrice())
                .duration(tour.getDuration())
                .date(tour.getDate())
                .startLocation(tour.getStartLocation())
                .imageUrl(tour.getImageUrl())
                .guideEmail(tour.getGuide().getEmail())
                .build();
    }
}
