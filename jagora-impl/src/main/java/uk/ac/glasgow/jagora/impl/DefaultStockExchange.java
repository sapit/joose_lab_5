package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Market;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.StockExchangeRestricted;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.World;

public class DefaultStockExchange extends StockExchangeDecorator implements StockExchangeRestricted{

	private Map<Stock,Market> markets;
	private World world;
	private List<TickEvent<Trade>> tradeHistory;
	
	public DefaultStockExchange(World world){
		super(world);
	}
	
	public void doClearing() {
		for (Market market: markets.values())
			tradeHistory.addAll(market.doClearing());
	}

	public List<TickEvent<Trade>> getTradeHistory(Stock stock) {
		return tradeHistory
			.stream()
			.filter(tradeEvent->tradeEvent.getEvent().getStock().equals(stock))
			.collect(Collectors.toList());
	}
	

}
