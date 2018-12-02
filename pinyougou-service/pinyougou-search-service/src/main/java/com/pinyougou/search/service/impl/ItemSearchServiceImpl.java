package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceName ="com.pinyougou.service.ItemSearchService")
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService {


//    {"keywords":"华为","category":"手机",
// "brand":"三星","price":"500-1000",
// "spec":{"机身内存":"64G","网络":"联通3G"}}

    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> Search(Map<String, Object> params) {
        Map<String,Object> map=new HashMap<>();
        String keywords= (String) params.get("keywords");
        //分页
        Integer page=(Integer) params.get("page");
        if (page==null){
            page=1;
        }
        Integer rows=(Integer) params.get("rows");
        if (rows==null){
            page=20;
        }

        if (StringUtils.isNoneBlank(keywords)){
            //高亮查询
            HighlightQuery highlightQuery=new SimpleHighlightQuery();
            //封装高亮的信息
            HighlightOptions highlightOptions=new HighlightOptions();
            highlightOptions.addField("title");//添加域

            highlightOptions.setSimplePrefix("<font color='red'>");//设置格式
            highlightOptions.setSimplePostfix("</font>");

            //封装高亮查询条件
            highlightQuery.setHighlightOptions(highlightOptions);
            //添加查询条件
            Criteria criteria=new Criteria("keywords").is(keywords);
            highlightQuery.addCriteria(criteria);

            //分类
            if (!"".equals(params.get("category"))){
                Criteria criteria1=new Criteria("category")
                        .is(params.get("category"));
                //添加过滤条件
                highlightQuery.addFilterQuery(new SimpleFacetQuery(criteria1));

            }
            //品牌
            if (!"".equals(params.get("brand"))){
                Criteria criteria2=new Criteria("brand")
                        .is(params.get("brand"));
                //添加过滤条件
                highlightQuery.addFilterQuery(new SimpleFacetQuery(criteria2));

            }
            //价格
            if (!"".equals(params.get("price"))){
                String[] price =
                        params.get("price").toString().split("-");
                if (!price[0].equals("0")){
                    Criteria criteria3=new Criteria("price").greaterThanEqual(price[0]);
                    highlightQuery.addFilterQuery(new
                            SimpleFilterQuery(criteria3));

                }
                if (!price[1].equals("*")){
                    Criteria criteria3=new Criteria("price").lessThanEqual(price[1]);
                    highlightQuery.addFilterQuery(new
                            SimpleFilterQuery(criteria3));
                }
            }

            //规格
            if (params.get("spec")!=null){
                Map<String,String> spec = (Map<String, String>) params.get("spec");
                for (String s : spec.keySet()) {
                    Criteria criteria4=new Criteria("spec_"+s).is(spec.get(s));
                    highlightQuery.addFilterQuery(new
                            SimpleFilterQuery(criteria4));
                }
            }
            /** 添加排序 */
            String sortValue = (String) params.get("sort"); // ASC  DESC
            String sortField = (String) params.get("sortField"); // 排序域
            if(StringUtils.isNoneBlank(sortValue)
                    && StringUtils.isNoneBlank(sortField)){
                Sort sort = new Sort("ASC".equalsIgnoreCase(sortValue) ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortField);
                /** 增加排序 */
                highlightQuery.addSort(sort);

            }


                //设置起始查询记录数
            highlightQuery.setOffset((page - 1) * rows);
            //每页记录数
            highlightQuery.setRows(rows);
            //获得高亮查询结果
            HighlightPage<SolrItem> solrItems = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);
            //循环集合
            List<HighlightEntry<SolrItem>> highlighted = solrItems.getHighlighted();
            for (HighlightEntry<SolrItem> he : solrItems.getHighlighted()) {
                SolrItem entity = he.getEntity();
                if (he.getHighlights().size()>0 &&
                        he.getHighlights().get(0).getSnipplets().size()>0){
                    entity.setTitle(he.getHighlights().get(0).getSnipplets().get(0));
                }
            }
            map.put("rows",solrItems.getContent());
            map.put("totalPages",solrItems.getTotalPages());
            map.put("total",solrItems.getTotalElements());
        }else {
            Query query=new SimpleQuery("*:*");
            query.setOffset((page-1)*rows);
            query.setRows(rows);
            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
            map.put("rows",solrItems.getContent());
            map.put("totalPages",solrItems.getTotalPages());
            map.put("total",solrItems.getTotalElements());
        }
            return map;

    }
}
