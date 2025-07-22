package com.matt.wiki.service;


import com.matt.wiki.domain.Ebook;
import com.matt.wiki.domain.EbookExample;
import com.matt.wiki.mapper.EbookMapper;
import com.matt.wiki.req.EbookReq;
import com.matt.wiki.resp.EbookResp;
import com.matt.wiki.util.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        criteria.andNameLike("%" + ebookReq.getName() + "%");

        List<Ebook> ebookList =  ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook, ebookResp);
////            respList.add(ebookResp);
//
//            EbookResp ebookREsp = CopyUtil.copy(ebook, EbookResp.class);
//
//            respList.add(ebookREsp);
//        }

        var respList = CopyUtil.copyList(ebookList, EbookResp.class);

        return respList;
    }

}
