package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.Box;
import at.ac.tgm.restbackendci_cd.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/box")
public class BoxController {

    @Autowired
    private final BoxService service;

    public BoxController(BoxService service) { this.service = service; }

    @GetMapping
    public Page<Box> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable paging = PageRequest.of(page, size, sort);
        return service.getAll(paging);
    }

    @PostMapping
    public Box create(@Valid @RequestBody Box box) {
        return service.create(box);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Box> update(@PathVariable String id, @Valid @RequestBody Box box) {
        Box updated = service.update(id, box);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}