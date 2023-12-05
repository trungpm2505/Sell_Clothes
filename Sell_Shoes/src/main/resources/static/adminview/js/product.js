var inputList = [ 'title', 'category', 'brand', 'discription', 'file-input' ];

			inputList.forEach(function(inputName) {
				var input = document.getElementById(inputName);
				var error = document.getElementById(inputName + '-error');

				if (input) { // Kiểm tra xem phần tử input có tồn tại không
					input.addEventListener("focus", function() {
						error.textContent = '';
					});
				} else {
					console.error("Element with id '" + inputName
							+ "' not found in the page.");
				}
			});
			
			function clearImage() {
				document.getElementById('file-input').value = null;
				//xóa khung chưa ảnh
				selectedImagesContainer.innerHTML = '';
				//xóa trong mảng chứa file
				selectedFiles.splice(0, selectedFiles.length);
				selectedFilesEdit.splice(0, selectedFilesEdit.length);
				//xóa lỗi
				$("#file-input-error").text('');
			}
			
			loadCategory();
			function loadCategory() {
			    const categorySelect = document.getElementById('category');
			    $.ajax({
			        url: "/category/getAll",
			        type: "GET",
			        dataType: 'json',
			        success: function(categories) {
			        	if (categories.length != 0) {
			        		categorySelect.innerHTML = "<option value='' selected>Choose category</option>";
				    		  categories.forEach(function(category) {
				    			    const optionElement = document.createElement('option');
				    			    optionElement.value = category.id;
				    			    optionElement.textContent = category.categoryName;
				    			    categorySelect.appendChild(optionElement);
				    			});
				    		  
				          } else {
				              
				          }
			        },
			        error: function(jqXHR, textStatus, errorMessage) {
			            var error = jqXHR.responseJSON || jqXHR.responseText;
			        }
			    });
			}
			
			loadBrand();
			function loadBrand() {
			    const brandSelect = document.getElementById('brand');
			    $.ajax({
			        url: "/brand/getAll",
			        type: "GET",
			        dataType: 'json',
			        success: function(brands) {
			        	if (brands.length != 0) {
			        		brandSelect.innerHTML = "<option value='' selected>Choose brand</option>";
			        		brands.forEach(function(brand) {
				    			    const optionElement = document.createElement('option');
				    			    optionElement.value = brand.id;
				    			    optionElement.textContent = brand.brandName;
				    			    brandSelect.appendChild(optionElement);
				    			});
				    		  
				          } else {
				              
				          }
			        },
			        error: function(jqXHR, textStatus, errorMessage) {
			            var error = jqXHR.responseJSON || jqXHR.responseText;
			        }
			    });
			}
			

			// Lấy khung hiển thị ảnh đã chọn
			const selectedImagesContainer = document.getElementById('selected-images');

			// Lấy khung chọn file
			const fileInput = document.getElementById('file-input');
			
			// Mảng chứa các file đã chọn
			let selectedFiles = [];
			let selectedFilesEdit = [];
			
			// Xử lý sự kiện khi người dùng chọn file
			fileInput.addEventListener('change', function () {
			    imageChange();
			});
			
			function imageChange(){
				// Xóa các ảnh đã hiển thị trước đó
				   selectedFilesEdit = [];
    			   selectedImagesContainer.innerHTML = '';

				    // Lấy danh sách các file đã chọn
				    if(fileInput != null){
						const files = fileInput.files;
						
						// Thêm các file mới vào mảng
					    for (let i = 0; i < files.length; i++) {
					        selectedFiles.push(files[i]);
					    }
					}
				    for (let i = 0; i < selectedFiles.length; i++) {
				    	// Tạo khung chứa ảnh
				        const imageContainer = document.createElement('div');
				        imageContainer.className = 'selected-image';
				        
				        // Tạo đối tượng ảnh
				        const image = document.createElement('img');
				     // Khi hiển thị ảnh, sử dụng đối tượng Blob hoặc File
				        if (selectedFiles[i] instanceof Blob || selectedFiles[i] instanceof File) {
				            image.src = URL.createObjectURL(selectedFiles[i]);
				        }

				        image.style.width = '100px';
				        
						
						// Tạo nút xóa ảnh
				        const removeButton = document.createElement('div');
				        removeButton.className = 'remove-selected-image';
				       	removeButton.innerHTML = '<i class="fas fa-times"></i>';
				        removeButton.style.marginBottom = '-4px';
						
						// Thêm nút xóa vào khung chứa
				        imageContainer.appendChild(removeButton);
				     // Thêm ảnh vào khung chứa
				        imageContainer.appendChild(image);
						
				        // Thêm khung chứa ảnh vào khung hiển thị ảnh đã chọn
				        selectedImagesContainer.appendChild(imageContainer);
				        // Xử lý sự kiện khi người dùng click nút xóa ảnh
				        removeButton.addEventListener('click', function(event){
						
				            selectedFiles.splice(i, 1);
				           	fileInput.value = null;
				            imageChange();
				        });
				      
			
				    }
			};
			
			var url1 ;
			var type1;
			clearFrom();
			function clearFrom() {
				console.log("clear");
				 url1 = "/product/add";
				 type1 = "POST";
				 $('#save-product-value').text('Save Changes');
				 
			    var inputId = ['id', 'title', 'category', 'brand','discription'];
			    inputId.forEach(function (input) {
			        document.getElementById(input).value = '';
			    });

			    var selectId = ['category'];
			    selectId.forEach(function (select) {
			        document.getElementById(select).selectedIndex = 0;
			    });

			    var inputList = ['title', 'category', 'brand', 'discription', 'file-input'];
			    inputList.forEach(function (error) {
			        var error = document.getElementById(error + '-error');
			        if (error !== null) {
			        	error.textContent = '';
			        }
			    });

			    clearImage();
			}
			
			//save product
			var csrfToken;
			function saveProduct(){
				var title = document.getElementById('title').value.trim();
				var category = document.getElementById("category").value;
				var brand = document.getElementById("brand").value;
				var discription = document.getElementById('discription').value.trim();
				
				 // Tạo đối tượng FormData để chứa dữ liệu
			    var formData = new FormData();
			 	// Thêm dữ liệu vào đối tượng FormData
			    if (url1 == "/product/update") {
			        formData.append('id', parseInt(document.getElementById('id').value)); // Nếu đang trong chế độ cập nhật sản phẩm, thêm giá trị id của sản phẩm vào formData.
			    }
			    formData.append('title', title);
			    
			    if (category != null && category != "") {
			        formData.append('category', parseInt(document.getElementById("category").value)); // Thêm giá trị loại sản phẩm vào formData nếu loại sản phẩm đã được chọn.
			    } else {
			        formData.append('category', ''); // Nếu không chọn loại sản phẩm, thêm giá trị trống vào formData.
			    }
			    
			    if (brand != null && brand != "") {
			        formData.append('brand', parseInt(document.getElementById("brand").value)); 
			    } else {
			        formData.append('brand', ''); 
			    }
			    
			    formData.append('discription', discription);
			    if (selectedFilesEdit.length > 0) {
			        selectedFiles = selectedFilesEdit;
			    }
			    var defaultFileIndex = 0; // Giả định mặc định là 0
			    if (selectedFiles.length > 0) {
			        defaultFileIndex = 0; // Nếu có ảnh được chọn, đặt giá trị thành 0
			    }

			    // Gắn giá trị mặc định cho defaultFileIndex
			    formData.append('defaultFileIndex', defaultFileIndex);

			 	// Thêm các file được chọn vào đối tượng FormData
			     for (let i = 0; i < selectedFiles.length; i++) {
				    if (typeof selectedFiles[i] === 'string') {
				        formData.append('fileNames', selectedFiles[i]);
				    } else {
				        formData.append('file', selectedFiles[i]);
				    }
				}			  	
			  	
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
				        	toastr.success(data.message); // Hiển thị thông báo thành công khi request thành công.
				            clearFrom(); 
				            
				            loadData(0, "");
				            loadDataVariant(0,0,0,"");
				        },
				        error: function(jqXHR, textStatus, errorMessage) {
				            // Xử lý lỗi khi request không thành công.
				            if (jqXHR.status === 400) {
				                var errors = jqXHR.responseJSON;
				                if (errors.hasOwnProperty("bindingErrors")) {
				                    var bindingErrors = errors["bindingErrors"];
				                    for (var i = 0; i < bindingErrors.length; i++) {
				                        var error = bindingErrors[i];
				                       
				                        $("#" + error.field + "-error").text(error.defaultMessage);
				                    }
				                }
				                if (errors.hasOwnProperty("fileErrors")) {
				                    var fileError = errors["fileErrors"];
				                    $("#file-input-error").text(fileError); 
				                }
				                if (errors.hasOwnProperty("titleDuplicate")) {
				                    var titleError = errors["titleDuplicate"];
				                    $("#title-error").text(titleError); 
				                }
				                
				            } else {
				                alert("Sorry! The system has errors!"); 
				            }
				        }
				    });
			}
			
			
			loadData(0,"");
			var imageNames = [] ;
			function loadData(page, keyword) {
				var table = "#product-table tbody";
			    $.ajax({
			      url: '/product/getProductPage',
			      type: 'GET',
			      dataType: 'json',
			      data: {page : page, keyword : keyword},
			      success: function(data) {
			        //clear table
			        $(table).empty();
			
			        //add record
			        $.each(data.productResponseDtos, function(index, product) {
			        	var row = $('<tr>');
			            row.append($('<td>').text(data.currentPage * data.size + index+1));
			            
			            $.each(product.images, function(i,image) {
							imageNames.push(product.images[i].inmageForSave);
	  						if (product.images[i].isDefault == true) {
								  
								  row.append(
								  $('<td>').attr({
								    'data-imageList': imageNames
								  }).append(
								    $('<img>').attr('src', "../upload/"+image.inmageForSave+"?v=1")
								  ).addClass('pic')
								);
					  		}
					  	});
					  	
					  row.append($('<td>').text(product.id).addClass('id').css({
					    'display': 'none'
					  }));
			           row.append($('<td>').text(product.title).addClass('truncate'));
			           row.append($('<td>').text(product.categoryName).addClass('category'));
			           row.append($('<td>').text(product.brandName).addClass('brand'));
			            
			            row.append($('<td>').text(product.discription).addClass('discription truncate'));
			            row.append($('<td>').text(product.createAt).addClass('createAt'));
			            row.append($('<td>').text(product.updateAt).addClass('updateAt'));
			            
			            row.append($('<td>').addClass('text-primary').html('<i class="fa fa-plus" aria-hidden="true"></i> Add Variant').click(function() {
			                addVariant(product.id);
			            }));
			           
			            row.append(
			            		  $('<td>').addClass('edit-button').attr({
			            		    'data-id': product.id  
			            		  }).html('<i class="fas fa-pencil-alt"></i> Edit')
			            		);

			            row.append($('<td>').attr({
			            	'data-id' : product.id
			            }).addClass('text-danger delete-product').html('<i class="fas fa-trash"></i> Delete'));
			            row.append($('<td>'));
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
			
			$(function() {
				$(document).on('click', '.edit-button', function() {
			    	console.log("edit");
			        clearFrom();
			    
			    	 var productId = parseInt($(this).data('id'), 10);

			    	// Kiểm tra xem productId có tồn tại không
			    	    if (typeof productId !== 'undefined') {
			    	    	 url1 = "/product/update";
					         type1 = "PUT";
					         $('#save-product-value').text('Update');
			    	        // Gửi yêu cầu AJAX đến máy chủ
			    	        $.ajax({
			    	            url: "/product/getProduct", 
			    	            type: "GET",  
			    	            data: { id: productId },
			    	            success: function(response) {
			    	            	document.getElementById('id').value = productId;
			    	            	document.getElementById('title').value = response.title;
			    	            	document.getElementById('category').value = response.categoryId;
			    	            	document.getElementById('brand').value = response.brandId;
			    	            	document.getElementById('discription').value = response.discription;
			    	            	
			    	            	
			    	            	const selectedImagesContainer = document.getElementById('selected-images');
			    	            	// Lấy danh sách ảnh từ phản hồi của máy chủ
			    	            	const imagesFromServer = response.images;

			    	            	// Lấy ra mảng các giá trị "inmageForSave"
			    	            	const inmageForSaveArray = imagesFromServer.map(image => image.inmageForSave);
			    	            	
			    	            	 for (let i = 0; i < inmageForSaveArray.length; i++) {
			    	            		 selectedFilesEdit.push(inmageForSaveArray[i]);
			 					    }
			    	            	
			    	            	// Hàm hiển thị ảnh
			    	            	function displayImages(imageList) {
								    selectedImagesContainer.innerHTML = '';
								
								    imageList.forEach(function (imageSrc, i) {
								        // Tạo khung chứa ảnh
								        const imageContainer = document.createElement('div');
								        imageContainer.style.width = '110px';
								        imageContainer.className = 'selected-image';
								
								        // Tạo đối tượng ảnh
								        const image = document.createElement('img');
								        image.src = "../upload/" + imageSrc;
								        image.style.width = '110px';
								
								        // Tạo nút xóa ảnh
								        const removeButton = document.createElement('div');
								        removeButton.className = 'remove-selected-image';
								        removeButton.innerHTML = '<i class="fas fa-times"></i>';
								        removeButton.style.marginBottom = '0px';
								
								        // Thêm nút xóa vào khung chứa
								        imageContainer.appendChild(removeButton);
								
								        // Thêm ảnh vào khung chứa
								        imageContainer.appendChild(image);
								
								        // Thêm khung chứa ảnh vào khung hiển thị ảnh đã chọn
								        selectedImagesContainer.appendChild(imageContainer);
								
								        // Xử lý sự kiện khi người dùng click nút xóa ảnh
								        removeButton.addEventListener('click', function () {
								            imageList.splice(i, 1);
								            displayImages(imageList);
								        });
								    });
								}


			    	            	// Gọi hàm hiển thị ảnh với danh sách ảnh sẵn có
			    	            	displayImages(selectedFilesEdit);

			    	            },
			    	            error: function(error) {
			    	                // Xử lý lỗi nếu có
			    	                console.error("Error:", error);
			    	            }
			    	        });
			    	    } 
			 	   });
			    });
			
			$(document).on('click', '.delete-product', function() {
		           let productId = $(this).data("id");

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
		        			   url: '/product/delete?productId=' + productId,
						        type: 'DELETE',
						        success: function(data) {
					        	     Swal.fire(data, '', 'success');
					        	     loadData(0, "");
						        },
						        error: function(jqXHR, textStatus, errorThrown){
						            if (jqXHR.status === 400) {
						                var error = jqXHR.responseJSON || jqXHR.responseText;
						                toastr.warning(error);			
						            } else {
						                // Xử lý lỗi khác
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
			
			
			
			 function createPagination(navId, pagination, currentPage, totalPages, keyWord ){
		    	  $(navId).empty();//xoa toan bo noi dung trong html có id navId(paging)
		          
		          pagination += '<ul class="pagination">';//tao danh sach 
		          //Thêm một nút điều hướng cho trang trước vào biến pagination nếu currentPage >0
		          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

		              var startPage = currentPage > 2 ? currentPage - 2 : 0;
		              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;
						
		              for (var i = startPage; i <= endPage; i++) {
		                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
		              }
		              
		             //Nếu có quá nhiều trang (totalPages > 5) và trang hiện tại (currentPage) nhỏ hơn totalPages-3, hàm sẽ thêm một nút chấm ...
		              if ((totalPages > 5) &&  (currentPage < totalPages - 3)) {
		                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
		              }
		             //Nếu không phải là trang cuối cùng (totalPages-1 != currentPage) và trang hiện tại (currentPage) không phải là trang thứ hai từ cuối (totalPages-2 != currentPage) và trang hiện tại (currentPage) nhỏ hơn totalPages-3, hàm sẽ thêm nút điều hướng đến trang cuối cùng.
		              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
		                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
		              }
					
		          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
		          pagination += '</ul>';
		           
		           $(navId).append(pagination);
		      }
			 
			 var searchButton = document.getElementById("searchButton");
			 var  keyInput = document.getElementById("key");
			 searchButton.addEventListener("click", function() {
				 var key="";
					
				 if(keyInput.value !== ""){
					
					 key = keyInput.value;
					 
				 }
				 loadData(0,key);
			});
			 
			 
			 //-------------variant----------------//
			 var inputList = [ 'price', 'currentPrice', 'size', 'color', 'quantity' ];

			inputList.forEach(function(inputName) {
				var input = document.getElementById(inputName);
				var error = document.getElementById(inputName + '-error');

				if (input) { // Kiểm tra xem phần tử input có tồn tại không
					input.addEventListener("focus", function() {
						error.textContent = '';
					});
				} else {
					console.error("Element with id '" + inputName
							+ "' not found in the page.");
				}
			});
			
			function clearFromVariant() {
				//console.log("clear");
				 urlV = "/variant/add";
				 typeV = "POST";
				 $('#save-variant-value').text('Save Change');
				 
			    var inputId = ['id','price','currentPrice', 'size', 'color', 'quantity','discription'];
			    inputId.forEach(function (input) {
			        document.getElementById(input).value = '';
			    });

			    var selectId = ['size'];
			    selectId.forEach(function (select) {
			        document.getElementById(select).selectedIndex = 0;
			    });
			    
			    var selectId = ['color'];
			    selectId.forEach(function (select) {
			        document.getElementById(select).selectedIndex = 0;
			    });

			    var inputList = ['price', 'currentPrice', 'size', 'discription', 'color','quantity'];
			    inputList.forEach(function (error) {
			        var error = document.getElementById(error + '-error');
			        if (error !== null) {
			        	error.textContent = '';
			        }
			    });
			}
			
			function loadSize(selectId) {
			    const sizeSelect = document.getElementById(selectId);
			    $.ajax({
			        url: "/size/getAll",
			        type: "GET",
			        dataType: 'json',
			        success: function(sizes) {
			        	if (sizes.length != 0) {
			        		sizeSelect.innerHTML = "<option value='' selected>Choose size</option>";
			        		sizes.forEach(function(size) {
				    			    const optionElement = document.createElement('option');
				    			    optionElement.value = size.id;
				    			    optionElement.textContent = size.name;
				    			    sizeSelect.appendChild(optionElement);
				    			});
				    		  
				          } else {
				              
				          }
			        },
			        error: function(jqXHR, textStatus, errorMessage) {
			            var error = jqXHR.responseJSON || jqXHR.responseText;
			        }
			    });
			}
			
			
			function loadColor(selectId) {
			    const colorSelect = document.getElementById(selectId);
			    $.ajax({
			        url: "/color/getAll",
			        type: "GET",
			        dataType: 'json',
			        success: function(colors) {
			        	if (colors.length != 0) {
			        		colorSelect.innerHTML = "<option value='' selected>Choose color</option>";
			        		colors.forEach(function(color) {
				    			    const optionElement = document.createElement('option');
				    			    optionElement.value = color.id;
				    			    optionElement.textContent = color.name;
				    			    colorSelect.appendChild(optionElement);
				    			});
				    		  
				          } else {
				              
				          }
			        },
			        error: function(jqXHR, textStatus, errorMessage) {
			            var error = jqXHR.responseJSON || jqXHR.responseText;
			        }
			    });
			}
			
			loadSize('size');
		    loadColor('color');
			var urlV ;
			var typeV;
			function addVariant(productId){
				  $('#variantModal').modal("show");
				  clearFromVariant();
				  $('#productId').val(productId);	
				  
				  $("#id").val("");
				  $('#save-variant-value').text('Save Change');
				  urlV = "/variant/add";
				  typeV = "POST";
				    
				    
			};
			
			function editVariant(variantId) {
				//clearFromVariant();
				
				$('#variantModal').modal("show");
			    clearFromVariant();
			    
			    $('#save-variant-value').text('Update');

			    urlV = "/variant/update";
			    typeV = "PUT";
			    // Chọn hàng bảng với variantId cụ thể
			    var variantRow = $('tr').find('.id:contains(' + variantId + ')').closest('tr');

			   // var name = variantRow.find('.truncate').text();
			    var price = variantRow.find('.price').text();
			    var currentPrice = variantRow.find('.currentPrice').text();
			    var quantity = variantRow.find('.quantity').text();
			    var sizeId = variantRow.find('.sizeId').text();
			    var colorId = variantRow.find('.colorId').text();
			    var productId = variantRow.find('.productId').text();

			    
			    			    
			    document.getElementById('variantId').value = variantId;
            	document.getElementById('productId').value = productId;
            	document.getElementById('size').value = sizeId;
            	document.getElementById('color').value = colorId;
            	document.getElementById('price').value = price;
            	document.getElementById('currentPrice').value = currentPrice;
            	document.getElementById('quantity').value = quantity;
			}

			
			function numberWithCommas(x) {
			    // Xóa tất cả các ký tự không phải số trong chuỗi x
			    x = x.replace(/\D/g, '');

			    // Thêm dấu chấm phân tách hàng nghìn vào số x
			    x = x.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.');

			    return x; 
			}

			$(document).on('input', '#price', function(){
			    var value = numberWithCommas($(this).val());
			    $(this).val(value);
			});

			
			$(document).on('input', '#currentPrice', function(){
			    var value = numberWithCommas($(this).val());
			    $(this).val(value);
			});

			$(document).on('input', '#quantity', function(){
			    var value = numberWithCommas($(this).val());
			    $(this).val(value);
			});
			
			//save variant
			function saveVariant() {
				var variantId = document.getElementById("variantId").value; 
				var product = document.getElementById("productId").value; 
				var size = document.getElementById("size").value; 
				var color = document.getElementById("color").value; 
			    var price = document.getElementById('price').value.replace(/\./g, ''); // Lấy giá trị giá sản phẩm và xóa dấu chấm phân tách hàng nghìn (nếu có).
			    var quantity = document.getElementById('quantity').value.replace(/\./g, ''); // Lấy giá trị số lượng sản phẩm và xóa dấu chấm phân tách hàng nghìn (nếu có).
			    var currentPrice = document.getElementById('currentPrice').value.replace(/\./g, '');
			    var note = document.getElementById('discription').value.trim();
			    
			 // Tạo đối tượng FormData để chứa dữ liệu
			    var formData = new FormData();
			 
			    if (variantId != null && variantId != "") {
			        formData.append('variantId', parseInt(document.getElementById("variantId").value));
			    }
			 
			 //product
			    if (product != null && product != "") {
			        formData.append('product', parseInt(document.getElementById("productId").value));
			    } else {
			        formData.append('product', '');
			    }
			    //size
			    if (size != null && size != "") {
			        formData.append('size', parseInt(document.getElementById("size").value));
			    } else {
			        formData.append('size', '');
			    }
			    //color
			    if (color != null && color != "") {
			        formData.append('color', parseInt(document.getElementById("color").value));
			    } else {
			        formData.append('color', '');
			    }
			
			    if (price != null && price != "") {
			        formData.append('price', parseFloat(document.getElementById('price').value.replace(/\./g, ''))); 
			    } else {
			        formData.append('price', -1); 
			    }
			
			    if (currentPrice != null && currentPrice != "") {
			        formData.append('currentPrice', parseFloat(document.getElementById('currentPrice').value.replace(/\./g, ''))); 
			    } else {
			        formData.append('currentPrice', 0); 
			    }
			
			    if (quantity != null && quantity != "") {
			        formData.append('quantity', parseInt(document.getElementById('quantity').value.replace(/\./g, '')));
			    } else {
			        formData.append('quantity', -1); 
			    }
			    
			    if (note !== "") {
			        formData.append('note', note);
			    }else{
			    	formData.append('note', "");
			    }
			    
			    csrfToken = Cookies.get('XSRF-TOKEN');
			    $.ajax({
			        url: urlV,
			        type: typeV,
			        contentType: false,
			        processData: false,
			        data: formData,
			        dataType: 'json',
			        headers: {
					    'X-XSRF-TOKEN': csrfToken
					  },
			        cache: false,
			        success: function(data) {
			        	toastr.success(data.message);
			            clearFromVariant(); 
			            // close modal
				        $('#variantModal').modal('hide');
				        $('#pills-profile-tab').tab('show')
				        loadDataVariant(0,0,0,"");
			        },
			        error: function(jqXHR, textStatus, errorMessage) {
			        	console.log(jqXHR.status);
			            if (jqXHR.status === 400) {
			                var errors = jqXHR.responseJSON;
			                if (errors.hasOwnProperty("bindingErrors")) {
			                    var bindingErrors = errors["bindingErrors"];
			                    for (var i = 0; i < bindingErrors.length; i++) {
			                        var error = bindingErrors[i];
			                        // Hiển thị thông báo lỗi trong các thẻ có ID đã tạo trước đó.
			                        $("#" + error.field + "-error").text(error.defaultMessage);
			                    }
			                }
			                
			                if (errors.hasOwnProperty("variantDuplicate")) {
			                    var variantDuplicate = errors["variantDuplicate"];
			                    toastr.warning(variantDuplicate); 
			                }
			            } else {
			                alert("Sorry! The system has errors!"); // Hiển thị thông báo lỗi nếu request không thành công với mã lỗi khác 400.
			            }
			        }
			    });
			}
						
			$(document).on('click', '.delete-variant', function() {
	           let variantId = $(this).data("id");

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
					        url: '/variant/delete?variantId=' + variantId,
					        type: 'DELETE',
					        success: function(data) {
				        	     Swal.fire(data, '', 'success');
					        	loadDataVariant(0,0,0,"");
							    $('#deleteVariantmodal').modal('hide');
					            
					        },
					        error: function(jqXHR, textStatus, errorThrown){
					            if (jqXHR.status === 400) {
					                var error = jqXHR.responseJSON || jqXHR.responseText;
					                toastr.warning(error);
					            } else {
					                // Xử lý lỗi khác
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
			
			
			//load table
			loadDataVariant(0,0,0,"");
			function loadDataVariant(page, sizeId, colorId, keyword) {
				loadSize('size-select');
				loadColor('color-select');
				var table = "#variant-table tbody";
			    $.ajax({
			      url: '/variant/getVariantPage',
			      type: 'GET',
			      dataType: 'json',
			      data: {page : page, sizeId : sizeId, colorId : colorId, keyword : keyword},
			      success: function(data) {
			        //clear table
			        $(table).empty();
			
			        //add record
			        $.each(data.variantResponseDtos, function(index, variant) {
			        	var row = $('<tr>');
			            row.append($('<td>').text(data.currentPage * data.size + index + 1));
			            					  	
					  row.append($('<td>').text(variant.id).addClass('id').css({
					    'display': 'none'
					  }));
					  row.append($('<td>').text(variant.sizeId).addClass('sizeId').css({
						    'display': 'none'
					  }));
					  row.append($('<td>').text(variant.colorId).addClass('colorId').css({
						    'display': 'none'
					  }));
					  row.append($('<td>').text(variant.productId).addClass('productId').css({
						    'display': 'none'
					  }));
					  
			           row.append($('<td>').text(variant.title).addClass('truncate'));
			           row.append($('<td>').text((variant.price).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })).addClass('price'));
			           row.append($('<td>').text((variant.currentPrice)!= null ? (variant.currentPrice).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) : variant.currentPrice).addClass('currentPrice'));
			           row.append($('<td>').text(variant.quantity).addClass('quantity'));
			           row.append($('<td>').text(variant.size).addClass('size'));
			           row.append($('<td>').text(variant.color).addClass('color'));
			           row.append($('<td>').text(variant.buyCount).addClass('buyCount'));
			           
			           row.append($('<td>').text(variant.createAt).addClass('createAt'));
			           row.append($('<td>').text(variant.updateAt).addClass('updateAt'));
			            
			           row.append($('<td>').attr({
			        	   'onclick': 'editVariant(' + variant.id + ')'
			           }).html('<i class="fas fa-pencil-alt"></i> Edit'));
			            row.append($('<td>').attr({
			            	'data-id': variant.id,
			               /*  'onclick': 'deleteVariant(' + variant.id + ')' */
			            }).addClass('text-danger delete-variant').html('<i class="fas fa-trash"></i> Delete'));
			            
			            $(table).append(row);
			        });
			         pagination = '';
			         currentPage = data.currentPage;
			         totalPages = data.totalPages;
			         size = data.size;
			        //create pagination 
				     createPagination_variant("#paging-variant",pagination,currentPage,totalPages,sizeId,colorId, keyword );
			      },
			      error: function(){}})
			  } 
			
			function createPagination_variant(navId,pagination,currentPage,totalPages,sizeId,colorId, keyword ){
		    	  $(navId).empty();
		          
		          pagination += '<ul class="pagination">';
		          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadDataVariant(' + (currentPage - 1) +','+sizeId +','+colorId +',\''+keyword+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

		         
		              var startPage = currentPage > 2 ? currentPage - 2 : 0;
		              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

		              for (var i = startPage; i <= endPage; i++) {
		                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadDataVariant(' + i +','+sizeId +','+colorId +',\''+keyword+'\')">' + (i + 1) + '</a></li>';
		              }
		             //${page.totalPages > 5 && page.number < page.totalPages - 3}
		              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
		                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
		              }
		             
		              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
		                  pagination += '<li><a class="page-link" onclick="loadDataVariant(' + (totalPages - 1) +','+sizeId +','+colorId +',\''+keyword+'\')" >' + totalPages + '</a></li>';
		              }

		          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadDataVariant(' + (currentPage + 1) +','+sizeId +','+colorId +',\''+keyword+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
		          pagination += '</ul>';
		           
		           $(navId).append(pagination);
		      }
			
			var searchButtonVariant = document.getElementById("searchButtonVariant");
			var keyInputVariant = document.getElementById("keyVariant");
			var sizeSelect = document.getElementById("size-select");
			var colorSelect = document.getElementById("color-select");
			
			searchButtonVariant.addEventListener("click", function() {

				var sizeId = 0;
				var colorId = 0;
				var keyVariant = "";
				
				 if(keyInputVariant.value !== ""){
					
					 keyVariant = keyInputVariant.value;
					 
				 }
				  if (sizeSelect.value !== "choose" && sizeSelect.value !== "") {
					 
					  sizeId = sizeSelect.value;
				  }
				  if (colorSelect.value !== "choose" && colorSelect.value !== "") {
						 
					  colorId = colorSelect.value;
				  }
			 	
				 loadDataVariant(0,sizeId,colorId,keyVariant);

			});
			
			var resetButton = document.getElementById("reset");

			// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
			resetButton.addEventListener("click", function() {
				loadDataVariant(0,0,0,"");
			 	keyInputVariant.value="";
			 	sizeSelect.value="";
			 	colorSelect.value="";
			});