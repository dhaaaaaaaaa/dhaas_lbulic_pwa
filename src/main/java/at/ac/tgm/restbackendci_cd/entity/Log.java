package at.ac.tgm.restbackendci_cd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log", schema = "venlab")
public class Log {

    @Id
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    private String level;
    private String info;

    // Diese Felder sind in deinem Screenshot leer, aber wir mappen sie trotzdem (optional)
    @Column(name = "s_id")
    private Long sId;

    @Column(name = "s_stamp")
    private LocalDateTime sStamp;

    @Column(name = "a_id")
    private Long aId;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    // --- Getter & Setter ---

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public Long getSId() { return sId; }
    public void setSId(Long sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }

    public Long getAId() { return aId; }
    public void setAId(Long aId) { this.aId = aId; }

    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}