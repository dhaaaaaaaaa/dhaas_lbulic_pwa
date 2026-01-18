package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.SampleBoxPos;
import at.ac.tgm.restbackendci_cd.repository.SampleBoxPosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/view/samplebox")
public class SampleBoxPosController {

    @Autowired
    private SampleBoxPosRepo repo;

    @GetMapping
    public Page<SampleBoxPos> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sId") String sortBy, // Sortieren nach sId ist okay
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repo.findAll(PageRequest.of(page, size, sort));
    }
}