package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.RoleDTO;
import com.bagrov.springpmhw.videorent.model.Role;
import com.bagrov.springpmhw.videorent.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(this::convertToRoleDTO).toList();
    }

    public RoleDTO getOne(int id) {
        return convertToRoleDTO(roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Роль с указанным id не найдена")));
    }

    @Transactional
    public RoleDTO save(RoleDTO roleDTO) {
        return convertToRoleDTO(roleRepository.save(convertToDirector(roleDTO)));
    }

    @Transactional
    public RoleDTO update(int id, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role updatedRole = enrichRole(optionalRole, roleDTO);
            updatedRole.setId(id);
            return convertToRoleDTO(roleRepository.save(updatedRole));
        }
        throw new NotFoundException("Роль с указанным id не найдена");
    }

    @Transactional
    public void delete(int id) {
        if (roleRepository.findById(id).isPresent()) {
            roleRepository.deleteById(id);
        } else {
            throw new NotFoundException("Роль с указанным id не найдена");
        }
    }

    private RoleDTO convertToRoleDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    private Role convertToDirector(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    private Role enrichRole(Optional<Role> optionalRole, RoleDTO roleDTO) {
        Role updatedRole = new Role();
        if (roleDTO.getTitle() != null) {
            updatedRole.setTitle(roleDTO.getTitle());
        } else {
            updatedRole.setTitle(optionalRole.get().getTitle());
        }

        if (roleDTO.getDescription() != null) {
            updatedRole.setDescription(roleDTO.getDescription());
        } else {
            updatedRole.setDescription(optionalRole.get().getDescription());
        }

        return updatedRole;
    }
}
