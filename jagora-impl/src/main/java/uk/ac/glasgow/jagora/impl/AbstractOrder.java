package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.Trader;

/**
 * Implements the common properties and behaviours of all order types (both buy and sell).
 * @author Tim
 *
 */
public abstract class AbstractOrder implements Order {

	private final Trader trader;
	private final Stock stock;
	private final Integer initialQuantity;
	protected final List<TickEvent<Trade>> tradeHistory;
	
	public AbstractOrder(Trader trader, Stock stock, Integer quantity){
		this.trader = trader;
		this.stock = stock;
		this.initialQuantity = quantity;
		tradeHistory = new ArrayList<TickEvent<Trade>>();
	}
	
	@Override
	public Trader getTrader() {
		return trader;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public Integer getRemainingQuantity (){
		Integer tradeQuantity = 
			tradeHistory
				.stream()
				.mapToInt(executedTrade -> executedTrade.getEvent().getQuantity())
				.sum();
		
		return initialQuantity - tradeQuantity;
	}
	
	@Override 
	public String toString (){
		return String.format("[%s, %d, %.2f]", stock, getRemainingQuantity(), getPrice());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractOrder other = (AbstractOrder) obj;
		if (getPrice() == null) {
			if (other.getPrice() != null)
				return false;
		} else if (!getPrice().equals(other.getPrice()))
			return false;
		return true;
	}
	
	
}