package at.ac.tgm.restbackendci_cd.id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import com.fasterxml.jackson.annotation.JsonFormat;

@Embeddable
public class SampleId implements Serializable {

    // ÄNDERUNG: String statt Long
    @Column(name = "s_id")
    private String sId;

    @Column(name = "s_stamp")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sStamp;

    public SampleId() {}

    public SampleId(String sId, LocalDateTime sStamp) {
        this.sId = sId;
        this.sStamp = sStamp;
    }

    public String getSId() { return sId; }
    public void setSId(String sId) { this.sId = sId; }

    public LocalDateTime getSStamp() { return sStamp; }
    public void setSStamp(LocalDateTime sStamp) { this.sStamp = sStamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleId sampleId = (SampleId) o;
        return Objects.equals(sId, sampleId.sId) && Objects.equals(sStamp, sampleId.sStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sId, sStamp);
    }
}