package io.feiyue.auth.client;

import io.feiyue.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("feiyue-user-service")
public interface UserClient extends UserApi {

}
