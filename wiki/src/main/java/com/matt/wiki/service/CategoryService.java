package com.matt.wiki.service;


import com.github.pagehelper.PageHelper;
import com.matt.wiki.domain.Category;
import com.matt.wiki.domain.CategoryExample;
import com.matt.wiki.mapper.CategoryMapper;
import com.matt.wiki.req.CategoryQueryReq;
import com.matt.wiki.req.CategorySaveReq;
import com.matt.wiki.resp.CategoryQueryResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.util.CopyUtil;
import com.matt.wiki.util.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if (!ObjectUtils.isEmpty(categoryQueryReq.getName())) {
            criteria.andNameLike("%" + categoryQueryReq.getName() + "%");
        }


        PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        var respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setList(respList);

        return pageResp;
    }

    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return respList;
    }


    /*
    * save
    * */
    public void save(CategorySaveReq categorySaveReq) {
        Category categorySave = CopyUtil.copy(categorySaveReq, Category.class);
        if(ObjectUtils.isEmpty(categorySave.getId())){
            categorySave.setId(snowFlake.nextId());
            categoryMapper.insert(categorySave);
        }else{
            categoryMapper.updateByPrimaryKey(categorySave);
        }

    }
    /*
    * delete
    * */
    public void delete(Long id){
        System.out.println("正在删除id： " + id);
        categoryMapper.deleteByPrimaryKey(id);
    }

}
