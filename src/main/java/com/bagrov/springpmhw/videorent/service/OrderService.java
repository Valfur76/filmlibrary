package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.OrderDTO;
import com.bagrov.springpmhw.videorent.model.Order;
import com.bagrov.springpmhw.videorent.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream().map(this::convertToOrderDTO).toList();
    }

    public OrderDTO getOne(int id) {
        return convertToOrderDTO(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с указанным id не найдена")));
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
        return modelMapper.map(order, OrderDTO.class);
    }

    private Order convertToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
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
