package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.dto.employee.EmployeeDetailDto;
import com.hrmf.hrms_backend.dto.employee.EmployeeListDto;
import com.hrmf.hrms_backend.dto.employee.EmployerDashboardStatsDto;
import com.hrmf.hrms_backend.dto.employer.*;
import com.hrmf.hrms_backend.dto.user.CreateUserDto;
import com.hrmf.hrms_backend.dto.user.PersonalDetailsDto;
import com.hrmf.hrms_backend.entity.Employee;
import com.hrmf.hrms_backend.entity.PersonalDetails;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.Gender;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.exception.ResourceNotFoundException;
import com.hrmf.hrms_backend.repository.EmployeeRepository;
import com.hrmf.hrms_backend.repository.PersonalDetailsRepository;
import com.hrmf.hrms_backend.repository.UserRepository;
import com.hrmf.hrms_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployerService {

    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    // Add employee
    @Transactional
    public AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmployeeRequestDto) {
        if (!securityUtil.isEmployer()) {
            throw new AccessDeniedException("You don't have permission to add employees");
        }

        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        // Check if email already exists
        if (userRepository.existsByEmail(addEmployeeRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + addEmployeeRequestDto.getEmail());
        }

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName(addEmployeeRequestDto.getName());
        createUserDto.setEmail(addEmployeeRequestDto.getEmail());
        createUserDto.setPassword(addEmployeeRequestDto.getPassword());
        createUserDto.setRole(UserRole.EMPLOYEE);

        User employeeUser = userService.createUser(createUserDto);

        Employee employee = new Employee();
        employee.setUser(employeeUser);
        employee.setEmployer(currentEmployer);
        employee.setEmployeeRole(addEmployeeRequestDto.getEmployeeRole());
        employee.setNumber(addEmployeeRequestDto.getNumber());
        employee.setShift(addEmployeeRequestDto.getShift());
        employee.setOfficeTime(addEmployeeRequestDto.getOfficeTime());

        Employee savedEmployee = employeeRepository.save(employee);

        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setUser(employeeUser);
        personalDetails.setDateOfBirth(addEmployeeRequestDto.getBirthDate());
        personalDetails.setGender(Gender.valueOf(addEmployeeRequestDto.getGender()));
        personalDetails.setMobile(addEmployeeRequestDto.getNumber());

        personalDetailsRepository.save(personalDetails);

        log.info("Employee added by employer {}: {} ({})",
                currentEmployer.getEmail(), employeeUser.getEmail(), employeeUser.getId());

        return AddEmployeeResponseDto.builder()
                .id(savedEmployee.getId().toString())
                .name(addEmployeeRequestDto.getName())
                .email(addEmployeeRequestDto.getEmail())
                .gender(addEmployeeRequestDto.getGender())
                .role(employeeUser.getRole())
                .employeeRole(addEmployeeRequestDto.getEmployeeRole())
                .build();
    }

    // Get all employees created by current employer
    public List<EmployeeListDto> getMyEmployees(String search, UserStatus status) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        List<Employee> employees;

        if (search != null && !search.trim().isEmpty()) {
            // Use the correct method name
            employees = employeeRepository.searchByEmployerAndNameOrEmail(
                    currentEmployer,
                    search.trim().toLowerCase()
            );
        } else if (status != null) {
            employees = employeeRepository.findByEmployerAndUserStatus(currentEmployer, status);
        } else {
            employees = employeeRepository.findByEmployer(currentEmployer);
        }

        return employees.stream()
                .map(this::convertToEmployeeListDto)
                .collect(Collectors.toList());
    }

    // Get paginated employees created by current employer
    public Page<EmployeeListDto> getMyEmployeesPaginated(int page, int size, String search, UserStatus status) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Employee> employeesPage;

        if (search != null && !search.trim().isEmpty()) {
            // Use the correct method name
            employeesPage = employeeRepository.searchByEmployerAndNameOrEmail(
                    currentEmployer,
                    search.trim().toLowerCase(),
                    pageable
            );
        } else if (status != null) {
            employeesPage = employeeRepository.findByEmployerAndUserStatus(currentEmployer, status, pageable);
        } else {
            employeesPage = employeeRepository.findByEmployer(currentEmployer, pageable);
        }

        return employeesPage.map(this::convertToEmployeeListDto);
    }

    // Get single employee
    public EmployeeDetailDto getEmployeeById(UUID employeeId) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByIdAndEmployer(employeeId, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        return convertToEmployeeDetailDto(employee);
    }

    // Get employee by email
    public EmployeeDetailDto getEmployeeByEmail(String email) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByUserEmailAndEmployer(email, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        return convertToEmployeeDetailDto(employee);
    }

    // Block employee
    @Transactional
    public void blockEmployee(UUID employeeId) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByIdAndEmployer(employeeId, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        User employeeUser = employee.getUser();

        if (employeeUser.getStatus() == UserStatus.BLOCKED) {
            throw new IllegalStateException("Employee is already blocked");
        }

        employeeUser.setStatus(UserStatus.BLOCKED);
        userRepository.save(employeeUser);

        log.info("Employee blocked by employer {}: {} ({})",
                currentEmployer.getEmail(), employeeUser.getEmail(), employeeUser.getId());
    }

    // Unblock employee
    @Transactional
    public void unblockEmployee(UUID employeeId) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByIdAndEmployer(employeeId, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        User employeeUser = employee.getUser();

        if (employeeUser.getStatus() != UserStatus.BLOCKED) {
            throw new IllegalStateException("Employee is not blocked");
        }

        employeeUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(employeeUser);

        log.info("Employee unblocked by employer {}: {} ({})",
                currentEmployer.getEmail(), employeeUser.getEmail(), employeeUser.getId());
    }

    // Update employee status (must be created by current employer)
    @Transactional
    public EmployeeDetailDto updateEmployeeStatus(UUID employeeId, UserStatus status) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByIdAndEmployer(employeeId, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        User employeeUser = employee.getUser();

        // Don't allow deleting via status update
        if (status == UserStatus.DELETED) {
            throw new IllegalArgumentException("Use delete endpoint to delete employee");
        }

        employeeUser.setStatus(status);
        User updatedUser = userRepository.save(employeeUser);

        log.info("Employee status updated to {} by employer {}: {} ({})",
                status, currentEmployer.getEmail(), employeeUser.getEmail(), employeeUser.getId());

        return convertToEmployeeDetailDto(employee);
    }

    // Delete employee (soft delete - must be created by current employer)
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByIdAndEmployer(employeeId, currentEmployer)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found or you don't have access"));

        User employeeUser = employee.getUser();
        employeeUser.setStatus(UserStatus.DELETED);
        userRepository.save(employeeUser);

        log.info("Employee deleted by employer {}: {} ({})",
                currentEmployer.getEmail(), employeeUser.getEmail(), employeeUser.getId());
    }

    // Get dashboard statistics for current employer
    public EmployerDashboardStatsDto getDashboardStats() {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        long totalEmployees = employeeRepository.countByEmployer(currentEmployer);
        long activeEmployees = employeeRepository.countByEmployerAndUserStatus(currentEmployer, UserStatus.ACTIVE);
        long blockedEmployees = employeeRepository.countByEmployerAndUserStatus(currentEmployer, UserStatus.BLOCKED);
        long deletedEmployees = employeeRepository.countByEmployerAndUserStatus(currentEmployer, UserStatus.DELETED);

        return EmployerDashboardStatsDto.builder()
                .totalEmployees(totalEmployees)
                .activeEmployees(activeEmployees)
                .blockedEmployees(blockedEmployees)
                .deletedEmployees(deletedEmployees)
                .build();
    }

    // Helper methods for DTO conversion
    private EmployeeListDto convertToEmployeeListDto(Employee employee) {
        User user = employee.getUser();

        return EmployeeListDto.builder()
                .id(employee.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .employeeRole(employee.getEmployeeRole())
                .contactNumber(employee.getNumber())
                .shift(employee.getShift())
                .officeTime(employee.getOfficeTime())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private EmployeeDetailDto convertToEmployeeDetailDto(Employee employee) {
        User user = employee.getUser();

        // Get personal details
        PersonalDetailsDto personalDetailsDto = null;
        if (user.getPersonalDetails() != null) {
            PersonalDetails pd = user.getPersonalDetails();
            personalDetailsDto = PersonalDetailsDto.builder()
                    .about(pd.getAbout())
                    .employerCode(pd.getEmployerCode())
                    .gender(pd.getGender())
                    .dateOfBirth(pd.getDateOfBirth())
                    .maritalStatus(pd.getMaritalStatus())
                    .mobile(pd.getMobile())
                    .emergencyContact(pd.getEmergencyContact())
                    .build();
        }

        return EmployeeDetailDto.builder()
                .id(employee.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .employeeRole(employee.getEmployeeRole())
                .contactNumber(employee.getNumber())
                .shift(employee.getShift())
                .officeTime(employee.getOfficeTime())
                .status(user.getStatus())
                .personalDetails(personalDetailsDto)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}