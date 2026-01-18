package at.ac.tgm.restbackendci_cd.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import at.ac.tgm.restbackendci_cd.id.SampleId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty; // <--- NEU

@Entity
@Table(name = "sample", schema = "venlab")
public class Sample {

    @EmbeddedId
    private SampleId id;

    private String name;

    @Column(name = "weight_net")
    private BigDecimal weightNet;
    @Column(name = "weight_bru")
    private BigDecimal weightBru;
    @Column(name = "weight_tar")
    private BigDecimal weightTar;

    private Integer quantity;
    private BigDecimal distance;

    @Column(name = "date_crumbled")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCrumbled;

    @Column(name = "s_flags")
    private String flags;

    private Integer lane;
    private String comment;

    @Column(name = "date_exported")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateExported;

    // --- Helper Getter/Setter ---

    @JsonProperty("sId") // <--- WICHTIG: Damit im JSON "sId" steht
    public String getSId() { return id != null ? id.getSId() : null; }

    public void setSId(String sId) {
        if (this.id == null) this.id = new SampleId();
        this.id.setSId(sId);
    }

    @JsonProperty("sStamp") // <--- Machen wir hier sicherheitshalber auch
    public LocalDateTime getSStamp() { return id != null ? id.getSStamp() : null; }

    public void setSStamp(LocalDateTime sStamp) {
        if (this.id == null) this.id = new SampleId();
        this.id.setSStamp(sStamp);
    }

    // --- Standard Getter/Setter ---
    public SampleId getId() { return id; }
    public void setId(SampleId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public BigDecimal getWeightNet() { return weightNet; }
    public void setWeightNet(BigDecimal weightNet) { this.weightNet = weightNet; }
    public BigDecimal getWeightBru() { return weightBru; }
    public void setWeightBru(BigDecimal weightBru) { this.weightBru = weightBru; }
    public BigDecimal getWeightTar() { return weightTar; }
    public void setWeightTar(BigDecimal weightTar) { this.weightTar = weightTar; }
    public BigDecimal getDistance() { return distance; }
    public void setDistance(BigDecimal distance) { this.distance = distance; }
    public LocalDateTime getDateCrumbled() { return dateCrumbled; }
    public void setDateCrumbled(LocalDateTime dateCrumbled) { this.dateCrumbled = dateCrumbled; }
    public String getFlags() { return flags; }
    public void setFlags(String flags) { this.flags = flags; }
    public Integer getLane() { return lane; }
    public void setLane(Integer lane) { this.lane = lane; }
    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}