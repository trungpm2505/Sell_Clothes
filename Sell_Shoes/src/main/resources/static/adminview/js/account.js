$(document).ready(function() {
		    // Gọi hàm để lấy danh sách account khi trang web được tải
		    getlistAccounts(0,'');
			
		});
		
       /*  function getAllAccounts() {
            getlistAccounts(0,''); // Lấy tất cả tài khoản
        } */
        function getlistAccounts(page, keyword){
        	 $.ajax({
				  url: '/account/getAccountPage',
				  type: 'GET',
				  data:{
		            page:page,
		            keyword: keyword
		               },
				  success: function(response) {				    
				    $("#accountTable tbody").empty();
	            	   if (response && response.accountResponeseDto){
	                       var accounts = response.accountResponeseDto;  
	                    
	                       for (var i = 0; i < accounts.length; i++) {
	                           var account = accounts[i];	                           		                      
	                           status = account.active ? 'Verified' : 'Not authenticated';
	                           var row = '<tr>' +
	                           '<td>' + (i+1) + '</td>'+
	                           '<td>' + account.fullName + '</td>' +
	                           '<td>' + account.address + '</td>' +
	                           '<td>' + account.email + '</td>' +
	                           '<td>' + account.phone + '</td>' +
	                           '<td>' + account.createAt + '</td>' +
	                           '<td>' + (account.updateAt ? account.updateAt : '-') + '</td>' +
	                           '<td>' + status + '</td>' +
	                           '<td>' + account.roleName + '</td>' +	                         
                              '<td>' +
'<span onclick="updateRole(' + account.id + ', \'' + account.roleName + '\')"><i class="fas fa-pencil-alt"></i> Edit Role</span>' +
'</td>'                          
                                '</tr>';
	                          
	                           $("#accountTable tbody").append(row);
	                       }	                 
	                	   }
	            	   pagination = '';
	      	         currentPage = response.currentPage;
	      	         console.log(response.currentPage)
	      	         totalPages = response.totalPages;
	      	         size = response.size;
	      	        createPagination("#paging",pagination,currentPage,totalPages,keyword);
				  },
				  error: function(error) {
				    console.error('Error:', error);
				  }
			}); 
         
        }
        
        var csrfToken = null;
  		document.getElementById("logout-form").addEventListener("submit", function(event) {
  		  // Gửi yêu cầu AJAX
  		  var xhr = new XMLHttpRequest();
  		  xhr.open("POST", "/logout", true);
  		  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  		  if (Cookies.get('XSRF-TOKEN')) {
  			  csrfToken = Cookies.get('XSRF-TOKEN');
  			  xhr.setRequestHeader("X-XSRF-TOKEN", csrfToken);
  			}
  		  
  			
  		  xhr.onload = function() {
  		     if (xhr.status === 200) {
  				 
  		  };
  		
  		  xhr.send(new FormData(event.target));
  		  }
  		});
  		
  		
  		
   function createPagination(navId, pagination, currentPage, totalPages, keyWord ){
		    	  $(navId).empty();
		          pagination += '<ul class="pagination">';//tao danh sach 
		          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'getlistAccounts(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

		              var startPage = currentPage > 2 ? currentPage - 2 : 0;
		              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;
						
		              for (var i = startPage; i <= endPage; i++) {
		                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="getlistAccounts(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
		              }
		              
		              if ((totalPages > 5) &&  (currentPage < totalPages - 3)) {
		                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
		              }
		              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
		                  pagination += '<li><a class="page-link" onclick="getlistAccounts(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
		              }
					
		          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'getlistAccounts(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
		          pagination += '</ul>';
		           
		           $(navId).append(pagination);
		      }
   var searchButton = document.getElementById("searchButton");
	var keyInput = document.getElementById("keyword");
	searchButton.addEventListener("click", function() {
		var keyword = "";

		if (keyInput.value !== "") {
			keyword = keyInput.value;
		}
		getlistAccounts(0, keyword);
		console.log(keyword);
	});
	  

	//var accountId = 0;
	 function updateRole(uid,currentRole) {
		 $('#accountId').val(uid); 
		 $('#editRoleModal').modal('show');

		 accountId = uid;
		 $('#newRole').val(currentRole);
	 }

	 function update(){
		 console.log(accountId)
		  var selectedRole = $('#newRole').val(); // Lấy giá trị được chọn từ dropdown
		  var  data= {
	    	  id: accountId,
	    	  roleName: selectedRole
	      };	
		   $.ajax({
		      url: '/account/updateRole',
		      type: 'POST',
		        contentType: 'application/json', // Đảm bảo gửi dữ liệu dưới dạng JSON
		     data : JSON.stringify(data), 
		      success: function(response) {
		        // Xử lý khi server trả về thành công
		         toastr.success(response);
		        console.log('Cập nhật vai trò thành công');
		        //đóng modal sau khi thực hiện thành công
		         $('#editRoleModal').modal('hide');
		        getlistAccounts(0, '');
		      },
		      error: function(error) {
				    console.error('Error:', error);
				  }
            
           });    
	     }
	