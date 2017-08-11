package com.flipkart.mdm.resource.request;

import lombok.Data;

import java.util.List;

/**
 * Created by setu.poddar on 11/08/17.
 */
@Data
public class TaskCompleteRequest {

    private String taskId;
    private String trendId;
    private List<String> fsns;
    private String userId;
}
