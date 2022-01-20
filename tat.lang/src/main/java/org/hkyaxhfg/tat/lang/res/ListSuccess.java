package org.hkyaxhfg.tat.lang.res;

import com.github.pagehelper.Page;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.util.List;

/**
 * 列表的成功, 带分页的和不带分页的.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
public class ListSuccess<T extends List<?>> extends Success<T> {
    /**
     * 总条数, 不是page时为当前list的元素个数.
     */
    private long total;
    /**
     * 当前页, 不是page时为1.
     */
    private int pageNum;
    /**
     * 当前页条数, 不是page时为当前list的元素个数.
     */
    private int pageSize;
    /**
     * 总页数, 不是page时为1.
     */
    private int pages;

    public ListSuccess(T data) {
        super(data);
        if (this.data == null) {
            return;
        }
        if (data instanceof Page<?>) {
            Page<?> page = Unaware.castUnaware(data);
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
        } else {
            this.pageNum = 1;
            this.pageSize = data.size();
            this.total = data.size();
            this.pages = 1;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
