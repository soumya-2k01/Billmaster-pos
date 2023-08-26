package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class InventoryService {

    private static final Integer MAX_QUANTITY = 1000000000;
    @Autowired
    private InventoryDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = get(inventoryPojo.getId());
        if (existingPojo == null) {
            dao.insert(inventoryPojo);
        } else {
            Integer qty = inventoryPojo.getQuantity() + existingPojo.getQuantity();
            if (qty > MAX_QUANTITY) {
                throw new ApiException("Quantity cannot exceed " + MAX_QUANTITY);
            }
            existingPojo.setQuantity(qty);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<InventoryPojo> inventoryPojos) throws ApiException {
        for (InventoryPojo inventoryPojo : inventoryPojos) {
            add(inventoryPojo);
        }
    }

    public InventoryPojo get(Integer id) {
        return dao.select(id);
    }

    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = get(id);
        if (inventoryPojo == null) {
            throw new ApiException("No Inventory exists with the given id");
        }
        return inventoryPojo;
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = getCheck(id);
        existingPojo.setQuantity(inventoryPojo.getQuantity());

    }


}
