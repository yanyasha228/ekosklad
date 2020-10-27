package com.ekoskladvalidator.RestDao;

import com.ekoskladvalidator.Models.DTO.GroupDto;
import com.ekoskladvalidator.Models.HelpRestModels.GroupsListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GroupRestDaoImpl implements GroupRestDao {

    @Value("${rest.prom.api.token}")
    private String apiToken;

    @Value("${rest.prom.api.get.groups.list}")
    private String getGroupsListUrl;

    @Autowired
    private RestTemplate restTemplate;



    @Autowired
    public List<GroupDto> getAllGroups() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", apiToken));
        HttpEntity<GroupsListResponse> entity = new HttpEntity<GroupsListResponse>(headers);
        ResponseEntity<GroupsListResponse> groupsRestListResponseEntity = restTemplate.exchange(getGroupsListUrl, HttpMethod.GET, entity, GroupsListResponse.class);
        if (groupsRestListResponseEntity.getStatusCode() == HttpStatus.OK) {
            if(groupsRestListResponseEntity.getBody()!=null)
            return groupsRestListResponseEntity.getBody().getGroups();
        }
        return Collections.emptyList();
    }
}
