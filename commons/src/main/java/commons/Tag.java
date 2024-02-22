package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@IdClass(TagPK.class)
public class Tag {

    @ManyToOne
    @Id
    @JoinColumn(name="event_id", referencedColumnName ="id", nullable=false)
    public Event event;

    @Id
    public String name;

    @Column(columnDefinition = "CLOB NOT NULL")
    public Color color;

    public enum Color {
        RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET;
    }

    private Tag() {
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
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
