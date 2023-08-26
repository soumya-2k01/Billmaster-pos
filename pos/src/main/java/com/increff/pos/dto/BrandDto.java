package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        Validate.validateBrandForm(brandForm);
        BrandPojo brandPojo = Helper.convertBrandFormToPojo(brandForm);
        brandService.add(brandPojo);
    }

    public void addAll(List<BrandForm> brandForms) throws ApiException {
        List<String> errors = Validate.validateBrandForms(brandForms);
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (BrandForm brandForm : brandForms) {
            BrandPojo brandPojo = Helper.convertBrandFormToPojo(brandForm);
            brandPojos.add(brandPojo);
        }
        brandService.addAll(brandPojos);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojos = brandService.getAll();
        return Helper.convertBrandPojosToDatas(brandPojos);
    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo brandPojo = brandService.get(id);
        return Helper.convert(brandPojo);
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException {
        Validate.validateBrandForm(brandForm);
        brandService.update(id, brandForm);
    }

}