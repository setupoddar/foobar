package com.flipkart.mdm.resource.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by setu.poddar on 26/06/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private List<String> roles;
}
