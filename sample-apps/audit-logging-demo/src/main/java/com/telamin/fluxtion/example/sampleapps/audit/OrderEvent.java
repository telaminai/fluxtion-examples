package com.telamin.fluxtion.example.sampleapps.audit;

public record OrderEvent(String id, String symbol, int qty, double price) {
    @Override
    public String toString() {
        return "OrderEvent{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
