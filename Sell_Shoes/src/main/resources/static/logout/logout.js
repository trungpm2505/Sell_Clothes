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
          		  
          		
          		  xhr.send(new FormData(event.target));
          		});