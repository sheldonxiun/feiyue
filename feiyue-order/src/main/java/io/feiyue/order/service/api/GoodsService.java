package io.feiyue.order.service.api;

import io.feiyue.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "feiyue-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
