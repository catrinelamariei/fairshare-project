package commons;

import commons.DTOs.TagDTO;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    
    @ManyToOne
    public Event event;

    @Column(nullable = false)
    public String name;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Color color;
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.REMOVE)
    public Set<Transaction> transactions;

    public enum Color {
        RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
    }

    public Tag() {
        // for object mapper
    }

    public Tag(TagDTO t) {
        this.id = t.id;
        this.name = t.name;
        this.color = t.color;
    }

    public Tag(Event event, String name, Color color) {
        this.event = event;
        if (event!=null) event.addTag(this);
        this.name = name;
        this.color = color;
        this.transactions = new HashSet<Transaction>();
    }

    public UUID getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
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
