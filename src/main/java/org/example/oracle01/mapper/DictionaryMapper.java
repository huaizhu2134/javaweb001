package org.example.oracle01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.oracle01.model.Dictionary;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictionaryMapper {

    // 查询数据字典列表
    List<Dictionary> selectDictionaryList(@Param("params") Map<String, Object> params);

    // 根据ID查询数据字典
    Dictionary selectDictionaryById(@Param("id") Long id);

    // 根据类型查询数据字典
    List<Dictionary> selectDictionaryByType(@Param("type") String type);

    // 新增数据字典
    int insertDictionary(Dictionary dictionary);

    // 更新数据字典
    int updateDictionary(Dictionary dictionary);

    // 删除数据字典（逻辑删除）
    int deleteDictionary(@Param("id") Long id);

    // 统计数据字典数量
    int countDictionary(@Param("params") Map<String, Object> params);
}