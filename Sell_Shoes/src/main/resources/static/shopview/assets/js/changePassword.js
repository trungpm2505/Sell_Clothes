
		function togglePasswsordVisibility1() {
		    const passwordInput1 = document.getElementById('currentPassword');
		    const showPasswordButton = document.getElementById('showPassword1');
		  
		    if (passwordInput1.type === 'password') {
		      passwordInput1.type = 'text';
		      showPasswordButton.classList.remove('fa-eye');
		      showPasswordButton.classList.add('fa-eye-slash');
		    } else {
		      passwordInput1.type = 'password';
		      showPasswordButton.classList.remove('fa-eye-slash');
		      showPasswordButton.classList.add('fa-eye');
		    }
		  } 
		  
		function togglePasswsordVisibility2() {
		    const passwordInput1 = document.getElementById('newPassword');
		    const showPasswordButton = document.getElementById('showPassword2');
		  
		    if (passwordInput1.type === 'password') {
		      passwordInput1.type = 'text';
		      showPasswordButton.classList.remove('fa-eye');
		      showPasswordButton.classList.add('fa-eye-slash');
		    } else {
		      passwordInput1.type = 'password';
		      showPasswordButton.classList.remove('fa-eye-slash');
		      showPasswordButton.classList.add('fa-eye');
		    }
		  }

		  
		  
		  function togglePasswsordVisibility3() {
		    const passwordInput2 = document.getElementById('confirmNewPassword');
		    const showPasswordButton = document.getElementById('showPassword3');
		  
		    if (passwordInput2.type === 'password') {
		      passwordInput2.type = 'text';
		      showPasswordButton.classList.remove('fa-eye');
		      showPasswordButton.classList.add('fa-eye-slash');
		    } else {
		      passwordInput2.type = 'password';
		      showPasswordButton.classList.remove('fa-eye-slash');
		      showPasswordButton.classList.add('fa-eye');
		    }
		  }




		  var csrfToken;
		  var inputList = ['currentPassword','newPassword','confirmNewPassword'];
			
			inputList.forEach(function(inputName) {
				
			  var input = document.getElementById(inputName);
			  var error = document.getElementById(inputName + '-error');
			  input.addEventListener("focus", function() {
				 error.textContent = '';
			  });
			});
					function checkChangePassword() {
						
						csrfToken = Cookies.get('XSRF-TOKEN');
						var currentPassword = document.getElementById("currentPassword").value.trim();
					    var newPassword = document.getElementById("newPassword").value.trim();
					    var confirmNewPassword = document.getElementById('confirmNewPassword').value.trim();
					    
					    var formData = new FormData();
					    formData.append('currentPassword', currentPassword);
					    formData.append('newPassword', newPassword);
					    formData.append('confirmNewPassword', confirmNewPassword);
					    
					    $.ajax({
					        url: "/changepassword/checkChangePass",
					        type: "PUT",
					        contentType: false,
					        processData: false,
					        data: formData,
					        dataType: 'json',
					         
					        

					        headers: {
					            'X-XSRF-TOKEN': csrfToken
					        },

					        success: function (data) {
					        	 $(".error").text("");
					        	toastr.success(data.message);

					        	// Xóa nội dung của form
				                document.getElementById("currentPassword").value = "";
				                document.getElementById("newPassword").value = "";
				                document.getElementById('confirmNewPassword').value = "";
					        },
					           
					        error: function(jqXHR, textStatus, errorMessage) {
					        	if (jqXHR.status === 400) {
					                var errors = jqXHR.responseJSON;

					                // Xử lý lỗi từ phản hồi JSON
					                if (errors.hasOwnProperty("bindingErrors")) {
					                    var bindingErrors = errors["bindingErrors"];

					                    for (var i = 0; i < bindingErrors.length; i++) {
					                        var error = bindingErrors[i];
					                        $("#" + error.field + "-error").text(error.defaultMessage);
					                    }
					                }
					                if (errors.hasOwnProperty("message") && errors.message === "Current password is incorrect.") {
					                    $("#currentPassword-error").text(errors.message);
					                    return;
					                }
					                if (errors.hasOwnProperty("message")) {
					                    $("#confirmNewPassword-error").text(errors.message);
					                }
					            } else {
					                alert("Sorry! The system has errors!");
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
					            // Chuyển hướng người dùng về trang chủ sau khi đăng xuất thành công
					            window.location.href = "/product/index";
					        } 
					    };
					    
					    xhr.send(new FormData(event.target));
					    event.preventDefault(); // Ngăn chặn sự kiện mặc định của form
					});