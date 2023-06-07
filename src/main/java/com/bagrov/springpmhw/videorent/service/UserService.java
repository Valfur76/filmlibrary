package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.UserDTO;
import com.bagrov.springpmhw.videorent.model.Film;
import com.bagrov.springpmhw.videorent.model.Order;
import com.bagrov.springpmhw.videorent.model.Role;
import com.bagrov.springpmhw.videorent.model.User;
import com.bagrov.springpmhw.videorent.repository.OrderRepository;
import com.bagrov.springpmhw.videorent.repository.RoleRepository;
import com.bagrov.springpmhw.videorent.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       RoleRepository roleRepository, OrderRepository orderRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToUserDTO).toList();
    }

    public UserDTO getOne(int id) {
        return convertToUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден")));
    }

    @Transactional
    public UserDTO save(UserDTO userDTO) {
        User newUser = convertToUser(userDTO);
        Role role = new Role();
        role.setId(1);

        newUser.setCreatedWhen(LocalDate.now());
        newUser.setRole(role);
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return convertToUserDTO(userRepository.save(newUser));
    }

    @Transactional
    public UserDTO update(int id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User updatedUser = enrichUser(optionalUser, userDTO);
            updatedUser.setId(id);
            return convertToUserDTO(userRepository.save(updatedUser));
        }
        throw new NotFoundException("Пользователь с указанным id не найден");
    }

    @Transactional
    public void delete(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
    }

    public List<Film> rentedFilms(int userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(Order::getFilm).toList();
    }

    public UserDTO getUserByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if (user != null) {
            return convertToUserDTO(user);
        } else {
            return null;
        }
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return convertToUserDTO(user);
        } else {
            return null;
        }
    }

    private UserDTO convertToUserDTO(User user) {
        if (modelMapper.getTypeMap(User.class, UserDTO.class) == null) {
            modelMapper.createTypeMap(User.class, UserDTO.class)
                    .addMappings(mapper -> mapper.skip(UserDTO::setRoleId))
                    .setPostConverter(postConverterToUserDTO());
        }
        return modelMapper.map(user, UserDTO.class);
    }

    private Converter<User, UserDTO> postConverterToUserDTO() {
        return context -> {
            User user = context.getSource();
            UserDTO userDTO = context.getDestination();
            userDTO.setRoleId(
                    Objects.isNull(user) || Objects.isNull(user.getRole())
                            ? null
                            : user.getRole().getId()
            );
            return context.getDestination();
        };
    }

    private User convertToUser(UserDTO userDTO) {
        if (modelMapper.getTypeMap(UserDTO.class, User.class) == null) {
            modelMapper.createTypeMap(UserDTO.class, User.class)
                    .addMappings(mapper -> mapper.skip(User::setRole))
                    .setPostConverter(postConverterToUser());
        }
        return modelMapper.map(userDTO, User.class);
    }

    private Converter<UserDTO, User> postConverterToUser() {
        return context -> {
            UserDTO userDTO = context.getSource();
            User user = context.getDestination();
            if (!Objects.isNull(userDTO.getRoleId())) {
                user.setRole(roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new NotFoundException("Роль с указанным id не найдена")));
            } else {
                user.setRole(null);
            }
            return context.getDestination();
        };
    }

    private User enrichUser(Optional<User> optionalUser, UserDTO userDTO) {
        User updatedUser = new User();
        if (userDTO.getLogin() != null) {
            updatedUser.setLogin(userDTO.getLogin());
        } else {
            updatedUser.setLogin(optionalUser.get().getLogin());
        }

        if (userDTO.getPassword() != null) {
            updatedUser.setPassword(userDTO.getPassword());
        } else {
            updatedUser.setPassword(optionalUser.get().getPassword());
        }

        if (userDTO.getFirstName() != null) {
            updatedUser.setFirstName(userDTO.getFirstName());
        } else {
            updatedUser.setFirstName(optionalUser.get().getFirstName());
        }

        if (userDTO.getLastName() != null) {
            updatedUser.setLastName(userDTO.getLastName());
        } else {
            updatedUser.setLastName(optionalUser.get().getLastName());
        }

        if (userDTO.getMiddleName() != null) {
            updatedUser.setMiddleName(userDTO.getMiddleName());
        } else {
            updatedUser.setMiddleName(optionalUser.get().getMiddleName());
        }

        if (userDTO.getBirthDate() != null) {
            updatedUser.setBirthDate(userDTO.getBirthDate());
        } else {
            updatedUser.setBirthDate(optionalUser.get().getBirthDate());
        }

        if (userDTO.getPhone() != null) {
            updatedUser.setPhone(userDTO.getPhone());
        } else {
            updatedUser.setPhone(optionalUser.get().getPhone());
        }

        if (userDTO.getAddress() != null) {
            updatedUser.setAddress(userDTO.getAddress());
        } else {
            updatedUser.setAddress(optionalUser.get().getAddress());
        }

        if (userDTO.getEmail() != null) {
            updatedUser.setEmail(userDTO.getEmail());
        } else {
            updatedUser.setEmail(optionalUser.get().getEmail());
        }
        updatedUser.setCreatedWhen(optionalUser.get().getCreatedWhen());

        if (userDTO.getRoleId() != null) {
            updatedUser.setRole(convertToUser(userDTO).getRole());
        } else {
            updatedUser.setRole(optionalUser.get().getRole());
        }
        return updatedUser;
    }
}
