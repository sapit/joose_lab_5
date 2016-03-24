package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.OrderBook;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.World;

public class DefaultOrderBook<O extends Order & Comparable<O>> implements OrderBook<O> {

	private final Queue<TickEvent<O>> backing;
	private final World world; 
	
	public DefaultOrderBook(World world) {
		this.backing = new PriorityQueue<TickEvent<O>>(new OrderBookComparator());
		this.world = world;
	}
	
	@Override
	public void recordOrder(O order) {
		backing.offer(world.createTickEvent(order));
	}

	@Override
	public void cancelOrder(O order) {
		TickEvent<O> toRemove = null;
		for (TickEvent<O> tickOrder: backing)
			if (tickOrder.getEvent() == order){
				toRemove = tickOrder;
				break;
			}
		if (toRemove != null) backing.remove(toRemove);
	}

	@Override
	public O getBestOrder() {
		TickEvent<O> tickOrder = backing.peek();
				
		while (tickOrder != null && tickOrder.getEvent().getRemainingQuantity() <= 0){
			backing.poll();
			tickOrder = backing.peek();
		}
		
		return tickOrder == null ? null : tickOrder.getEvent();
	}

	@Override
	public List<TickEvent<O>> getOrdersAsList() {
		List<TickEvent<O>> result = new ArrayList<TickEvent<O>>();
		result.addAll(backing);
		return result;
	}
	
	private class OrderBookComparator implements Comparator<TickEvent<O>> {

		@Override
		public int compare(TickEvent<O> tickEvent1, TickEvent<O> tickEvent2) {
			Integer comparison = tickEvent1.getEvent().compareTo(tickEvent2.getEvent());
			if (comparison == 0)
				comparison = tickEvent1.compareTo(tickEvent2);
			return comparison;
		}
	}
	
	@Override
	public String toString (){
		return backing.toString();
	}
}
