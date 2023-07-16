package com.exam.service;

import com.exam.dto.RoleDTO;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleDTO addNewRole(RoleDTO roleDTO);
    List<RoleDTO> getAllRoles();
}
