package org.example.oracle01.controller;

import org.example.oracle01.model.Dictionary;
import org.example.oracle01.service.DictionaryService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    // 查询数据字典列表
    @GetMapping("/list")
    public Result<PageResult<Dictionary>> getDictionaryList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "dictType", required = false) String dictType,
            @RequestParam(value = "dictCode", required = false) String dictCode) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        params.put("dictType", dictType);
        params.put("dictCode", dictCode);
        
        return dictionaryService.getDictionaryList(params);
    }

    // 根据ID查询数据字典
    @GetMapping("/{id}")
    public Result<Dictionary> getDictionaryById(@PathVariable Long id) {
        return dictionaryService.getDictionaryById(id);
    }

    // 根据类型查询数据字典
    @GetMapping("/type/{type}")
    public Result<List<Dictionary>> getDictionaryByType(@PathVariable String type) {
        return dictionaryService.getDictionaryByType(type);
    }

    // 新增数据字典
    @PostMapping
    public Result<String> addDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.addDictionary(dictionary);
    }

    // 更新数据字典
    @PutMapping
    public Result<String> updateDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.updateDictionary(dictionary);
    }

    // 删除数据字典
    @DeleteMapping("/{id}")
    public Result<String> deleteDictionary(@PathVariable Long id) {
        return dictionaryService.deleteDictionary(id);
    }
}