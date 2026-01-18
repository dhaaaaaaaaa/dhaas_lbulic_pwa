package at.ac.tgm.restbackendci_cd.repository;

import at.ac.tgm.restbackendci_cd.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxRepo extends JpaRepository<Box, String> {
}