package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

public abstract class AbstractBuyOrder extends AbstractOrder implements BuyOrder  {

	public AbstractBuyOrder(Trader trader, Stock stock, Integer quantity) {
		super(trader, stock, quantity);
	}


	@Override
	public void satisfyTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		Trade trade = tradeEvent.getEvent();
		getTrader().buyStock(getStock(), trade.getQuantity(), trade.getPrice());
		tradeHistory.add(tradeEvent);
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		Trade trade = tradeEvent.getEvent();
		getTrader().sellStock(getStock(), trade.getQuantity(), trade.getPrice());
		tradeHistory.remove(tradeEvent);
	}

	@Override
	public int compareTo(BuyOrder order) {
		return order.getPrice().compareTo(getPrice());
	}

}
