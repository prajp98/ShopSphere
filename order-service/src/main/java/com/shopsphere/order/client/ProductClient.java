package com.shopsphere.order.client;

import com.shopsphere.order.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service",url= "http://localhost:8082/products")
public interface ProductClient {
    @GetMapping("/bulk")
    List<ProductDto> getProductsByIds(@RequestParam("ids") List<Long> ids);
}
