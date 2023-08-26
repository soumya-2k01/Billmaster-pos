
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	toastr.error(response.message, "Error: ", {
	    "closeButton": true,
	    "timeOut": "0",
	    "extendedTimeOut": "0"
	});
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;
    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}


function setActive(){
    var navLinks = document.querySelectorAll('.nav-link');
    var currentURL = window.location.href;
    var list = ["http://localhost:9000/pos/ui/brandReport", "http://localhost:9000/pos/ui/inventoryReport", "http://localhost:9000/pos/ui/salesReport", "http://localhost:9000/pos/ui/dailyReport"];
    for (var i = 0; i < navLinks.length; i++) {
      var link = navLinks[i];
      if (link.href === currentURL) {
        link.classList.add('active');
        link.style.borderBottom = "3px solid #ffffff";
      }
    }
    if(list.includes(currentURL)){
       var link = document.querySelector('#navbarDropdown');
       link.style.borderBottom = "3px solid #ffffff";
    }
}




