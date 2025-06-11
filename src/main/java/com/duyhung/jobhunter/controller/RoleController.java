package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Permission;
import com.duyhung.jobhunter.domain.Role;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.service.RoleService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role)
        throws IdInvalidException {
        //check name
        if (this.roleService.existsByName(role.getName())) {
            throw new IdInvalidException(role.getName() + " already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        //check id
        if (this.roleService.findById(role.getId()) == null) {
            throw new IdInvalidException(role.getId() + " does not exist");
        }

        //check name
//        if (this.roleService.existsByName(role.getName())) {
//            throw new IdInvalidException(role.getName() + " already exists");
//        }

        return ResponseEntity.ok().body(this.roleService.update(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) throws IdInvalidException {
        //check id
        if (this.roleService.findById(id) == null) {
            throw new IdInvalidException(id + " does not exist");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Get roles with pagination")
    public ResponseEntity<ResultPaginationDTO> getRoles(
            @Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.getRoles(spec, pageable));
    }

}
