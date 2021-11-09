package com.kciray;

import org.springframework.beans.factory.sterotupe.Component;

@Component
public class ProductService {
    private PromotionsService promotionsService;

    public PromotionsService getPromotionsService(){
        return promotionsService;
    }

    public void setPromotionsService(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }
}
