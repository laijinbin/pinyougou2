package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Content;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.service.ContentService;
import java.util.List;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.io.Serializable;
import java.util.Arrays;

@Service(interfaceName = "com.pinyougou.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentMapper contentMapper;


	@Override
	public PageResult findByPage(Integer page, Integer rows, Content content) {
		try {
			PageInfo pageInfo=null;
			pageInfo=PageHelper.startPage(page,rows).doSelectPageInfo(
					new ISelect() {
						@Override
						public void doSelect() {
							contentMapper.selectAll();
						}
					}
			);
			return new PageResult(pageInfo.getTotal(),pageInfo.getList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public void delete(Long[] ids) {
		try {
			Example example=new Example(Content.class);
			Example.Criteria criteria=example.createCriteria();
			criteria.andIn("id",Arrays.asList(ids));
			contentMapper.deleteByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void save(Content content) {
		try {
			contentMapper.insertSelective(content);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Content content) {
		try {
			contentMapper.updateByPrimaryKeySelective(content);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}