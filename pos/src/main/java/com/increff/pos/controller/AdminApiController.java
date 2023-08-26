package com.increff.pos.controller;

import com.increff.pos.dto.AdminDto;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/admin/user")
public class AdminApiController {

    @Autowired
    private AdminDto dto;

    @ApiOperation(value = "Adds a user")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addUser(@RequestBody UserForm userForm) throws ApiException {
        dto.add(userForm);
    }

    @ApiOperation(value = "Deletes a user")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable int id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<UserData> getAllUser() {
        return dto.getAll();
    }
}
