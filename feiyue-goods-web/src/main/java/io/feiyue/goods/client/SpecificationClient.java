package io.feiyue.goods.client;

import io.feiyue.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "feiyue-item-service",contextId = "SpecificationClient")
public interface SpecificationClient extends SpecificationApi {

}
