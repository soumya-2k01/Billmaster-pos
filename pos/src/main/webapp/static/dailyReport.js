function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/dailyReport";
}

let dailyReportDataTable = $('#daily-report-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            paging: false,
            searching: false,
            info: false
        });

function getReport(){
    var $form = $('#daily-report-form');
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
    	        formatDate();
    	   }
    });
}

function displayReport(data){
    var $tbody = $('#daily-report-table').find('tbody');
    dailyReportDataTable.destroy();
    $tbody.empty();
    let count = 1;
    for(var i in data){
        var entry = data[i];
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + entry.date + '</td>'
        + '<td>'  + entry.invoicedOrderCount + '</td>'
        + '<td>'  + entry.invoicedItemCount + '</td>'
        + '<td>'  + entry.totalRevenue.toFixed(2) + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count+1;
    }
    dailyReportDataTable = $('#daily-report-table').DataTable({
                scrollY: '45vh',
                scrollCollapse: false,
                paging: false,
                searching: false,
                info: false
            });
}

function formatDate(){
    var date = new Date();
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
    $('#generate-daily-report').click(getReport);
    $('.active').removeClass('active');
    $('#navbarDropdown').addClass('active');
    formatDate();
    setActive();
}

$(document).ready(init);