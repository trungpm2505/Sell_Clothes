
document.addEventListener("DOMContentLoaded", function() {
    // Gọi hàm để lấy danh sách categories khi trang web được tải
    getlistCategories(0, '');
});

function searchCategories() {
    var keyword = document.getElementById('searchcategoryName').value;
    getlistCategories(0,keyword);
    console.log(keyword)
}
  function getAllCategories() {
      getlistCategories(0, ''); // Lấy tất cả danh mục
 }
  var csrfToken;
 function getlistCategories(page, keyword) {
	 csrfToken = Cookies.get('XSRF-TOKEN');
     $.ajax({
         type: "GET",
         url: "/category/getCategoryPage", // Endpoint đã chỉnh sửa
         data: {
             page: page,
             keyword: keyword
         },
         headers: {
			    'X-XSRF-TOKEN': csrfToken
		 },
         success: function (response) {
             // Xóa dữ liệu cũ trong tbody
             $("#categoryTable tbody").empty();

             if (response && response.categoryResponseDtos) {
                 var categories = response.categoryResponseDtos;

                 // Duyệt qua danh sách categories và thêm chúng vào tbody
                 for (var i = 0; i < categories.length; i++) {
                     var category = categories[i];

                     var row = '<tr>' +
                     	 '<td>' + (i+1) + '</td>' +
                         '<td>' + category.categoryName + '</td>' +
                         '<td>' + category.createAt + '</td>' +
                         '<td>' + (category.updateAt ? category.updateAt : '-') + '</td>' +
                         '<td>' +
                         '<span onclick="editCategory(' + category.id + ', \'' + category.categoryName + '\')" ><i class="fas fa-pencil-alt"></i>Edit</span>' +
                         '</td>' +
                         '<td>' +
                         '<span onclick="deleteCategory(' + category.id + ', \'' + category.categoryName + '\')"  style="color: red;"><i class="fas fa-trash"></i> Delete</span>' +
                         '</td>' +
                         
                         '</tr>';
                     $("#categoryTable tbody").append(row);
                 }
             }

             pagination = '';
	         currentPage = response.currentPage;
	         totalPages = response.totalPages;
	         size = response.size;
	        createPagination("#paging",pagination,currentPage,totalPages,keyword);
	     
         },
         error: function (error) {
             console.error(error);
         }
     });
 }



	 function createPagination(navId, pagination, currentPage, totalPages, keyWord ){
   	  $(navId).empty();//xoa toan bo noi dung trong html cÃ³ id navId(paging)
         
         pagination += '<ul class="pagination">';//tao danh sach 
         //ThÃªm má»t nÃºt Äiá»u hÆ°á»ng cho trang trÆ°á»c vÃ o biáº¿n pagination náº¿u currentPage >0
         pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'getlistCategories(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

             var startPage = currentPage > 2 ? currentPage - 2 : 0;
             var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;
				
             for (var i = startPage; i <= endPage; i++) {
                 pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="getlistCategories(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
             }
             
            //Náº¿u cÃ³ quÃ¡ nhiá»u trang (totalPages > 5) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm má»t nÃºt cháº¥m ...
             if ((totalPages > 5) &&  (currentPage < totalPages - 3)) {
                 pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
             }
            //Náº¿u khÃ´ng pháº£i lÃ  trang cuá»i cÃ¹ng (totalPages-1 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) khÃ´ng pháº£i lÃ  trang thá»© hai tá»« cuá»i (totalPages-2 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm nÃºt Äiá»u hÆ°á»ng Äáº¿n trang cuá»i cÃ¹ng.
             if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
                 pagination += '<li><a class="page-link" onclick="getlistCategories(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
             }
			
         pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'getlistCategories(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
         pagination += '</ul>';
          
          $(navId).append(pagination);
     }

function clearEditCategoryForm() {
	  // Lấy tham chiếu đến các phần tử trong biểu mẫu
	  var editCategoryId = document.getElementById("editCategoryId");
	  var editCategoryName = document.getElementById("editCategoryName");
	  var categoryNameError = document.getElementById("categoryName-error");

	  // Xóa giá trị trong các trường nhập liệu
	  editCategoryId.value = "";
	  editCategoryName.value = "";
	  
	  // Xóa bất kỳ thông báo lỗi nào
	  categoryNameError.innerText = "";
	}
function addCategory() {
	csrfToken = Cookies.get('XSRF-TOKEN');
	clearEditCategoryForm();
    // Hiển thị modal chỉnh sửa danh mục
    $("#editCategoryModal").modal("show");
    $("#saveCategoryChanges").off("click").on("click", function () {
    	 var categoryName = document.getElementById("editCategoryName").value;
        // Gửi AJAX request để cập nhật danh mục (sử dụng newCategoryId và newCategoryName)
        $.ajax({
            type: 'post',
            url : '/category/saveCategory',
            contentType : 'application/json',
            data : JSON.stringify({
            	categoryName : categoryName
			}),
			headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
            success: function (response) {
                getlistCategories(0, '');
                toastr.success(response);
                $("#categoryName").val("");
                $("#categoryName-error").text("");
                // Đóng modal sau khi chỉnh sửa thành công
                $("#editCategoryModal").modal("hide");
            },
            error: function(jqXHR, textStatus, errorMessage) {
	        	console.log(jqXHR.status);
	            if (jqXHR.status === 400) {
	                var errors = jqXHR.responseJSON;
	                if (errors.hasOwnProperty("bindingErrors")) {
	                    var bindingErrors = errors["bindingErrors"];
	                    for (var i = 0; i < bindingErrors.length; i++) {
	                        var error = bindingErrors[i];
	                        // Hiá»n thá» thÃ´ng bÃ¡o lá»i trong cÃ¡c tháº» cÃ³ ID ÄÃ£ táº¡o trÆ°á»c ÄÃ³.
	                        $("#" + error.field + "-error").text(error.defaultMessage);
	                    }
	                }
	                if(errors.hasOwnProperty("nameDuplicate")){
	                	var editnameError = errors["nameDuplicate"];
	                	$("#categoryName-error").text(editnameError);
	                }
	            } else {
	                alert("Sorry! The system has errors!"); // Hiá»n thá» thÃ´ng bÃ¡o lá»i náº¿u request khÃ´ng thÃ nh cÃ´ng vá»i mÃ£ lá»i khÃ¡c 400.
	            }
	        }
        });
    });
    
}


function deleteCategory(id,categoryName) {
	csrfToken = Cookies.get('XSRF-TOKEN');
    // Hiển thị modal xác nhận xóa
	$("#categoryIdToDelete").text(categoryName);
    // Thiết lập sự kiện cho nút "Xóa" trong modal xác nhận
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
                 type: 'get',
                 url: 'category/deleteCategory',
                 data: {
                     idCategory: id,
                 },
                 headers: {
					    'X-XSRF-TOKEN': csrfToken
					  },
                 success: function (response) {
                     console.log("Xóa thành công");
                     getlistCategories(0, '');
                     // Đóng modal sau khi xóa thành công
                     $("#confirmDeleteModal").modal("hide");
                     toastr.success(response);
                 },
                 error: function(jqXHR, textStatus, errorThrown){
     	            if (jqXHR.status === 400) {
     	                var error = jqXHR.responseJSON || jqXHR.responseText;
     	                $("#confirmDeleteModal").modal("hide");
     	                toastr.warning(error);
     	                
     	            }
     	        }
              
             });
      	   } else if (result.dismiss === Swal.DismissReason.cancel) {
      	     Swal.fire('Canceled!', '', 'error');
      	   }
      	 });
    	
    	
       
}

function editCategory(id, categoryName) {
	csrfToken = Cookies.get('XSRF-TOKEN');
	clearEditCategoryForm();
    // Điền giá trị của id và categoryName vào các trường input
    // Hiển thị modal chỉnh sửa danh mục
    $("#editCategoryModal").modal("show");
    $("#editCategoryName-error").text("");
    $("#editCategoryId").val(id);
    $("#editCategoryName").val(categoryName);
    // Thiết lập sự kiện cho nút "Lưu thay đổi"
    $("#saveCategoryChanges").off("click").on("click", function () {
    	  var newIdCategory = document.getElementById("editCategoryId").value;
          var categoryName = document.getElementById("editCategoryName").value;
        // Lấy giá trị mới từ các trường input (nếu bạn muốn cập nhật)
      //  var newCategoryName = $("#editCategoryName").val();
       // var newIdCategory = $("#editCategoryId").val();
      

        // Gửi AJAX request để cập nhật danh mục (sử dụng newCategoryId và newCategoryName)
        $.ajax({
            type: 'post',
            url: 'category/editCategory',
            contentType : 'application/json',
            data : JSON.stringify({
            	id : id ,
            	categoryName : categoryName
			}),
			headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
            success: function (response) {
                console.log("Chỉnh sửa thành công");
                console.log(categoryName);
                toastr.success(response);
                getlistCategories(0, '');
                // Đóng modal sau khi chỉnh sửa thành công
                $("#editCategoryModal").modal("hide");
            },
            error: function(jqXHR, textStatus, errorMessage) {
                console.log(jqXHR.status);
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
                        var nameError = errors["nameDuplicate"];
                        $("#categoryName-error").text(nameError);
                    }
                } else {
                    // Hiển thị thông báo lỗi khác nếu request không thành công với mã lỗi 400.
                    alert("Sorry! The system has errors!");
                }
            }
        });
    });
}
