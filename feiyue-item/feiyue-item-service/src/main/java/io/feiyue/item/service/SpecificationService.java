package io.feiyue.item.service;

import io.feiyue.item.mapper.SpecGroupMapper;
import io.feiyue.item.mapper.SpecParamMapper;
import io.feiyue.item.pojo.SpecGroup;
import io.feiyue.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    @Autowired
    private SpecParamMapper specParamMapper;


    /**
     * 根据分类ID查询参数组
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return this.specGroupMapper.select(record);
    }

    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);
    }

    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> groups = this.querySpecGroupsByCid(cid);
        groups.forEach(group->{
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }
}
