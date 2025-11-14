package leonardoferrante.progettofinale.services;

import leonardoferrante.progettofinale.DTO.TourDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TourService {

    TourDto createTour(TourDto dto);

    TourDto getTourById(Long id);

    List<TourDto> getAllTours();

    TourDto updateTour(Long id, TourDto dto);

    void deleteTour(Long id);

    List<TourDto> getToursByDifficulty(String difficulty);

    List<TourDto> getToursByDate(LocalDateTime date);

    List<TourDto> getToursByGuideEmail(String email);

    TourDto getTourByName(String name);
}

