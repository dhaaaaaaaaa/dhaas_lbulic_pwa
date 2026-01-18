package at.ac.tgm.restbackendci_cd.service;

import at.ac.tgm.restbackendci_cd.entity.BoxPos;
import at.ac.tgm.restbackendci_cd.id.BoxPosId;
import at.ac.tgm.restbackendci_cd.repository.BoxPosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoxPosService {

    @Autowired
    private final BoxPosRepo repo;

    public BoxPosService(BoxPosRepo repo) {
        this.repo = repo;
    }

    public Page<BoxPos> getAll(Pageable p) {
        return repo.findAll(p);
    }

    public BoxPos create(BoxPos bp) {
        // Sicherstellen, dass die ID gesetzt ist
        if (bp.getBId() != null && bp.getBposId() != null) {
            // Wir erstellen das ID-Objekt explizit, falls es fehlt
            BoxPosId id = new BoxPosId(bp.getBId(), bp.getBposId());
            // Hier könnte man theoretisch setId(id) aufrufen, wenn es Setter gäbe
            // JPA mappt das aber meist automatisch über die Felder
        }
        return repo.save(bp);
    }

    // HIER WAR DER FEHLER: Methode angepasst auf (BoxPos bp)
    public BoxPos update(BoxPos newData) {
        // Wir bauen die ID aus den Daten im Objekt
        BoxPosId id = new BoxPosId(newData.getBId(), newData.getBposId());

        return repo.findById(id).map(existing -> {
            // Felder aktualisieren
            existing.setSId(newData.getSId());
            existing.setSStamp(newData.getSStamp());
            return repo.save(existing);
        }).orElse(null);
    }

    public void delete(String bId, Integer bposId) {
        repo.deleteById(new BoxPosId(bId, bposId));
    }
}