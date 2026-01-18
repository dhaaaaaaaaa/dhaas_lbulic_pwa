package at.ac.tgm.restbackendci_cd.repository;

import at.ac.tgm.restbackendci_cd.entity.SampleBoxPos;
import at.ac.tgm.restbackendci_cd.id.SampleBoxPosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoxPosRepo extends JpaRepository<SampleBoxPos, SampleBoxPosId> {
}