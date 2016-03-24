package uk.ac.glasgow.jagora.impl.demos;

import static java.lang.String.format;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.Trader;
import uk.ac.glasgow.jagora.impl.DefaultStock;
import uk.ac.glasgow.jagora.impl.DefaultStockExchange;
import uk.ac.glasgow.jagora.impl.DefaultTrader;
import uk.ac.glasgow.jagora.impl.DefaultWorld;
import uk.ac.glasgow.jagora.impl.LimitBuyOrder;
import uk.ac.glasgow.jagora.impl.LimitSellOrder;
import uk.ac.glasgow.jagora.impl.RandomTrader;

public class DemoA {
	public static void main (String [] args){
		StockExchange stockExchange = new DefaultStockExchange(new DefaultWorld());
		Stock lemons = new DefaultStock("lemons");
		
		
		Trader bootstrapper = new DefaultTrader("BootStrapper", 100.0, lemons, 50);
		stockExchange.placeBuyOrder(new LimitBuyOrder(bootstrapper, lemons, 5, 9.0));
		stockExchange.placeSellOrder(new LimitSellOrder(bootstrapper, lemons, 5, 10.0));

		Random sourceRandom = new Random(1);		
		List<Trader> traders = new ArrayList<Trader>();

		range(0, 100)
			.forEach(i -> traders.add(
				new RandomTrader(
					format("Trader[%d]",i), 100.0, lemons, 50, 2, 5.0,
					new Random(sourceRandom .nextInt()))));
		
		for (Integer i : range(0,1000).toArray()){
			Integer nextIndex = sourceRandom.nextInt(traders.size());
			Trader trader = traders.get(nextIndex);
			trader.speak(stockExchange);
			stockExchange.doClearing();
		}
		
		List<TickEvent<Trade>> trades = stockExchange.getTradeHistory(lemons);
		trades.stream().forEach(trade -> System.out.println(trade));
	}
}
