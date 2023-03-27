package com.zjy.elasticsearch.enums;

/**
 * 搜索条件数据转换
 *
 * @author zjy
 */
public enum FormatRule {
    /**
     * 什么都不做
     */
    NONE,
    /**
     * 将标准格式的 date 字符串转换为时间戳
     */
    DATE_STR_TO_LONG,
    /**
     * 删除博士硕士的第二个数据
     */
    MAJOR_REMOVE_TWO,
    /**
     * 年份-时间戳范围
     */
    YEAR_TIME_STAMP_RAND,
    /**
     * 在尾部如果不存在*号，则追加*号
     */
    ADD_MATCH_SYMBOL_SUFFIX_IF_NOT,
    /**
     * 在值的前后两边都增加*号(如不存在),即环绕添加*号
     */
    ADD_MATCH_SYMBOL_SURROUND_IF_NOT,
}
