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
			
		loadData();

	    function loadData() {		
	    	var productId = getParameterByName('productId');
	    	console.log(productId);
	        $.ajax({
	            url: '/product/get?productId=' + productId,
	            type: 'GET',
	            //dataType: 'json',
	            success: function(data) {
	            	// Mảng để lưu trữ các phần tử cần append
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
	            	    .append($('<button>').attr('type', 'submit').attr('id', 'add-to-cart-single').append(
	            	    	            $('<i>').addClass('fa fa-shopping-cart')
	            	    	        ).append(' add to cart')
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


	            },
	            error: function() {
	                // Handle errors here
	            }
	        });
	        
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
	     
	     	//caurosel image
	        initSlider();
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
	                        991: { items: 4 }, // Thay đổi giá trị này từ 5 thành 4
	                        1280: { items: 4 }, // Thay đổi giá trị này từ 5 thành 4
	                        1440: { items: 4 }
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
	        $(document).ready(function () {
	            // Lấy các ảnh nhỏ
	            var thumbnails = $('.thumbnail-image');

	            // Xử lý sự kiện khi hover vào ảnh nhỏ
	            thumbnails.on('mouseover', function () {
	                // Lấy URL của ảnh nhỏ khi hover
	                var newImageSrc = $(this).attr('src');

	                // Thay đổi ảnh lớn khi hover vào ảnh nhỏ
	                $('.main-image').attr('src', newImageSrc);
	            });
	        });
	        
	    }
	    
	    $(document).ready(function () {
	        var colorOptionsState = [];
	        var sizeOptionsState = [];

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
	        
	        $(document).on('change', '.modal_size input[type="radio"], .widget_color input[type="radio"]', function () {
	        	if (isSizeAndColorSelected()) {
                    callAdditionalAjax();
                }
	        });

	        function isSizeAndColorSelected() {
	            var isSizeSelected = $('.modal_size input[type="radio"]:checked').length > 0;
	            var isColorSelected = $('.widget_color input[type="radio"]:checked').length > 0;

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
		        event.stopPropagation();
					 var quantity = document.getElementById("quantity-single-product").value;
					  csrfToken = Cookies.get('XSRF-TOKEN');
					  
					  if (typeof globalVariantId === 'undefined' || globalVariantId === null) {
					        $('.error-text.text-danger').text("Please select size and color before adding to cart.");
					        return;
					   }
					  $('.error-text.text-danger').text("");
					    // Tạo đối tượng dữ liệu để gửi qua Ajax
					   if (csrfToken) {
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
					    	  
						    var contentType = jqXHR.getResponseHeader("Content-Type");
						    if (contentType.includes("text/html")) {
						    	Swal.fire({
						    		  icon: 'error',
						    		  title: 'Error!',
						    		  text: 'Please log in',
						    		  showCancelButton: true,
						    		  confirmButtonText: 'Login',
						    		  cancelButtonText: 'Cancel',
						    		}).then((result) => {
						    		  if (result.isConfirmed) {
						    		    window.location.href = '/login'; 
						    		  }
						    		});

							    } else{
							    	toastr.success(response);
								}
					      },
					      error: function(xhr, status, error) {
						       alert("The system has errors!");
					      }
					    });
					   }
					  } 
		    
		     $("#add-to-cart-single").click(addToCartInSingleProduct);
		     
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
				
	    });
	    var productId;
	    $(document).ready(function() {
	         productId = $('.productId').text();
	    

	//// load rate
			loadRate(0,0);
			
	    });
	    var currentPage ;
		var totalPages ;
		var size;
		var pagination;
		function loadRate(page, rating){
			var productContainer = '.shopee-product-comment-list';
			
			$.ajax({
				url: '/rate/getRateProductPage',
				type: 'GET',
				dataType: 'json',
			    data: {page : page, productId : productId, rateScore : rating},
			    success: function(data) {
			    	 $(productContainer).empty();
			    	 $.each(data.rateResponseDto, function(index, rate) {
			    		 //avatar
			    		 var thumnailRate = $('<div>').addClass('shopee-product-rating');
			    		 var shopeeProductRatingAvatar = $('<div>').addClass('shopee-product-rating__avatar');
			    		 var shopeeAvatar = $('<div>').addClass('shopee-avatar').append($('<img>').attr('src', "https://img.freepik.com/premium-vector/account-icon-user-icon-vector-graphics_292645-552.jpg").addClass('shopee-avatar__img'));
			    		 shopeeProductRatingAvatar.append(shopeeAvatar);
						 thumnailRate.append(shopeeProductRatingAvatar);
						 
						 var shopeeProductRatingMain = $('<div>').addClass('shopee-product-rating__main');
						//fullname
						 shopeeProductRatingMain.append($('<div>').addClass('shopee-product-rating__author-name').text(rate.userResponseDto.fullName));
			    	 	
						//star
						var  repeatPurchaseCon=  $('<div>').addClass('repeat-purchase-con');
						var  shopeeProductRatingRating=  $('<div>').addClass('shopee-product-rating__rating');
						var ratingUl = $('<ul>').addClass('rating rating-with-review-item');

						for(var i = 1; i <= 5; i++){
							
							if(i <= rate.rating){
								ratingUl.append($('<li>').append($('<i>').addClass('fas fa-star yellowStar')))
							}else{
								ratingUl.append($('<li>').append($('<i>').addClass('fas fa-star grayStar')))
							}
							
						}
						shopeeProductRatingRating.append(ratingUl);
						repeatPurchaseCon.append(shopeeProductRatingRating);
						shopeeProductRatingMain.append(repeatPurchaseCon);
						
						//date
						var  shopeeProductRatingTime=  $('<div>').addClass('shopee-product-rating__time').text(rate.createAt);
						shopeeProductRatingMain.append(shopeeProductRatingTime);
						
						//content rate
						var  Rk6V3=  $('<div>').addClass('Rk6V+3').text( rate.content!=''?rate.content : 'The customer did not leave any comments.');
						shopeeProductRatingMain.append(Rk6V3);
						
						//image
						var imageList=$('<div>').addClass('imageList');

						for(var i =0;i<rate.images.length;i++){
							
							  imageList.append($('<img>').attr('src', "/upload/"+rate.images[i].inmageForSave).addClass('thumbnail'))
							
						}
						shopeeProductRatingMain.append(imageList);
						
						//response
						if(rate.responses.length!=0){
							var  fwJamt =  $('<div>').addClass('fwJamt');
							var  response=  $('<div>').addClass('response');
							response.append($('<h5>').addClass('CaoTo').text("DS Phản hồi"));
							response.append($('<hr>'));
							for(var i = 0; i < rate.responses.length; i++){
								  
								if(rate.responses[i].userResponseDto.roleName=="ADMIN"){
									response.append($('<div>').addClass('CaoTou').text("ADELA"));
								}else{
									response.append($('<div>').addClass('CaoTou').text('Name: '+rate.responses[i].userResponseDto.fullName));
								}
								response.append($('<div>').addClass('_2kece8').text(rate.responses[i].content));
								response.append($('<hr>'));
								fwJamt.append(response);
								
							}
							shopeeProductRatingMain.append(fwJamt);
						}
						
						shopeeProductRatingMain.append($('<button>').addClass('btn btn-primary btn-response mt-2').attr({'type': 'button'}).text('Comment').click(function(event) {
						      opentResponse(event);
						 }))
						 shopeeProductRatingMain.append($('<button>').addClass('btn btn-danger btn-close-response mt-2 hide').attr({'type': 'button'}).text("close").click(function(event) {
						      clickCLoseResponse(event);
						 }));
						
						
						 var div = ($('<div>').addClass('d-flex contain-new-response'));
						 div.append($('<textarea>').addClass('content-response hide mt-2'));
						 div.append($('<button>').addClass('btn btn-success btn-save-response hide mt-5 ml-2').attr({'type': 'button','data-rate-id': rate.id}).text("Sent").click(function(event) {
					      saveResponse(event);
					     }));
						 
						 shopeeProductRatingMain.append(div);
						 shopeeProductRatingMain.append($('<div>').addClass('text-danger error-account'));
						 
						 thumnailRate.append(shopeeProductRatingMain);
						  $(productContainer).append(thumnailRate);
						  	
						  
						  pagination = '';
					      currentPage = data.currentPage;
					      totalPages = data.totalPages;
					      size = data.size;
					      //create pagination 
					      createPagination("#paging",pagination,currentPage,totalPages);
					       
							var pagingElements = $(".page-link");
							for (var i = 0; i < pagingElements.length; i++) {
								var pagingElement = pagingElements[i];
								
								  $(pagingElement).click(function() {
								
								    var dataNumber = $(this).attr("data-number");
								    if(parseInt(dataNumber)>=0){
										loadRate(parseInt(dataNumber),rating);
									}
								  });	
							}
						
			    	 });
			    },
			    error: function(){
			    	
			    }
			});
		}
		
		 function createPagination(navId,pagination,currentPage,totalPages){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled li-paging"') + '><a class="page-link" data-number=' + (currentPage > 0 ? (currentPage - 1)  : -1) + ' aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item  li-paging"' : 'class=" li-paging"') + '><a class="page-link" data-number=' + i +'>' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li class="li-paging"><a class="page-link" data-number=' + (totalPages - 1) +' >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? 'class=" li-paging"' : 'class="page-item disabled li-paging"') + '><a class="page-link" data-number=' + (currentPage < totalPages - 1 ? (currentPage + 1) : -1) + ' aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
		
		function opentResponse(event){
			 
			 $(event.target).closest('.shopee-product-rating__main').find('.btn-response').addClass("hide");
			 $(event.target).closest('.shopee-product-rating__main').find('.btn-close-response').removeClass("hide");
			 $(event.target).closest('.shopee-product-rating__main').find('.content-response').removeClass("hide");
			 $(event.target).closest('.shopee-product-rating__main').find('.btn-save-response').removeClass("hide");
		}
		
		function clickCLoseResponse(event){
			$(event.target).closest('.shopee-product-rating__main').find('.btn-response').removeClass("hide");
			$(event.target).closest('.shopee-product-rating__main').find('.btn-close-response').addClass("hide");
		    $(event.target).closest('.shopee-product-rating__main').find('.content-response').addClass("hide");
			$(event.target).closest('.shopee-product-rating__main').find('.btn-save-response').addClass("hide");
			
		}
	    
	    function changeColor(element) {
            // Lấy giá trị của thuộc tính data-rating từ phần tử <a>
             var dataRating = $(element).find('a').data('rating');
            // Xóa class 'selected' từ tất cả các phần tử có class 'product-rating-overview__filter'
            var filters = document.getElementsByClassName('product-rating-overview__filter');
            for (var i = 0; i < filters.length; i++) {
                filters[i].classList.remove('selected');
            }

            element.classList.add('selected');

            loadRate(0, dataRating);
        }
	    
		var csrfToken;
		
	    function saveResponse(event) {

		  csrfToken = Cookies.get('XSRF-TOKEN');
		  
		    // Tạo đối tượng dữ liệu để gửi qua Ajax
		   
		    var data = {
			  rateId: $(event.target).attr('data-rate-id'),
			  content: $(event.target).closest('.shopee-product-rating__main').find('.content-response').val()
			};
		    // Gửi Ajax request
		    $.ajax({
		      url: '/response/add',
		      type: 'POST',
		      contentType: "application/json",
		      data: JSON.stringify(data),
		      headers: {
		        'X-XSRF-TOKEN': csrfToken
		      },
		      success: function(response,textStatus, jqXHR) {
				
			    var contentType = jqXHR.getResponseHeader("Content-Type");
			    if (contentType.includes("text/html")) {
					 
			      // Redirect the user to the login page
			     window.location.href = "/login";
			    } else{
					
                	loadRate(currentPage,$('.search-rating.selected').attr('data-rating'));
				}
		      },
		      error: function(jqXHR, textStatus, errorThrown) {
		    	  if (jqXHR.status === 400) {
		    		  var errorText = jqXHR.responseText;

		              // Hiển thị thông báo lỗi trong thẻ <small>
		              $(".error-account").text(errorText);
		          } else {
		        	  alert("The system has errors!");
		          }
			           
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