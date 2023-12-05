//Login
//Check password 
function togglePasswordVisibility() {
  const passwordInput = document.getElementById('password-input');
  const showPasswordButton = document.getElementById('showPassword');

  if (passwordInput.type === 'password') {
    passwordInput.type = 'text';
    showPasswordButton.classList.remove('fa-eye');
    showPasswordButton.classList.add('fa-eye-slash');
  } else {
    passwordInput.type = 'password';
    showPasswordButton.classList.remove('fa-eye-slash');
    showPasswordButton.classList.add('fa-eye');
  }
}

const loginForm = document.getElementById('userLogin');
const emailInput = document.getElementById('email-input');
const passwordInput = document.getElementById('password-input');
const emailError = document.getElementById('email-error');
const passwordError = document.getElementById('password-error');

emailInput.addEventListener("focus", function(){
	emailError.textContent = "";
});
passwordInput.addEventListener("focus",function(){
	passwordError.textContent = "";
});

if (loginForm) {
    loginForm.addEventListener('submit', async function(event) { // Đặt hàm là async
        event.preventDefault(); // Ngăn việc tải lại trang khi form gửi đi
        const formData = new FormData(loginForm);
        const email = formData.get('email');
        const password = formData.get('password');
        
        try {
            const response = await fetch('/login/checkLogin', {
                method: 'POST',
                 headers: {
			        'Content-Type': 'application/json'
			    },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });
            if (response.ok) {
                const data = await response.json();
                const role = data.role;
                const token = data.token;
                const homeResponse = await fetch('/product/all-product', {
			        headers: {
			          'Authorization': 'Bearer ' + token
			        }
			      });
			      
			      const homeAdminResponse = await fetch('/order/admin/all', {
				        headers: {
				          'Authorization': 'Bearer ' + token
				        }
				      });
				  
                if (homeResponse.ok) {
			        if (role === 'USER') {
			          window.location.href = "/product/all-product";
			          
			        } else if (role === 'ADMIN') {
			          window.location.href = '/order/admin/all';
			        }
			      } else {
			        alert('Log in failed!');
			      }
            } else {
                const errors = await response.json();
                for (const key in errors) {
                    if (errors.hasOwnProperty("EmailErrors")) {
                        emailError.textContent = errors[key];
                    }
                    
                    if (errors.hasOwnProperty("PasswordErrors")) {
                        passwordError.textContent = errors[key];
                    }
                    
                    if (errors.hasOwnProperty("bindingErrors")) {
                        var bindingErrors = errors["bindingErrors"];
                        for (var i = 0; i < bindingErrors.length; i++) {
                            var error = bindingErrors[i];
                            if (error.field == "email") {
                                emailError.textContent = error.defaultMessage;
                            }
                            if (error.field == "password") {
                                passwordError.textContent = error.defaultMessage;
                            }
                        }
                    }
                    
                    if (errors.hasOwnProperty("accountError")) {
                        alert(errors[key]);
                    }
                }
            }
        } catch (error) {
            console.error(error);
        }
    });
}
