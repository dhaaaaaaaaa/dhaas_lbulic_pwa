package at.ac.tgm.restbackendci_cd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import at.ac.tgm.restbackendci_cd.id.BoxPosId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "boxpos", schema = "venlab")
public class BoxPos {

    @EmbeddedId
    private BoxPosId id;

    @Column(name = "s_id")
    @NotBlank(message = "Sample ID darf nicht leer sein.")
    @JsonProperty("sId")
    private String sId;

    @Column(name = "s_stamp")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sStamp;

    // Helper für Frontend
    public String getBId() { return id != null ? id.getBId() : null; }
    public void setBId(String bId) {
        if(this.id == null) this.id = new BoxPosId();
        this.id.setBId(bId);
    }
    public Integer getBposId() { return id != null ? id.getBposId() : null; }
    public void setBposId(Integer bposId) {
        if(this.id == null) this.id = new BoxPosId();
        this.id.setBposId(bposId);
    }

    public String getSId() { return sId; }
    public void setSId(String sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }
}