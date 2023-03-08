package pro.javadev.event;

public interface Event {

    EventType getEventType();

    Object getEventData();

}
