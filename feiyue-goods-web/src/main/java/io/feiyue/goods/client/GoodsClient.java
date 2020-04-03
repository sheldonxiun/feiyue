package io.feiyue.goods.client;

import io.feiyue.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "feiyue-item-service",contextId = "GoodsClient")
public interface GoodsClient extends GoodsApi {

}
