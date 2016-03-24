package uk.ac.glasgow.jagora.impl;

import static java.lang.String.format;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.Trader;

public class LimitBuyOrder extends AbstractBuyOrder {

	private Double price;

	public LimitBuyOrder(Trader trader, Stock stock, Integer quantity, Double price) {
		super(trader, stock, quantity);
		this.price = price;
	}

	@Override
	public Double getPrice() {
		return price;
	}
	
	public String toString(){
		return format("LimBO[%s, %s, %d, %.2f]",
			getTrader().getName(), getStock().getName(), getRemainingQuantity(), price);
	}


}
