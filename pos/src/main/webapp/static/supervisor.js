
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}

let roleDataTable = $('#role-table').DataTable({
        scrollY: '45vh',
        scrollCollapse: false,
        ordering: false,
        paging: false,
        searching: false,
        info: false
    });

//BUTTON ACTIONS
function addRole(){
	var $form = $("#role-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        toastr.success("Role added successfully!");
	        $("#inputEmail").val("");
	        $("#inputPassword").val("");
	        $("#inputRole").val("");
	        createRole();
	   		getUserList();    
	   },
	   error: function(response){
	        handleAjaxError(response);
	   }
	});

	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: function(response){
	        handleAjaxError(response);
	   }
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: function(response){
                handleAjaxError(response);
           }
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
    roleDataTable.destroy();
	var $tbody = $('#role-table').find('tbody');
	$tbody.empty();
	var count =1;
	var curlogin = $('#curlogin').text();
	for(var i in data){
		var e = data[i];
		if(e.email==curlogin){
		    var buttonHtml ='<button class="btn btn-outline-danger" onclick="deleteUser(' + e.id + ')" disabled>Remove</button>'
		}
		else{
		    var buttonHtml ='<button class="btn btn-outline-danger" onclick="deleteUser(' + e.id + ')">Remove</button>'
		}
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count = count +1;
	}
	roleDataTable = $('#role-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            ordering: false,
            paging: false,
            searching: false,
            info: false
        });
}
function createRole(){
    $('#add-role-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#create-role').click(createRole);
	$('#add-role').click(addRole);
	setActive();
}

$(document).ready(init);
$(document).ready(getUserList);

