package com.increff.pos.dto;

import com.increff.pos.client.InvoiceClient;
import com.increff.pos.model.*;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OrderItemDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceClient invoiceClient;

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<OrderItemForm> orderItemForms) throws ApiException {
        if (orderItemForms.isEmpty()) {
            throw new ApiException("Cannot place an empty order!");
        }
        List<String> errors = Validate.validateOrderItemForms(orderItemForms);
        //Creating an order id
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }
        OrderPojo orderPojo = Helper.createOrderPojo();
        orderService.add(orderPojo);
        //Converting OrderItemForms to pojos
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            OrderItemPojo orderItemPojo = addOrderItem(orderItemForm, errors);
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemPojos.add(orderItemPojo);
        }
        if (!errors.isEmpty()) {
            throw new ApiException(Helper.convertListToString(errors));
        }
        orderItemService.addAll(orderItemPojos);
    }

    public List<OrderItemData> getAllByOrderId(Integer id) throws ApiException {
        orderService.get(id);
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll(id);
        List<OrderItemData> orderItemDatas = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            OrderItemData orderItemData = Helper.convertOrderItemPojoToData(orderItemPojo);
            orderItemData.setBarcode(productService.get(orderItemPojo.getProductId()).getBarcode());
            orderItemDatas.add(orderItemData);
        }
        return orderItemDatas;
    }

    public List<OrderData> getAllOrders() {
        List<OrderPojo> orderPojos = orderService.getAll();
        List<OrderData> orderDatas = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojos) {
            OrderData orderData = new OrderData();
            orderData.setId(orderPojo.getId());
            orderData.setTime(orderPojo.getTime());
            orderData.setIsInvoiceGenerated(orderPojo.getIsInvoiceGenerated());
            orderDatas.add(orderData);
        }
        return orderDatas;
    }

    @Transactional(rollbackOn = ApiException.class)
    public String generateInvoice(Integer orderId) throws ApiException, IOException {
        OrderPojo orderPojo = orderService.get(orderId);
        if (orderPojo.getIsInvoiceGenerated() == 1) {
            return downloadInvoice(orderId);
        }
        InvoiceData invoiceData = createInvoiceData(orderPojo);
        String invoice;
        try {
            invoice = invoiceClient.generateInvoice(invoiceData);
        } catch (Exception e) {
            throw new ApiException("Unable to create invoice at this moment!");
        }
        String pdfFileName = "invoice_" + invoiceData.getInvoiceNumber() + ".pdf";
        storeFile(invoice, pdfFileName);
        orderPojo.setIsInvoiceGenerated(1);
        return downloadInvoice(orderId);
    }

    @Transactional(rollbackOn = ApiException.class)
    public String downloadInvoice(Integer orderId) throws IOException, ApiException {
        OrderPojo orderPojo = orderService.get(orderId);
        if (orderPojo.getIsInvoiceGenerated() == 0) {
            throw new ApiException("Invoice needs to be generated before downloading!");
        }
        String filePath = "src/main/resources/pdf/invoice_order_" + orderId + ".pdf";
        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return Base64.getEncoder().encodeToString(bytesArray);
    }

    //Existing product and inventory check
    private OrderItemPojo addOrderItem(OrderItemForm orderItemForm, List<String> errors) {
        OrderItemPojo orderItemPojo = Helper.convertOrderItemFormToPojo(orderItemForm);
        ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
        if (productPojo == null) {
            errors.add("No Product exists with the given barcode: " + orderItemForm.getBarcode());
        } else {
            orderItemPojo.setProductId(productPojo.getId());
            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
            if (inventoryPojo == null || inventoryPojo.getQuantity() < orderItemPojo.getQuantity()) {
                errors.add("Product with the barcode " + productPojo.getBarcode() + " has insufficient inventory!");
            } else if (orderItemPojo.getSellingPrice() > productPojo.getMrp()) {
                errors.add("Selling price cannot exceed mrp!");
            } else {
                orderItemPojo.setQuantity(orderItemForm.getQuantity());
                inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemForm.getQuantity());
            }
        }
        return orderItemPojo;
    }

    private InvoiceData createInvoiceData(OrderPojo orderPojo) throws ApiException {
        InvoiceData invoiceData = new InvoiceData();
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll(orderPojo.getId());
        List<InvoiceLineItem> invoiceLineItems = createInvoiceItemList(orderItemPojos);
        String[] time = orderPojo.getTime().split(" ");
        invoiceData.setInvoiceNumber("order_" + orderPojo.getId());
        invoiceData.setInvoiceDate(time[0]);
        invoiceData.setInvoiceTime(time[1]);
        invoiceData.setLineItems(invoiceLineItems);
        invoiceData.setTotal(invoiceData.getTotal());
        return invoiceData;
    }

    private List<InvoiceLineItem> createInvoiceItemList(List<OrderItemPojo> orderItemPojos) throws ApiException {
        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        int sno = 1;
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            InvoiceLineItem invoiceLineItem = createInvoiceItem(orderItemPojo, sno);
            invoiceLineItems.add(invoiceLineItem);
            sno++;
        }
        return invoiceLineItems;
    }

    private InvoiceLineItem createInvoiceItem(OrderItemPojo orderItemPojo, int sno) throws ApiException {
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setSno(sno);
        invoiceLineItem.setBarcode(productPojo.getBarcode());
        invoiceLineItem.setName(productPojo.getName());
        invoiceLineItem.setQuantity(orderItemPojo.getQuantity());
        invoiceLineItem.setSellingPrice(orderItemPojo.getSellingPrice());
        invoiceLineItem.setTotal(invoiceLineItem.getTotal());
        return invoiceLineItem;
    }

    private void storeFile(String invoice, String pdfFileName) {
        File pdfDir = new File("src/main/resources./pdf");
        pdfDir.mkdirs();
        File file = new File(pdfDir, pdfFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] decoder = Base64.getDecoder().decode(invoice);
            fos.write(decoder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
