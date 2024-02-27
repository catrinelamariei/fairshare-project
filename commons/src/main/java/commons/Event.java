package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="event", cascade = CascadeType.ALL)
    private Set<Tag> tags;


    @SuppressWarnings("unused")
    public Event() {
        // for object mapper
    }

    public Event(String name) {
        this.name = name;
        this.tags = new HashSet<>();
    }

    public boolean addTag(Tag tag) {
        if (tags.contains(tag)) return false;
        else tags.add(tag);
        return true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // this is a custom equals method that doesn't consider tags
    // need some method later on to ensure event with same name can't be instantiated multiple times
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return id == event.id && Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
