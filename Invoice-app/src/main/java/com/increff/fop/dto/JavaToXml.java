package com.increff.fop.dto;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.increff.fop.model.InvoiceData;
import com.increff.fop.model.InvoiceLineItem;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

@Service
public class JavaToXml {

    public void javaToXmlConverter(InvoiceData invoiceData){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element invoice = doc.createElement("invoice");
            doc.appendChild(invoice);

            Element invoiceNumber = doc.createElement("invoiceNumber");
            invoiceNumber.appendChild(doc.createTextNode("#" + invoiceData.getInvoiceNumber()));
            invoice.appendChild(invoiceNumber);

            Element invoiceDate = doc.createElement("invoiceDate");
            invoiceDate.appendChild(doc.createTextNode("" + invoiceData.getInvoiceDate()));
            invoice.appendChild(invoiceDate);

            Element invoiceTime = doc.createElement("invoiceTime");
            invoiceTime.appendChild(doc.createTextNode("" + invoiceData.getInvoiceTime()));
            invoice.appendChild(invoiceTime);

            Element companyName = doc.createElement("companyName");
            companyName.appendChild(doc.createTextNode("BillMaster"));
            invoice.appendChild(companyName);

            Element building = doc.createElement("building");
            building.appendChild(doc.createTextNode("Room No 18, Panhala Boys Hostel,"));
            invoice.appendChild(building);

            Element street = doc.createElement("street");
            street.appendChild(doc.createTextNode("IIIT Pune, Vadgoan"));
            invoice.appendChild(street);

            Element city = doc.createElement("city");
            city.appendChild(doc.createTextNode("Pune, Maharashtra 411041"));
            invoice.appendChild(city);

            Element total = doc.createElement("total");
            total.appendChild(doc.createTextNode("Rs. " + invoiceData.getTotal()));
            invoice.appendChild(total);

            // supercars element
            Element lineitems = doc.createElement("lineitems");
            invoice.appendChild(lineitems);

            for(InvoiceLineItem invoiceLineItem: invoiceData.getLineItems()){
                Element lineitem = doc.createElement("lineitem");
                lineitems.appendChild(lineitem);
                Element sno = doc.createElement("sno");
                sno.appendChild(doc.createTextNode("" + invoiceLineItem.getSno()));
                lineitem.appendChild(sno);

                Element productName = doc.createElement("productName");
                productName.appendChild(doc.createTextNode("" + invoiceLineItem.getName()));
                lineitem.appendChild(productName);

                Element barcode = doc.createElement("barcode");
                barcode.appendChild(doc.createTextNode("" + invoiceLineItem.getBarcode()));
                lineitem.appendChild(barcode);

                Element quantity = doc.createElement("quantity");
                quantity.appendChild(doc.createTextNode("" + invoiceLineItem.getQuantity()));
                lineitem.appendChild(quantity);

                Element unitPrice = doc.createElement("unitPrice");
                unitPrice.appendChild(doc.createTextNode("Rs. " + invoiceLineItem.getSellingPrice()));
                lineitem.appendChild(unitPrice);

                Element totalAmount = doc.createElement("total");
                totalAmount.appendChild(doc.createTextNode("Rs. " + invoiceLineItem.getTotal()));
                lineitem.appendChild(totalAmount);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/resources/invoice.xml"));
            transformer.transform(source, result);

            // Output to console for testing
//            StreamResult consoleResult = new StreamResult(System.out);
//            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
