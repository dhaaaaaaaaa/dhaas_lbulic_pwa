package at.ac.tgm.restbackendci_cd.repository;
import at.ac.tgm.restbackendci_cd.entity.Sample;
import at.ac.tgm.restbackendci_cd.id.SampleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepo extends JpaRepository<Sample, SampleId> {
}