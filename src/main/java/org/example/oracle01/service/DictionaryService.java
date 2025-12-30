package org.example.oracle01.service;

import org.example.oracle01.model.Dictionary;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;

import java.util.List;
import java.util.Map;

public interface DictionaryService {

    // 查询数据字典列表
    Result<PageResult<Dictionary>> getDictionaryList(Map<String, Object> params);

    // 根据ID查询数据字典
    Result<Dictionary> getDictionaryById(Long id);

    // 根据类型查询数据字典
    Result<List<Dictionary>> getDictionaryByType(String type);

    // 新增数据字典
    Result<String> addDictionary(Dictionary dictionary);

    // 更新数据字典
    Result<String> updateDictionary(Dictionary dictionary);

    // 删除数据字典
    Result<String> deleteDictionary(Long id);

    // 统计数据字典数量
    Result<Integer> countDictionary(Map<String, Object> params);
}