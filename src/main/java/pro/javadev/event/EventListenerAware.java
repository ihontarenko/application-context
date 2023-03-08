package pro.javadev.event;

public interface EventListenerAware {

    void setEventListener(EventListener listener);

    EventListener getEventListener();

}
