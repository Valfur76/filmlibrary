package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.OrderDTO;
import com.bagrov.springpmhw.videorent.model.Order;
import com.bagrov.springpmhw.videorent.repository.FilmRepository;
import com.bagrov.springpmhw.videorent.repository.OrderRepository;
import com.bagrov.springpmhw.videorent.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final FilmService filmService;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public OrderService(OrderRepository orderRepository,
                        ModelMapper modelMapper,
                        FilmService filmService,
                        UserRepository userRepository,
                        FilmRepository filmRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.filmService = filmService;
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream().map(this::convertToOrderDTO).toList();
    }

    public OrderDTO getOne(int id) {
        return convertToOrderDTO(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с указанным id не найдена")));
    }

    @Transactional
    public OrderDTO orderFilm(OrderDTO orderDTO) {
        int rentPeriod = orderDTO.getRentPeriod() != null ? orderDTO.getRentPeriod() : 14;
        orderDTO.setFilmId(orderDTO.getFilmId());
        orderDTO.setRentDate(LocalDateTime.now());
        orderDTO.setRentPeriod(rentPeriod);
        orderDTO.setPurchase(true);
        return convertToOrderDTO(orderRepository.save(convertToOrder(orderDTO)));
    }

    public Page<OrderDTO> listUserOrderFilms(int id, Pageable pageRequest) {
        Page<Order> objects = orderRepository.getFilmRentInfoByUserId(id, pageRequest);
        List<OrderDTO> results = objects.getContent().stream().map(this::convertToOrderDTO).toList();
        return new PageImpl<>(results, pageRequest, objects.getTotalElements());
    }

    @Transactional
    public OrderDTO save(OrderDTO orderDTO) {
        return convertToOrderDTO(orderRepository.save(convertToOrder(orderDTO)));
    }

    @Transactional
    public OrderDTO update(int id, OrderDTO orderDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order updatedOrder = enrichOrder(optionalOrder, orderDTO);
            updatedOrder.setId(id);
            return convertToOrderDTO(orderRepository.save(updatedOrder));
        }
        throw new NotFoundException("Заказ с указанным id не найдена");
    }

    @Transactional
    public void delete(int id) {
        if (orderRepository.findById(id).isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new NotFoundException("Заказ с указанным id не найдена");
        }
    }

    private OrderDTO convertToOrderDTO(Order order) {
        if (modelMapper.getTypeMap(Order.class, OrderDTO.class) == null) {
            modelMapper.createTypeMap(Order.class, OrderDTO.class)
                    .addMappings(
                            mapper -> {
                                mapper.skip(OrderDTO::setUserId);
                                mapper.skip(OrderDTO::setFilmId);
                                mapper.skip(OrderDTO::setFilmDTO);
                            })
                    .setPostConverter(postConverterToOrderTO());
        }
        return modelMapper.map(order, OrderDTO.class);
    }

    private Converter<Order, OrderDTO> postConverterToOrderTO() {
        return context -> {
            Order order = context.getSource();
            OrderDTO orderDTO = context.getDestination();
            orderDTO.setUserId(order.getUser().getId());
            orderDTO.setFilmId(order.getFilm().getId());
            orderDTO.setFilmDTO(filmService.convertToFilmDTO(order.getFilm()));
            return context.getDestination();
        };
    }

    private Order convertToOrder(OrderDTO orderDTO) {
        if (modelMapper.getTypeMap(OrderDTO.class, Order.class) == null) {
            modelMapper.createTypeMap(OrderDTO.class, Order.class)
                    .addMappings(
                            mapper -> {
                                mapper.skip(Order::setUser);
                                mapper.skip(Order::setFilm);
                            })
                    .setPostConverter(postConverterToOrder());
        }
        return modelMapper.map(orderDTO, Order.class);
    }

    private Converter<OrderDTO, Order> postConverterToOrder() {
        return context -> {
            OrderDTO orderDTO = context.getSource();
            Order order = context.getDestination();
            order.setUser(userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
            order.setFilm(filmRepository.findById(orderDTO.getFilmId()).orElseThrow(() -> new NotFoundException("Фильм не найден")));
            return context.getDestination();
        };
    }

    private Order enrichOrder(Optional<Order> optionalOrder, OrderDTO orderDTO) {
        Order updatedRole = new Order();
        if (orderDTO.getUserId() != 0) {
            updatedRole.setUser(convertToOrder(orderDTO).getUser());
        } else {
            updatedRole.setUser(optionalOrder.get().getUser());
        }

        if (orderDTO.getFilmId() != 0) {
            updatedRole.setFilm(convertToOrder(orderDTO).getFilm());
        } else {
            updatedRole.setFilm(optionalOrder.get().getFilm());
        }

        if (orderDTO.getRentDate() != null) {
            updatedRole.setRentDate(orderDTO.getRentDate());
        } else {
            updatedRole.setRentDate(optionalOrder.get().getRentDate());
        }

        if (orderDTO.getRentPeriod() != 0) {
            updatedRole.setRentPeriod(orderDTO.getRentPeriod());
        } else {
            updatedRole.setRentPeriod(optionalOrder.get().getRentPeriod());
        }

        if (orderDTO.isPurchase()) {
            updatedRole.setPurchase(orderDTO.isPurchase());
        } else {
            updatedRole.setPurchase(optionalOrder.get().isPurchase());
        }

        return updatedRole;
    }
}
