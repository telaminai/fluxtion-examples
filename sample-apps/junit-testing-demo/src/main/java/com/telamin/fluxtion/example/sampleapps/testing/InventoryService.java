package com.telamin.fluxtion.example.sampleapps.testing;

/**
 * Service interface for managing inventory.
 * In production, this would connect to an inventory management system.
 * In tests, this can be easily mocked.
 */
public interface InventoryService {
    
    /**
     * Check if sufficient stock is available for a product.
     * 
     * @param productId the product ID
     * @param quantity the required quantity
     * @return true if sufficient stock is available, false otherwise
     */
    boolean checkStock(String productId, int quantity);
    
    /**
     * Reduce stock for a product.
     * 
     * @param productId the product ID
     * @param quantity the quantity to reduce
     */
    void reduceStock(String productId, int quantity);
}
