const orderSoFar = [];
let flag = 1;
const barcodeSet = new Set();
let orderDataTable = $('#order-table').DataTable({
        scrollY: '45vh',
        scrollCollapse: false,
        paging: false,
        searching: false,
        info: false,
        language: {
                    emptyTable: "",
                    }
    });
let viewOrderItemDataTable = $('#view-order-item-table').DataTable({
        //scrollY: '20vh',
        scrollCollapse: false,
        ordering: false,
        paging: false,
        searching: false,
        info: false
    });
let orderItemDataTable = $('#order-item-table').DataTable({
        scrollY: '25vh',
        scrollCollapse: false,
        ordering: false,
        paging: false,
        searching: false,
        info: false,
        language: {
        			emptyTable: "No items in the cart",
        		}
    });

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function addOrder(event){
	json = JSON.stringify(orderSoFar);
	console.log(json);
	var url = getOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#inputBarcode').val("");
	        $('#inputQuantity').val("");
	        $('#inputSellingPrice').val("");
	        $('#order-item-table').find('tbody').empty();
	        $('#add-order-modal').modal('toggle');
	   		getOrderList();
	   		orderSoFar.length = 0;
	   		barcodeSet.clear();
            toastr["success"]("Order Successfully placed!");

	   },
	   error: function(response){
	       handleAjaxError(response);
	   }

	});
}

function addOrderItem(){
    var $form = $("#order-form");
    var json = toJson($form);
    console.log(json);
    var entry = JSON.parse(json);
    var msg = isValid(entry);
    if(msg!=""){
        toastr.error(msg, "Error: ", {
                "closeButton": true,
                "timeOut": "0",
                "extendedTimeOut": "0"
            });
        return;
    }
    if(barcodeSet.has(entry.barcode)){
        toastr.error("This product has already been already added!", "Error: ", {
        	    "closeButton": true,
        	    "timeOut": "0",
        	    "extendedTimeOut": "0"
        	});

    }
    else{
        orderSoFar.push(entry);
        barcodeSet.add(entry.barcode);
    }
    console.log(orderSoFar[0]);
    $('#inputBarcode').val("");
    $('#inputQuantity').val("");
    $('#inputSellingPrice').val("");
    //table pe show order item
    displayOrderItemList();
}

function isValid(json){
    var msg = "";
        if(json.barcode==""){
            msg = "Barcode cannot be empty";
        }
        else if(json.quantity==""){
            msg = "Quantity cannot be empty";
        }
        else if(json.quantity<0){
                msg = "Quantity cannot be negative";
        }
        else if(json.quantity>10000000){
                msg = "Quantity cannot exceed 10000000";
        }
        else if(json.sellingPrice==""){
                msg = "Selling price cannot be empty";
        }
        else if(json.sellingPrice<0){
                msg = "Selling Price cannot be negative";
        }
        return msg;
}

function createOrderModal(){
    if(flag){
        orderItemDataTable.destroy();
    }
    $('#add-order-modal').modal('toggle');
    flag=0;

}

function cancelOrder(){
    if(flag){
        orderItemDataTable.destroy();
    }
    $('#order-item-table').find('tbody').empty();
    $("#add-order-modal").modal('toggle');
    orderSoFar.length = 0;
    barcodeSet.clear();
    flag=0;
}

function getOrderItemList(id){
	var url = getOrderUrl() + "/" + id;
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		viewOrderItemList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function viewOrderItemList(data){
    viewOrderItemDataTable.destroy();
    var $tbody = $('#view-order-item-table').find('tbody');
    $tbody.empty();
    $("#view-order-modal").modal('toggle');
    let count = 1;
    for(var i in data){
        var item = data[i];
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + item.barcode + '</td>'
        + '<td>'  + item.quantity + '</td>'
        + '<td>'  + item.sellingPrice + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count +1;
    }

    viewOrderItemDataTable = $('#view-order-item-table').DataTable({
            //scrollY: '40vh',
            scrollCollapse: false,
            ordering: false,
            paging: false,
            searching: false,
            info: false
        });

}

//addition ke time wala display
function displayOrderItemList(){
    if(flag){
        orderItemDataTable.destroy();
    }

    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
    let count = 1;
    for(var i in orderSoFar){
        var item = orderSoFar[i];
        var buttonHtml = '<button class="btn btn-danger" onclick="removeOrderItem(' + i +')"><i class="fa-solid fa-trash" style="color:white"></i> Remove</button>';
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + item.barcode + '</td>'
        + '<td>'  + item.quantity + '</td>'
        + '<td>'  + item.sellingPrice + '</td>'
        + '<td>'  + buttonHtml + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count +1;
    }
    orderItemDataTable = $('#order-item-table').DataTable({
            scrollY: '25vh',
            scrollCollapse: false,
            ordering: false,
            paging: false,
            searching: false,
            info: false,
            language: {
                        emptyTable: "No items in the cart",
                    }
        });
        flag = 1;
}

function removeOrderItem(row){
    barcodeSet.delete(orderSoFar[row].barcode);
    orderSoFar.splice(row,1);
    displayOrderItemList();

}
function editOrderItem(row){
    var item = orderSoFar[row];
    $('#inputBarcode').val(item.barcode);
    $('#inputQuantity').val(item.quantity);
    $('#inputSellingPrice').val(item.sellingPrice);
    removeOrderItem(row);

}

function getOrderList(){
    var url = getOrderUrl();
        $.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                displayOrderList(data);

           },
           error: function(response){
                handleAjaxError(response);
           }
        });

}

function generateInvoice(id){
    var url = getOrderUrl() + "/" + id+"/generate";
    $.ajax({
       url: url,
       type: 'GET',
       success: function(response) {
            toastr.success("Invoice downloaded successfully!");
            const link = document.createElement("a");
            link.href = "data:application/pdf;base64," + response;
            link.download = "invoice_" + id + ".pdf";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            getOrderList();
       },
       error: function(response){
            handleAjaxError(response);
       }
    });
}

function displayOrderList(data){
    orderDataTable.destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	let count = 1;
	for(var i in data){
		var singleEntry = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="getOrderItemList(' + singleEntry.id + ')" style = "margin-right:8px"><i class="fa-solid fa-eye" style = "margin-right:8px;"></i>View</button>';
		if(singleEntry.isInvoiceGenerated==1){
		    buttonHtml += '<button class="btn btn-success" onclick="generateInvoice(' + singleEntry.id + ')" disabled style = "margin-right:8px"><i class="fa-solid fa-file" style = "margin-right:8px"></i>Generate Invoice</button>';
        	buttonHtml += '<button class="btn btn-warning" onclick="generateInvoice(' + singleEntry.id + ')"><i class="fa-solid fa-download" style = "margin-right:8px"></i>Download Invoice</button>';
		}
		else{
		    buttonHtml += '<button class="btn btn-success" onclick="generateInvoice(' + singleEntry.id + ')" style = "margin-right:8px"><i class="fa-solid fa-file" style = "margin-right:8px"></i>Generate Invoice</button>';
        	buttonHtml += '<button class="btn btn-warning" onclick="generateInvoice(' + singleEntry.id + ')" disabled><i class="fa-solid fa-download" style = "margin-right:8px"></i>Download Invoice</button>';
		}
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>'  + singleEntry.time + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		count = count +1;
        $tbody.append(row);
	}
	orderDataTable = $('#order-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            paging: false,
            searching: false,
            info: false,
            language: {
                        emptyTable: " ",
                    }
        });

}

//INITIALIZATION CODE
function init(){
    $('#create-order').click(createOrderModal);
    $('#add-order-item').click(addOrderItem);
	$('#add-order').click(addOrder);
	$('#cancel-order').click(cancelOrder);
	getOrderList();
    setActive();
}

$(document).ready(init);
