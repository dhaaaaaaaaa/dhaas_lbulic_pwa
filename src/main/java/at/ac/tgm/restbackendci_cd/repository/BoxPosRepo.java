package at.ac.tgm.restbackendci_cd.repository;

import at.ac.tgm.restbackendci_cd.entity.BoxPos;
import at.ac.tgm.restbackendci_cd.id.BoxPosId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxPosRepo extends JpaRepository<BoxPos, BoxPosId> {
}