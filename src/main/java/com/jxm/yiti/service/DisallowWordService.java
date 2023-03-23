package com.jxm.yiti.service;

import com.jxm.yiti.domain.DisallowWord;
import com.jxm.yiti.mapper.DisallowWordMapper;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.utils.Trie;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DisallowWordService {

    @Resource
    private DisallowWordMapper disallow_wordMapper;

    public static Trie root;  // 项目中的唯一Trie

    @PostConstruct
    public void init() {
        List<String> impolitePhrases = selectValue();
        root = new Trie();
        for (String s : impolitePhrases) {
            root.insert(s, root);
        }
    }

    /**
     * 增加一个敏感词
     */
    public void add(String value, CommonResp<DisallowWord> commonResp) {
        if (Objects.equals(value, "")) {
            commonResp.setSuccess(false);
            commonResp.setMessage(commonResp.getMessage() + "填入敏感词为空");
        }
        DisallowWordService.root.insert(value, DisallowWordService.root);
    }

    /**
     * 删除一个敏感词
     */
    public void delete(String value, CommonResp<DisallowWord> commonResp) {
        if (Objects.equals(value, "")) {
            commonResp.setSuccess(false);
            commonResp.setMessage(commonResp.getMessage() + "填入敏感词为空");
        }
        DisallowWordService.root.delete(value, DisallowWordService.root);
    }

    /**
     * 查询所有敏感词
     */
    public List<String> selectValue() {
        List<DisallowWord> disallow_wordList = disallow_wordMapper.selectByExample(null);
        List<String> impolitePhrases = new ArrayList<>();
        for (DisallowWord disallow_word : disallow_wordList) {
            impolitePhrases.add(disallow_word.getValue());
        }
        return  impolitePhrases;
    }
}
