package com.remrin.wiki.service;

import com.remrin.wiki.domain.Ebook;
import com.remrin.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper EbookMapper;

    public List<Ebook> list() {
        return EbookMapper.selectByExample(null);
    }
}
