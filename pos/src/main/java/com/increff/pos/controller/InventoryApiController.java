package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class InventoryApiController {

    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Adds Inventory")
    @RequestMapping(path = "/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        dto.add(inventoryForm);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/inventorys", method = RequestMethod.POST)
    public void addAll(@RequestBody List<InventoryForm> inventoryForms) throws ApiException {
        dto.addAll(inventoryForms);
    }

    @ApiOperation(value = "Gets the inventory by id")
    @RequestMapping(path = "/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of whole inventory")
    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return dto.getAll();

    }

    @ApiOperation(value = "Updates Inventory")
    @RequestMapping(path = "/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        dto.update(id, inventoryForm);
    }


}
