package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/order")
public class OrderApiController {
    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Creates the Customer order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addAll(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        dto.addAll(orderItemForms);
    }

    @ApiOperation(value = "Gets the order items by order id")
    @RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getAllByOrderId(@PathVariable Integer orderId) throws ApiException {
        return dto.getAllByOrderId(orderId);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return dto.getAllOrders();
    }

    @ApiOperation(value = "Generate invoice by orderId")
    @RequestMapping(path = "/{orderId}/generate", method = RequestMethod.GET)
    public String generateInvoice(@PathVariable Integer orderId) throws ApiException, IOException {
        return dto.generateInvoice(orderId);
    }
}
