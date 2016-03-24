package uk.ac.glasgow.jagora.impl;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Market;
import uk.ac.glasgow.jagora.OrderBook;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.World;

public class ContinuousOrderDrivenMarket implements Market {
	
	private final OrderBook<BuyOrder> buyBook;
	private final OrderBook<SellOrder> sellBook;
	
	private final Stock stock;
	private final World world;
	
	public ContinuousOrderDrivenMarket(Stock stock, World world) {
		this.stock = stock;
		this.world = world;
		buyBook = new DefaultOrderBook<BuyOrder>(world);
		sellBook = new DefaultOrderBook<SellOrder>(world);
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public List<TickEvent<Trade>> doClearing() {
		List<TickEvent<Trade>> result = new ArrayList<TickEvent<Trade>>();
		BuyOrder bestBid = buyBook.getBestOrder();
		SellOrder bestOffer = sellBook.getBestOrder();
		
		while (aTradeIsPossible(bestBid, bestOffer)){
			Integer quantity = 
				Math.min(bestBid.getRemainingQuantity(), bestOffer.getRemainingQuantity());
			Double price =
				bestOffer.getPrice();
			Trade trade = new DefaultTrade(world, bestBid, bestOffer, stock, quantity, price);
			try {
				result.add(trade.execute());
			} catch (TradeException e) {
				if (e.culprit == bestBid.getTrader())
					cancelBuyOrder(bestBid);
				else 
					cancelSellOrder(bestOffer);
			}
			bestBid = buyBook.getBestOrder();
			bestOffer = sellBook.getBestOrder();
		}
		return result;
		
	}

	private boolean aTradeIsPossible(BuyOrder bestBid, SellOrder bestOffer) {
		return bestBid !=null && bestOffer != null && bestBid.getPrice() >= bestOffer.getPrice();
	}

	@Override
	public void placeBuyOrder(BuyOrder buyOrder) {
		buyBook.recordOrder(buyOrder);
	}

	@Override
	public void placeSellOrder(SellOrder sellOrder) {
		sellBook.recordOrder(sellOrder);

	}

	@Override
	public void cancelBuyOrder(BuyOrder buyOrder) {
		buyBook.cancelOrder(buyOrder);
	}

	@Override
	public void cancelSellOrder(SellOrder sellOrder) {
		sellBook.cancelOrder(sellOrder);
	}

	@Override
	public Double getBestBid() {
		BuyOrder buyOrder = buyBook.getBestOrder();
		return buyOrder == null?null:buyOrder.getPrice();
	}

	@Override
	public Double getBestOffer() {
		SellOrder sellOrder = sellBook.getBestOrder();
		return sellOrder == null?null:sellOrder.getPrice();
	}
	
	public String toString(){
		return format("%s[bids:%s, offers:%s]", stock, buyBook, sellBook);
	}
}
