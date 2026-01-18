package at.ac.tgm.restbackendci_cd.id;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BoxPosId implements Serializable {
    @Column(name = "b_id", length = 4)
    private String bId;

    @Column(name = "bpos_id")
    private Integer bposId;

    public BoxPosId() {}
    public BoxPosId(String bId, Integer bposId) {
        this.bId = bId;
        this.bposId = bposId;
    }

    // Getters/Setters/Equals/HashCode (Standard generieren)
    public String getBId() { return bId; }
    public void setBId(String bId) { this.bId = bId; }
    public Integer getBposId() { return bposId; }
    public void setBposId(Integer bposId) { this.bposId = bposId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxPosId that = (BoxPosId) o;
        return Objects.equals(bId, that.bId) && Objects.equals(bposId, that.bposId);
    }
    @Override
    public int hashCode() { return Objects.hash(bId, bposId); }
}