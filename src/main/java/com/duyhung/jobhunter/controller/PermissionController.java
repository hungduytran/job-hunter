package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Permission;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.PermissionRepository;
import com.duyhung.jobhunter.service.PermissionService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionRepository permissionRepository, PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
    throws IdInvalidException {
        //check exist
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exists");
        }

        //create a permission
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permisson")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission)
        throws IdInvalidException {
        if (this.permissionService.findPermissionById(permission.getId()) == null) {
            throw new IdInvalidException("Permission with id " + permission.getId() + " does not exist");
        }

        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exists");
        }

        return ResponseEntity.ok().body(this.permissionService.updatePermission(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id)
        throws IdInvalidException {
        if (this.permissionService.findPermissionById(id) == null) {
            throw new IdInvalidException("Permission with id " + id + " does not exist");
        }
        this.permissionService.deletePermissionById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Get permissions with pagination")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.getPermissions(spec, pageable));
    }


}
