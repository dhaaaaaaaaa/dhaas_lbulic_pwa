package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.Analysis;
import at.ac.tgm.restbackendci_cd.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // <--- DIESER IMPORT FEHLTE
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private final AnalysisService service;

    public AnalysisController(AnalysisService service) {
        this.service = service;
    }

    // READ (mit Pagination und Sortierung)
    @GetMapping
    public Page<Analysis> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        // Hier wird die Sortierung festgelegt
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable paging = PageRequest.of(page, size, sort);
        return service.getAll(paging);
    }

    // READ (Einzeln)
    @GetMapping("/{id}")
    public ResponseEntity<Analysis> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public Analysis create(@RequestBody Analysis analysis) {
        return service.create(analysis);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Analysis> update(@PathVariable Long id, @RequestBody Analysis analysis) {
        Analysis updated = service.update(id, analysis);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}