package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.Column;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(columnDefinition = "CLOB NOT NULL")
    public String name;


    public List<String> tags;

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
     * @param tags  list of tags the event is associated to
     */
    public Event(String name, List<String> tags) {
        this.name = name;
        this.tags = tags;
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
