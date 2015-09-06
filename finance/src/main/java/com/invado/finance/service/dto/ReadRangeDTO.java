/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class ReadRangeDTO<T>  {

    private Integer page;
    private Integer numberOfPages;
    private List<T> data;

    public ReadRangeDTO() {
    }

    public ReadRangeDTO(Integer page, Integer numberOfPages, List<T> data) {
        this.page = page;
        this.numberOfPages = numberOfPages;
        this.data = data;
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public Integer getPage() {
        return page;
    }
}