 $(document).ready(function () {
                	    loadCartData();
                	 
                	});
                  var quantityInput;
                  
                	function loadCartData() {
                	    $.ajax({
                	        url: '/cart/findAll', // Đường dẫn của endpoint Spring Boot
                	        type: 'GET',
                	        success: function (data) {
                	            console.log(data);
                	            var cartBody = $('#cartBody');
                	            cartBody.empty();

                	            $.each(data, function (index, item) {
                	                var imagesHtml = "";
                	                $.each(item.productResponseDto.images, function (imgIndex, image) {
                	                	if (item.productResponseDto.images[imgIndex].isDefault == true) {
                	                    	imagesHtml += `<a href="#"><img src="../upload/${image.inmageForSave}?v=1" alt=""></a>`;
                	                	}
                	                });
                	                quantityInput = item.quantity;
                	                var row = `
                	                    <tr>
                	                        <td class="product_checkbox">
                	                            <input type="checkbox" class="itemCheckbox" data-cart-id="${item.cartId}">
                	                        </td>
                	                        <td class="product_thumb">${imagesHtml}</td>
                	                        <td class="quantity d-none"><a href="#">${item.productResponseDto.quantity}</a></td>
                	                        <td class="product_name"><a href="#">${item.productResponseDto.title}</a></td>
                	                        <td class="product_size"><a href="#">${item.variantResponseDto.size}</a></td>
                	                        <td class="product_color"><a href="#">${item.variantResponseDto.color}</a></td>
                	                        <td class="product-price">${item.variantResponseDto.currentPrice != null ? (item.variantResponseDto.currentPrice).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) : (item.variantResponseDto.price).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</td>
                	                        <td class="product_quantity">
                	                            <div class="col-md-2 text-center">
                	                                <div class="input-group">
                	                                    <div class="input-group-prepend">
                	                                        <button class="btn btn-outline-secondary decreaseQty" type="button" data-cart-id="${item.cartId}">-</button>
                	                                    </div>
                	                                    <input type="text" class="form-control text-center quantity-input" value="${item.quantity}"  readonly>
                	                                    <div class="input-group-prepend">
                	                                        <button class="btn btn-outline-secondary increaseQty" type="button" data-cart-id="${item.cartId}">+</button>
                	                                    </div>
                	                                </div>
                	                            </div>
                	                        </td>
                	                        <td class="product_total">${(item.total).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</td>
                	                        <td class="product_remove">
                	                            <a href="#" onclick="deleteCartItem(${item.cartId})"><i class="fa fa-trash-o"></i></a>
                	                        </td>
                	                    </tr>`;
                	                cartBody.append(row);
                	                if (quantityInput === item.variantResponseDto.quantity) {
                	                    $('[data-cart-id="' + item.cartId + '"]').closest('.input-group').find('.increaseQty').prop('disabled', true);
                	                }
                	                 if(item.variantResponseDto.quantity < item.quantity){
                	                	updateQuantity(item.cartId, item.variantResponseDto.quantity);
                	                } 
                	            });

                	            $('.increaseQty').on('click', function() {
                	                var cartId = $(this).data('cart-id');
                	                var quantityInput = $(this).closest('.input-group').find('.quantity-input');
                	                var currentQuantity = parseInt(quantityInput.val());
                	                quantityInput.val(currentQuantity + 1); 
                	                updateQuantity(cartId, currentQuantity + 1);
                	            });

                	            $('.decreaseQty').on('click', function() {
                	                var cartId = $(this).data('cart-id');
                	                var quantityInput = $(this).closest('.input-group').find('.quantity-input');
                	                var currentQuantity = parseInt(quantityInput.val());
                	                if (currentQuantity > 1) {
                	                    quantityInput.val(currentQuantity - 1);
                	                    updateQuantity(cartId, currentQuantity - 1);
                	                }
                	            });

                	            $('.itemCheckbox').on('change', function() {
                	                calculateTotalCost();
                	            });

                	            $('#selectAllCheckbox').on('change', function() {
                	                var selectAllChecked = this.checked;
                	                $('.itemCheckbox').each(function() {
                	                    this.checked = selectAllChecked;
                	                });
                	                calculateTotalCost();
                	            });
                	        },
                	        error: function (error) {
                	            console.error('Error loading data:', error);
                	        }
                	    });
                	}
                  
                  console.log("qt: ", quantityInput);

                	 function calculateTotalCost() {
                	    var checkboxes = document.getElementsByClassName('itemCheckbox');
                	    var totalCostSelected = 0;
                	    var totalCostAll = 0;
                	    var selectedProductsHTML = '';

                	    for (var i = 0; i < checkboxes.length; i++) {
                	        var checkbox = checkboxes[i];
                	        var row = checkbox.closest('tr');
                	        var priceElement = row.querySelector('.product-price');
                	        var priceText = priceElement.innerText.replace(/\D/g, ''); // Loại bỏ tất cả các ký tự không phải là số
                	        var price = Number(priceText);
                	        var quantity = parseInt(row.querySelector('.quantity-input').value);
                	        totalCostAll += price * quantity;

                	        if (checkbox.checked) {
                	            totalCostSelected += price * quantity;
                	            // Tạo HTML để hiển thị thông tin sản phẩm đã chọn
                	           // selectedProductsHTML += '<p>'+'Quantity: ' + quantity + ' - Price: ' + totalCostSelected.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) + '</p>';
                	        }
                	    }

                	    var totalCostSelectedDisplay = document.getElementById('totalCostAmount');
                	    if (totalCostSelectedDisplay) {
                	        totalCostSelectedDisplay.textContent = totalCostSelected.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
                	    }

                	    var totalCostAllDisplay = document.getElementById('totalCostAllAmount');
                	    if (totalCostAllDisplay) {
                	        totalCostAllDisplay.textContent = totalCostAll.toFixed(2);
                	    }

                	    var selectedProductsDiv = document.getElementById('selectedProducts');
                	    if (selectedProductsDiv) {
                	        // Cập nhật nội dung HTML của phần tử selectedProducts
                	        selectedProductsDiv.innerHTML = selectedProductsHTML;
                	    }
                	} 


                	 function selectAllItems() {
                	        var checkboxes = document.getElementsByClassName('itemCheckbox');
                	        var selectAllButton = document.getElementById('selectAllItemsButton');
                	        var areAllChecked = true;

                	        // Kiểm tra xem có checkbox nào chưa được chọn không
                	        for (var i = 0; i < checkboxes.length; i++) {
                	            if (!checkboxes[i].checked) {
                	                areAllChecked = false;
                	                break;
                	            }
                	        }

                	        // Nếu có checkbox chưa được chọn, chọn tất cả các checkbox
                	        for (var j = 0; j < checkboxes.length; j++) {
                	            checkboxes[j].checked = !areAllChecked;
                	        }

                	        // Cập nhật trạng thái và văn bản của nút
                	        if (!areAllChecked) {
                	            selectAllButton.textContent = 'Select All';
                	        } else {
                	            selectAllButton.textContent = 'Select All';
                	        }
                	        
                	        //calculateTotalCost();
                	       
                	    }
                	 

                	 
                	 var csrfToken;
                  function updateQuantity(cartId, quantity) {
                	  csrfToken = Cookies.get('XSRF-TOKEN');
                	    $.ajax({
                	        url: '/cart/updateQuantity',
                	        type: 'POST',
                	        data: {
                	            cartId: cartId,
                	            quantity: quantity
                	        },
                	        headers: {
    					        'X-XSRF-TOKEN': csrfToken
    					      },
                	        success: function (response) {
                	            loadCartData();
                	        },
                	        error: function (error) {
                	            // Hiển thị thông báo lỗi cho người dùng
                	            alert("Cập nhật số lượng thất bại: " + error.responseJSON.message); // Hiển thị thông báo hoặc cập nhật giao diện để thông báo việc thất bại
                	        }
                	    });
                	} 

                  function deleteCartItem(cartId) {
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
                	                type: 'POST',
                	                url: '/cart/deleteCart',  // Đảm bảo rằng đường dẫn URL đúng
                	                data: {
                	                    cartId: cartId
                	                },
                	                headers: {
            					        'X-XSRF-TOKEN': csrfToken
            					      },
                	                success: function (response) {
                	                    console.log(response);
                	                    // Refresh your cart data or update the UI as needed
                	                    loadCartData();
                	                    Swal.fire('Deleted!', 'Your cart has been deleted.', 'success');
                	                    document.getElementById('totalCostAmount').textContent = '0';
                	                },
                	                error: function (jqXHR, textStatus, errorThrown) {
                	                    if (jqXHR.status === 400) {
                	                        var error = jqXHR.responseJSON || jqXHR.responseText;
                	                        Swal.fire('Error!', error, 'error');
                	                    }
                	                }
                	            });
                	        } else if (result.dismiss === Swal.DismissReason.cancel) {
                	            Swal.fire('Canceled!', '', 'error');
                	        }
                	    });
                	}
                  
                  function deleteAllCart() {
                	  csrfToken = Cookies.get('XSRF-TOKEN');
                	   Swal.fire({
               	        title: 'Are you sure you want to delete all cart?',
               	        text: 'This action cannot be undone!',
               	        icon: 'warning',
               	        showCancelButton: true,
               	        confirmButtonText: 'Yes',
               	        cancelButtonText: 'No'
               	    }).then((result) => {
               	        if (result.isConfirmed) {
                	    $.ajax({
                	        type: 'POST',
                	        url: '/cart/deleteAllCart', // Đường dẫn tới endpoint xóa tất cả giỏ hàng
                	        headers: {
    					        'X-XSRF-TOKEN': csrfToken
    					      },
                	        success: function (response) {
                	            console.log(response);
                	            loadCartData();
                	            Swal.fire('Deleted!', 'All items have been removed from the cart.', 'success');
                	            document.getElementById('totalCostAmount').textContent = '0';
                	        },
        	                error: function (jqXHR, textStatus, errorThrown) {
        	                    if (jqXHR.status === 400) {
        	                        var error = jqXHR.responseJSON || jqXHR.responseText;
        	                        Swal.fire('Error!', error, 'error');
        	                    }
        	                }
        	            });
        	        } else if (result.dismiss === Swal.DismissReason.cancel) {
        	            Swal.fire('Canceled!', '', 'error');
        	        }
        	    });
                	}

                  function openCenterPopup() {
                      document.getElementById('centerPopup').style.display = 'block';
                      document.removeEventListener('click', outsideClickHandler);
                  }

                  function closeCenterPopup() {
                      document.getElementById('centerPopup').style.display = 'none';
                      document.addEventListener('click', outsideClickHandler);
                  }
                  function copyToClipboard() {
                      const predefinedText = document.getElementById('predefinedText').innerText;
                      const textarea = document.createElement('textarea');
                      textarea.value = predefinedText;
                      document.body.appendChild(textarea);
                      textarea.select();
                      document.execCommand('copy');
                      document.body.removeChild(textarea);
                      alert('Đã sao chép vocher');
                  }

                ///// promotion
                loadPromotion()
                function loadPromotion() {
                    $.ajax({
                        url: '/promotion/getProductPage',
                        type: 'GET',
                        success: function (response) {
                            var centerPopup = $("#centerPopup");

                            //Xóa nội dung hiện có
                            centerPopup.empty();

                            if (response.promotionResponseDtos && response.promotionResponseDtos.length > 0) {
                                // Lặp lại dữ liệu khuyến mãi và thêm vào centerPopup
                                $.each(response.promotionResponseDtos, function (index, promotion) {
                                // Kiểm tra xem chương trình khuyến mãi có đang hoạt động không
                                if (promotion.active) {
                                	var imagesHtml = `<img src="https://cdn-icons-png.flaticon.com/512/3258/3258499.png" alt="Promotion Image">`;
 									// Giả sử bạn có hình ảnh cho mỗi chương trình khuyến mãi
 									
                                    // Lặp qua các hình ảnh quảng cáo
                                    $.each(promotion.images, function (imgIndex, image) {
                                        imagesHtml += `<img src="${image.url}" alt="Promotion Image">`;
                                    });

                                    // Xây dựng promotion HTML
                                                                    
                                    var promotionHtml = `
                                    	<div id="closeButton" onclick="closeCenterPopup()">X</div>
                                        <div class="voucher" id="Promotion${promotion.id}">
                                            <div class="margin-title">
                                                <div class="title-vocher">
                                                    <h4>Voucher ${promotion.name}</h4>
                                                </div>
                                                ${imagesHtml}
                                            </div>
                                            <div class="voucher-details">
                                            <button class="review-button" data-promotion-id="${promotion.id}" onclick="applyPromotion('${promotion.couponCode}')">Coppy code</button>
                                                <div class="voucher-header">Discount code ${promotion.name} applies to entire order </div>
                                                <ul>                                                
                                                    <li>Discount Value: ${promotion.discountType === 1 ? `${promotion.discountValue}%` : `${promotion.discountValue.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}`}</li>
                                                    <li>Max Value: ${promotion.maxValue.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</li>                                                    
                                                    <li>Expired Date: ${promotion.expiredDate}</li>
                                                    <li >Coupon Code: ${promotion.couponCode}</li> <!-- Add this line for Coupon Code -->
                                                </ul>
                                            </div>
                                        </div>
                                    `;

                                    // Nối HTML voucher vào centerPopup
                                    centerPopup.append(promotionHtml);
                                }
                            });
                            } else {
                                // Nếu không có voucher, hiển thị ảnh full
                                centerPopup.html('<div id="closeButton" onclick="closeCenterPopup()">X</div><img src="https://images.fpt.shop/unsafe/filters:quality(90)/fptshop.com.vn/uploads/images/2015/Tin-Tuc/ThuVTK/giam8phantram_1.png" alt="Chưa có voucher nào"><div><p style="font-size:20px; text-align: center;font-weight: bold;">Cùng chờ đón mùa sale lớn giáng sinh</p</div>');
								
                            }

                            //Lặp lại dữ liệu khuyến mãi và thêm vào centerPopup
                            
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.error('Error loading promotion data:', errorThrown);
                        }
                    });
                }

                                        
               //Thêm chức năng này để xử lý việc sao chép mã giảm giá
               function applyPromotion(couponCode) {
                   //Sử dụng tham số couponCode được cung cấp để sao chép vào bảng nhớ tạm
                   const textarea = document.createElement('textarea');
                   textarea.value = couponCode;
                   document.body.appendChild(textarea);
                   textarea.select();
                   document.execCommand('copy');
                   document.body.removeChild(textarea);

                   //Hiển thị thông báo hoặc thực hiện các hành động bổ sung
                   alert(`Coupon Code "${couponCode}" has been copied to the clipboard.`);
             	}


                  function checkoutSelectedItems() {
                	    var selectedCartIds = [];
                	    var checkboxes = document.querySelectorAll('.itemCheckbox:checked');

                	    checkboxes.forEach(function(checkbox) {
                	        selectedCartIds.push(checkbox.getAttribute('data-cart-id'));
                	    });

                	    if (selectedCartIds.length > 0) {
                	        // Chuyển hướng đến trang thanh toán, truyền danh sách cartIds được chọn
                	        var checkoutUrl = '/userOrder/checkout?cartIdList=' + selectedCartIds.join(',');
                	        window.location.href = checkoutUrl;
                	    } else {
                	        // Nếu không có mục nào được chọn, hiển thị thông báo hoặc thực hiện hành động khác
                	        alert('Please select items before checkout.');
                	    }
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