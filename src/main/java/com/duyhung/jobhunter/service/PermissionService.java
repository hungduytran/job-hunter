package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Permission;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod());
    }

    public Permission createPermission(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission findPermissionById(Long id) {
        Optional<Permission> permission = this.permissionRepository.findById(id);
        if (permission.isPresent()) {
            return permission.get();
        }
        return null;
    }

    public Permission updatePermission(Permission permission) {
        Permission permissionDB = this.findPermissionById(permission.getId());
        if (permissionDB != null) {
            permissionDB.setName(permission.getName());
            permissionDB.setApiPath(permission.getApiPath());
            permissionDB.setMethod(permission.getMethod());
            permissionDB.setModule(permission.getModule());

            //update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    public void deletePermissionById(Long id) {
        //delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        //delete permission
        this.permissionRepository.deleteById(id);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage =this.permissionRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(permissionPage.getContent());

        return rs;
    }

}
