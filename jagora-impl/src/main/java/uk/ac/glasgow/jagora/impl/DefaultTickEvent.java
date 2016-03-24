package uk.ac.glasgow.jagora.impl;

import static java.lang.String.format;
import uk.ac.glasgow.jagora.TickEvent;

public class DefaultTickEvent<T> implements TickEvent<T> {

	private final T event;
	private final Long tick;
	
	public DefaultTickEvent(T event, Long tick) {
		this.event = event;
		this.tick = tick;
	}

	@Override
	public int compareTo(TickEvent<T> tickEvent) {
		return tick.compareTo(tickEvent.getTick());
	}

	@Override
	public T getEvent() {
		return event;
	}

	@Override
	public Long getTick() {
		return tick;
	}
	
	public String toString(){
		return format("%d=[%s]", tick, event);
	}
}
