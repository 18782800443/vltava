package com.dmall.vltava.domain.base;

import lombok.Data;

/**
 * @author Rob
 * @date Create in 2:07 PM 2020/1/19
 */
@Data
public class FilteredResult<T> {
    private long totalPages;
    private T resultList;
    public FilteredResult(long totalPages, T resultList){
        this.totalPages = totalPages;
        this.resultList = resultList;
    }
}
