package uk.ac.glasgow.jagora.impl;

import static java.lang.String.format;
import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.World;

public class DefaultTrade implements Trade {

	private World world;
	private BuyOrder buyOrder;
	private SellOrder sellOrder;
	private Integer quantity;
	private Stock stock;
	private Double price;
	
	private Boolean executed;

	public DefaultTrade(World world, BuyOrder buyOrder, SellOrder sellOffer, Stock stock, Integer quantity, Double price) {
		this.world = world;
		this.buyOrder = buyOrder;
		this.sellOrder = sellOffer;
		this.stock = stock;
		this.quantity = quantity;
		this.price = price;
		
		this.executed = false;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public Double getPrice() {
		return price;
	}

	@Override
	public TickEvent<Trade> execute() throws TradeException {
		
		if (executed)
			throw new TradeException("Already executed");
		
		TickEvent<Trade> tickEvent = world.createTickEvent(this);
		sellOrder.satisfyTrade(tickEvent);
		try {
			buyOrder.satisfyTrade(tickEvent);
		} catch(TradeException e){
			sellOrder.rollBackTrade(tickEvent);
			throw e;
		}
		executed = true;
		return tickEvent;
	}
	
	public String toString(){
		return format(
			"%s-(%s,%d,$%.2f)->%s",
			sellOrder.getTrader().getName(), stock.getName(), quantity, price, buyOrder.getTrader().getName());
	}

}
