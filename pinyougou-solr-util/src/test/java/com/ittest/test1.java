package com.ittest;

import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class test1 {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void delete(){
        Query query=new SimpleQuery("*:*");
        UpdateResponse delete = solrTemplate.delete(query);
        if (delete.getStatus()==0){
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
    }

    @Test
    public void findById(){
        SolrItem byId = solrTemplate.getById("1369324", SolrItem.class);
        System.out.println(byId.getTitle());

    }
    @Test
    public void findById2(){
        Query query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("title").contains("2");
        query.addCriteria(criteria);
        query.setOffset(0);
        query.setRows(20);
        ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
        System.out.println(solrItems.getTotalElements());
        List<SolrItem> content = solrItems.getContent();
        for (SolrItem solrItem : content) {
            System.out.println(solrItem.getTitle());
        }

    }


}
