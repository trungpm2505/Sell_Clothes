function toggleDiscountInput() {
				var discountType = document.getElementById("discount-type");
				var discountInput = document.querySelector(".discount-exactly");

				if (discountType.value === "1") {
					discountInput.style.display = "block";
				} else {
					discountInput.style.display = "none";
				}
			}

			function numberWithCommas(x) {
			    // XÃ³a táº¥t cáº£ cÃ¡c kÃ½ tá»± khÃ´ng pháº£i sá» trong chuá»i x
			    x = x.replace(/\D/g, '');

			    // ThÃªm dáº¥u cháº¥m phÃ¢n tÃ¡ch hÃ ng nghÃ¬n vÃ o sá» x
			    x = x.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.');

			    return x; 
			}

			$(document).on('input', '#discountValue', function(){
			    var value = numberWithCommas($(this).val());
			    $(this).val(value);
			});
			
			$(document).on('input', '#maxValue', function(){
			    var value = numberWithCommas($(this).val());
			    $(this).val(value);
			});
			// Gá»i hÃ m khi trang ÄÆ°á»£c táº£i Äá» kiá»m tra giÃ¡ trá» máº·c Äá»nh
			toggleDiscountInput();
			
			var inputList = [ 'couponCode', 'name', 'expiredDate', 'discountValue','maxValue' ];

			 inputList.forEach(function(inputName) {
				var input = document.getElementById(inputName);
				var error = document.getElementById(inputName + '-error');

				if (input) { // Kiá»m tra xem pháº§n tá»­ input cÃ³ tá»n táº¡i khÃ´ng
					input.addEventListener("focus", function() {
						error.textContent = '';
					});
				} else {
					console.error("Element with id '" + inputName
							+ "' not found in the page.");
				}
			}); 
			 clearFrom();
			 function clearFrom() {
					 url1 = "/promotion/add";
					 type1 = "POST";
					 $('#save-promotion-value').text('Save Changes');
					 
				    var inputId = ['couponCode', 'name', 'expiredDate', 'discountValue','maxValue'];
				    inputId.forEach(function (input) {
				        document.getElementById(input).value = '';
				    });


				    var inputList = ['couponCode', 'name', 'expiredDate', 'discountValue','maxValue'];
				    inputList.forEach(function (error) {
				        var error = document.getElementById(error + '-error');
				        if (error !== null) {
				        	error.textContent = '';
				        }
				    });
				    
				    document.getElementById('ispublic').checked = true;
				    document.getElementById('active').checked = true;

				}
			 
			var csrfToken;
			function savePromotion(){
				
				var code = document.getElementById('couponCode').value.trim();
				var name = document.getElementById("name").value;
				var discountType = parseInt(document.getElementById("discount-type").value);
				var discountValueInput = document.getElementById("discountValue").value.replace(".", "");
				if (discountValueInput !== "") {
				    if (!isNaN(discountValueInput)) {
				        var discountValue = parseFloat(discountValueInput);
				    } else {
				        var discountValue = 0;
				        console.error("Giá trị không hợp lệ cho discountValue");
				    }
				} else {
				    var discountValue = "";
				}


				var maxValueInput = document.getElementById("maxValue").value.replace(".", "");
				if (maxValueInput !== "") {
				    if (!isNaN(maxValueInput)) {
				        var maxValue = parseFloat(maxValueInput);
				    } else {
				        var maxValue = "";
				        console.error("Giá trị không hợp lệ cho discountValue");
				    }
				}else{
					var maxValue = "";
				}
				
				var expiredDate = document.getElementById("expiredDate").value;
				var isPublic = document.getElementById('ispublic').checked;
				var active = document.getElementById('active').checked;
				
				
				 // Táº¡o Äá»i tÆ°á»£ng FormData Äá» chá»©a dá»¯ liá»u
			    var formData = new FormData();
			 	// ThÃªm dá»¯ liá»u vÃ o Äá»i tÆ°á»£ng FormData
			   if (url1 == "/promotion/update") {
			        formData.append('id', parseInt(document.getElementById('id').value)); 
			    }
			    formData.append('couponCode', code);
			    formData.append('name', name);
			    formData.append('discountType', discountType);
			    formData.append('discountValue', discountValue);
			    formData.append('maxValue', maxValue);
			    formData.append('expiredDate', expiredDate);
			    formData.append('public', isPublic);
			    formData.append('active', active);
			    
			     csrfToken = Cookies.get('XSRF-TOKEN');
			     $.ajax({
			    	 	url: url1,
			    	    type: type1,
			    	    contentType: false,
				        processData: false,
				        data: formData,
				        dataType: 'json',
				        headers: {
						    'X-XSRF-TOKEN': csrfToken
						  },
				        cache: false,
				        success: function(data) {
				        	toastr.success(data.message); // Hiá»n thá» thÃ´ng bÃ¡o thÃ nh cÃ´ng khi request thÃ nh cÃ´ng.
				            clearFrom(); 
				            
				            loadData(0, ""); 
				        },
				        error: function(jqXHR, textStatus, errorMessage) {
				            // Xá»­ lÃ½ lá»i khi request khÃ´ng thÃ nh cÃ´ng.
				            if (jqXHR.status === 400) {
				                var errors = jqXHR.responseJSON;
				                if (errors.hasOwnProperty("bindingErrors")) {
				                    var bindingErrors = errors["bindingErrors"];
				                    for (var i = 0; i < bindingErrors.length; i++) {
				                        var error = bindingErrors[i];
				                       
				                        $("#" + error.field + "-error").text(error.defaultMessage);
				                    }
				                }
				                 if (errors.hasOwnProperty("couponDuplicate")) {
				                    var couponError = errors["couponDuplicate"];
				                    $("#couponCode-error").text(couponError); 
				                }
				                if (errors.hasOwnProperty("expiredDate")) {
				                    var expiredDateError = errors["expiredDate"];
				                    $("#expiredDate-error").text(expiredDateError); 
				                } 
				                if (errors.hasOwnProperty("discountValue")) {
				                    var discountValueError = errors["discountValue"];
				                    $("#discountValue-error").text(discountValueError); 
				                } 
				                if (errors.hasOwnProperty("maxValue")) {
				                    var maxValueError = errors["maxValue"];
				                    $("#maxValue-error").text(maxValueError); 
				                }
				                
				            } else {
				            	console.log("er:",errorMessage);
				            	console.log(jqXHR.status);
				                alert("Sorry! The system has errors!"); 
				            }
				        }
				    });
			}
			
			function editPromotion(promotionId) {
				clearFrom();
			    // Chá»n hÃ ng báº£ng vá»i variantId cá»¥ thá»
			    var promotionRow = $('tr').find('.id:contains(' + promotionId + ')').closest('tr');

			    var couponcode = promotionRow.find('.couponCode').text();
			    var name = promotionRow.find('.name').text();
			    var discountValue = promotionRow.find('.discountValue').text();
			    
			    // Äá»c giÃ¡ trá» boolean cho tráº¡ng thÃ¡i vÃ  cÃ´ng khai tá»« Ã´ báº£ng
			    var ispublic = promotionRow.find('.public').text() === 'true';
			    var active = promotionRow.find('.active').text() === 'true';

			    var discountType = parseInt(promotionRow.find('.discountType').text());
			    var maxValue = promotionRow.find('.maxValue').text();
			    var expiredDate = promotionRow.find('.expiredDate').text();
			    
			    console.log(active);
			    console.log(ispublic);

			    $('#save-promotion-value').text('Update');

			    url1 = "/promotion/update";
			    type1 = "PUT";
			    
			    document.getElementById('id').value = promotionId;
			    document.getElementById('couponCode').value = couponcode;
			    document.getElementById('name').value = name;
			    document.getElementById('discountValue').value = discountValue;
			    document.getElementById('discount-type').value = discountType;
			    
			    // Äáº·t giÃ¡ trá» cho checkbox "CÃ´ng khai" vÃ  "KÃ­ch hoáº¡t"
			    document.getElementById('ispublic').checked = ispublic;
			    document.getElementById('active').checked = active;
				if(discountType === 1){
					document.getElementById('maxValue').value = maxValue;
				}
			    
			    document.getElementById('expiredDate').value = expiredDate;
			    toggleDiscountInput();
			}

			
			loadData(0,"");
			function loadData(page, keyword) {
				csrfToken = Cookies.get('XSRF-TOKEN');
				var table = "#promotion-table tbody";
			    $.ajax({
			      url: '/promotion/getProductPage',
			      type: 'GET',
			      dataType: 'json',
			      data: {page : page, keyword : keyword},
			      headers: {
					    'X-XSRF-TOKEN': csrfToken
					  },
			      success: function(data) {
			        //clear table
			        $(table).empty();
			
			        //add record
			        $.each(data.promotionResponseDtos, function(index, promotion) {
			        	var row = $('<tr>');
			            row.append($('<td>').text(data.currentPage * data.size + index+1));
			            
					  row.append($('<td>').text(promotion.id).addClass('id').css({
					    'display': 'none'
					  }));
			           row.append($('<td>').text(promotion.couponCode).addClass('couponCode'));
			           row.append($('<td>').text(promotion.name).addClass('name'));
			           row.append($('<td>').text(promotion.discountValue).addClass('discountValue'));
			            
			           var publicText = promotion.public ? 'Public' : 'Hide';
			           var publicColorClass = promotion.public ? 'badge badge-primary' : 'badge badge-danger';

			           var publicCell = $('<td>');
			           var publicSpan = $('<span>').text(publicText).addClass(publicColorClass);

			           publicCell.append(publicSpan);
			           row.append(publicCell);

			           var activeText = promotion.active ? 'Activated' : 'Disable';
			           var colorClass = promotion.active ? 'badge badge-success' : 'badge badge-danger';

			           var activeCell = $('<td>');
			           var activeSpan = $('<span>').text(activeText).addClass(colorClass);

			           activeCell.append(activeSpan);
			           row.append(activeCell);
			           row.append($('<td>').text(promotion.public).addClass('public d-none'));
			           row.append($('<td>').text(promotion.active).addClass('active d-none'));
			           row.append($('<td>').text(promotion.discountType).addClass('discountType d-none'));
			           row.append($('<td>').text(promotion.maxValue).addClass('maxValue d-none'));
			            row.append($('<td>').text(promotion.createAt).addClass('createAt'));
			            row.append($('<td>').text(promotion.updateAt).addClass('updateAt'));
			            row.append($('<td>').text(promotion.expiredDate).addClass('expiredDate'));
			            
			            row.append($('<td>').attr({
				        	   'onclick': 'editPromotion(' + promotion.id + ')'
				           }).html('<i class="fas fa-pencil-alt"></i> Edit'));

			            row.append($('<td>').attr({
			            	'data-id' : promotion.id
			            }).addClass('text-danger delete-promotion').html('<i class="fas fa-trash"></i> Delete'));
			            
			            $(table).append(row);
			        });
			         pagination = '';
			         currentPage = data.currentPage;
			         totalPages = data.totalPages;
			         size = data.size;
			        //create pagination 
			        createPagination("#paging",pagination,currentPage,totalPages,keyword);
			      },
			      error: function(){}})
			  } 
			 function createPagination(navId, pagination, currentPage, totalPages, keyWord ){
		    	  $(navId).empty();//xoa toan bo noi dung trong html cÃ³ id navId(paging)
		          
		          pagination += '<ul class="pagination">';//tao danh sach 
		          //ThÃªm má»t nÃºt Äiá»u hÆ°á»ng cho trang trÆ°á»c vÃ o biáº¿n pagination náº¿u currentPage >0
		          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

		              var startPage = currentPage > 2 ? currentPage - 2 : 0;
		              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;
						
		              for (var i = startPage; i <= endPage; i++) {
		                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
		              }
		              
		             //Náº¿u cÃ³ quÃ¡ nhiá»u trang (totalPages > 5) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm má»t nÃºt cháº¥m ...
		              if ((totalPages > 5) &&  (currentPage < totalPages - 3)) {
		                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
		              }
		             //Náº¿u khÃ´ng pháº£i lÃ  trang cuá»i cÃ¹ng (totalPages-1 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) khÃ´ng pháº£i lÃ  trang thá»© hai tá»« cuá»i (totalPages-2 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm nÃºt Äiá»u hÆ°á»ng Äáº¿n trang cuá»i cÃ¹ng.
		              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
		                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
		              }
					
		          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
		          pagination += '</ul>';
		           
		           $(navId).append(pagination);
		      }
			 
			 $(document).on('click', '.delete-promotion', function() {
		           let promotionId = $(this).data("id");
		           csrfToken = Cookies.get('XSRF-TOKEN');
		           Swal.fire({
		        	   title: 'Are you sure you want to delete?',
		        	   text: 'This action cannot be undone!',
		        	   icon: 'warning',
		        	   showCancelButton: true,
		        	   confirmButtonText: 'Yes',
		        	   cancelButtonText: 'No'
		        	 }).then((result) => {
		        	   if (result.isConfirmed) {
		        		   $.ajax({
						        url: '/promotion/delete?promotionId=' + promotionId,
						        type: 'DELETE',
						        headers: {
								    'X-XSRF-TOKEN': csrfToken
								  },
						        success: function(data) {
					        	     Swal.fire(data, '', 'success');
						        	loadData(0,"");
						        },
						        error: function(jqXHR, textStatus, errorThrown){
						            if (jqXHR.status === 400) {
						                var error = jqXHR.responseJSON || jqXHR.responseText;
						                toastr.warning(error);
						            } else {
						                // Xá»­ lÃ½ lá»i khÃ¡c
						                console.log(textStatus);
						                console.log(errorThrown);
						            }
						        }
						    });
		        	   } else if (result.dismiss === Swal.DismissReason.cancel) {
		        	     Swal.fire('Canceled!', '', 'error');
		        	   }
		        	 });
		       	})
		       	
		     var searchButton = document.getElementById("searchButton");
			 var  keyInput = document.getElementById("key");
			 searchButton.addEventListener("click", function() {
				 var key="";
					
				 if(keyInput.value !== ""){
					
					 key = keyInput.value;
					 
				 }
				 loadData(0,key);
			});