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
 *
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

    public static class SearchCriterion implements Serializable {

        private String key;
        private Object value;
//        private Condition c;

        public SearchCriterion(String key, Object value) {
            this.key = key;
            this.value = value;
//            this.c = Condition.EQUAL;
        }

//        public SearchCriterion(String key, Object value, Condition c) {
//            this.key = key;
//            this.value = value;
//            this.c = c;
//        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

//        public Condition getC() {
//            return c;
//        }
    }

//    public static enum Condition implements Serializable {
//        
//        EQUAL,
//        NOT_EQUAL,
//        GREATER,
//        LESS,
//        LESS_OR_EQUAL,
//        GREATER_OR_EQUAL;
//    }
}
