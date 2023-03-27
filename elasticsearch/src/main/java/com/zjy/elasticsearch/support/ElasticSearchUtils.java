package com.zjy.elasticsearch.support;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.zjy.elasticsearch.annotation.ElasticSearchParam;
import com.zjy.elasticsearch.consts.ApplicationConst;
import com.zjy.elasticsearch.domain.ElasticQueryParam;
import com.zjy.elasticsearch.enums.ElasticQueryType;
import com.zjy.elasticsearch.enums.ElasticSearchType;
import com.zjy.elasticsearch.enums.FormatRule;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用于解析@ElasticSearchParam注解信息 的工具类
 *
 * @author zjy
 */
public final class ElasticSearchUtils {

    private ElasticSearchUtils() {
    }

    /**
     * 根据param 为 ES搜索对象
     *
     * @param param
     */
    public static List<ElasticQueryParam> buildElasticQueryParamByRequestObject(Object param) {
        if (param == null) {
            return Collections.emptyList();
        }
        // 创建 paramList 对象
        List<ElasticQueryParam> paramList = new ArrayList<>();
        ReflectionUtils.doWithFields(param.getClass(), field -> {
            Object fieldValue = ReflectUtil.getFieldValue(param, field);
            // 如果当前字段为空就直接跳过
            if (ObjectUtil.isEmpty(fieldValue)) {
                return;
            }
            ElasticSearchParam esAnnotation = AnnotationUtil.getAnnotation(field, ElasticSearchParam.class);
            if (esAnnotation == null) {
                return;
            }
            //如果是多字段组合查询，则根据分割符，拆分为多个查询条件
            if (esAnnotation.isMultipart()) {
                List<String> annotationValueSplit = StrUtil.split(esAnnotation.value(), ApplicationConst.BASE_SPLIT);
                annotationValueSplit.forEach(fieldName -> {
                    paramList.add(createQueryParam(esAnnotation, fieldName, fieldValue));
                });
            } else {
                paramList.add(createQueryParam(esAnnotation, esAnnotation.value(), fieldValue));
            }

        }, ReflectionUtils.COPYABLE_FIELDS);

        return paramList;
    }

    /**
     * 构建ES查询参数
     *
     * @param annotation 查询元注解
     * @param fieldValue 字段
     * @return ES查询参数
     */
    private static ElasticQueryParam createQueryParam(ElasticSearchParam annotation, String fieldName, Object fieldValue) {
        ElasticQueryType elasticQueryType = annotation.queryType();
        ElasticSearchType elasticSearchType = annotation.searchType();
        FormatRule rule = annotation.rule();

        ElasticQueryParam elasticQueryParam = new ElasticQueryParam();
        elasticQueryParam.setQueryType(elasticQueryType);
        elasticQueryParam.setSearchType(elasticSearchType);
        elasticQueryParam.setName(fieldName);

        switch (elasticQueryType) {
            case TERM:
            case MATCH:
            case WILDCARD:
                elasticQueryParam.setValue(convertValue(fieldValue, rule));
                break;
            case GE:
                elasticQueryParam.setGe(convertValue(fieldValue, rule));
                break;
            case LE:
                elasticQueryParam.setLe(convertValue(fieldValue, rule));
                break;
            case TERMS:
                elasticQueryParam.setTerms((List) convertValue(fieldValue, rule));
                break;
            case RAND:
                convertRand(elasticQueryParam, fieldValue, rule);
                break;
            default:
                break;
        }
        return elasticQueryParam;
    }

    /**
     * 转换一般类型的参数
     *
     * @param value 前端传递来的值
     * @param rule  查询参数处理规则
     * @return ES查询参数
     */
    private static Object convertValue(Object value, FormatRule rule) {
        switch (rule) {
            case DATE_STR_TO_LONG:
                return DateUtil.parse(String.valueOf(value), DatePattern.NORM_DATETIME_FORMATTER).getTime();
            case MAJOR_REMOVE_TWO:
                if (value instanceof List) {
                    List arrayValue = (List) value;
                    List result = new ArrayList();
                    for (Object subValue : arrayValue) {
                        result.add(removeTwo(subValue.toString()));
                    }
                    return result;
                } else {
                    return removeTwo(value.toString());
                }
            case ADD_MATCH_SYMBOL_SUFFIX_IF_NOT:
                return StrUtil.addSuffixIfNot(String.valueOf(value), ApplicationConst.BASE_MATCH_SYMBOL);
            case ADD_MATCH_SYMBOL_SURROUND_IF_NOT:
                String valueStr = String.valueOf(value);
                String addPrefixStr = StrUtil.addPrefixIfNot(valueStr, ApplicationConst.BASE_MATCH_SYMBOL);
                return StrUtil.addSuffixIfNot(addPrefixStr, ApplicationConst.BASE_MATCH_SYMBOL);
            case NONE:
            default:
                return value;
        }
    }

    public static String removeTwo(String value) {
        List<String> split = StrUtil.split(Convert.toStr(value), ApplicationConst.PATH_SPLIT);
        if (split.size() == 6) {
            split.set(2, ApplicationConst.ZERO_STR);
            return StrUtil.join(ApplicationConst.PATH_SPLIT, split);
        } else {
            return value;
        }
    }

    /**
     * 转换Rand类型的ES查询参数
     *
     * @param elasticQueryParam ES查询参数
     * @param value             前端传递来的值
     * @param rule              查询参数处理规则
     */
    private static void convertRand(ElasticQueryParam elasticQueryParam, Object value, FormatRule rule) {
        Object ge = null;
        Object le = null;
        switch (rule) {
            case YEAR_TIME_STAMP_RAND:
                DateTime parse = DateUtil.parse(String.valueOf(value), DatePattern.NORM_DATETIME_FORMATTER);
                ge = DateUtil.beginOfYear(parse).getTime();
                le = DateUtil.endOfYear(parse).getTime();
            case NONE:
            default:
        }
        elasticQueryParam.setGe(ge);
        elasticQueryParam.setLe(le);
    }


    /**
     * 根据封装的ElasticQueryParam 构建ES查询参数
     *
     * @param queryParams ES查询参数抽象集合
     * @return query查询
     */
    public static QueryBuilder buildQueryBuilderByElasticQueryParam(List<ElasticQueryParam> queryParams) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (ElasticQueryParam queryParam : queryParams) {
            addBoolQueryBySearchType(boolQueryBuilder, queryParam);
        }
        return boolQueryBuilder;
    }

    /**
     * 向boolQuery中添加QueryBuilder
     *
     * @param boolQueryBuilder 一个boolQuery
     * @param queryParam       ES查询参数抽象
     */
    private static void addBoolQueryBySearchType(BoolQueryBuilder boolQueryBuilder, ElasticQueryParam queryParam) {
        QueryBuilder queryBuilder = createQueryBuilderByQueryType(queryParam);
        switch (queryParam.getSearchType()) {
            case FILTER:
                boolQueryBuilder.filter().add(queryBuilder);
                break;
            case SHOULD:
                boolQueryBuilder.should().add(queryBuilder);
                break;
            case MUST:
                boolQueryBuilder.must().add(queryBuilder);
                break;
            case MUST_NOT:
                boolQueryBuilder.mustNot().add(queryBuilder);
                break;
            default:
        }
    }

    /**
     * 根据 ES查询参数抽象 创建 QueryBuilder
     *
     * @param param ES参数抽象
     * @return QueryBuilder
     */
    private static QueryBuilder createQueryBuilderByQueryType(ElasticQueryParam param) {
        switch (param.getQueryType()) {
            case TERM:
                return QueryBuilders.termQuery(param.getName(), param.getValue());
            case TERMS:
                return QueryBuilders.termsQuery(param.getName(), param.getTerms());
            case LE:
                return QueryBuilders.rangeQuery(param.getName()).lte(param.getLe());
            case GE:
                return QueryBuilders.rangeQuery(param.getName()).gte(param.getGe());
            case MATCH:
                return QueryBuilders.matchQuery(param.getName(), param.getValue());
            case RAND:
                return QueryBuilders.rangeQuery(param.getName()).gte(param.getGe()).lte(param.getLe());
            case WILDCARD:
                return QueryBuilders.wildcardQuery(param.getName(), Convert.toStr(param.getValue()));
            default:
                return null;
        }
    }


}
