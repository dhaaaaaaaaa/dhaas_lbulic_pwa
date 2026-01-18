package at.ac.tgm.restbackendci_cd.id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class SampleBoxPosId implements Serializable {
    // WICHTIG: Long statt String!
    private Long sId;
    private LocalDateTime sStamp;
    private String bId;
    private Integer bposId;

    public SampleBoxPosId() {}

    // Konstruktor mit Long
    public SampleBoxPosId(Long sId, LocalDateTime sStamp, String bId, Integer bposId) {
        this.sId = sId;
        this.sStamp = sStamp;
        this.bId = bId;
        this.bposId = bposId;
    }

    // Getter & Setter (Long)
    public Long getSId() { return sId; }
    public void setSId(Long sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }

    public String getBId() { return bId; }
    public void setBId(String bId) { this.bId = bId; }

    public Integer getBposId() { return bposId; }
    public void setBposId(Integer bposId) { this.bposId = bposId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleBoxPosId that = (SampleBoxPosId) o;
        return Objects.equals(sId, that.sId) &&
                Objects.equals(sStamp, that.sStamp) &&
                Objects.equals(bId, that.bId) &&
                Objects.equals(bposId, that.bposId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sId, sStamp, bId, bposId);
    }
}