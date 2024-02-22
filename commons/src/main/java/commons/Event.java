package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(columnDefinition = "CLOB NOT NULL")
    public String name;

    @OneToMany(mappedBy="event")
    public Set<Tag> tags;


    /**
     * Empty constructor for event
     */
    @SuppressWarnings("unused")
    private Event() {
        // for object mapper
    }

    /**
     * Constructor for event
     * @param name  name of the event
     */
    public Event(String name) {
        this.name = name;
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
