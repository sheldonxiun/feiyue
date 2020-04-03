package io.feiyue.search.client;

import io.feiyue.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "feiyue-item-service",contextId = "CategoryClient")
public interface CategoryClient extends CategoryApi {
}
