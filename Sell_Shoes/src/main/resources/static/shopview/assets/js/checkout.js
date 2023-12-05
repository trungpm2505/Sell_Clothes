var inputList = [ 'fullName','phone_Number','address','province'];

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
	 
        var confirmedPrice = 0;
		var promotionId=0;
	 	var selectedProvince = null;
		var selectedDistrict = null;
		var selectedward = null; 

		function formatCurrency(value) {
		    return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		}

		// Áp dụng định dạng tiền tệ cho các giá trị giày
		document.querySelectorAll("#checkoutBody td:nth-child(6)").forEach(function(element) {
		    var value = parseFloat(element.textContent);
		    if (!isNaN(value)) {
		        element.textContent = formatCurrency(value);
		    }
		});
		var originalTotalValue = document.getElementById("originalTotalValue").value;
		var totalElement = document.getElementById("total");

		if (totalElement && !isNaN(originalTotalValue)) {
		    var formattedTotal = formatCurrency(parseFloat(originalTotalValue));
		    totalElement.innerHTML = `<strong>${formattedTotal}</strong>`;

		}
	
	     // Sử dụng jQuery để lắng nghe sự kiện khi giá trị của select thay đổi
	         $('#province').on('change', function() {
	        	selectedProvince =   $(this).find("option:selected").text(); // Lấy văn bản của lựa chọn đã chọn
	            console.log('Tên tỉnh được chọn:', selectedProvince);
	           
	        });
	        $('#district').on('change', function() {
	        	selectedDistrict = $(this).find("option:selected").text();
	            console.log('Tên quận huyện được chọn:', selectedDistrict);
	           
	        });
	        $('#ward').on('change', function() {
	        	selectedward = $(this).find("option:selected").text(); 
	            console.log('Tên phường xã được chọn:', selectedward);
	          
	        }); 

	        var csrfToken;
		$('#checkoutButton').on('click', function() {
			 csrfToken = Cookies.get('XSRF-TOKEN');
						
		    var formData = {
		        fullName: $('#fullName').val(),
		        phone_Number: $('#phone_Number').val(),
		        address:  $('#address').val(),
		        province : selectedProvince ,
		        district : selectedDistrict ,
		        ward : selectedward , 
		        note: $('#note').val(),
		        confirmedPrice: confirmedPrice,
		        promotionId : promotionId, 
		        cartIds: [] // Khởi tạo mảng cartIds
		    };

		    // Lặp qua các phần tử có class 'cartId' và thêm giá trị vào mảng 'cartIds'
		    $('.cartId').each(function() {
		        formData.cartIds.push($(this).val());
		    });
		    if (!selectedProvince || selectedProvince === 'chọn tỉnh') {
		        // Nếu không có tỉnh/thành phố được chọn, hiển thị thông báo lỗi
		        $("#province-error").text("Please select province/city!");
		        return; // Dừng việc tiếp tục thực hiện thanh toán
		    } else {
		        // Nếu tỉnh/thành phố đã được chọn, xóa thông báo lỗi (nếu có)
		        $("#province-error").text("");
		    }
		  if (!selectedDistrict || selectedDistrict === 'chọn quận huyện') {		
		        $("#district-error").text("Please district!");
		        return; 
		    } else {
		        $("#district-error").text("");
		    }
		  if (!selectedward || selectedward === 'chọn phường xã') {		      
		        $("#ward-error").text("Please commune!");
		        return; 
		    } else {		       
		        $("#ward-error").text("");
		    }
		    // Gửi dữ liệu đặt hàng lên server
		    $.ajax({
		        type: 'POST',
		        url: '/userOrder/addOrder',
		        contentType: 'application/json',
		        data: JSON.stringify(formData),
		        headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
		        success: function(response) {
		            console.log('Đặt hàng thành công!', response);		           
		            var totalElement = document.getElementById("total");
		            window.location.href = "/userOrder/order-success";
		        },
		    	error: function(jqXHR, textStatus, errorThrown){
				 	if (jqXHR.status === 400) {
		      	       var errors = jqXHR.responseJSON;
		      	        if (errors.hasOwnProperty("bindingErrors")) {
		      	            var bindingErrors = errors["bindingErrors"];
		      	            for (var i = 0; i < bindingErrors.length; i++) {
		      	                var error = bindingErrors[i];
							    // Hiển thị thông báo lỗi trong tag ID đã tạo
							    $("#" + error.field + "-error").text(error.defaultMessage);
		      	            }
		      	        }
		      	    }else{
						   alert("Sorry! The system have errors!") 
					}
				 	 	
				}
		    });
		});


		document.getElementById("applyCouponBtn").addEventListener("click", function() {
		    var couponCode = document.getElementById("coupon_code").value;
		    var originalTotalValue = document.getElementById("originalTotalValue").value; // lấy giá gốc

		    $.ajax({
		        type: 'GET',
		        url: '/userOrder/applyPromotionByCode',
		        data: { coupon_code: couponCode }, // Truyền mã giảm giá qua tham số truy vấn
		        success: function(response) {
		            console.log('Áp dụng mã giảm giá thành công!', response);
		            $("#couponCode-error").text("");
		            promotionId = response.id;

		            // Hiển thị giá trị giảm giá và cập nhật confirmedPrice
		            if (response.discountType == 2) {
		            	console.log("goc")
		                document.getElementById("discountTypeDisplay").innerHTML = response.discountValue.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		            
		                var discountedPrice = originalTotalValue - response.discountValue;
		                updateConfirmedPrice(discountedPrice);
		                updateTotalElement(discountedPrice);
		            } else if (response.discountType === 1) {
		            	console.log("%")

		                var totalRate = parseFloat(originalTotalValue);
		                var discountValue = parseFloat(response.discountValue);

		                if (!isNaN(totalRate) && !isNaN(discountValue)) {
		                    var rate = 0;
		                    var discount = totalRate * discountValue / 100;
			            	console.log(response)

		                    if (discount > response.maxValue) {
		                        rate = totalRate - response.maxValue;
		                        document.getElementById("discountTypeDisplay").innerHTML = response.maxValue.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });;
		                    } else {
		                        rate = totalRate - discount;
		                        document.getElementById("discountTypeDisplay").innerHTML = discount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		                    }

		                    updateConfirmedPrice(rate);
		                    updateTotalElement(rate);
		                } else {
		                    console.error("Invalid input. Please check the values.");
		                }
		            }
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		            if (jqXHR.status === 400) {
		                var errors = jqXHR.responseJSON;
		                if (errors.hasOwnProperty("couponCode")) {
		                    $("#couponCode-error").text(errors["couponCode"]);
		                }
		            } else if (jqXHR.status === 404) {
		                $("#couponCode-error").text("Invalid discount code! Please re-enter.");
		            } else {
		                $("#couponCode-error").text("Xảy ra lỗi. Vui lòng thử lại sau.");
		            }
		        }
		    });
		});

		// Hàm cập nhật confirmedPrice
		function updateConfirmedPrice(price) {
		    confirmedPrice = price;
		}

		// Hàm cập nhật giá trị total
		function updateTotalElement(value) {
			 var formattedValue = formatCurrency(value);
		    document.getElementById("total").innerHTML = formattedValue;		    
		}
		

	//api gọi tỉnh thành
		const host = "https://provinces.open-api.vn/api/";
        var callAPI = (api) => {
            return axios.get(api).then((response) => {
                renderData(response.data, "province");
            });
        };
        callAPI("https://provinces.open-api.vn/api/?depth=1");

        var callApiDistrict = (api) => {
            return axios.get(api).then((response) => {
                renderData(response.data.districts, "district");
            });
        };
        var callApiWard = (api) => {
            return axios.get(api).then((response) => {
                renderData(response.data.wards, "ward");
            });
        };

        var renderData = (array, select) => {
            let row = ' <option disable value="">chọn</option>';
            array.forEach((element) => {
                row += `<option value="${element.code}">${element.name}</option>`;
            });
            document.querySelector("#" + select).innerHTML = row;
        };

        $("#province").change(() => {
            callApiDistrict(host + "p/" + $("#province").val() + "?depth=2");            
        });
        $("#district").change(() => {
            callApiWard(host + "d/" + $("#district").val() + "?depth=2");         
        });


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
    