package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    //TODO make final static string of date format

    public static BrandPojo convertBrandFormToPojo(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brandForm.getBrand());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

    public static List<BrandData> convertBrandPojosToDatas(List<BrandPojo> brandPojos) {
        List<BrandData> brandDatas = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            brandDatas.add(convert(brandPojo));
        }
        return brandDatas;
    }

    public static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setId(brandPojo.getId());
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        return brandData;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm productForm) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(productForm.getName());
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setMrp(productForm.getMrp());
        return productPojo;
    }

    public static ProductData convertProductPojoToData(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBrandCategory(productPojo.getBrandCategory());
        productData.setName(productPojo.getName());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        return productData;
    }

    public static InventoryPojo convertInventoryFormToPojo(InventoryForm inventoryForm) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    public static InventoryData convertInventoryPojoToData(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static OrderItemPojo convertOrderItemFormToPojo(OrderItemForm orderItemForm) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return orderItemPojo;
    }

    public static OrderItemData convertOrderItemPojoToData(OrderItemPojo orderItemPojo) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemData;
    }

    public static OrderPojo createOrderPojo() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = dtf.format(LocalDateTime.now());
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(time);
        orderPojo.setIsInvoiceGenerated(0);
        return orderPojo;
    }

    public static List<DailyReportData> convertDailyReportPojosToDatas(List<DailyReportPojo> dailyReportPojos) {
        List<DailyReportData> dailyReportDataList = new ArrayList<>();
        for (DailyReportPojo dailyReportPojo : dailyReportPojos) {
            DailyReportData dailyReportData = new DailyReportData();
            dailyReportData.setDate(dailyReportPojo.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dailyReportData.setInvoicedOrderCount(dailyReportPojo.getInvoicedOrdersCount());
            dailyReportData.setInvoicedItemCount(dailyReportPojo.getInvoicedItemsCount());
            dailyReportData.setTotalRevenue(dailyReportPojo.getTotalRevenue());
            dailyReportDataList.add(dailyReportData);
        }
        return dailyReportDataList;
    }

    public static UserPojo convertSignupFormToPojo(SignupForm signupForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(signupForm.getEmail());
        userPojo.setPassword(signupForm.getPassword());
        return userPojo;
    }

    public static UserPojo convertUserFormToPojo(UserForm userForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(userForm.getEmail());
        userPojo.setRole(userForm.getRole());
        userPojo.setPassword(userForm.getPassword());
        return userPojo;
    }

    public static UserData convertUserPojoToData(UserPojo userPojo) {
        UserData userData = new UserData();
        userData.setEmail(userPojo.getEmail());
        userData.setRole(userPojo.getRole());
        userData.setId(userPojo.getId());
        return userData;
    }

    public static InventoryReportData createInventoryReportData(BrandData brandData, Integer qty) {
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand(brandData.getBrand());
        inventoryReportData.setCategory(brandData.getCategory());
        inventoryReportData.setQuantity(qty);
        return inventoryReportData;
    }

    public static String convertListToString(List<String> errors) {
        StringBuilder result = new StringBuilder();
        for (String error : errors) {
            result.append(error);
            result.append('\n');
        }
        return result.toString();
    }

    public static Authentication convertUserPojoToAuthObject(UserPojo userPojo) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getId());
        principal.setRole(userPojo.getRole());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userPojo.getRole()));

        // Create Authentication
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
//TODO ALl privates method should be below public methods

