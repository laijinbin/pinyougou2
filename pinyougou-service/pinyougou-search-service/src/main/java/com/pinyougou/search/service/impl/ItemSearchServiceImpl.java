package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> Search(Map<String, Object> params) {
        Map<String,Object> map=new HashMap<>();
        String keywords= (String) params.get("keywords");
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

            //获得高亮查询结果
            HighlightPage<SolrItem> solrItems = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);
            //循环集合
            for (HighlightEntry<SolrItem> he : solrItems.getHighlighted()) {
                SolrItem entity = he.getEntity();
                if (he.getHighlights().size()>0 &&
                        he.getHighlights().get(0).getSnipplets().size()>0){
                    entity.setTitle(he.getHighlights().get(0).getSnipplets().get(0));
                }
            }
            map.put("rows",solrItems.getContent());
        }else {
            Query query=new SimpleQuery("*:*");
            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
            map.put("rows",solrItems.getContent());
        }
            return map;

    }
}
