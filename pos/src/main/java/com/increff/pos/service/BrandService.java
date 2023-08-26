package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class BrandService {

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo brandPojo) throws ApiException {
        BrandPojo existingPojo = getByBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (existingPojo == null) dao.insert(brandPojo);
        else
            throw new ApiException("Brand: " + existingPojo.getBrand() + " and category: " + existingPojo.getCategory() + " pair already exists!");
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<BrandPojo> brandPojos) throws ApiException {
        int count = 1;
        List<String> errors = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            BrandPojo existingPojo = getByBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
            if (existingPojo != null) {
                errors.add("Brand: " + existingPojo.getBrand() + " and category: " + existingPojo.getCategory() + " pair already exists for row no." + count);
            }
            count++;
        }
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }
        for (BrandPojo brandPojo : brandPojos) {
            dao.insert(brandPojo);
        }
    }

    public BrandPojo get(Integer id) throws ApiException {
        return dao.select(id);

    }

    public BrandPojo getByBrandCategory(String brand, String category) {
        return dao.select(brand, category);
    }

    public List<BrandPojo> getAllByBrandCategory(String brand, String category) {
        if (Objects.equals(brand, "")) {
            return dao.selectAllByCategory(category);
        }
        if (Objects.equals(category, "")) {
            return dao.selectAllByBrand(brand);
        }
        return dao.selectAllByBrandCategory(brand, category);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = getCheck(id);
        BrandPojo existingPojo = getByBrandCategory(brandForm.getBrand(), brandForm.getCategory());
        if (existingPojo == null || Objects.equals(existingPojo.getId(), id)) {
            brandPojo.setBrand(brandForm.getBrand());
            brandPojo.setCategory(brandForm.getCategory());
        } else {
            throw new ApiException("Given Brand Category pair already exists!");
        }

    }

    private BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo brandPojo = dao.select(id);
        if (brandPojo == null) {
            throw new ApiException("No brand category pair exists with the given id");
        }
        return brandPojo;
    }
}
