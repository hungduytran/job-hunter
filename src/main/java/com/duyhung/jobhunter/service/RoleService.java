package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Permission;
import com.duyhung.jobhunter.domain.Role;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.PermissionRepository;
import com.duyhung.jobhunter.repository.RoleRepository;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Role findById(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public Role save(Role role) {
        //check
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return roleRepository.save(role);
    }

    public Role update(Role role) {
        Role roleDB = findById(role.getId());
        //check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());
        roleDB.setPermissions(role.getPermissions());
        roleDB = roleRepository.save(roleDB);
        return roleDB;
    }

    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage =this.roleRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(rolePage.getContent());

        return rs;
    }

}
