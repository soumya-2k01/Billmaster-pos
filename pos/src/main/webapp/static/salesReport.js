function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesReport";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

let salesReportDataTable = $('#sales-report-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            paging: false,
            searching: false,
            info: false
        });

const brandSet = new Set();
const categorySet = new Set();

// Filling the Dropdowns

function fillBrandDropdown(){
   var $drop = $('#inputBrand');
   $drop.empty();
   $drop.append('<option value = "" selected>All</option>');
   for(const brand of brandSet){
        var option = '<option value = "'+ brand +'">' + brand + '</option>';
        $drop.append(option);
   }
}

function fillCategoryDropdown(){
   var $drop = $('#inputCategory');
   $drop.empty();
   $drop.append('<option value = "" selected>All</option>');
   for(const category of categorySet){
        var option = '<option value = "'+ category +'">' + category + '</option>';
        $drop.append(option);
   }
}

function populateDropdown(data){
    for(var i in data){
         var item = data[i];
         brandSet.add(item.brand);
         categorySet.add(item.category);
     }
     fillBrandDropdown();
     fillCategoryDropdown();
}

function getAll(){
    var url = getBrandUrl();
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	        populateDropdown(data);
    	   },
    	   error: function(response){
                handleAjaxError(response);
           }
    	});
}

// Generating Report
function getReport(){
    var $form = $('#sales-report-form');
    var url = getReportUrl();
    var json = toJson($form);
    console.log(json);

    $.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	        displayReport(response);
    	   },
    	   error: function(response){
    	        handleAjaxError(response);
    	        displayReport();
    	        formatDate();
    	   }
    });
}

function displayReport(data){
    var $tbody = $('#sales-report-table').find('tbody');
    salesReportDataTable.destroy();
    $tbody.empty();
    let count = 1;
    for(var i in data){
        var entry = data[i];
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + entry.brand + '</td>'
        + '<td>'  + entry.category + '</td>'
        + '<td>'  + entry.quantity + '</td>'
        + '<td>'  + entry.revenue.toFixed(2) + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count+1;
    }
    salesReportDataTable = $('#sales-report-table').DataTable({
                scrollY: '45vh',
                scrollCollapse: false,
                paging: false,
                searching: false,
                info: false
            });
}

function formatDate(){
    var date = new Date();
    //var today = date.toISOString().split('T')[0];
    var dd = String(date.getDate()).padStart(2, '0');
    var mm = String(date.getMonth() + 1).padStart(2, '0');
    var yyyy = date.getFullYear();
    var endDate = yyyy + '-' + mm + '-' + dd;
    $('#inputEndDate').val(endDate);
    date.setMonth(date.getMonth()-1);
    dd = String(date.getDate()).padStart(2, '0');
    mm = String(date.getMonth() + 1).padStart(2, '0');
    yyyy = date.getFullYear();
    var startDate = yyyy + '-' + mm + '-' + dd;
    $('#inputStartDate').val(startDate);
    $('#inputEndDate').attr('max',endDate);
    $('#inputEndDate').attr('min',startDate);
    $('#inputStartDate').attr('max',endDate);
    $('#inputStartDate').attr('min',startDate);
}

function init(){
    $('#generate-sales-report').click(getReport);
    getAll();
    $('.active').removeClass('active');
    $('#navbarDropdown').addClass('active');
    formatDate();
    setActive();
}

$(document).ready(init);