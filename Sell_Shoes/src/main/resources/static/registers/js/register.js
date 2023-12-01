
function togglePasswsordVisibility() {
    const passwordInput1 = document.getElementById('passwordInput1');
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
  
  function togglePasswsordVisibility1() {
    const passwordInput2 = document.getElementById('passwordInput2');
    const showPasswordButton = document.getElementById('showPassword2');
  
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

  
  function ValidateEmail(email) {
    // Kiểm tra định dạng email hợp lệ
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
  
  const inputList = [ 'fullname', 'passwordInput1', 'address',
				'email', 'phone' ];

	inputList.forEach(function(inputName) {

		const input = document.getElementById(inputName);
		const errors = document.getElementsByName(inputName + '-error');
		input.addEventListener("focus", function() {
			errors.forEach(function(error) {
				error.textContent = '';
			});
		});
	});