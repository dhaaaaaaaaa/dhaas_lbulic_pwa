package at.ac.tgm.restbackendci_cd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "box", schema = "venlab")
public class Box {

    @Id
    @Column(name = "b_id", length = 4)
    @NotBlank(message = "Box ID ist erforderlich.")
    private String id;

    @NotBlank(message = "Name ist erforderlich.")
    private String name;

    @Column(name = "num_max")
    @NotNull(message = "Maximalanzahl muss angegeben werden.")
    @Min(value = 1, message = "Der Wert muss mindestens 1 sein.")
    private Integer numMax;

    private Integer type;
    private String comment;

    @Column(name = "date_exported")
    private LocalDateTime dateExported;

    // Getter und Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getNumMax() { return numMax; }
    public void setNumMax(Integer numMax) { this.numMax = numMax; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getDateExported() { return dateExported; }
    public void setDateExported(LocalDateTime dateExported) { this.dateExported = dateExported; }
}