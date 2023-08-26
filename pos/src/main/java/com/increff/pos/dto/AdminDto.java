package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.AdminService;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminDto {
    @Autowired
    private AdminService adminService;

    @Transactional
    public void add(UserForm userForm) throws ApiException {
        Validate.validateUserForm(userForm);
        UserPojo userPojo = Helper.convertUserFormToPojo(userForm);
        adminService.add(userPojo);
    }

    public void delete(Integer id) {
        adminService.delete(id);
    }

    public List<UserData> getAll() {
        List<UserPojo> userPojos = adminService.getAll();
        List<UserData> userDatas = new ArrayList<>();
        for (UserPojo userPojo : userPojos) {
            userDatas.add(Helper.convertUserPojoToData(userPojo));
        }
        return userDatas;
    }

}
