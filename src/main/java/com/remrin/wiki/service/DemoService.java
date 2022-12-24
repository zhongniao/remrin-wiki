package com.remrin.wiki.service;

import com.remrin.wiki.domain.Demo;
import com.remrin.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {
    @Resource
    private DemoMapper DemoMapper;

    public List<Demo> list() {
        return DemoMapper.selectByExample(null);
    }
}
