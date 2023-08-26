function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

var productDataTable = $('#product-table').DataTable({
                               scrollY: '45vh',
                               scrollCollapse: true,
                               paging: false,
                               searching: false,
                               info: false
                           });

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	//console.log(json);
	var url = getProductUrl();
    var msg = isValid(json);
    if(msg!=""){
        console.log("frontend check!");
        toastr.error(msg, "Error: ", {
            "closeButton": true,
            "timeOut": "0",
            "extendedTimeOut": "0"
        });
        return;
    }

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        getProductList();
	        $("#inputBrand").val("");
	        $("#inputCategory").val("");
	        $("#inputBarcode").val("");
	        $("#inputProduct").val("");
	        $("#inputMrp").val("");
	        $('#add-product-modal').modal('toggle');
	        toastr["success"]("Success!");
	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});
}

function isValid(json){
    var json = JSON.parse(json);
    var msg = "";
    if(json.brand==""){
        msg = "Brand cannot be empty";
    }
    else if(json.category==""){
        msg = "Category cannot be empty";
    }
    else if(json.barcode==""){
        msg = "Barcode cannot be empty";
    }
    else if(json.name==""){
        msg = "Product name cannot be empty";
    }
    else if(json.mrp==""){
        msg = "Mrp cannot be empty";
    }
    else if(json.mrp<=0){
        msg = "Mrp cannot be less than or equal to 0";
    }
    else if(json.mrp>10000000){
        msg = "Mrp cannot exceed 10000000";
    }
    return msg;
}

function updateProduct(event){
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);
    var msg = isValid(json);
        if(msg!=""){
            toastr.error(msg, "Error: ", {
                "closeButton": true,
                "timeOut": "0",
                "extendedTimeOut": "0"
            });
            return;
        }

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-product-modal').modal('toggle');
	        getProductList();
	   		toastr.success("Updated Successfully!");

	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: function(response){
          handleAjaxError(response);
       }
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: function(response){
          handleAjaxError(response);
      }
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData;

function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	var row = fileData[0];
    var title = Object.keys(row);
    if(title.length!=5 || title[0]!='barcode' || title[1]!='brand' || title[2]!='category' || title[3]!='mrp' || title[4]!='name'){
      toastr.error("Incorrect tsv format", "Error: ", {
           "closeButton": true,
           "timeOut": "0",
           "extendedTimeOut": "0"
       });
      return;
    }
    var json = JSON.stringify(fileData);
    var url = getProductUrl()+'s';
        //Make ajax call
        $.ajax({
           url: url,
           type: 'POST',
           data: json,
           headers: {
            'Content-Type': 'application/json'
           },
           success: function() {
                toastr.success("Uploaded Successfully!");
                $('#upload-product-modal').modal('toggle');
                getProductList();
           },
           error: function(response){
                toastr.error("Errors in the file uploaded. Download the error file!", "Error: ", {
                        "closeButton": true,
                        "timeOut": "0",
                        "extendedTimeOut": "0"
                    });
                errorData=response.responseJSON.message;
                $('#download-errors').removeAttr('hidden');
           }
        });
}

function downloadErrors(){
    var element = document.createElement('a');
    element.setAttribute('href','data:text/plain;charset=utf-8,' + encodeURIComponent(errorData));
    element.setAttribute('download',"product_errors.txt");
    element.click();
    $('#download-errors').attr('hidden',true);
}

//UI DISPLAY METHODS

function displayProductList(data){
    productDataTable.destroy();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();

    let count = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-sm btn-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
		var row = '<tr>'
        + '<td>' + count + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.barcode + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.mrp.toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count = count +1;
	}
	productDataTable = $('#product-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: true,
            paging: false,
            searching: false,
            info: false
        });
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: function(response){
          handleAjaxError(response);
      }
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	$('#download-errors').attr('hidden',true);
    $('#process-data').attr('disabled',true);
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	if(fileName.slice(-4)!=".tsv"){
            toastr.error("File must be in .tsv format!", "Error: ", {
            	    "closeButton": true,
            	    "timeOut": "0",
            	    "extendedTimeOut": "0"
            	});
            	return;
    	}
    	$('#productFileName').html(fileName); // putting file name on label
    	$("#process-data").removeAttr('disabled');
}

function displayUploadData(){
     if(userRole=="operator"){
        toastr.error("You are not authorized to upload!", "Error: ", {
                "closeButton": true,
                "timeOut": "0",
                "extendedTimeOut": "0"
            });
            return;
       }
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){

	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=id]").val(data.id);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$('#edit-product-modal').modal('toggle');
}

function createProduct(){
    $('#add-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#create-product').click(createProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
    $('#process-data').attr('disabled',true);
    $('#download-errors').attr('hidden',true);
    setActive();
}

$(document).ready(init);
$(document).ready(getProductList);



