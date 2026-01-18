package at.ac.tgm.restbackendci_cd.entity;

import at.ac.tgm.restbackendci_cd.id.SampleBoxPosId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import java.time.LocalDateTime;

@Entity
@Immutable
@IdClass(SampleBoxPosId.class)
// Deine SQL-Query ist perfekt, sie verknüpft alles via Joins
@Subselect(
        "SELECT " +
                "    s.s_id, " +
                "    s.s_stamp, " +
                "    s.name AS s_name, " +
                "    bp.b_id, " +
                "    b.name AS b_name, " +
                "    bp.bpos_id " +
                "FROM venlab.sample s " +
                "JOIN venlab.boxpos bp ON s.s_id = bp.s_id AND s.s_stamp = bp.s_stamp " +
                "JOIN venlab.box b ON bp.b_id = b.b_id"
)
public class SampleBoxPos {

    @Id
    @Column(name = "s_id")
    @JsonProperty("sId")
    private Long sId; // <--- WICHTIG: Long

    @Id
    @Column(name = "s_stamp")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sStamp;

    @Column(name = "s_name")
    private String sName;

    @Id
    @Column(name = "b_id")
    private String bId;

    @Column(name = "b_name")
    private String bName;

    @Id
    @Column(name = "bpos_id")
    private Integer bposId;

    // Getter (Typen angepasst)
    public Long getSId() { return sId; }
    public LocalDateTime getSStamp() { return sStamp; }
    public String getSName() { return sName; }
    public String getBId() { return bId; }
    public String getBName() { return bName; }
    public Integer getBposId() { return bposId; }
}