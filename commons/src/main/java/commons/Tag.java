package commons;

import commons.DTOs.TagDTO;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.*;

import java.util.*;

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
        RED("#FF000080"),
        ORANGE("#FFA50080"),
        YELLOW("#FFFF0080"),
        GREEN("#00800080"),
        BLUE("#ADD8E6"),
        INDIGO("#0000FF33"),
        VIOLET("#EE82EE80");

        public final String colorCode;

        Color(String colorCode) {
            this.colorCode = colorCode;
        }
    }

    public Tag() {
        // for object mapper
    }

    public Tag(TagDTO t) {
        this.id = t.id;
        this.name = t.name;
        this.color = t.color;
        this.transactions = new HashSet<>();
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
