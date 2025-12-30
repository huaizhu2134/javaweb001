package org.example.oracle01.service.impl;

import org.example.oracle01.mapper.DictionaryMapper;
import org.example.oracle01.model.Dictionary;
import org.example.oracle01.service.DictionaryService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public Result<PageResult<Dictionary>> getDictionaryList(Map<String, Object> params) {
        try {
            // 获取总记录数
            Integer page = params.get("page") != null ? (Integer) params.get("page") : 0;
            Integer size = params.get("size") != null ? (Integer) params.get("size") : 10;

            int total = dictionaryMapper.countDictionary(params);
            List<Dictionary> dictionaryList = dictionaryMapper.selectDictionaryList(params);

            PageResult<Dictionary> pageResult = new PageResult<>((long) total, dictionaryList, page/size + 1, size);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询数据字典列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Dictionary> getDictionaryById(Long id) {
        try {
            Dictionary dictionary = dictionaryMapper.selectDictionaryById(id);
            if (dictionary != null) {
                return Result.success(dictionary);
            } else {
                return Result.error("未找到ID为 " + id + " 的数据字典");
            }
        } catch (Exception e) {
            return Result.error("查询数据字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Dictionary>> getDictionaryByType(String type) {
        try {
            List<Dictionary> dictionaryList = dictionaryMapper.selectDictionaryByType(type);
            return Result.success(dictionaryList);
        } catch (Exception e) {
            return Result.error("查询数据字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> addDictionary(Dictionary dictionary) {
        try {
            int result = dictionaryMapper.insertDictionary(dictionary);
            if (result > 0) {
                return Result.success("新增数据字典成功");
            } else {
                return Result.error("新增数据字典失败");
            }
        } catch (Exception e) {
            return Result.error("新增数据字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> updateDictionary(Dictionary dictionary) {
        try {
            int result = dictionaryMapper.updateDictionary(dictionary);
            if (result > 0) {
                return Result.success("更新数据字典成功");
            } else {
                return Result.error("更新数据字典失败");
            }
        } catch (Exception e) {
            return Result.error("更新数据字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> deleteDictionary(Long id) {
        try {
            int result = dictionaryMapper.deleteDictionary(id);
            if (result > 0) {
                return Result.success("删除数据字典成功");
            } else {
                return Result.error("删除数据字典失败");
            }
        } catch (Exception e) {
            return Result.error("删除数据字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> countDictionary(Map<String, Object> params) {
        try {
            int count = dictionaryMapper.countDictionary(params);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计数据字典数量失败：" + e.getMessage());
        }
    }
}