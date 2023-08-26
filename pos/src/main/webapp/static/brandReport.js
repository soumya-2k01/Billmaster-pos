function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandReport";
}

let brandReportDataTable = $('#brand-report-table').DataTable({
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
    var brand = $("#inputBrand").val();
    var category = $("#inputCategory").val();
    var object = {brand,category};
    var json = JSON.stringify(object);
    var url = getReportUrl();
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
    	   }
    });
}

function displayReport(data){
    var $tbody = $('#brand-report-table').find('tbody');
    brandReportDataTable.destroy();
    $tbody.empty();
    let count = 1;
    for(var i in data){
        var entry = data[i];
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + entry.brand + '</td>'
        + '<td>'  + entry.category + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count+1;
    }
    brandReportDataTable = $('#brand-report-table').DataTable({
                scrollY: '45vh',
                scrollCollapse: false,
                paging: false,
                searching: false,
                info: false
            });
}

function init(){
    $('#generate-brand-report').click(getReport);
    getAll();
    setActive();
}

$(document).ready(init);