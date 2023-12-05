var csrfToken;
		function addFeedback() {
			 csrfToken = Cookies.get('XSRF-TOKEN');
			// Mảng chứa các trường input cần kiểm tra focus
		    var inputList = ['fullName', 'email', 'subjectName', 'note', 'phone'];
		
		    // Lặp qua từng trường input và thêm sự kiện focus
		    inputList.forEach(function (inputName) {
		        var input = $("#" + inputName);
		        var error = $("#" + inputName + '-error');
		
		        if (input) {
		            input.on("focus", function () {
		                error.text('');
		            });
		        } else {
		            console.error("Element with id '" + inputName + "' not found in the page.");
		        }
		    });
		
			//Xoá form khi thêm thành công
		    var fullName = $("#fullName").val();
		    var email = $("#email").val();
		    var subjectName = $("#subjectName").val();
		    var phone = $("#phone").val();
		    var note = $("#note").val();
		
		    console.log(fullName, email, subjectName, phone, note); // Kiểm tra giá trị đã lấy được
		
		    // Gửi AJAX request để lưu phản hồi từ người dùng
		    $.ajax({
		        type: 'post',
		        url: '/feedback/saveUserfeedback',
		        contentType: 'application/json',
		        data: JSON.stringify({
		            fullName: fullName,
		            email: email,
		            subjectName: subjectName,
		            phone: phone,
		            note: note
		        }),
		        headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
		        success: function (response) {
		            // Xử lý thành công, có thể thực hiện các hành động khác nếu cần
		            toastr.success(response);
		         // Xoá dữ liệu trên form
		            $("#fullName").val("");
		            $("#email").val("");
		            $("#subjectName").val("");
		            $("#phone").val("");
		            $("#note").val("");
		
		            // Hoặc nếu bạn có các thẻ span để hiển thị lỗi, bạn cũng có thể xoá chúng
		            $(".error-message").text("");
		        },
		        error: function (jqXHR, textStatus, errorMessage) {
		            // Xử lý lỗi
		            console.log(errorMessage); // Kiểm tra thông báo lỗi từ máy chủ
		
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
		
		                if (errors.hasOwnProperty("nameDuplicate")) {
		                    var editnameError = errors["nameDuplicate"];
		                    $("#fullName-error").text(editnameError);
		                    $("#email-error").text(editnameError);
		                    $("#subjectName-error").text(editnameError);
		                    $("#phone-error").text(editnameError);
		                    $("#note-error").text(editnameError);
		                }
		            } else {
		                alert("Sorry! The system has errors!"); // Hiển thị thông báo lỗi nếu request không thành công với mã lỗi khác 400.
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