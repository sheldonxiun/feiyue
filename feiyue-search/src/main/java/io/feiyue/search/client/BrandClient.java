package io.feiyue.search.client;

import io.feiyue.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "feiyue-item-service",contextId = "BrandClient")
public interface BrandClient extends BrandApi {
}
