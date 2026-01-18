package at.ac.tgm.restbackendci_cd.service;

import at.ac.tgm.restbackendci_cd.entity.Sample;
import at.ac.tgm.restbackendci_cd.id.SampleId;
import at.ac.tgm.restbackendci_cd.repository.SampleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SampleService {
    @Autowired private final SampleRepo repo;
    public SampleService(SampleRepo repo) { this.repo = repo; }

    public Page<Sample> getAll(Pageable p) { return repo.findAll(p); }

    public Sample create(Sample s) {
        if (s.getSStamp() == null) s.setSStamp(LocalDateTime.now());
        return repo.save(s);
    }

    // ÄNDERUNG: String sId
    public Sample update(String sId, LocalDateTime sStamp, Sample newData) {
        return repo.findById(new SampleId(sId, sStamp)).map(existing -> {
            existing.setName(newData.getName());
            existing.setQuantity(newData.getQuantity());
            existing.setComment(newData.getComment());
            existing.setWeightNet(newData.getWeightNet());
            existing.setWeightBru(newData.getWeightBru());
            existing.setWeightTar(newData.getWeightTar());
            existing.setDistance(newData.getDistance());
            existing.setFlags(newData.getFlags());
            existing.setLane(newData.getLane());
            existing.setDateCrumbled(newData.getDateCrumbled());
            existing.setDateExported(newData.getDateExported());
            return repo.save(existing);
        }).orElse(null);
    }

    // ÄNDERUNG: String sId
    public void delete(String sId, LocalDateTime sStamp) {
        repo.deleteById(new SampleId(sId, sStamp));
    }
}