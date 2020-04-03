package io.feiyue.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.feiyue.common.pojo.PageResult;
import io.feiyue.item.mapper.BrandMapper;
import io.feiyue.item.pojo.Brand;
import io.feiyue.item.service.IBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件,分页并排序查询品牌信息
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //初始化example对象
        Example          example  = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();


        //由key传入的值,根据name查询,或者根据字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        //添加分页条件
        PageHelper.startPage(page, rows);

        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        //查询数据库,返回品牌列表
        List<Brand> brands = this.brandMapper.selectByExample(example);

        //封装成PageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        //封装成PageResult,分页结果集并返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @param cids
     */
    @Transactional //添加事务后,就不用判断是否插入成功.
    public void saveBrand(Brand brand, List<Long> cids) {

        //添加品牌
        this.brandMapper.insertSelective(brand);

        //再新增中间表
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });
    }


    /**
     * 根据分类id串品牌列表
     *
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandsByCid(cid);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
