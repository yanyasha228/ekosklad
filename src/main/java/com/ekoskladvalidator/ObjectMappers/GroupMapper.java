package com.ekoskladvalidator.ObjectMappers;

import com.ekoskladvalidator.Models.DTO.GroupDto;
import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Services.GroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GroupMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private GroupService groupService;

    public Group toEntity(GroupDto groupDto) {
        return Objects.isNull(groupDto) ? null : mapper.map(groupDto, Group.class);
    }


    public GroupDto toDto(Group group) {
        return Objects.isNull(group) ? null : mapper.map(group, GroupDto.class);
    }


}
