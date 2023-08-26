package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class ReportApiController {

    @Autowired
    private ReportDto dto;

    @ApiOperation(value = "Fetch Brand Report")
    @RequestMapping(path = "/brandReport", method = RequestMethod.POST)
    public List<BrandData> getBrandReport(@RequestBody BrandForm brandForm) {
        return dto.getBrandReport(brandForm);
    }

    @ApiOperation(value = "Fetch Inventory Report")
    @RequestMapping(path = "/inventoryReport", method = RequestMethod.POST)
    public List<InventoryReportData> getInventoryReport(@RequestBody BrandForm brandForm) {
        return dto.getInventoryReport(brandForm);
    }

    @ApiOperation(value = "Fetch Sales Report")
    @RequestMapping(path = "/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm) throws ApiException {
        return dto.getSalesReport(salesReportForm);
    }

    @ApiOperation(value = "Fetch Daily Report by date range")
    @RequestMapping(path = "/dailyReport", method = RequestMethod.POST)
    public List<DailyReportData> getDailyReport(@RequestBody DailyReportForm dailyReportForm) throws ApiException {
        return dto.getDailyReport(dailyReportForm);
    }

}
