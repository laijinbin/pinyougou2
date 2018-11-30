package com.pinyougou.common.pojo;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    /** 总记录数 */
    private long total;
    /** 分页数据 */
    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageResult() {
    }

    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
