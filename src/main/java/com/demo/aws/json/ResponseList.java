package com.demo.aws.json;

import java.util.Collection;

public class ResponseList<T> {
    private Integer       total;
    private Collection<T> items;

    public ResponseList(Integer total, Collection<T> items) {
        this.total = total;
        this.items = items;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Collection<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }
}
