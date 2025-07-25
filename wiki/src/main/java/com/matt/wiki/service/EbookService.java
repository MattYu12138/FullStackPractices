package com.matt.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.matt.wiki.domain.Ebook;
import com.matt.wiki.domain.EbookExample;
import com.matt.wiki.mapper.EbookMapper;
import com.matt.wiki.req.EbookQueryReq;
import com.matt.wiki.req.EbookSaveReq;
import com.matt.wiki.resp.EbookQueryResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.util.CopyUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if (!ObjectUtils.isEmpty(ebookQueryReq.getName())) {
            criteria.andNameLike("%" + ebookQueryReq.getName() + "%");
        }


        PageHelper.startPage(ebookQueryReq.getPage(), ebookQueryReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());

//        List<EbookQueryResp> respList = new ArrayList<>();
//
//        for (Ebook ebook : ebookList) {
//            EbookQueryResp ebookResp = new EbookQueryResp();
////            BeanUtils.copyProperties(ebook, ebookResp);
////            respList.add(ebookResp);
//
//            EbookQueryResp ebookREsp = CopyUtil.copy(ebook, EbookQueryResp.class);
//
//            respList.add(ebookREsp);
//        }


        var respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /*
    * save
    * */
    public void save(EbookSaveReq ebookSaveReq) {
        Ebook ebookSave = CopyUtil.copy(ebookSaveReq, Ebook.class);
        if(ObjectUtils.isEmpty(ebookSave.getId())){
            ebookMapper.insert(ebookSave);
        }else{
            ebookMapper.updateByPrimaryKey(ebookSave);
        }

    }

}
