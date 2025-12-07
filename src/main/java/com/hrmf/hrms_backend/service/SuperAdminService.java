package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.dto.superAdmin.CreateSubAdminRequestDto;
import com.hrmf.hrms_backend.dto.superAdmin.CreateSubAdminResponseDto;
import com.hrmf.hrms_backend.dto.superAdmin.SubAdminDto;
import com.hrmf.hrms_backend.dto.user.CreateUserDto;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.exception.SubAdminNotFoundException;
import com.hrmf.hrms_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final UserService userService;
    private final UserRepository userRepository;
    // private final EmployerRepository employerRepository;

    @Transactional
    public CreateSubAdminResponseDto createSuperAdmin(
            CreateSubAdminRequestDto createSubAdminRequestDto
    ) {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail(createSubAdminRequestDto.getEmail());
        createUserDto.setPassword(createSubAdminRequestDto.getPassword());
        createUserDto.setName(createSubAdminRequestDto.getName());
        createUserDto.setRole(UserRole.SUB_ADMIN);

        User user = userService.createUser(createUserDto);

        return CreateSubAdminResponseDto.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public List<SubAdminDto> allSubAdmins(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                limit != null ? limit : 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<User> subAdminsPage = userRepository.findByRole(UserRole.SUB_ADMIN, pageable);

        return subAdminsPage.getContent().stream()
                .map(this::convertToSubAdminDto)
                .collect(Collectors.toList());
    }

    public List<SubAdminDto> allSubAdmins() {
        List<User> subAdmins = userRepository.findByRole(UserRole.SUB_ADMIN);

        return subAdmins.stream()
                .map(this::convertToSubAdminDto)
                .collect(Collectors.toList());
    }

    public SubAdminDto getSubAdminById(UUID id) {
        User subAdmin = userRepository.findByIdAndRole(id, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new RuntimeException("Sub-admin not found with id: " + id));

        return convertToSubAdminDto(subAdmin);
    }

    public SubAdminDto getSubAdminByEmail(String email) {
        User subAdmin = userRepository.findByEmailAndRole(email, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new RuntimeException("Sub-admin not found with email: " + email));

        return convertToSubAdminDto(subAdmin);
    }

    @Transactional
    public SubAdminDto updateSubAdminStatus(UUID id, UserStatus newStatus) {
        User subAdmin = userRepository.findByIdAndRole(id, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new RuntimeException("Sub-admin not found with id: " + id));

        if (newStatus.equals(UserStatus.DELETED)) {
            throw new RuntimeException("Use delete endpoint to delete sub-admin");
        }

        subAdmin.setStatus(newStatus);
        User updatedSubAdmin = userRepository.save(subAdmin);

        return convertToSubAdminDto(updatedSubAdmin);
    }

    @Transactional
    public void blockSubAdmin(UUID id) {
        User subAdmin = userRepository.findByIdAndRole(id, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new RuntimeException("Sub-admin not found with id: " + id));

        subAdmin.setStatus(UserStatus.BLOCKED);
        userRepository.save(subAdmin);
    }

    @Transactional
    public void unblockSubAdmin(UUID id) {
        User subAdmin = userRepository.findByIdAndRole(id, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new RuntimeException("Sub-admin not found with id: " + id));

        subAdmin.setStatus(UserStatus.ACTIVE);
        userRepository.save(subAdmin);
    }

    @Transactional
    public void deleteSubAdmin(UUID id) {
        User subAdmin = userRepository.findByIdAndRole(id, UserRole.SUB_ADMIN)
                .orElseThrow(() -> new SubAdminNotFoundException(id.toString()));

        // Soft delete
        subAdmin.setStatus(UserStatus.DELETED);
        userRepository.save(subAdmin);

    }

    public long getTotalSubAdminsCount() {
        return userRepository.countByRole(UserRole.SUB_ADMIN);
    }

    public long getActiveSubAdminsCount() {
        return userRepository.countByRoleAndStatus(UserRole.SUB_ADMIN, UserStatus.ACTIVE);
    }

    public long getBlockedSubAdminsCount() {
        return userRepository.countByRoleAndStatus(UserRole.SUB_ADMIN, UserStatus.BLOCKED);
    }

    private SubAdminDto convertToSubAdminDto(User user) {
        SubAdminDto dto = new SubAdminDto();
        dto.setId(user.getId().toString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus().name());

        // have to contact number field
        // dto.setContactNumber(user.getContactNumber());

        // Count employers added by this sub-admin
        int totalEmployers = countEmployersAddedBySubAdmin(user.getId());
        dto.setTotalEmployer(totalEmployers);

        return dto;
    }

    private int countEmployersAddedBySubAdmin(UUID subAdminId) {
        // Implement this method based on your Employer entity
        // Example:
        // return employerRepository.countByCreatedBy(subAdminId);
        // or
        // return employerRepository.countBySubAdminId(subAdminId);

        // For now, return 0 or implement based on your business logic
        return 0;
    }
}