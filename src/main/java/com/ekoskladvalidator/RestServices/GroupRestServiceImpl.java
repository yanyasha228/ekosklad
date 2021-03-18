package com.ekoskladvalidator.RestServices;

import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.ObjectMappers.GroupMapper;
import com.ekoskladvalidator.RestDao.GroupRestDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRestServiceImpl implements GroupRestService {

    private final GroupRestDao groupRestDao;

    private final GroupMapper groupMapper;

    public GroupRestServiceImpl(GroupRestDao groupRestDao, GroupMapper groupMapper) {
        this.groupRestDao = groupRestDao;
        this.groupMapper = groupMapper;
    }

    @Override
    public List<Group> getAll() {
        return groupRestDao.getAllGroups().stream().
                map(groupDto -> groupMapper.toEntity(groupDto)).collect(Collectors.toList());
    }

}
