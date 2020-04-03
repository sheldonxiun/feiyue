package io.feiyue.item.mapper;

import io.feiyue.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Select("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("select * from tb_brand a inner join tb_category_brand b on a.id=b.brand_id where b.category_id=#{cid}")
    List<Brand> selectBrandsByCid(Long cid);
}
