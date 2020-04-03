package io.feiyue.item.service.impl;

import io.feiyue.item.mapper.CategoryMapper;
import io.feiyue.item.pojo.Category;
import io.feiyue.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     *
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    public List<String> queryNamesByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);

        //处理旧集合 返回新集合
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
