package org.example.oracle01.util;

import java.util.List;

public class PageResult<T> {
    private Long total;
    private List<T> records;
    private Integer page;
    private Integer size;

    public PageResult() {}

    public PageResult(Long total, List<T> records, Integer page, Integer size) {
        this.total = total;
        this.records = records;
        this.page = page;
        this.size = size;
    }

    // Getters and Setters
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPages() {
        if (size == null || size == 0) {
            return 0;
        }
        return (int) Math.ceil((double) total / size);
    }
}