package io.feiyue.item.service;

import io.feiyue.item.pojo.Category;

import java.util.List;

public interface ICategoryService {
    /**
     *  根据父节点查询子节点
     * @param pid
     * @return
     */
    List<Category> queryCategoriesByPid(Long pid);


    List<String> queryNamesByIds(List<Long> ids);
}
