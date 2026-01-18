package at.ac.tgm.restbackendci_cd.service;

import at.ac.tgm.restbackendci_cd.entity.Box;
import at.ac.tgm.restbackendci_cd.repository.BoxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BoxService {
    @Autowired
    private final BoxRepo repository;

    public BoxService(BoxRepo repository) { this.repository = repository; }

    public Page<Box> getAll(Pageable pageable) { return repository.findAll(pageable); }
    public Optional<Box> getById(String id) { return repository.findById(id); }
    public Box create(Box box) { return repository.save(box); }
    public Box update(String id, Box box) {
        return repository.findById(id).map(existing -> {
            existing.setName(box.getName());
            existing.setNumMax(box.getNumMax());
            existing.setType(box.getType());
            existing.setComment(box.getComment());
            return repository.save(existing);
        }).orElse(null);
    }
    public void delete(String id) { repository.deleteById(id); }
}