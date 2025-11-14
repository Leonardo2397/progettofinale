package leonardoferrante.progettofinale.controller;

import leonardoferrante.progettofinale.DTO.TourDto;
import leonardoferrante.progettofinale.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    @PostMapping
    public ResponseEntity<TourDto> createTour(@RequestBody TourDto dto) {
        return ResponseEntity.ok(tourService.createTour(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDto> getTourById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getTourById(id));
    }

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourDto> updateTour(@PathVariable Long id, @RequestBody TourDto dto) {
        return ResponseEntity.ok(tourService.updateTour(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<TourDto>> getToursByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(tourService.getToursByDifficulty(difficulty));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TourDto>> getToursByDate(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(tourService.getToursByDate(date));
    }

    @GetMapping("/guide/{email}")
    public ResponseEntity<List<TourDto>> getToursByGuide(@PathVariable String email) {
        return ResponseEntity.ok(tourService.getToursByGuideEmail(email));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TourDto> getTourByName(@PathVariable String name) {
        return ResponseEntity.ok(tourService.getTourByName(name));
    }
}
