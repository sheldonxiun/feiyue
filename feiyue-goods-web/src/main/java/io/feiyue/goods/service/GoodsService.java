package io.feiyue.goods.service;

import io.feiyue.goods.client.BrandClient;
import io.feiyue.goods.client.CategoryClient;
import io.feiyue.goods.client.GoodsClient;
import io.feiyue.goods.client.SpecificationClient;
import io.feiyue.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> model = new HashMap<>();

        //获取spu
        Spu spu = this.goodsClient.querySpuById(spuId);
        //获取sou'Detail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        //查询分类:List<Map<String,Object>>
        List<Long>   cids  = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        //初始化一个分类的map
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));

            categories.add(map);
        }

        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //初始化特殊规格参数的map
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param->{
            paramMap.put(param.getId(), param.getName());
        });


        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("categories", categories);
        model.put("brand", brand);
        model.put("skus", skus);
        model.put("groups", groups);
        model.put("paramMap", paramMap);

        return model;
    }
}
