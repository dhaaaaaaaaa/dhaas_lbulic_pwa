package at.ac.tgm.restbackendci_cd.repository;

import at.ac.tgm.restbackendci_cd.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {
}