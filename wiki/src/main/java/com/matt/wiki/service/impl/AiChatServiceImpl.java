package com.matt.wiki.service.impl;

import com.matt.wiki.aioutput.GuestRegisterOutput;
import com.matt.wiki.aioutput.IntentionOutput;
import com.matt.wiki.aiservice.AiAssistant;
import com.matt.wiki.aop.ChatFlow;
import com.matt.wiki.entity.ChatHistoryEntity;
import com.matt.wiki.entity.GuestRegisterEntity;
import com.matt.wiki.repository.ChatHistoryRepository;
import com.matt.wiki.repository.GuestRegisterRepository;
import com.matt.wiki.service.AiChatService;
import com.matt.wiki.service.CategoryService;
import com.matt.wiki.vo.ChatHistoryVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private AiAssistant aiAssistant;

//    @Resource
//    private AiIntentionAssistant aiIntentionAssistant;

    @Resource
    private ChatHistoryRepository chatHistoryRepository;

    @Resource
    private GuestRegisterRepository guestRegisterRepository;

    @ChatFlow
    @Override
    public String chatStream(String userId, String message) {

//        用户意图
        IntentionOutput intention = aiAssistant.intention(userId, message);
        LOG.info("----" + intention);
        Integer intent = intention.getIntention();
        String output = intention.getOutput();
        switch (intent){
            case 1 :
//                    1.预约
                output = guestRegister(userId,message);
                break;
            case 2 :
//                    2.费用
                break;
            case 3 :
//                    3.进度查询
                break;
            case 4 :
//                    4.FAQ
                break;
            case 5 :
//                    5.其他"
                break;
            default :
                return output;
        }
        return output;
    }

    @Override
    public Page<ChatHistoryVo> queryChatHistory(String userId, Pageable page) {
        // 准备VO列表
        List<ChatHistoryVo> voList = new ArrayList<>();

        // 遍历实体并转换为VO
        Page<ChatHistoryEntity> entityPage = chatHistoryRepository.findAllBySessionId(userId, page);

        for (ChatHistoryEntity entity : entityPage.getContent()) {
            // 过滤掉非USER角色
            if("1".equals(entity.getRole())){
                continue;
            }

            ChatHistoryVo vo = ChatHistoryVo.of(
                    entity.getRole(),
                    entity.getContent(),
                    entity.getCreatedDate()
            );
            voList.add(vo);
        }

        // 重新构建分页对象
        return new PageImpl<>(
                voList,                          // VO列表
                entityPage.getPageable(),        // 保持分页信息
                entityPage.getTotalElements()    // 保持总记录数
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearChatHistory(String userId) {
        chatHistoryRepository.deleteBySessionId(userId);
    }

//    @Override
//    public String embeddingIndex() {
//        //将找到失物登记的数据进行查询并且写入到一个文件中去
//        String path = "/Users/Zhuanz/Desktop/data/data.txt";
//        FileUtil.writeString("",path, StandardCharsets.UTF_8);
//        List<String> collect = StreamSupport.stream(lostPropertyRepository.findAll().spliterator(), false).map(JsonUtils::toJson).toList();
//        FileUtil.appendLines(collect,path, StandardCharsets.UTF_8);
//        //加载并解析文档的内容
//        DocumentParser documentParser = new TextDocumentParser();
//        //加载
//        Document document = FileSystemDocumentLoader.loadDocument(FileUtil.getAbsolutePath(path), documentParser);
//        //定义文档分割器
//        DocumentSplitter splitter  = new DocumentByLineSplitter(200,100);
//        List<TextSegment> split = splitter.split(document);
//        //进行向量化并且存储到向量数据库里面
//        List<Embedding> content = embeddingModel.embedAll(split).content();
//        embeddingStore.addAll(content,split);
//        return "success";
//    }
//
//    @Override
//    public List<String> embeddingQuery(String message) {
//        //构造检索条件
//        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder().
//                queryEmbedding(embeddingModel.embed(message).content())
//                .maxResults(2).minScore(0.8).build();
//        //进行向量检索
//        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);
//
//        //返回向量检索结果
//        return result.matches().stream().map(x -> x.embedded().text()).collect(Collectors.toList());
//    }

    private String guestRegister(String userId, String message){
        GuestRegisterOutput guestRegisterOutput = aiAssistant.guestRegister(userId,message);
        LOG.info("----" + guestRegisterOutput);
        if(guestRegisterOutput.getCompleted()){
//持久化层数据库
            GuestRegisterEntity entity = new GuestRegisterEntity();
            BeanUtils.copyProperties(guestRegisterOutput,entity);
            guestRegisterRepository.save(entity);

        }
        return guestRegisterOutput.getOutput();
    }
}
