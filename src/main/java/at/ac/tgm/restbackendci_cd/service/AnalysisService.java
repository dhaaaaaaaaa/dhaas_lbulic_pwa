package at.ac.tgm.restbackendci_cd.service;

import at.ac.tgm.restbackendci_cd.entity.Analysis;
import at.ac.tgm.restbackendci_cd.repository.AnalysisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnalysisService {

    @Autowired
    private final AnalysisRepo repository;

    public AnalysisService(AnalysisRepo repository) {
        this.repository = repository;
    }

    public Page<Analysis> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Analysis> getById(Long id) {
        return repository.findById(id);
    }

    public Analysis create(Analysis analysis) {
        return repository.save(analysis);
    }

    public Analysis update(Long id, Analysis analysis) {
        return repository.findById(id).map(existing -> {
            // Felder aktualisieren
            existing.setSId(analysis.getSId());
            existing.setPol(analysis.getPol());
            existing.setNat(analysis.getNat());
            existing.setComment(analysis.getComment());
            // Hier können weitere Felder ergänzt werden, falls nötig
            return repository.save(existing);
        }).orElse(null);
    }

    // WICHTIG: @Transactional sorgt dafür, dass beides passiert oder gar nichts
    @Transactional
    public void delete(Long id) {
        // 1. Zuerst die abhängigen Logs löschen (sonst FK-Error)
        // Diese Methode muss im AnalysisRepo definiert sein!
        repository.deleteAnalysisLogs(id);

        // 2. Dann die Analyse selbst löschen
        repository.deleteById(id);
    }
}