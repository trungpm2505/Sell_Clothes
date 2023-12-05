loadCategory();
    function loadCategory() {
      const categorySelect = document.getElementById('category');
      $.ajax({
          url: "/category/getAll",
          type: "GET",
          dataType: 'json',
          success: function (categories) {
            if (categories.length != 0) {
              categorySelect.innerHTML = "<option value='0' selected>Choose category</option>";
              categories.forEach(function (category) {const optionElement = document.createElement('option');
                  optionElement.value = category.id;
                  optionElement.textContent = category.categoryName;
                  categorySelect.appendChild(optionElement);
                });

            } else {

            }
          },
          error: function (jqXHR, textStatus, errorMessage) {
            var error = jqXHR.responseJSON
              || jqXHR.responseText;
          }
        });
    }
    
    function loadSize() {
        const sizeContainer = document.getElementById('sizeContainer');

        $.ajax({
            url: "/size/getAll",
            type: "GET",
            dataType: 'json',
            success: function(sizes) {
                if (sizes.length !== 0) {
                	const allLiElement = document.createElement('li');
                    const allInputElement = document.createElement('input');
                    allInputElement.type = 'radio';
                    allInputElement.name = 'size';
                    allInputElement.id = 'size_all';
                    allInputElement.value = '0';

                    const allLabelElement = document.createElement('label');
                    allLabelElement.setAttribute('for', 'size_all');
                    allLabelElement.textContent = 'All';

                    allLiElement.appendChild(allInputElement);
                    allLiElement.appendChild(allLabelElement);
                    sizeContainer.appendChild(allLiElement);
                    allInputElement.addEventListener('click', function() {
                        // Lấy giá trị của radio button được chọn
                        selectedSizeId = allInputElement.value;
                        searchButtonClick();
                        // Gọi hàm hoặc thực hiện các thao tác khác với giá trị đã chọn
                    });
                    

                    sizes.forEach(function(size) {
                        const liElement = document.createElement('li');
                        const inputElement = document.createElement('input');
                        inputElement.type = 'radio';
                        inputElement.name = 'size';
                        inputElement.id = `size_${size.id}`;
                        inputElement.value = size.id;

                        const labelElement = document.createElement('label');
                        labelElement.setAttribute('for', `size_${size.id}`);
                        labelElement.textContent = size.name;

                        liElement.appendChild(inputElement);
                        liElement.appendChild(labelElement);
                        sizeContainer.appendChild(liElement);

                        inputElement.addEventListener('click', function() {
                            // Lấy giá trị của radio button được chọn
                            selectedSizeId = inputElement.value;
                            searchButtonClick();
                        });
                    });
                }
            },
            error: function(jqXHR, textStatus, errorMessage) {
                var error = jqXHR.responseJSON || jqXHR.responseText;
            }
        });
    }

    loadSize();
    
    function loadColor() {
        const colorContainer = document.getElementById('colorContainer');

        $.ajax({
            url: "/color/getAll",
            type: "GET",
            dataType: 'json',
            success: function(colors) {
                if (colors.length !== 0) {
                    // Thêm radio button "All" vào đầu danh sách
                    const allLiElement = document.createElement('li');
                    const allInputElement = document.createElement('input');
                    allInputElement.type = 'radio';
                    allInputElement.name = 'color';
                    allInputElement.id = 'color_all';
                    allInputElement.value = '0';

                    const allLabelElement = document.createElement('label');
                    allLabelElement.setAttribute('for', 'color_all');
                    allLabelElement.textContent = 'All';

                    allLiElement.appendChild(allInputElement);
                    allLiElement.appendChild(allLabelElement);
                    colorContainer.appendChild(allLiElement);
                    allInputElement.addEventListener('click', function() {
                        // Lấy giá trị của radio button được chọn
                        selectedColorId = allInputElement.value;
                        searchButtonClick();
                    });
                    

                    colors.forEach(function(color) {
                        const liElement = document.createElement('li');
                        const inputElement = document.createElement('input');
                        inputElement.type = 'radio';
                        inputElement.name = 'color';
                        inputElement.id = `color_${color.id}`;
                        inputElement.value = color.id;

                        const labelElement = document.createElement('label');
                        labelElement.setAttribute('for', `color_${color.id}`);
                        labelElement.textContent = color.name;

                        liElement.appendChild(inputElement);
                        liElement.appendChild(labelElement);
                        colorContainer.appendChild(liElement);

                        inputElement.addEventListener('click', function() {
                            // Lấy giá trị của radio button được chọn
                            selectedColorId = inputElement.value;
                            console.log('Đã chọn màu có ID: ' + selectedColorId);
                            searchButtonClick();
                            // Gọi hàm hoặc thực hiện các thao tác khác với giá trị đã chọn
                        });
                    });
                }
            },
            error: function(jqXHR, textStatus, errorMessage) {
                var error = jqXHR.responseJSON || jqXHR.responseText;
            }
        });
    }


    loadColor();



    loadAllProduct(0, 0, 0, 0, 0,null, null, "");
    function loadAllProduct(page, categoryId, brandId, sizeId, colorId, minPrice, maxPrice, keyword) {
      $.ajax({
        url: '/product/getProductView',
        type: 'GET',
        dataType: 'json',
        data: {
          page: page,categoryId: categoryId,brandId: brandId,sizeId: sizeId,colorId: colorId, minPrice : minPrice, maxPrice : maxPrice, keyword: keyword
        },
        success: function (data) {
          $('.productAll').empty();

          $.each(data.productResponseDtos, function (index, product) {
            var col = $('<div>').addClass('col-lg-3 col-md-6');
            var single_product = $('<div>').addClass('single_product');
            var product_thumb = $('<div>').addClass('product_thumb');

            var product_content = $('<div>').addClass('product_content');
            var product_info = $('<div>') .addClass( 'product_info');
            //image
            $.each(
                product.images,function (i,image) {
                  if (product.images[i].isDefault == true) {
                    product_thumb.append($('<a>',{
                          href: '/product/details?productId='+product.id}).append($('<img>').attr('src',"../upload/" + image.inmageForSave).addClass('card-img-top')));
                  }
                });

            //variant
            $.each( product.variantResponseDtos,function (i,variant) {
                  if (i === 0) {
                    var formattedPrice = variant.price.toLocaleString('vi-VN',{style: 'currency', currency: 'VND'});

                    if (variant.currentPrice != null) {
                      var formattedCurentPrice = variant.currentPrice.toLocaleString('vi-VN',{style: 'currency', currency: 'VND'});
                      product_content.append($('<span>').text(formattedCurentPrice).addClass('product_price '));
                      product_content.append($('<span>').text(formattedPrice).addClass('product_price del'));

                    } else {
                      product_content.append($('<span>').text(formattedPrice).addClass('product_price'));
                    }

                  }
                });
            product_content.append($('<div>').text(product.id).hide().addClass('id'));
            product_content.append($('<h3>').append($('<a>',{
                      href: '/product/details?productId='+product.id,
                      text: product.title
                    }).addClass('product_title')));
            product_info.append($('<ul>').append(
            	  $('<li>').append($('<a>',{
                         href: '/product/details?productId='+product.id,
                         text: 'View Detail',
                       })),
                  $('<li>').append($('<a>',{
                          href: '#',
                          text: 'Add to cart',
                          'data-toggle': 'modal',
                          'data-target': '#modal_box',
                          onclick: 'loadData(' + product.id + ');'
                   }))));

            single_product.append(product_thumb)
            single_product.append(product_content)
            single_product.append(product_info)

            col.append(single_product);
            $('.productAll').append(col);
          });
          pagination = '';
	         currentPage = data.currentPage;
	         totalPages = data.totalPages;
	         size = data.size;
	        //create pagination 
		     createPagination("#paging",pagination,currentPage,totalPages,categoryId,brandId,sizeId,colorId,minPrice, maxPrice, keyword );
        }
      });
    }
    
    function createPagination(navId,pagination,currentPage,totalPages,categoryId,brandId,sizeId,colorId,minPrice,maxPrice, keyword ){
  	  $(navId).empty();
        
        pagination += '<ul class="pagination">';
        pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadAllProduct(' + (currentPage - 1) +','+categoryId +','+brandId +','+sizeId +','+colorId +','+minPrice +','+maxPrice +',\''+keyword+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

       
            var startPage = currentPage > 2 ? currentPage - 2 : 0;
            var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

            for (var i = startPage; i <= endPage; i++) {
                pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadAllProduct(' + i +','+categoryId +','+brandId +','+sizeId +','+colorId +','+minPrice +','+maxPrice +',\''+keyword+'\')">' + (i + 1) + '</a></li>';
            }
           //${page.totalPages > 5 && page.number < page.totalPages - 3}
            if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
                pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
            }
           
            if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
                pagination += '<li><a class="page-link" onclick="loadAllProduct(' + i +','+categoryId +','+brandId +','+sizeId +','+colorId +','+minPrice +','+maxPrice +',\''+keyword+'\')">';
            }

        pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadAllProduct(' + (currentPage + 1) +','+categoryId +','+brandId +','+sizeId +','+colorId +','+minPrice +','+maxPrice +',\''+keyword+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
        pagination += '</ul>';
         
        $(navId).append(pagination);
    }
    
    //var searchButton = document.getElementById("searchButton");
	// var  keyInput = document.getElementById("key");
	 
      
    	      /*--------------------------
    	     ScrollUp
    	    ---------------------------- */
    	    $.scrollUp({
    	        scrollText: '<i class="fa fa-angle-double-up"></i>',
    	        easingType: 'linear',
    	        scrollSpeed: 900,
    	        animation: 'fade'
    	    });
    		
    	    
    	     /* brand activation */
    	    $('.brand_active').owlCarousel({
    	        animateOut: 'fadeOut',
    			loop: true,
    	        nav: true,
    	        autoplay: false,
    	        autoplayTimeout: 8000,
    	        items: 6,
    	        dots:true,
    	        navText: ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],
    	        responsiveClass:true,
    			responsive:{
    				0:{
    					items:1,
    				},
    	            480:{
    					items:3,
    				},
    	            992:{
    					items:4,
    				},
    	            1200:{
    					items:6,
    				},

    			  }
    	    });
    	    
    	    
    	    $("#slider-range").slider({
    	        range: true,
    	        min: 0,
    	        max: 2000000,
    	        step: 10000,
    	        values: [0, 1000000],
    	        slide: function (event, ui) {
    	        	minPrice = ui.values[0];
    	            maxPrice = ui.values[1];
    	            $("#amount").val(ui.values[0].toLocaleString('vi-VN') + "đ" + " - " + ui.values[1].toLocaleString('vi-VN') + "đ");
    	        },
    	        change: function(event, ui) {
    	            searchButtonClick();
    	        }
    	    });

    	    // Đặt giá trị mặc định của input khi trang web được tải
    	    $("#amount").val(
    	        $( "#slider-range" ).slider("values", 0).toLocaleString('vi-VN') + "đ" +
    	        " - " +
    	        $( "#slider-range" ).slider("values", 1).toLocaleString('vi-VN') + "đ"
    	    );

    	      

    	     var selectedColorId;
    		  var selectedSizeId;
    		  var minPrice;
    		  var maxPrice;
    	      function searchButtonClick() {
    			  var key = "";
    			  var categoryId = document.getElementById("category").value;
    			  loadAllProduct(0, categoryId, 0, selectedSizeId, selectedColorId, minPrice,maxPrice, key);
    		  }
    	      
    	      
    	//////////////
    	 var ProductDetails = {
    		createRadioList: function(variantList, propertyName, container, labelText, divClass) {
	            var propertyList = $('<ul>'); // Tạo một danh sách ul
	            var addedProperties = {};
	
	            $.each(variantList, function(i, variant) {
	                // Nếu thuộc tính chưa được thêm vào danh sách
	                if (!addedProperties[variant[propertyName]]) {
	                    addedProperties[variant[propertyName]] = true;
	
	                    var id = variant[propertyName];
	                    var listItem = $('<li>');
	
	                    var propertyRadio = $('<input>').attr({
	                        type: 'radio',
	                        name: propertyName,
	                        value: variant[propertyName + 'Id'],
	                        id: id,
	                        class: propertyName + '-button' // Thêm class tại đây
	                    });
	
	                    // Tạo label và thiết lập for attribute
	                    var propertyLabel = $('<label>').attr('for', id).text(variant[propertyName]);
	
	                    // Thêm input radio và label vào mục li
	                    listItem.append(propertyRadio).append(propertyLabel);
	
	                    // Thêm mục li vào danh sách
	                    propertyList.append(listItem);
	                }
	            });
	
	            if (propertyName === 'size') {
	                var propertyDiv = $('<div>').addClass(divClass).append($('<h2>').text(labelText));
	                propertyDiv.append(propertyList);
	                var productDPropertyDiv = $('<div>').addClass('product_d_' + propertyName);
	                productDPropertyDiv.append(propertyDiv);
	                container.append(productDPropertyDiv);
	            } else {
	                var propertyDiv = $('<div>').addClass('sidebar_widget color').append($('<h2>').text(labelText));
	                container.append(propertyDiv);
	                propertyDiv.append($('<div>').addClass('widget_color').append(propertyList));
	            }
	        }
    	}
    	
	    function loadData(productId) {	
		    	globalVariantId = null;
    		// Hủy bỏ Owl Carousel cũ trước khi tạo mới
    		 $('.owl-carousel.owl-theme.product_slider').trigger('destroy.owl.carousel');

	        $.ajax({
	            url: '/product/get?productId=' + productId,
	            type: 'GET',
	            //dataType: 'json',
	            success: function(data) {
	            	
	            	$('.single_product_image').empty();
	            	 $('.owl-carousel.owl-theme.product_slider').empty();
	                // Thêm ảnh lớn vào .single_product_image (chỉ lấy ảnh có isDefault)
	                if (data.images && data.images.length > 0) {
	                    $.each(data.images, function(i, image) {
	                        if (image.isDefault) {
	                            var imageDiv = $('<div>').addClass('single_product_image_background');
	                            var imageUrl = '/upload/' + image.inmageForSave;
	                            var img = $('<img>').addClass('main-image').attr({
	                                'src': imageUrl,
	                                'alt': 'Product Image'
	                            });

	                            imageDiv.append(img);
	                            $('.single_product_image').append(imageDiv);
	                        }
	                        
	                        var imageDiv = $('<div>').addClass('owl-item product_slider_item');
	                        var productImageDiv = $('<div>').addClass('product_image');
	                        var imageUrl = '/upload/' + image.inmageForSave;
	                        var img = $('<img>').addClass('thumbnail-image').attr({
	                            'src': imageUrl,
	                            'alt': 'Product Image'
	                        });

	                        productImageDiv.append(img);
	                        imageDiv.append(productImageDiv);

	                        // Thêm vào .imageSmall
	                        $('.owl-carousel.owl-theme.product_slider').append(imageDiv);
	                        
	                    });
	                    initSlider();
	                    var thumbnails = $('.thumbnail-image');

	   	             // Xử lý sự kiện khi hover vào ảnh nhỏ
	   	             thumbnails.on('mouseover', function () {
	   	                 // Lấy URL của ảnh nhỏ khi hover
	   	                 var newImageSrc = $(this).attr('src');

	   	                 // Thay đổi ảnh lớn khi hover vào ảnh nhỏ
	   	                 $('.main-image').attr('src', newImageSrc);
	   	             });
	                }
	                
	                var colorOptionsState = [];
		 	        var sizeOptionsState = [];

	            	// Mảng để lưu trữ các phần tử cần append
	            	var elementsToAppend = [];

	            	//clear table
	            	$('.signle').empty();

	            	//add record
	            	var product_d_right = $('<div>').addClass('product_d_right');

	            	//add id
	            	product_d_right.append($('<div>').text(data.id).hide().addClass('productId'));

	            	//title
	            	product_d_right.append($('<h1>').text(data.title));
	            	//description
	            	product_d_right.append($('<div>').addClass('product_desc').append($('<p>').text(data.discription)));

	            	var countItems = 0;
	            	$.each(data.variantResponseDtos, function (i, variant) {

	            	    if (i === 0) {
	            	        var formattedPrice = variant.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

	            	        if (variant.currentPrice != null) {
	            	            var formattedCurentPrice = variant.currentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

	            	            product_d_right.append($('<div>').addClass('content_price mb-15')
	            	                .append($('<span>').addClass('new_price').text(formattedCurentPrice))
	            	                .append($('<span>').addClass('old-price').text(formattedPrice))
	            	            );
	            	        } else {
	            	            product_d_right.append($('<div>').addClass('content_price mb-15')
	            	                .append($('<span>').text(formattedPrice))
	            	            );
	            	        }
	            	        //items;

	            	    }
	            	    countItems += variant.quantity;
	            	});
	            	var items = $('<p>').text(countItems + ' items');
	            	//button
	            	var boxQuantityDiv = $('<div>').addClass('box_quantity mb-20')
	            	    .append(
	            	        $('<form>').attr('action', '#').append(
	            	            $('<span>').addClass('minus').append(
	            	                $('<i>').addClass('fa fa-minus').attr('aria-hidden', 'true')
	            	            ),
	            	            $('<input>').attr({
	            	                'type': 'number',
	            	                'id': 'quantity-single-product',
	            	                'value': 1,
	            	                'min': 1
	            	            }),
	            	            $('<span>').addClass('plus').append(
	            	                $('<i>').addClass('fa fa-plus').attr('aria-hidden', 'true')
	            	            )
	            	        ),
	            	    )
	            	    .append(
						    $('<button>').attr('type', 'submit').attr('id', 'add-to-cart-single').append(
						        $('<i>').addClass('fa fa-shopping-cart')
						    ).append(' add to cart').on('click', function() {
						        // Gọi hàm khi nút "add to cart" được nhấp
						        addToCartInSingleProduct();
						    })
						);

	            	boxQuantityDiv.append('<br>');
	            	var errorTextDiv = $('<div>').addClass('error-text text-danger');

	            	// Thêm thẻ chữ báo lỗi vào boxQuantityDiv
	            	

	            	


	            	// Sử dụng hàm để tạo danh sách size
	            	ProductDetails.createRadioList(data.variantResponseDtos, 'size', product_d_right, 'size:', 'modal_size');

	            	// Sử dụng hàm để tạo danh sách color
	            	ProductDetails.createRadioList(data.variantResponseDtos, 'color', product_d_right, 'color:', '');

	            	// Hàm để tạo danh sách radio

	            	product_d_right.append(boxQuantityDiv);
	            	product_d_right.append(errorTextDiv);

	            	var stockDiv = $('<div>').addClass('product_stock mb-20');
	            	stockDiv.append(items);
	            	stockDiv.append('<span>In stock</span>');
	            	product_d_right.append(stockDiv);

	            	// Tạo và append div thứ hai với class 'wishlist-share'
	            	var wishlistDiv = $('<div>').addClass('wishlist-share');
	            	wishlistDiv.append('<h4>Share on:</h4>');
	            	wishlistDiv.append('<ul><li><a href="#"><i class="fa fa-rss"></i></a></li><li><a href="#"><i class="fa fa-vimeo"></i></a></li><li><a href="#"><i class="fa fa-tumblr"></i></a></li><li><a href="#"><i class="fa fa-pinterest"></i></a></li><li><a href="#"><i class="fa fa-linkedin"></i></a></li></ul>');
	            	product_d_right.append(wishlistDiv);

	            	// Thêm product_d_right và các phần tử khác vào mảng
	            	elementsToAppend.push(product_d_right);
	            	elementsToAppend.push(boxQuantityDiv);
	            	elementsToAppend.push(stockDiv);
	            	elementsToAppend.push(wishlistDiv);

	            	// Sử dụng append một lần duy nhất để thêm tất cả các phần tử vào .signle
	            	$('.signle').append(elementsToAppend);
	            	  
	  	 		  // Lấy phần tử ô nhập liệu số lượng
	  	 				var quantityInput = $('#quantity-single-product');

	  	 				// Xử lý sự kiện khi người dùng thay đổi giá trị trong ô nhập liệu
	  	 				quantityInput.on('input', function() {
	  	 					// Lấy giá trị số lượng từ ô nhập liệu
	  	 					var currentValue = parseInt($(this).val());

	  	 					// Kiểm tra giá trị số lượng để đảm bảo nó không nhỏ hơn 1
	  	 					if (currentValue < 1) {
	  	 						currentValue = 1;
	  	 					}

	  	 					// Cập nhật giá trị số lượng trong ô nhập liệu
	  	 					$(this).val(currentValue);
	  	 				});

	  	 				// Xử lý sự kiện khi nhấn nút "plus"
	  	 				$('.plus').on('click', function() {
	  	 					var currentValue = parseInt(quantityInput.val());
	  	 					// Tăng giá trị số lượng lên 1
	  	 					if(currentValue < quantitySelect){
	  	 						quantityInput.val(currentValue + 1);
	  	 						currentValue = quantitySelect;
	  	 					}
	  	 					
	  	 				});

	  	 				// Xử lý sự kiện khi nhấn nút "minus"
	  	 				$('.minus').on('click', function() {
	  	 					var currentValue = parseInt(quantityInput.val());
	  	 					// Giảm giá trị số lượng xuống 1, nhưng không cho phép nhỏ hơn 1
	  	 					quantityInput.val(Math.max(currentValue - 1, 1));
	  	 				});

	  	 		        quantityInput.on('input', function() {
	  	 		            var enteredValue = parseInt($(this).val(), 10); 
	  	 		            if (enteredValue > quantitySelect) {
	  	 		            	quantityInput.val(quantitySelect);
	  	 		            }
	  	 		        });
	  	 				
	  	 		     $('.size-button').on('click', function () {
			 	            var sizeId = $(this).val();
			 	            var productId = $('.productId').text();
			 	            $('.error-text.text-danger').text("");
			 	            sizeOptionsState = saveSizeOptionsState();

			 	            $.ajax({
			 	                type: 'GET',
			 	                url: '/variant/getVariant',
			 	                data: {
			 	                    productId: productId,
			 	                    sizeId: sizeId
			 	                },
			 	                success: function (response) {
			 	                    $('.sidebar_widget.color').remove();
			 	                    ProductDetails.createRadioList(response, 'color', $('.product_d_right'), 'color:', '');
			 	                    restoreColorOptionsState(colorOptionsState);

			 	                    
			 	                },
			 	                error: function (error) {
			 	                    console.error(error);
			 	                }
			 	            });
			 	        }); 

			 	        $('.color-button').on('click', function () {
			 	            var colorId = $(this).val();
			 	            var productId = $('.productId').text();
			 	            $('.error-text.text-danger').text("");
			 	            colorOptionsState = saveColorOptionsState();

			 	            $.ajax({
			 	                type: 'GET',
			 	                url: '/variant/getVariant',
			 	                data: {
			 	                    productId: productId,
			 	                    colorId: colorId
			 	                },
			 	                success: function (response) {
			 	                    $('.product_d_size').remove();
			 	                    var newSizeList = $('<div>'); 
			 	                    ProductDetails.createRadioList(response, 'size', newSizeList, 'size:', 'modal_size');
			 	                    $('.sidebar_widget').before(newSizeList);
			 	                },
			 	                error: function (error) {
			 	                    console.error(error);
			 	                }
			 	            });
			 	        });

	            },
	            error: function() {
	                // Handle errors here
	            }
	        });
	        
    	}
	     // Hàm để lấy giá trị của tham số từ URL
	        function getParameterByName(name, url) {
	            if (!url) url = window.location.href;
	            name = name.replace(/[\[\]]/g, '\\$&');
	            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
	                results = regex.exec(url);
	            if (!results) return null;
	            if (!results[2]) return '';
	            return decodeURIComponent(results[2].replace(/\+/g, ' '));
	        }
	         
	         function initSlider() {
	             if ($('.product_slider').length) {
	                 var slider1 = $('.product_slider');

	                 slider1.owlCarousel({
	                     loop: false,
	                     dots: false,
	                     nav: false,
	                     responsive: {
	                         0: { items: 1 },
	                         480: { items: 2 },
	                         768: { items: 3 },
	                     }
	                 });

	                 // Các phần xử lý sự kiện nhấp chuột tương tự vẫn được giữ nguyên
	                 if ($('.product_slider_nav_left').length) {
	                     $('.product_slider_nav_left').on('click', function () {
	                         slider1.trigger('prev.owl.carousel');
	                     });
	                 }

	                 if ($('.product_slider_nav_right').length) {
	                     $('.product_slider_nav_right').on('click', function () {
	                         slider1.trigger('next.owl.carousel');
	                     });
	                 }
	             }
	         }
	         
	 	       
	 	        
	 	        $(document).on('change', '.modal_size input[type="radio"], .widget_color input[type="radio"]', function () {
	 	        	if (isSizeAndColorSelected()) {
	                     callAdditionalAjax();
	                 }
	 	        });
	 	       var isSizeSelected = false;
	 	       var isColorSelected = false;
	 	        function isSizeAndColorSelected() {
	 	        	  
	 	             isSizeSelected = $('.modal_size input[type="radio"]:checked').length > 0;
	 	             isColorSelected = $('.widget_color input[type="radio"]:checked').length > 0;

	 	            return isSizeSelected && isColorSelected;
	 	        }

	 	        var globalVariantId;
	 	        var quantitySelect
	 	        function callAdditionalAjax() {
	 	            if (!isSizeAndColorSelected()) {
	 	                alert("Please select size and color before adding to cart.");
	 	                return;
	 	            }

	 	            var productId = $('.productId').text();
	 	            var sizeId = $('.modal_size input[type="radio"]:checked').val();
	 	            var colorId = $('.widget_color input[type="radio"]:checked').val();

	 	            $.ajax({
	 	                type: 'GET',
	 	                url: '/variant/getVariant',
	 	                data: { 
	 	                    productId: productId,
	 	                    sizeId: sizeId,
	 	                    colorId: colorId
	 	                },
	 	                success: function (response) {
	 	                    if (response.length > 0) {
	 	                        var firstVariant = response[0];
	 	                        var currentPrice = firstVariant.currentPrice;
	 	                        quantitySelect = firstVariant.quantity;
	 	                        globalVariantId = firstVariant.id;

	 	                        var formattedPrice = firstVariant.price ? firstVariant.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) : '';

	 	                        if (currentPrice != null) {
	 	                            var formattedCurentPrice = currentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

	 	                            $('.content_price span').text(formattedCurentPrice);
	 	                            $('.content_price span.old-price').text(formattedPrice);

	 	                        } else {
	 	                            $('.content_price span').text(formattedPrice);
	 	                        }

	 	                        // Cập nhật giá trị quantity
	 	                        var quantityText = quantitySelect + ' items';
	 	                        $('.product_stock p').text(quantityText);

	 	                        // Cập nhật giá trị của ô input
	 	                        var quantityInput = $('#quantity-single-product');
	 	                        var enteredValue = parseInt(quantityInput.val(), 10);
	 	                        if (enteredValue > quantitySelect) {
	 	                            quantityInput.val(quantitySelect);
	 	                        }
	 	                    }
	 	                },
	 	                error: function (error) {
	 	                    console.error(error);
	 	                }
	 	            });
	 	        }




	 //////////////////////////////////////////////
	 	        function saveColorOptionsState() {
	 	            var optionsState = [];
	 	            $('.widget_color input[type="radio"]').each(function () {
	 	                optionsState.push({
	 	                    id: $(this).attr('id'),
	 	                    checked: $(this).prop('checked')
	 	                });
	 	            });
	 	            return optionsState;
	 	        }

	 	        function restoreColorOptionsState(optionsState) {
	 	            optionsState.forEach(function (option) {
	 	                $('#' + option.id).prop('checked', option.checked);
	 	            });
	 	        }

	 	        function saveSizeOptionsState() {
	 	            var optionsState = [];
	 	            $('.size-widget input[type="radio"]').each(function () {
	 	                optionsState.push({
	 	                    id: $(this).attr('id'),
	 	                    checked: $(this).prop('checked')
	 	                });
	 	            });
	 	            return optionsState;
	 	        }

	 	        function restoreSizeOptionsState(optionsState) {
	 	            optionsState.forEach(function (option) {
	 	                $('#' + option.id).prop('checked', option.checked);
	 	            });
	 	        }
	 	        
	 	        ////////////////
	 	       

	 	        
	 	        var quantity;
	 			var csrfToken;
	 		    function addToCartInSingleProduct(event) {
					 var quantity = document.getElementById("quantity-single-product").value;
					  csrfToken = Cookies.get('XSRF-TOKEN');
					  
					  // Kiểm tra xem token có tồn tại hay không
					  if (typeof globalVariantId === 'undefined' || globalVariantId === null) {
					        $('.error-text.text-danger').text("Please select size and color before adding to cart.");
					        return;
					   }
					  $('.error-text.text-danger').text("");
					    // Tạo đối tượng dữ liệu để gửi qua Ajax
					    var data = {
					      variantId: globalVariantId,
					      quantity : quantity
					    }
					    // Gửi Ajax request
					    $.ajax({
					      url: '/cart/save',
					      type: 'POST',
					      contentType: "application/json",
					      data: JSON.stringify(data),
					      headers: {
					        'X-XSRF-TOKEN': csrfToken
					      },
					      success: function(response,textStatus, jqXHR) {
					    	  toastr.success(response);
					      },
					      error: function(xhr, status, error) {
						       alert("The system has errors!");
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
         				 
         			      // Đăng xuất thành công, ẩn class "user"
         			      //var userElement = document.getElementsByClassName("ht-user")[0];
         			      //var userName = document.getElementsByClassName("userName")[0];
         			      
         			      //userElement.classList.add("hide");
         			      //userName.classList.add("hide");
         			   
         			    } 
         		  };
         		
         		  xhr.send(new FormData(event.target));
         		});
