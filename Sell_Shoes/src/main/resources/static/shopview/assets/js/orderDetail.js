function toggleProfile() {
	            var profileLink = document.getElementById('profileLink');
	            profileLink.classList.add('active');
	       }
		  
		  function openModal() {
              document.getElementById("modal").style.display = "block";
          }

          function closeModal() {
              document.getElementById("modal").style.display = "none";
          }
          
          const ratingStars = document.querySelectorAll('.rating-star');
          const ratingDescription = document.querySelector('.rating-description');

          ratingStars.forEach((star, index) => {
              star.addEventListener('click', () => {
                  // Remove active class from all stars
                  ratingStars.forEach((star) => {
                      star.classList.remove('active');
                  });

                  // Add active class to clicked star and previous stars
                  for (let i = 0; i <= index; i++) {
                      ratingStars[i].classList.add('active');
                  }

                  // Update rating description based on the selected rating
                  switch (index + 1) {
                      case 1:
                          ratingDescription.textContent = 'Bad';
                          break;
                      case 2:
                          ratingDescription.textContent = 'Unsatisfied';
                          break;
                      case 3:
                          ratingDescription.textContent = 'Normal';
                          break;
                      case 4:
                          ratingDescription.textContent = 'Satisfied';
                          break;
                      case 5:
                          ratingDescription.textContent = 'Great';
                          break;
                      default:
                          ratingDescription.textContent = '';
                          break;
                  }
              });
          });
          
          var thumbnailOrder;
        	var id;
        	function redirectToDetail(event) {
      		  thumbnailOrder = event.currentTarget.closest('.card');
      		 
      		  id = thumbnailOrder.querySelector('.order-id').textContent;
      		 
              window.location.href = '/orderDetails/get?orderId='+ id ;
          }
		  
          loadData(0,0,"");
  		//get data for table
  		function loadData(page,status,key) {
  			var orderWrap = ".order-wrap";
  		    $.ajax({
  		      url: '/order/user/getOrderPage',
  		      type: 'GET',
  		      dataType: 'json',
  		      data: {page : page,status: status,keyWord:key},
  		      success: function(orderPageResponseDto) {
  		        //clear table
  		        $(orderWrap).empty();
  				
  		        //add record
  		        $.each(orderPageResponseDto.orderResponseDtos, function(index, order) {
  					
  					var hiXKxx = $('<div>').addClass('hiXKxx');
  					var card = $('<div>').addClass('card');
  					
  					//Cart
  					//1
  					//KrPQEI
  					var KrPQEI = $('<div>').addClass('KrPQEI');
  					KrPQEI.append($('<div>').addClass('qCUYY8').append($('<div>').addClass('_9Ro5mP').text("Puu Store")));
  					
  					
  					var EQko8g = $('<div>').addClass('EQko8g');
  					var KmBIg2 = $('<a>').addClass('KmBIg2');
  					KmBIg2.href='#';
  					var message;
  					if(order.status==1){
  						message="Order is awaiting confirmation..";
  					}else if(order.status==2){
  						message="The order has been cancelled.";
  					}else if(order.status==3){
  						message="Order has been confirmed.";
  					}else if(order.status==4){
  						message="The order has been delivered successfully.";
  					}
  					var nkmfr2 = $('<span>').addClass('nkmfr2');
  					//nkmfr2.append($('<i>').addClass('fa fa-truck').attr('aria-hidden', true));
  					nkmfr2.append("<i class='fa fa-truck' aria-hidden='true'></i>");
  					
  					nkmfr2.text(message).click(redirectToDetail);
  					KmBIg2.append(nkmfr2);
  					EQko8g.append(KmBIg2);
  					KrPQEI.append(EQko8g)
  					//card add KrPQEI
  					card.append(KrPQEI);
  					
  					//2
  					//card-body
  					var cardBody = $('<div>').addClass('card-body');
  					//add id
  					cardBody.append($('<div>').text(order.id).hide().addClass('order-id'));
  					//row(product)
  					
  					$.each(order.orderDetaitsResponseDtos, function(index, orderDetails) {
  						var row = $('<div>').addClass('row').click(redirectToDetail);
  						row.append('<hr>')
  						//col-md-2
  						var subRow = $('<div>').addClass('col-md-2');
  						var shopeeImageWrapper = $('<div>').addClass('shopee-image__wrapper');
  						var shopeeImagePlaceHolder = $('<div>').addClass('shopee-image__place-holder');
  						//var imageElement = $('<img>').addClass("shopee-image__content--blur").attr('src', '../upload/' + orderDetails.imageForSave);
  						 shopeeImagePlaceHolder.append($('<img>').addClass("shopee-image__content--blur").attr('src', '/upload/' + orderDetails.imageForSave));
  						 shopeeImageWrapper.append(shopeeImagePlaceHolder);
  						 subRow.append(shopeeImageWrapper);
  						 row.append(subRow);
  						 
  						 //col-md-8
  						var subRow2 = $('<div>').addClass('col-md-8');
  						var _7uZf6Q = $('<div>').addClass('_7uZf6Q');
  						
  						var iJlxsT = $('<div>').append($('<div>').addClass('iJlxsT').append($('<span>').attr({
  		            	'data-id': orderDetails.variantResponseDto.id

  		            }).addClass('x5GTyN').text(orderDetails.variantResponseDto.title)));
  						_7uZf6Q.append(iJlxsT);
  						
  						var div = $('<div>');
  						if(orderDetails.variantResponseDto.sizeId != "" || orderDetails.variantResponseDto.color != ""){
  							if(orderDetails.variantResponseDto.size!="" && orderDetails.variantResponseDto.color!=""){
  								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.variantResponseDto.size + ' | ' +orderDetails.variantResponseDto.color));
  							}else if(orderDetails.variantResponseDto.size!="" && orderDetails.variantResponseDto.color==""){
  								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.variantResponseDto.size));
  							}else if(orderDetails.variantResponseDto.size=="" && orderDetails.variantResponseDto.color!=""){
  								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.variantResponseDto.color));
  							}
  						}
  						 
  						 div.append( $('<div>').addClass('_3F1-5M').text('x'+orderDetails.number));
  						
  						 _7uZf6Q.append(div);
  						 
  						 subRow2.append(_7uZf6Q);
  						 row.append(subRow2);
  						 
  						 //sub3
  						 var subRow3 = $('<div>').addClass('col-md-2');
  						 var textCenter = $('<div>').addClass('text-center').css('margin-top', '60px');
  						 var rjqzk1 = $('<div>').addClass('rjqzk1');
  						 formattedPrice =orderDetails.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
  						 if(orderDetails.curentPrice!=0){
  							 formattedCurrentPrice =orderDetails.curentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
  							 
  							 rjqzk1.append($('<span>').addClass('j2En5+').text(formattedCurrentPrice));
  							 rjqzk1.append($('<span>').addClass('-x3Dqh OkfGBc').text(formattedPrice));
  						 }else{
  							 rjqzk1.append($('<span>').addClass('-x3Dqh OkfGBc').text(formattedPrice));
  						 }
  						 
  						 textCenter.append(rjqzk1);
  						 subRow3.append(textCenter);
  						 row.append(subRow3);
  						 
  						 cardBody.append(row);
  					});
  					
  					
  					
  					 
  					 //kvXy0c
  					 formattedTotal =order.totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
  					var kvXy0c = $('<div>').addClass('kvXy0c');
  					var div78s2g= $('<div>').addClass('-78s2g').append($('<div>').addClass('_0NMXyN').text('Total: ')).append($('<div>').addClass('DeWpya').text(formattedTotal).click(redirectToDetail));
  					kvXy0c.append(div78s2g);
  					cardBody.append(kvXy0c);
  					
  					//AM4Cxf
  					var AM4Cxf = $('<div>').addClass('AM4Cxf');
  					//var qtUncs = $('<div>').addClass('qtUncs').append($('<span>').addClass('_0NMXyN').text('Không nhận được đánh giá'));
  					AM4Cxf.append(  $('<div>').addClass('_8ex9dW').text('7 days return and exchange'));
  					
  					var EOjXew_4IR9IT = $('<div>').addClass('EOjXew _4IR9IT');
  					if(order.status==1){
  						var PF0AU = $('<div>').addClass('PF0-AU').append($('<button>').attr({
  		            	'data-order-id': order.id,
  		            	'data-update-status' : 2,
  		                'data-toggle': 'modal',
  		                'data-target': '#refuseOrder'
  		            }).addClass('stardust-button stardust-button--primary WgYvse cancelOrderBtn').text("Cancel")
  		            
  		            	.on('click', function(event) {
  						  // Xử lý khi phần tử được click
  						  cancleOrder(event);
  						})
  		            );
  						EOjXew_4IR9IT.append(PF0AU);
  					}else if(order.status==4 || order.status==2){
  						var PF0AU = $('<div>').attr({
  		            	'data-order-id': order.id
  		            }).addClass('PF0-AU').append($('<button>').attr({
  		            	'data-order-id': order.id
  		            }).addClass('stardust-button stardust-button--primary WgYvse buy-again-btn').text("Buy Again").on('click', function(event) {
  						  // Xử lý khi phần tử được click
  						  addToCart(event);
  						}));
  					
  						EOjXew_4IR9IT.append(PF0AU);
  					}
  					
  					
  					var PgtIur= $('<div>').addClass('PgtIur').append($('<button>').addClass('stardust-button stardust-button--primary WgYvse btn-contact mr-10').text("Contact Seller"));
  					EOjXew_4IR9IT.append(PgtIur);
  					
  					if((order.status==4 || order.status==2) && order.rated == false){
  						EOjXew_4IR9IT.append($('<button>').attr({
  		            	'data-order-id': order.id
  		            }).addClass('stardust-button stardust-button--primary WgYvse rating-btn ').text("Rating").on('click', function(event) {
  						  // Xử lý khi phần tử được click
  						  openModal(event);
  						}));
  					}else if((order.status==4 || order.status==2) && order.rated == true){
  						EOjXew_4IR9IT.append($('<button>').attr({
  		            	'data-order-id': order.id
  		            }).addClass('stardust-button stardust-button--primary WgYvse rating-btn ').text("View rating").on('click', function(event) {
  						  // Xử lý khi phần tử được click
  						  viewRating(event);
  						}));
  					}
  					AM4Cxf.append(EOjXew_4IR9IT);
  					cardBody.append(AM4Cxf);
  					card.append(cardBody);
  					hiXKxx.append(card);
  					$(orderWrap).append(hiXKxx);
  					
  		        });
  		         pagination = '';
  		         currentPage = orderPageResponseDto.currentPage;
  		         totalPages = orderPageResponseDto.totalPages;
  		         size = orderPageResponseDto.size;
  		        //create pagination 
  		     	// createPagination("#paging",pagination,currentPage,totalPages,status,key );
  		      },
  		      error: function(){}})}
  		
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