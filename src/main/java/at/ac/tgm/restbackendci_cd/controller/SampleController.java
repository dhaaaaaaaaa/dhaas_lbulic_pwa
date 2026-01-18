package at.ac.tgm.restbackendci_cd.controller;

import at.ac.tgm.restbackendci_cd.entity.Sample;
import at.ac.tgm.restbackendci_cd.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sample")
public class SampleController {
    @Autowired private final SampleService service;
    public SampleController(SampleService service) { this.service = service; }

    @GetMapping
    public Page<Sample> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id.sId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return service.getAll(PageRequest.of(page, size, sort));
    }

    @PostMapping
    public Sample create(@RequestBody Sample s) { return service.create(s); }

    @PutMapping
    public Sample update(@RequestBody Sample s) { return service.update(s.getSId(), s.getSStamp(), s); }

    @DeleteMapping
    // ÄNDERUNG: String sId
    public void delete(@RequestParam String sId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sStamp) {
        service.delete(sId, sStamp);
    }
}