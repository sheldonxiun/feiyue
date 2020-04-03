package io.feiyue.cart.service;

import io.feiyue.cart.client.GoodsClient;
import io.feiyue.cart.interceptor.LoginInterceptor;
import io.feiyue.cart.pojo.Cart;
import io.feiyue.common.pojo.UserInfo;
import io.feiyue.common.utils.JsonUtils;
import io.feiyue.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    public void addCart(Cart cart) {

        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String  key = cart.getSkuId().toString();
        Integer num = cart.getNum();

        //判断当前的商品是否在购物车中
        if (hashOperations.hasKey(key)) {
            //在,更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            //不在,新增购物车
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());

            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
        }

        hashOperations.put(key, JsonUtils.serialize(cart));

    }

    public List<Cart> queryCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)) {
            return null;
        }

        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);

        //获取购物车map中所有cart值集合
        List<Object> cartsJson = hashOperations.values();

        //如果购物车集合为空,直接返回null
        if (CollectionUtils.isEmpty(cartsJson)) {
            return null;
        }

        //把List<Object>集合转化为List<Cart>集合
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)) {
            return;
        }

        Integer num = cart.getNum();

        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);

        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();

        cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);

        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }
}
