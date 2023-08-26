package com.increff.pos.service;

import com.increff.pos.dao.AdminDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(UserPojo userPojo) throws ApiException {
        UserPojo existing = get(userPojo.getEmail());
        if (existing == null) {
            dao.insert(userPojo);
        } else {
            throw new ApiException("Email already registered!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    public UserPojo get(String email) {
        return dao.select(email);
    }

    public List<UserPojo> getAll() {
        return dao.selectAll();
    }

}
