package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;
import uk.ac.glasgow.jagora.impl.AbstractOrder;

/**
 * Defines the trade satisfaction behaviour of a SellOrder.
 * @author tws
 *
 */
public abstract class AbstractSellOrder extends AbstractOrder implements SellOrder {

	public AbstractSellOrder(Trader trader, Stock stock, Integer quantity) {
		super(trader, stock, quantity);
	}

	@Override
	public void satisfyTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		Trade trade = tradeEvent.getEvent();
		getTrader().sellStock(trade.getStock(), trade.getQuantity(), trade.getPrice());
		tradeHistory.add(tradeEvent);
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		Trade trade = tradeEvent.getEvent();
		getTrader().buyStock(trade.getStock(), trade.getQuantity(), trade.getPrice());
		tradeHistory.remove(tradeEvent);
	}
	
	@Override
	public int compareTo(SellOrder order) {
		return getPrice().compareTo(order.getPrice());
	}

}
