package com.increff.fop.controller;

import com.increff.fop.dto.JavaToXml;
import com.increff.fop.dto.PdfGenerator;
import com.increff.fop.model.InvoiceData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api
@RestController
public class pdfController {
    @Autowired
    private JavaToXml javaToXml;
    @Autowired
    private PdfGenerator pdfGenerator;

    @ApiOperation(value = "JavaToPdf")
    @RequestMapping(path = "/api/pdf", method = RequestMethod.POST)
    public String classToPDF(@RequestBody InvoiceData invoiceData) {
        javaToXml.javaToXmlConverter(invoiceData);
        return pdfGenerator.xmlToPdfConverter(invoiceData);
    }
}