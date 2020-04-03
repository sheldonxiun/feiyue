package io.feiyue.cart.client;

import io.feiyue.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("feiyue-item-service")
public interface GoodsClient extends GoodsApi {
}
