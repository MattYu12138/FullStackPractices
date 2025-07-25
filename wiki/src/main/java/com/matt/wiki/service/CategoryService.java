package com.matt.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

//        if (!ObjectUtils.isEmpty(categoryQueryReq.getName())) {
//            criteria.andNameLike("%" + categoryQueryReq.getName() + "%");
//        }


        PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());

//        List<CategoryQueryResp> respList = new ArrayList<>();
//
//        for (Category category : categoryList) {
//            CategoryQueryResp categoryResp = new CategoryQueryResp();
////            BeanUtils.copyProperties(category, categoryResp);
////            respList.add(categoryResp);
//
//            CategoryQueryResp categoryREsp = CopyUtil.copy(category, CategoryQueryResp.class);
//
//            respList.add(categoryREsp);
//        }


        var respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
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
