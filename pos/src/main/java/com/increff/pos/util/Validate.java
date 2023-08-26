package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class Validate {
    private static final Integer MAX_QUANTITY = 1000000000;

    public static void validateProductForm(ProductForm productForm) throws ApiException{
        if(StringUtil.isEmpty(productForm.getBrand())){
            throw new ApiException("Brand cannot be empty");
        }
        if(StringUtil.isEmpty(productForm.getCategory())){
            throw new ApiException("Category cannot be empty");
        }
        if(StringUtil.isEmpty(productForm.getBarcode())){
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(productForm.getName())){
            throw new ApiException("Product Name cannot be empty");
        }
        if(productForm.getMrp()==null){
            throw new ApiException("Mrp cannot be empty");
        }
        if(productForm.getMrp()<=0){
            throw new ApiException("Mrp cannot be less than or equal to 0");
        }
        if(productForm.getMrp()>=MAX_QUANTITY){
            throw new ApiException("Mrp cannot exceed "+MAX_QUANTITY);
        }
        Normalize.normalize(productForm);
        Normalize.normalizeDouble(productForm);
    }

    public static List<String> validateProductForms(List<ProductForm> productForms) throws ApiException {
        if (productForms.size() > 5000) {
            throw new ApiException("File size cannot exceed 5000 rows!");
        }
        List<String> errors = new ArrayList<>();
        Set<String> set = new HashSet<>();
        int count = 1;
        for(ProductForm productForm: productForms){
            Normalize.normalize(productForm);
            Normalize.normalizeDouble(productForm);
            if(StringUtil.isEmpty(productForm.getBrand())){
                errors.add("Brand cannot be empty in row "+count);
            }
            else if(StringUtil.isEmpty(productForm.getCategory())){
                errors.add("Category cannot be empty in row "+count);
            }
            else if(StringUtil.isEmpty(productForm.getBarcode())){
                errors.add("Barcode cannot be empty in row "+count);
            }
            else if(set.contains(productForm.getBarcode())){
                errors.add("Duplicate barcode entry in row "+count+" for barcode "+ productForm.getBarcode());
            }
            else if(StringUtil.isEmpty(productForm.getName())){
                errors.add("Product Name cannot be empty in row "+count);
            }
            else if(productForm.getMrp()==null){
                errors.add("Mrp cannot be empty in row: "+count);
            }
            else if(productForm.getMrp()<0){
                errors.add("Mrp is negative in row: "+count);
            }
            set.add(productForm.getBarcode());
        }
        return errors;


    }

    public static void validateInventoryForm(InventoryForm inventoryForm) throws ApiException{
        if(StringUtil.isEmpty(inventoryForm.getBarcode())){
            throw new ApiException("Barcode cannot be empty");
        }
        if(inventoryForm.getQuantity()==null){
            throw new ApiException("Quantity cannot be empty");
        }
        if(inventoryForm.getQuantity()<0){
            throw new ApiException("Quantity cannot be negative");
        }
        if(inventoryForm.getQuantity()>MAX_QUANTITY){
            throw new ApiException("Quantity cannot exceed "+MAX_QUANTITY);
        }
    }
    public static List<String> validateInventoryForms(List<InventoryForm> inventoryForms) throws ApiException {
        if(inventoryForms.size()>5000){
            throw new ApiException("File size cannot exceed 5000 rows!");
        }
        List<String> errors = new ArrayList<>();
        Set<String> set = new HashSet<>();
        int count = 1;
        for(InventoryForm inventoryForm: inventoryForms){
            if(StringUtil.isEmpty(inventoryForm.getBarcode())){
                errors.add("Barcode cannot be empty in row "+count);
            }
            else if(set.contains(inventoryForm.getBarcode())){
                errors.add("Duplicate barcode entry in row "+count+" for barcode "+ inventoryForm.getBarcode());
            }
            else if(inventoryForm.getQuantity()==null){
                errors.add("Quantity cannot be empty in row: "+count);
            }
            else if(inventoryForm.getQuantity()<1){
                errors.add("Quantity is negative in row: "+count);
            }
            count++;
            set.add(inventoryForm.getBarcode());
        }
        return errors;
    }

    public static void validateBrandForm(BrandForm brandForm) throws ApiException{
        Normalize.normalize(brandForm);
        if(StringUtil.isEmpty(brandForm.getBrand())) {
            throw new ApiException("Brand cannot be empty");
        }
        if(StringUtil.isEmpty(brandForm.getCategory())) {
            throw new ApiException("Category cannot be empty");
        }
    }

    public static List<String> validateBrandForms(List<BrandForm> brandForms) throws ApiException {
        if(brandForms.size()>5000){
            throw new ApiException("File size cannot exceed 5000 rows!");
        }
        List<String> errors = new ArrayList<>();
        Set<String> set = new HashSet<>();
        int count = 1;
        for(BrandForm brandForm: brandForms){
            Normalize.normalize(brandForm);
            if(StringUtil.isEmpty(brandForm.getBrand())){
                errors.add("Brand cannot be empty in row "+count);
            }
            else if(StringUtil.isEmpty(brandForm.getCategory())){
                errors.add("Category cannot be empty in row "+count);
            }
            else if(set.contains(brandForm.getBrand()+"_"+brandForm.getCategory())){
                errors.add("Duplicate entry for brand: "+brandForm.getBrand()+" and category: "+brandForm.getCategory() + " in row "+count);
            }
            count++;
            set.add(brandForm.getBrand()+"_"+brandForm.getCategory());
        }
        return errors;
    }

    public static List<String> validateOrderItemForms(List<OrderItemForm> orderItemForms){
        List<String> errors = new ArrayList<>();
        Set<String> set = new HashSet<>();
        int count = 1;
        for(OrderItemForm orderItemForm: orderItemForms){
            if(StringUtil.isEmpty(orderItemForm.getBarcode())){
                errors.add("Barcode cannot be empty for item "+count);
            }
            else if(set.contains(orderItemForm.getBarcode())){
                errors.add("Duplicate barcode entry for barcode: "+orderItemForm.getBarcode());
            }
            else if(orderItemForm.getQuantity()==null){
                errors.add("Quantity field cannot be empty for barcode: "+orderItemForm.getBarcode());
            }
            else if(orderItemForm.getQuantity()<1){
                errors.add("Quantity should be greater than 0 for barcode: "+orderItemForm.getBarcode());
            }
            else if(orderItemForm.getSellingPrice()==null){
                errors.add("Selling Price field cannot be empty for barcode: "+orderItemForm.getBarcode());
            }
            else if(orderItemForm.getSellingPrice()<0){
                errors.add("Selling price cannot be in negative for barcode: "+orderItemForm.getBarcode());
            }
            count++;
            set.add(orderItemForm.getBarcode());
        }
        return errors;
    }

    public static void validateSalesReportForm(SalesReportForm salesReportForm) throws ApiException{
        if(salesReportForm.getStartDate().isEmpty()){
            throw new ApiException("Invalid start date!");
        }
        if(salesReportForm.getEndDate().isEmpty()){
            throw new ApiException("Invalid end date!");
        }
        int compare = salesReportForm.getStartDate().compareTo(salesReportForm.getEndDate());
        if(compare>0){
            throw new ApiException("Start date cannot be beyond end date!");
        }

    }
    public static void validateDailyReportForm(DailyReportForm dailyReportForm) throws ApiException{
        if(dailyReportForm.getStartDate().isEmpty()){
            throw new ApiException("Invalid start Date!");
        }
        if(dailyReportForm.getEndDate().isEmpty()){
            throw new ApiException("Invalid end Date!");
        }
        int compare = dailyReportForm.getStartDate().compareTo(dailyReportForm.getEndDate());
        if(compare>0){
            throw new ApiException("Start date cannot be beyond end date!");
        }
    }

    public static String validateSignupForm(SignupForm signupForm){
        if(StringUtil.isEmpty(signupForm.getEmail())){
           return "Email cannot be empty!";
        }
        signupForm.setEmail(signupForm.getEmail().toLowerCase().trim());
        if(validEmail(signupForm.getEmail())){
            return "Not a valid email address!";
        }
        if(StringUtil.isEmpty(signupForm.getPassword())){
            return "Password cannot be empty!";
        }
        return "";
    }

    public static String validateLoginForm(LoginForm loginForm){
        loginForm.setEmail(loginForm.getEmail().toLowerCase().trim());
        if(StringUtil.isEmpty(loginForm.getEmail())){
            return "Enter email!";
        }
        if(validEmail(loginForm.getEmail())){
            return "Not a valid email address!";
        }
        if(StringUtil.isEmpty(loginForm.getPassword())){
            return "Enter a password!";
        }
        return "";
    }
    public static void validateUserForm(UserForm userForm) throws ApiException{
        Normalize.normalizeUserForm(userForm);
        if(StringUtil.isEmpty(userForm.getEmail())){
            throw new ApiException("Email cannot be empty!");
        }
        if(validEmail(userForm.getEmail())){
            throw new ApiException("Invalid email!");
        }
        if(StringUtil.isEmpty(userForm.getPassword())){
            throw new ApiException("Password cannot be empty!");
        }
        if(StringUtil.isEmpty(userForm.getRole())){
            throw new ApiException("Select a role!");
        }
    }

    private static boolean validEmail(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return !pattern.matcher(email).matches();
    }
}
