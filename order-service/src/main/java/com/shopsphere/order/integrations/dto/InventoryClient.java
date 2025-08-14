package com.shopsphere.order.integrations.dto;

import com.shopsphere.order.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class InventoryClient {
    private final RestTemplate rest;
    private final String baseUrl;

    public InventoryClient(
            RestTemplateBuilder builder,
            @Value("${inventory.base-url}") String baseUrl
    ) {
        this.rest = builder.setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
        this.baseUrl = baseUrl;
    }

    public String reserve(Long productId, int qty, String reference) {
        ReserveStockRequest req = new ReserveStockRequest();
        req.setProductId(productId);
        req.setQuantity(qty);
        req.setReference(reference);

        ResponseEntity<ReserveStockResponse> resp =
                rest.postForEntity(baseUrl + "/inventory/reservations", req, ReserveStockResponse.class);

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null ||
                resp.getBody().getReservationId() == null) {
            throw new ExternalServiceException("Failed to reserve stock");
        }
        return resp.getBody().getReservationId();
    }

    public void release(String reservationId) {
        ReleaseStockRequest req = new ReleaseStockRequest();
        req.setReservationId(reservationId);
        rest.postForEntity(baseUrl + "/inventory/reservations/" + reservationId + "/release", req, Void.class);
    }
}
