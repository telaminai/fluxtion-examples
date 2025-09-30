package com.telamin.fluxtion.example.compile.aot.node;


import com.telamin.fluxtion.example.compile.aot.pricer.PriceCalculator;
import com.telamin.fluxtion.example.compile.aot.pricer.PriceLadder;
import com.telamin.fluxtion.runtime.annotations.ExportService;

public class PriceDistributor implements @ExportService(propagate = false)PriceCalculator {
    private PriceLadder priceLadder;

    public PriceLadder getPriceLadder() {
        return priceLadder;
    }

    public void setPriceLadder(PriceLadder priceLadder) {
        this.priceLadder = priceLadder;
    }
}
