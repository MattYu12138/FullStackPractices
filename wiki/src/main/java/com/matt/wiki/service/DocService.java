package com.matt.wiki.service;


import com.github.pagehelper.PageHelper;
import com.matt.wiki.domain.Content;
import com.matt.wiki.domain.Doc;
import com.matt.wiki.domain.DocExample;
import com.matt.wiki.mapper.ContentMapper;
import com.matt.wiki.mapper.DocMapper;
import com.matt.wiki.req.DocQueryReq;
import com.matt.wiki.req.DocSaveReq;
import com.matt.wiki.resp.DocQueryResp;
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
public class DocService {

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        if (!ObjectUtils.isEmpty(docQueryReq.getName())) {
            criteria.andNameLike("%" + docQueryReq.getName() + "%");
        }


        PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        var respList = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setList(respList);

        return pageResp;
    }

    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }


    /*
    * save
    * */
    public void save(DocSaveReq docSaveReq) {
        Doc docSave = CopyUtil.copy(docSaveReq, Doc.class);
        Content contentSave = CopyUtil.copy(docSaveReq, Content.class);
        if(ObjectUtils.isEmpty(docSave.getId())){
            docSave.setId(snowFlake.nextId());
            docMapper.insert(docSave);
            contentSave.setId(docSave.getId());
            contentMapper.insert(contentSave);
        }else{
            docMapper.updateByPrimaryKey(docSave);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(contentSave);
            if(count == 0){
                contentMapper.insert(contentSave);
            }
        }

    }
    /*
    * delete
    * */
    public void delete(Long id){
        System.out.println("正在删除id： " + id);
        docMapper.deleteByPrimaryKey(id);
    }
    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        return content.getContent();
    }
}
