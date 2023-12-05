$(document).ready(function() {
		    // Gọi hàm để lấy danh sách account khi trang web được tải
		    getlistAccounts(0, '');
			
		});
		
        function getAllAccounts() {
            getlistAccounts(0, ''); // Lấy tất cả tài khoản
        }
        function getlistAccounts(page, keyword){
        	 $.ajax({
				  url: '/account/getAccountPage',
				  type: 'GET',
				  data:{
		            page:page,
		            keyword: keyword
		               },
				  success: function(response) {
				    console.log(response);
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
	                           
	                           '</tr>';
	                          
	                           $("#accountTable tbody").append(row);
	                       }
	                	   }
	            	   pagination = '';
	      	         currentPage = response.currentPage;
	      	         totalPages = response.totalPages;
	      	         size = response.size;
	      	        //createPagination("#paging",pagination,currentPage,totalPages,keyword);
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