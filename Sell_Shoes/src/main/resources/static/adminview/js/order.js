document.addEventListener('DOMContentLoaded', function() {
				// Láº¥y pháº§n tá»­ select
				var select = document.getElementById('time');

				// Láº¥y pháº§n tá»­ wrap cá»§a Ã´ date
				var dateWrap = document.getElementById('date-wrap');

				// Láº¥y pháº§n tá»­ wrap cá»§a Ã´ start date vÃ  end date
				var dateRangeWrap = document.getElementById('dateRange-wrap');

				// áº¨n ban Äáº§u Ã´ date, start date vÃ  end date
				dateWrap.style.display = 'none';
				dateRangeWrap.style.display = 'none';

				// ThÃªm sá»± kiá»n khi thay Äá»i giÃ¡ trá» trong dropdown
				select.addEventListener('change', function() {
					// áº¨n táº¥t cáº£ Ã´ date, start date vÃ  end date
					dateWrap.style.display = 'none';
					dateRangeWrap.style.display = 'none';

					// Hiá»n thá» Ã´ tÆ°Æ¡ng á»©ng vá»i lá»±a chá»n trong dropdown
					if (select.value === 'date') {
						dateWrap.style.display = 'block';
					} else if (select.value === 'dateRange') {
						dateRangeWrap.style.display = 'flex';
					}
				});
			}); 
			 var csrfToken;
			loadData(0,0,false,"",null,null,null);
			function loadData(page, status, search, key, createAt, startDate, endDate) {
				 csrfToken = Cookies.get('XSRF-TOKEN');
				var table = "#order-table tbody";
				$.ajax({
					 url: '/order/getOrderPage',
				     type: 'GET',
				     dataType: 'json',
				     data: {page : page, status: status, search:search, key:key, createAt:createAt, startDate:startDate, endDate:endDate},
				     headers: {
						    'X-XSRF-TOKEN': csrfToken
						  },
				     success: function(data) {
						  if(data == null){
							  return;
						  }
				     //clear table
				     $(table).empty();
				     $.each(data.orderResponseDtos, function(index, order) {
				    	 var row = $('<tr>');
				         row.append($('<td>').text(data.currentPage * data.size + index+1));
				         row.append($('<td>').text(order.fullName));
				         row.append($('<td>').text(order.phone));
				         var total = order.totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
				         row.append($('<td>').text(total));
				         row.append($('<td>').text(order.createAt));
				         
				         if(order.status==1){
				        	 	row.append($('<td>').append($('<span>').text("New order").addClass('badge badge-primary')));
								row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)'  class='btn btn-outline-primary' type='button'>Details</button>"));
								row.append($('<td>').addClass('text-danger').html("<button data-id="+order.id+" onclick='showCancel("+order.id+")' data-update-status='2'  class='btn btn-sm btn-danger' type='button'>Refuse</button> <button onclick='updateStatus(event)' data-id="+order.id+" data-update-status='3' class='btn btn-sm btn-primary' type='button'>Agree</button>"));
				            	
				            	
						 }else if(order.status==2){
								row.append($('<td>').append($('<span>').text("Cancelled").addClass('badge badge-danger')));
								row.append($('<td>').html("<button data-id="+order.id+"  onclick='getOrderDetails(event)' class='btn btn-outline-primary' type='button'>Details</button>"));
								row.append($('<td>').text("Order has been cancelled."));
						 }else if(order.status==3){
								row.append($('<td>').append($('<span>').text("Order has been confirm.").addClass('badge badge-warning')));
								row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)' class='btn btn-outline-primary' type='button'>Details</button>"));
								row.append(
									    $('<td>').addClass('text-danger').html(
									        '<button onclick="updateStatus(event)" data-id=' + order.id + ' data-update-status="4" class="btn btn-sm btn-success" type="button">Complete</button>' +
									        '<button data-id=' + order.id + ' onclick="showCancel(' + order.id + ')" data-update-status="2" class="btn btn-sm btn-danger" type="button">Cancel</button>'
									    )
									);

				            	
						 }else if(order.status==4){
								row.append($('<td>').append($('<span>').text("Completed").addClass('badge badge-success')));
								row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)' class='btn btn-outline-primary' type='button'>Details</button> "));
								//row.append($('<td>').text("Order has been completed."));
								row.append($('<td>').html("<button onclick='updateStatus(event)' data-id=" + order.id + " data-update-status='3' class='btn btn-sm btn-primary' type='button'>Undo</button> "));
								
						}
							
				            
				        $(table).append(row);
				     });
				     pagination = '';
			         currentPage = data.currentPage;
			         totalPages = data.totalPages;
			         size = data.size;
			        //create pagination 
			       	createPagination("#paging",pagination,currentPage,totalPages,status,search,key,createAt,startDate,endDate );
			      },
			      error: function(){}})}
			
			 function createPagination(navId,pagination,currentPage,totalPages, status,search,key,dateValue,startDateValue,endDateValue){
		    	  $(navId).empty();
		          
		          pagination += '<ul class="pagination">';
		          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

		         
		              var startPage = currentPage > 2 ? currentPage - 2 : 0;
		              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

		              for (var i = startPage; i <= endPage; i++) {
		                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')">' + (i + 1) + '</a></li>';
		              }
		             //${page.totalPages > 5 && page.number < page.totalPages - 3}
		              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
		                
		                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
		              }
		             
		              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
		                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')" >' + totalPages + '</a></li>';
		              }

		          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
		          pagination += '</ul>';
		           
		           $(navId).append(pagination);
		      }
			 
			// Biến địa phương để lưu trữ giá trị orderId
			 function showCancel(orderId){
	  			$('#refuseOrder').modal('show');
	  			 $('#orderIdRefuseModal').val(orderId);
	  		}

			 //update status
			 var buttonText;
			 var undo = false;

			 function updateStatus(event) {
				 csrfToken = Cookies.get('XSRF-TOKEN');
			     // Lấy giá trị orderId từ biến địa phương hoặc data của modal
			     var orderId = $('#orderIdRefuseModal').val();
					console.log("OrderId:", orderId);
					
					if (orderId == null || orderId === "") {
					    orderId = event.target.getAttribute('data-id');
					} else {
					    // Bạn có thể giữ nguyên giá trị orderId nếu nó đã được đặt trước đó
					}

			     csrfToken = Cookies.get('XSRF-TOKEN');
			     var button = event.target;
			     buttonText = button.textContent;
			     if (buttonText == "Undo") {
			         undo = true;
			     }

			     $.ajax({
			         url: '/order/updateStatus',
			         type: 'PUT',
			         dataType: 'json',
			         data: {
			             orderId: parseInt(orderId),
			             status: parseInt($(button).data('update-status')),
			             undo: undo
			         },
			         headers: {
						    'X-XSRF-TOKEN': csrfToken
						},
			         success: function () {
			             $('#refuseOrder').modal('hide');
			             $('.modal-backdrop').remove();
			             loadData(0, 0, false, "", null, null, null);
			         },
			         error: function (jqXHR, textStatus, errorThrown) {
			             var errors = jqXHR.responseJSON;
			             alert(errors);
			         }
			     });
			 }

			 
			 	var timeSelect = document.getElementById("time");				
			  	var dateWrap = document.getElementById("date-wrap");
			  	var startDate = document.getElementById("startDate-wrap");
			  	var endDate = document.getElementById("endDate-wrap");

			  var searchButton = document.getElementById("searchButton");
			  var  keyInput = document.getElementById("key");
			  var selectStatus = document.getElementById("selectStatus");

			  function searchButtonClick() {
				  var startDateValue = "";
				  var endDateValue = "";
				  var dateValue = "";
				  var key = "";
				  var search = false;
				  var status = 0;

				  if (keyInput.value !== "") {
				    key = keyInput.value;
				    search = true;
				  }

				  if (timeSelect.value !== "choose" && timeSelect.value !== "") {
				    search = true;

				    if (timeSelect.value === "date" && document.getElementById("dateInput").value !== "" && document.getElementById("dateInput").value != null) {
				      dateValue = document.getElementById("dateInput").value;
				    } else if (timeSelect.value === "dateRange" && (document.getElementById("startDate").value !== "" || document.getElementById("endDate").value !== "")) {
				      startDateValue = document.getElementById("startDate").value;
				      endDateValue = document.getElementById("endDate").value;
				    } else if (timeSelect.value === "today") {
				      var date = new Date(); // NgÃ y hiá»n táº¡i
				      dateValue = date.toISOString().substring(0, 10);
				    } else if (timeSelect.value === "lastDate") {
				      var date = new Date();
				      var yesterday = new Date(date);
				      yesterday.setDate(date.getDate() - 1);
				      dateValue = yesterday.toISOString().substring(0, 10);
				    }
				  }

				  //status
				  if (selectStatus.value !== "choose" && selectStatus.value !== "") {
				    search = true;
				    switch (selectStatus.value) {
				      case "1":
				        status = 1;
				        break;
				      case "2":
				        status = 2;
				        break;
				      case "3":
				        status = 3;
				        break;
				      case "4":
				        status = 4;
				        break;
				    }
				  }

				  loadData(0, status, search, key, dateValue, startDateValue, endDateValue);
				}

				searchButton.addEventListener("click", searchButtonClick);
				
				function getOrderDetails(event) {
					var button = event.target;
					var orderId = $(button).data('id');
				  // Tạo URL mới với orderId
				  var url = '/orderDetails/get?orderId=' + orderId;
			  
				  // Chuyển hướng đến URL mới
				  window.location.href = url;
				};