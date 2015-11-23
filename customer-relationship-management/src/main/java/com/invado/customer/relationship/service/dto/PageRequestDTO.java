/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bobic Dragan
 */
public class PageRequestDTO {

    private Integer page;
    private final List<SearchCriterion> list = new ArrayList<>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void addSearchCriterion(SearchCriterion s) {
        list.add(s);
    }

    public List<SearchCriterion> readAllSearchCriterions() {
        return Collections.unmodifiableList(list);
    }

    public List<SearchCriterion> getList() {
        return list;
    }

    public static class SearchCriterion implements Serializable {

        private String key;
        private Object value;

        public SearchCriterion(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

    }


}
