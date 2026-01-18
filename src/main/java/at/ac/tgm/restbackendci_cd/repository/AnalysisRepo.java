package at.ac.tgm.restbackendci_cd.repository;

import at.ac.tgm.restbackendci_cd.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AnalysisRepo extends JpaRepository<Analysis, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM venlab.log WHERE a_id = :aId", nativeQuery = true)
    void deleteAnalysisLogs(@Param("aId") Long aId);
}