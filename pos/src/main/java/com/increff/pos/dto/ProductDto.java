package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductForm productForm) throws ApiException {
        Validate.validateProductForm(productForm);
        ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
        BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
        if (brandPojo == null) {
            throw new ApiException("The given brand-category pair does not exist.");
        }
        productPojo.setBrandCategory(brandPojo.getId());
        productService.add(productPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<ProductForm> productForms) throws ApiException {
        List<String> errors = Validate.validateProductForms(productForms);
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }

        List<ProductPojo> productPojos = new ArrayList<>();
        int count = 1;
        //check if given brand category exists
        for (ProductForm productForm : productForms) {
            BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
            if (brandPojo == null) {
                errors.add("Brand " + productForm.getBrand() + " and category " + productForm.getCategory() + " pair does not exist for row " + count);
            } else {
                ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
                productPojo.setBrandCategory(brandPojo.getId());
                productPojos.add(productPojo);
            }
            count++;
        }
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }
        productService.addAll(productPojos);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productService.getCheck(id);
        ProductData productData = Helper.convertProductPojoToData(productPojo);
        productData.setBrand(brandService.get(productPojo.getBrandCategory()).getBrand());
        productData.setCategory(brandService.get(productPojo.getBrandCategory()).getCategory());
        return productData;
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojos = productService.getAll();
        List<ProductData> productDatas = new ArrayList<>();
        for (ProductPojo productPojo : productPojos) {
            ProductData productData = Helper.convertProductPojoToData(productPojo);
            productData.setBrand(brandService.get(productPojo.getBrandCategory()).getBrand());
            productData.setCategory(brandService.get(productPojo.getBrandCategory()).getCategory());
            productDatas.add(productData);
        }
        return productDatas;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, ProductForm productForm) throws ApiException {
        Validate.validateProductForm(productForm);
        BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
        if (brandPojo == null) {
            throw new ApiException("Given brand-category pair does not exist!");
        }
        ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
        productPojo.setBrandCategory(brandPojo.getId());
        productService.update(id, productPojo);
    }


}
