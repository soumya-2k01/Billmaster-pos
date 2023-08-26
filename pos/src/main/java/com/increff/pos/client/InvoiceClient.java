package com.increff.pos.client;
import com.increff.pos.model.InvoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InvoiceClient {
    @Autowired
    private RestTemplate restTemplate;
    private static final String INVOICE_URL = "http://localhost:8000/fop/api/pdf";

    public String generateInvoice(InvoiceData invoiceData){
        HttpEntity<InvoiceData> httpEntity = new HttpEntity<>(invoiceData);
        return restTemplate.exchange(INVOICE_URL, HttpMethod.POST,httpEntity,String.class).getBody();
    }
}
