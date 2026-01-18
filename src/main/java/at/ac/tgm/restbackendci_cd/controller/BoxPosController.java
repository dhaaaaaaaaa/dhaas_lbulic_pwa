package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.BoxPos;
import at.ac.tgm.restbackendci_cd.service.BoxPosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boxpos")
public class BoxPosController {

    @Autowired
    private final BoxPosService service;

    public BoxPosController(BoxPosService service) { this.service = service; }

    @GetMapping
    public Page<BoxPos> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id.bId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return service.getAll(PageRequest.of(page, size, sort));
    }

    @PostMapping
    public BoxPos create(@Valid @RequestBody BoxPos bp) { // @Valid hinzugefügt
        return service.create(bp);
    }

    @PutMapping
    public BoxPos update(@Valid @RequestBody BoxPos bp) { // @Valid hinzugefügt
        return service.update(bp);
    }

    @DeleteMapping
    public void delete(@RequestParam String bId, @RequestParam Integer bposId) {
        service.delete(bId, bposId);
    }
}