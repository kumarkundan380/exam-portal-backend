package com.exam.service.impl;

import com.exam.dto.RoleDTO;
import com.exam.dto.UserDTO;
import com.exam.enums.UserRole;
import com.exam.enums.UserStatus;
import com.exam.exception.ExamPortalException;
import com.exam.exception.ResourceNotFoundException;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.VerificationToken;
import com.exam.repository.RoleRepository;
import com.exam.repository.UserRepository;
import com.exam.request.PasswordChangeRequest;
import com.exam.response.PageableResponse;
import com.exam.service.UserService;
import com.exam.service.VerificationTokenService;
import com.exam.util.AuthorityUtil;
import com.exam.util.EmailUtil;
import com.exam.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.exam.constants.ExamPortalConstant.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO addUser(UserDTO userDTO) {
        log.info("addUser method invoking");
        userDTO = UserValidation.validateUser(userDTO);
        if (!StringUtils.hasText(userDTO.getPassword())) {
            log.error("Password is empty");
            throw new ExamPortalException("Password cannot be empty");
        }
        userDTO.setPassword(userDTO.getPassword().trim());
        Optional<User> optionalUser;
        optionalUser = userRepository.findByUserName(userDTO.getUserName());
        if(optionalUser.isPresent()){
            log.error("Username already exist");
            throw new ExamPortalException("Username already exist. Please try other Username");
        }
        optionalUser = userRepository.findByEmail(userDTO.getEmail());
        if(optionalUser.isPresent()){
            log.error("Email already exist");
            throw new ExamPortalException("Email already exist. Please try other Email");
        }
        optionalUser = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
        if(optionalUser.isPresent()){
            log.error("Phone Number already exist");
            throw new ExamPortalException("Phone Number exist. Please try other Username");
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        Optional<Role> role = roleRepository.findByRoleName(UserRole.USER);
        if(role.isPresent()){
            user.getRoles().add(role.get());
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            userDTO = convertUserToUserDTO(user);
            User finalUser = user;
            log.info("Sending verification email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
            log.info("addUser method called");
            return userDTO;
        }
        log.error("Role is not available");
        throw new ExamPortalException("Role is not available");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        log.info("updateUser method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        log.info("validateUser method invoking");
        userDTO = UserValidation.validateUser(userDTO);
        if(AuthorityUtil.isAdminRole()) {
            user.setUserStatus(userDTO.getStatus());
            user.setIsVerified(userDTO.getIsUserVerified());
            return getUpdatedUser(user,userDTO);
        } else {
            if(!userRepository.isDeletedUser(userDTO.getUserName())){
                if(AuthorityUtil.isSameUser(userDTO.getUserName())){
                    return getUpdatedUser(user,userDTO);
                }
                log.error("You cannot update other user profile");
                throw new ExamPortalException("You cannot update other user profile");
            }
            log.error("User is deleted");
            throw new ExamPortalException("User is deleted");
        }
    }

    @Override
    public PageableResponse<?> getAllUsers(Integer pageNumber, Integer pageSize) {
        log.info("getAllUsers method invoking");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, USER_SORT_FIELD));
        Page<User> pageUsers;
        if(AuthorityUtil.isAdminRole()){
            pageUsers = userRepository.findAll(pageable);
        } else {
            pageUsers = userRepository.findAllUser(pageable);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = null;
        if(pageUsers != null){
            users = pageUsers.getContent();
        }
        if(!CollectionUtils.isEmpty(users)){
            for(User user : users){
                UserDTO userDTO = convertUserToUserDTO(user);
                userDTOS.add(userDTO);
            }
        }
        log.info("getAllUsers method called");
        return PageableResponse.builder()
                .content(userDTOS)
                .pageNumber(pageUsers.getNumber())
                .pageSize(pageUsers.getSize())
                .totalElement(pageUsers.getTotalElements())
                .totalPages(pageUsers.getTotalPages())
                .isLast(pageUsers.isLast())
                .isFirst(pageUsers.isFirst())
                .build();

    }

    @Override
    public UserDTO getUserById(Long userId) {
        log.info("getUserById method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        return convertUserToUserDTO(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Long userId) {
        log.info("deleteUser method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        if(AuthorityUtil.isAdminRole() || AuthorityUtil.isSameUser(user.getUserName())) {
            if(!user.getUserStatus().equals(UserStatus.DELETED)){
                userRepository.deleteUser(userId);
            } else {
                log.error("User already deleted");
                throw new ExamPortalException("User already deleted");
            }

        } else {
            log.error("You cannot delete other user");
            throw new ExamPortalException("You cannot delete other user");
        }
        log.info("deleteUser method called");
    }

    @Override
    public UserDTO getUserByUsername(String userName) {
        log.info("getUserByUsername method called");
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new ExamPortalException("User not found with this: "+userName));
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String verifyUser(String token) {
        log.info("verifyUser method invoking");
        VerificationToken verificationToken = verificationTokenService.getVerificationTokenByToken(token);
        User user = verificationToken.getUser();
        if(verificationToken.getCreatedAt().plusMinutes(15).isAfter(LocalDateTime.now())) {
            user.setIsVerified(Boolean.TRUE);
            log.info("Saving user information into data bases");
            userRepository.save(user);
            log.info("verifyUser method called");
            return "Congratulations! Your account has been activated and email is verified!";
        } else {
            verificationTokenService.removeVerificationToken(verificationToken);
            sendVerificationEmail(user);
            log.info("verifyUser method called");
            return "This Link is expired. New Verification Link send to your E-Mail Please Verify it Within 15 min.";
        }
    }

    @Override
    public String changePassword(PasswordChangeRequest passwordChangeRequest, Long userId) {
        log.info("changePassword method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        if (passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            if (passwordChangeRequest.getOldPassword().trim().equals(passwordChangeRequest.getNewPassword().trim())) {
                log.error("New Password matched with Existing Password");
                throw new ExamPortalException("New Password matched with Existing Password. Please enter different Password");
            }
            passwordChangeRequest.setNewPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword().trim()));
            user.setPassword(passwordChangeRequest.getNewPassword());
            log.info("Saving user information into data base");
            userRepository.save(user);
            log.info("changePassword method called");
            return "Password Changed";
        } else {
            log.error("Password Not matched with Existing Password");
            throw new ExamPortalException("Password Not matched with Existing Password");
        }
    }

    @Override
    public Boolean isDeletedUser(String userName) {
        log.info("isDeletedUser method invoking");
        return userRepository.isDeletedUser(userName);
    }

    @Override
    public Boolean isUserExist(String userName) {
        log.info("isUserExist method invoking");
        return userRepository.isUserExist(userName);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO updateUserRole(List<UserRole> userRoles, Long userId) {
        log.info("updateUserRole method invoking");
        if(AuthorityUtil.isAdminRole()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
            Set<Role> roles = roleRepository.findByRoleNameIn(userRoles);
            user.getRoles().addAll(roles);
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            UserDTO userDTO = convertUserToUserDTO(user);
            return userDTO;
        } else {
            log.error("You do not have permission to add role");
            throw new ExamPortalException("You do not have permission to add role");
        }
    }

    private UserDTO getUpdatedUser(User user, UserDTO userDTO){
        log.info("getUpdatedUser method invoking");
        user.setName(userDTO.getName());
        Optional<User> optionalUser;
        if(!user.getPhoneNumber().equals(userDTO.getPhoneNumber())){
            optionalUser = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
            if(optionalUser.isPresent()){
                log.error("Phone Number already exist");
                throw new ExamPortalException("Phone Number exist. Please try other Username");
            }
        }
        user.setPhoneNumber(userDTO.getPhoneNumber());
        if(!user.getEmail().trim().equalsIgnoreCase(userDTO.getEmail().trim())){
            optionalUser = userRepository.findByEmail(userDTO.getEmail());
            if(optionalUser.isPresent()) {
                log.error("Email already exist");
                throw new ExamPortalException("Email already exist. Please try other Email");
            }
            if(!AuthorityUtil.isAdminRole()){
                log.info("Setting isVerified field false for new email");
                user.setIsVerified(Boolean.FALSE);
            }
        }
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setGender(userDTO.getGender());
        log.info("Saving user information into data base");
        user = userRepository.save(user);
        userDTO = convertUserToUserDTO(user);
        if(!user.getIsVerified()){
            User finalUser = user;
            log.info("Sending verification mail for verifying email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
        }
        log.info("getUpdatedUser method called");
        return userDTO;
    }
    private UserDTO convertUserToUserDTO(User user) {
        log.info("convertUserToUserDTO method invoking");
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        if(AuthorityUtil.isAdminRole()){
            userDTO.setStatus(user.getUserStatus());
            userDTO.setIsUserVerified(user.getIsVerified());
            userDTO.setIsAccountLocked(!user.getAccountNonLocked());
            userDTO.setIsAccountExpired(!user.getAccountNonExpired());
            userDTO.setIsCredentialsExpired(!user.getCredentialsNonExpired());
        }
        Set<RoleDTO> roleDTOS = user.getRoles().stream().map(r -> modelMapper.map(r, RoleDTO.class)).collect(Collectors.toSet());
        userDTO.setRoles(roleDTOS);
        log.info("convertUserToUserDTO method called");
        return userDTO;
    }

    private void sendVerificationEmail(User user){
        log.info("sendVerificationEmail method invoking");
        try {
            emailUtil.sendEmailWithAttachment(user);
        } catch (UnsupportedEncodingException e) {
            log.error("Exception raised while sending VerificationEmail:"+e.getMessage());
            userRepository.delete(user);
            throw new ExamPortalException("Verification link is not sent to your email. Please register once again");
        } catch (MessagingException e) {
            log.error("Exception raised while sending VerificationEmail:"+e.getMessage());
            userRepository.delete(user);
            throw new ExamPortalException("Verification link is not sent to your email. Please register once again");
        }
        log.info("sendVerificationEmail method called");
    }


}
