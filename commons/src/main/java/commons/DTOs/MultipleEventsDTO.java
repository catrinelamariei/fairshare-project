package commons.DTOs;
import commons.Event;

//import java.util.ArrayList;
import java.util.List;
//import java.util.List;

public class MultipleEventsDTO {
    private List<Event> events;

    public MultipleEventsDTO() {
        this.events = null;
    }

    public MultipleEventsDTO(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

