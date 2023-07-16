package com.exam.controller;

import com.exam.dto.RoleDTO;
import com.exam.enums.Status;
import com.exam.response.ExamPortalResponse;
import com.exam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.exam.constants.ExamPortalConstant.ROLE_BASE_PAT;

@RestController
@RequestMapping(ROLE_BASE_PAT)
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ExamPortalResponse<?>> addRoles(@RequestBody RoleDTO roleDTO){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Role added Successfully")
                .body(roleService.addNewRole(roleDTO))
                .build(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ExamPortalResponse<?>> getAllRoles(){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Role fetched Successfully")
                .body(roleService.getAllRoles())
                .build(),
                HttpStatus.OK);

    }



}
