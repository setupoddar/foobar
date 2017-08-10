package com.flipkart.mdm.resource.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by setu.poddar on 10/08/17.
 */

@Data
public class SearchResult {

    private Map<String,Set<String>> productIds = new HashMap<String, Set<String>>();
    private int start;
    private int count;
    private int totalCount;
}
