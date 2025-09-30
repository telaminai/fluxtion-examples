package com.telamin.fluxtion.example.compile.aot.node;


import com.telamin.fluxtion.example.compile.aot.pricer.PriceCalculator;
import com.telamin.fluxtion.example.compile.aot.pricer.PriceLadder;
import com.telamin.fluxtion.runtime.annotations.ExportService;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;

public class SkewCalculator implements @ExportService(propagate = false)PriceCalculator {

    private final MidCalculator midCalculator;
    private PriceLadder skewedPriceLadder;
    private int skew;

    public SkewCalculator(MidCalculator midCalculator) {
        this.midCalculator = midCalculator;
    }

    public SkewCalculator() {
        this(new MidCalculator());
    }

    @Override
    public void setSkew(int skew) {
        this.skew = skew;
    }

    @OnTrigger
    public boolean calculateSkewedLadder(){
        PriceLadder priceLadder = midCalculator.getPriceLadder();

        int[] bidPrices = priceLadder.getBidPrices();
        for (int i = 0, bidPricesLength = bidPrices.length; i < bidPricesLength; i++) {
            int bidPrice = bidPrices[i];
            bidPrices[i] = bidPrice + skew;
        }

        int[] askPrices = priceLadder.getAskPrices();
        for (int i = 0, askPricesLength = askPrices.length; i < askPricesLength; i++) {
            int askPrice = askPrices[i];
            askPrices[i] = askPrice + skew;
        }

        return true;
    }

    public PriceLadder getSkewedPriceLadder() {
        return midCalculator.getPriceLadder();
    }
}
