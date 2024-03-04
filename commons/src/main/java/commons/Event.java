package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="event", cascade = CascadeType.ALL)
    private Set<Tag> tags;

    @Column (nullable = false)
    private Date creationDate;

    @OneToMany(mappedBy="event", cascade = CascadeType.ALL)
    public Set<Participant> participants;


    @SuppressWarnings("unused")
    public Event() {
        // for object mapper
    }

    public Event(String name) {
        this.name = name;
        this.tags = new HashSet<>();
        this.participants = new HashSet<>();
        this.creationDate = new Date();
    }

    public boolean addTag(Tag tag) {
        if(tags == null) tags = new HashSet<>();
        if (tags.contains(tag)) return false;
        else tags.add(tag);
        tag.event = this;
        return true;
    }

    public boolean deleteTag(Tag tag) {
        if (tag == null) return false;
//        var temp1 = tags.contains(tag);
        var temp = tags.remove(tag);
        return temp;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
