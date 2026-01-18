package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.Log;
import at.ac.tgm.restbackendci_cd.repository.LogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogRepo repo;

    @GetMapping
    public Page<Log> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "logId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir // Logs zeigen wir meistens neueste zuerst (desc)
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repo.findAll(PageRequest.of(page, size, sort));
    }
}