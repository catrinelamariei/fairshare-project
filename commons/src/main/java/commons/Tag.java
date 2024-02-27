package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Tag {

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name="event_id", referencedColumnName ="id", nullable=false)
    public Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String name;


    @Column(columnDefinition = "enum('RED','ORANGE','YELLOW','GREEN','BLUE','INDIGO','VIOLET')")
    @Enumerated(EnumType.STRING)
    public Color color;


    public enum Color {
        RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
    }

    public Tag() {
        // for object mapper
    }


    public Tag(Event event, String name, Color color) {
        this.event = event;
        this.name = name;
        this.color = color;
    }


    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, name, color);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
