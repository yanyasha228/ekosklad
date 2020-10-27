package com.ekoskladvalidator.RestServices;

import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.ObjectMappers.GroupMapper;
import com.ekoskladvalidator.RestDao.GroupRestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRestServiceImpl implements GroupRestService {

    @Autowired
    private GroupRestDao groupRestDao;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public List<Group> getAll() {
        return groupRestDao.getAllGroups().stream().
                map(groupDto -> groupMapper.toEntity(groupDto)).collect(Collectors.toList());
    }

}
