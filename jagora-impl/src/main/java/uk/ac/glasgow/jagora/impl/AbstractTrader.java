package uk.ac.glasgow.jagora.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

public abstract class AbstractTrader implements Trader {

	private final String name;
	private Double cash;
	private final Map<Stock,Integer> inventory;
	
	public AbstractTrader(String name, Double cash, Map<Stock, Integer> inventory) {
		this.name = name;
		this.cash = cash;
		this.inventory = new HashMap<Stock,Integer>(inventory);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getCash() {
		return cash;
	}
	
	@Override
	public Set<Stock> getTradingStocks() {
		return new HashSet<Stock>(inventory.keySet());
	}

	@Override
	public void sellStock(Stock stock, Integer quantity, Double price) throws TradeException {
		Integer availableQuantity = getInventoryHolding(stock);
		if (availableQuantity < quantity)
			throw new TradeException("Seller couldn't satisfy trade.", this);
		else {
			Double cost = quantity * price;
			cash += cost;
			inventory.put(stock, availableQuantity - quantity);
		}
	}

	@Override
	public void buyStock(Stock stock, Integer quantity, Double price) throws TradeException {
		double cost = price  * quantity;
		if (cash < cost)
			throw new TradeException("Buyer couldn't satisfy trade", this);
		else {
			Integer availableQuantity = getInventoryHolding(stock);
			cash -= cost;
			inventory.put(stock, availableQuantity + quantity);
		}
	}

	@Override
	public Integer getInventoryHolding(Stock stock) {
		return inventory.getOrDefault(stock, 0);
	}
	
	@Override 
	public String toString (){
		return String.format("%s[cash=%.2f,stocks=%s]",getName(),getCash(),inventory);
	}

}
