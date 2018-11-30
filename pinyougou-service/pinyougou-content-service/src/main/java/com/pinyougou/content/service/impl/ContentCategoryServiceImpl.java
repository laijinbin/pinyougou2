package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.mapper.ContentCategoryMapper;
import com.pinyougou.service.ContentCategoryService;
import java.util.List;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.io.Serializable;
import java.util.Arrays;

@Service(interfaceName = "com.pinyougou.service.ContentCategoryService")
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;


	@Override
	public PageResult findByPage(Integer page, Integer rows, ContentCategory contentCategory) {
		try {
			PageInfo<ContentCategory> pageInfo=null;
			pageInfo=PageHelper.startPage(page,rows).doSelectPageInfo(
					new ISelect() {
						@Override
						public void doSelect() {
							contentCategoryMapper.selectAll();
						}
					}
			);
			return new PageResult(pageInfo.getTotal(),pageInfo.getList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(ContentCategory contentCategory) {
		try {
			contentCategoryMapper.insertSelective(contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(ContentCategory contentCategory) {
		try {
			contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Long[] ids) {
		try {
			Example example=new Example(ContentCategory.class);
			Example.Criteria criteria=example.createCriteria();
			criteria.andIn("id",Arrays.asList(ids));
			contentCategoryMapper.deleteByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ContentCategory> findAllContentCategory() {
		return contentCategoryMapper.selectAll();
	}
}