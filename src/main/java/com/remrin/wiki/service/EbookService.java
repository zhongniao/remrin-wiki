package com.remrin.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.remrin.wiki.domain.Ebook;
import com.remrin.wiki.domain.EbookExample;
import com.remrin.wiki.mapper.EbookMapper;
import com.remrin.wiki.req.EbookQueryReq;
import com.remrin.wiki.req.EbookSaveReq;
import com.remrin.wiki.resp.EbookQueryResp;
import com.remrin.wiki.resp.PageResp;
import com.remrin.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<EbookResp> respList = new ArrayList<>();
        // for (Ebook ebook : ebookList) {
        //    EbookResp ebookResp = new EbookResp();
        //    BeanUtils.copyProperties(ebook, ebookResp);
        //    respList.add(ebookResp);
        // }

        // 列表复制
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req)
    {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            ebookMapper.insert(ebook);
        } else {
            // 更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
