package at.ac.tgm.restbackendci_cd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "analysis", schema = "venlab")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(name = "s_id")
    @NotBlank(message = "Sample ID darf nicht leer sein.")
    @JsonProperty("sId")
    private String sId;

    @Column(name = "s_stamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sStamp;

    // --- Virtuelle Spalten (Verbesserte Version) ---
    // Wir entfernen 'venlab.' falls es Probleme macht und nutzen explizite Aliases

    @Formula("(SELECT s.name FROM venlab.sample s WHERE s.s_id = s_id AND s.s_stamp = s_stamp LIMIT 1)")
    private String sampleName;

    @Formula("(SELECT s.weight_bru FROM venlab.sample s WHERE s.s_id = s_id AND s.s_stamp = s_stamp LIMIT 1)")
    private BigDecimal weightBru;

    @Formula("(SELECT s.weight_net FROM venlab.sample s WHERE s.s_id = s_id AND s.s_stamp = s_stamp LIMIT 1)")
    private BigDecimal weightNet;

    // Hier prüfen wir auch, ob die BoxPos überhaupt existiert
    @Formula("(SELECT bp.b_id FROM venlab.boxpos bp WHERE bp.s_id = s_id AND bp.s_stamp = s_stamp LIMIT 1)")
    private String boxId;

    @Formula("(SELECT bp.bpos_id FROM venlab.boxpos bp WHERE bp.s_id = s_id AND bp.s_stamp = s_stamp LIMIT 1)")
    private Integer bposId;

    // -------------------------------------

    private String pol;
    private String nat;
    private String comment;

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public LocalDateTime getSStamp() {
        return sStamp;
    }

    public void setSStamp(LocalDateTime sStamp) {
        this.sStamp = sStamp;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter für virtuelle Felder
    public String getSampleName() {
        return sampleName;
    }

    public BigDecimal getWeightBru() {
        return weightBru;
    }

    public BigDecimal getWeightNet() {
        return weightNet;
    }

    public String getBoxId() {
        return boxId;
    }

    public Integer getBposId() {
        return bposId;
    }
}