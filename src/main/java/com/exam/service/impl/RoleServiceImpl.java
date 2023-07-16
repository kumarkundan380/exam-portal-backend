package com.exam.service.impl;

import com.exam.dto.RoleDTO;
import com.exam.exception.ExamPortalException;
import com.exam.model.Role;
import com.exam.repository.RoleRepository;
import com.exam.service.RoleService;
import com.exam.util.AuthorityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDTO addNewRole(RoleDTO roleDTO) {
        log.info("addNewRole method invoking");
        if(AuthorityUtil.isAdminRole()) {
            Role role = modelMapper.map(roleDTO, Role.class);
            role = roleRepository.save(role);
            roleDTO = modelMapper.map(role, RoleDTO.class);
            return roleDTO;
        } else {
            log.error("You do not have permission to add role");
            throw new ExamPortalException("You do not have permission to add role");
        }

    }

    @Override
    public List<RoleDTO> getAllRoles() {
        log.info("getAllRoles method invoking");
        if(AuthorityUtil.isAdminRole()) {
            List<Role> roles = roleRepository.findAll();
            List<RoleDTO> roleDTOS = roles.stream().map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
            return roleDTOS;
        } else {
            log.error("You do not have permission to perform this task");
            throw new ExamPortalException("You do not have permission to perform this task");
        }
    }

}
