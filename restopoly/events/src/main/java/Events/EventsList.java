package Events;

import java.util.ArrayList;
import java.util.List;

public class EventsList {
    public List<Event> events = new ArrayList<>();

    public EventsList() {}

    public EventsList(List<Event> events) {
        this.events = events;
    }
}
