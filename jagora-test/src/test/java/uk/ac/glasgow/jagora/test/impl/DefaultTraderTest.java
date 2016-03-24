package uk.ac.glasgow.jagora.test.impl;

import static uk.ac.glasgow.jagora.test.stub.StubStock.lemons;

import org.junit.Before;

import uk.ac.glasgow.jagora.impl.DefaultTrader;
import uk.ac.glasgow.jagora.test.TraderTest;

public class DefaultTraderTest extends TraderTest {
			
	@Before
	public void setUp() throws Exception {
		
		stock = lemons;
		quantity = 100;
		cash = 1000.0;
		name = "default";
		
		this.trader = new DefaultTrader(name, cash, stock, quantity);
	}
}
