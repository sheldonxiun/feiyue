package io.feiyue.search.client;

import io.feiyue.item.bo.SpuBo;
import io.feiyue.FeiyueSearchApplication;
import io.feiyue.common.pojo.PageResult;
import io.feiyue.search.pojo.Goods;
import io.feiyue.search.repository.GoodsRepository;
import io.feiyue.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = FeiyueSearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testElastic() {
        // 创建索引库，以及映射
        this.template.createIndex(Goods.class);
        this.template.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        do {
            //分页查询spu,获取分页结果集
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, null, page, rows);
            //获取当前页的数据
            List<SpuBo> items = result.getItems();
            //处理List<SpuBo> ==> List<Goods>
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }).collect(Collectors.toList());

            //执行新增数据的方法
            this.goodsRepository.saveAll(goodsList);

            rows = items.size();
            page++;

        } while (rows == 100);
    }
}