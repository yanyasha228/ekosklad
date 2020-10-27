package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends JpaRepository<Group , Integer> {

    List<Group> findAll();

    Optional<Group> findByName(String groupName);

    Optional<Group> findById(int id);

}
