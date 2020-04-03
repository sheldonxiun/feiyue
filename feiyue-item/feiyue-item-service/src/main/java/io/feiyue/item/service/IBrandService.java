package io.feiyue.item.service;

import io.feiyue.common.pojo.PageResult;
import io.feiyue.item.pojo.Brand;

import java.util.List;

public interface IBrandService {

    /**
     * 根据查询条件,分页并排序查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandById(Long id);
}
