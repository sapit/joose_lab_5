package uk.ac.glasgow.jagora.impl;

import static java.lang.String.format;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.Trader;

public class LimitSellOrder extends AbstractSellOrder {

	private Double price;
	
	public LimitSellOrder(Trader trader, Stock stock, Integer quantity, Double price) {
		super(trader, stock, quantity);
		this.price = price;
	}

	@Override
	public Double getPrice() {
		return price;
	}
	
	public String toString(){
		return format("LimSO[%s, %s, %d, %.2f]",
			getTrader(), getStock(), getRemainingQuantity(), price);
	}

}
