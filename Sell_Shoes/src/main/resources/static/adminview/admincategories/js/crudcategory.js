/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
  getlistCategories()
});


function getlistCategories(){

    // Sử dụng Ajax để lấy danh sách categories
    $.ajax({
        type: "GET",
        url: "/category/getCategories", // Điều này cần phải phù hợp với mapping của bạn
        success: function(response) {
        console.log(response)
            // Xóa dữ liệu cũ trong tbody
            $("#categoryTable tbody").empty();
            // Duyệt qua danh sách categories và thêm chúng vào tbody
            for (var i = 0; i < response.length; i++) {
                var category = response[i];

                var row = '<tr>' +
                    '<td>' + category.id + '</td>' +
                    '<td>' + category.categoryName + '</td>' +
                    '<td>' + category.createAt + '</td>' +
'<td>' + (category.updateAt ? category.updateAt : '') + '</td>' +
               //    '<td>' +
                //   '<i class="fa fa-trash-o" style="color: red;"></i><span onclick="deleteCategory(' + category.id + ')" style="color: red;">Delete</span>' +
                //   '</td>' +
                '<td>' +
                   '<i class="fa fa-trash-o" style="color: red;"></i><span onclick="deleteCategory(' + category.id + ', \'' + category.categoryName + '\')"  style="color: red;">Delete</span>' +
                   '</td>' +  
                
                   '<td>' +
                   '<i class="fa fa-pencil"></i><span onclick="editCategory(' + category.id + ', \'' + category.categoryName + '\')" >Edit</span>' +
                   '</td>' +
                   '</tr>';
                $("#categoryTable tbody").append(row);
            }
        },
        error: function(error) {
            console.error(error);
        }
    });
}



 

function ss() {
  
			var categoryName = document.getElementById("categoryName").value;
		
			$.ajax({
				type : 'POST',
				url : '/category/saveCategory',
				data : {
					
					categoryName : categoryName
				},
				success : function(response) {
					console.log("thanh cong")
					  getlistCategories();
					  $('#exampleModalCenter').modal('hide');
				},
				error : function(xhr, status, error) {
					console.log(error);
					console.log(status);
				}
			});
}



function deleteCategory(id,categoryName) {
    // Hiển thị modal xác nhận xóa
    $("#confirmDeleteModal").modal("show");
$("#categoryIdToDelete").text(categoryName);
    // Thiết lập sự kiện cho nút "Xóa" trong modal xác nhận
    $("#confirmDeleteButton").off("click").on("click", function () {
        $.ajax({
            type: 'get',
            url: 'category/deleteCategory',
            data: {
                idCategory: id,
            },
            success: function (response) {
                console.log("Xóa thành công");
                getlistCategories();
                // Đóng modal sau khi xóa thành công
                $("#confirmDeleteModal").modal("hide");
            },
            error: function (xhr, status, error) {
                console.log("Lỗi khi xóa bình luận");
                console.log(error);
                console.log(status);
                // Đóng modal nếu xóa thất bại
                $("#confirmDeleteModal").modal("hide");
            }
        });
    });
}

function editCategory(id, categoryName) {
    // Điền giá trị của id và categoryName vào các trường input
    $("#editCategoryId").val(id);
    $("#editCategoryName").val(categoryName);

    // Hiển thị modal chỉnh sửa danh mục
    $("#editCategoryModal").modal("show");

    // Thiết lập sự kiện cho nút "Lưu thay đổi"
    $("#saveCategoryChanges").off("click").on("click", function () {
        // Lấy giá trị mới từ các trường input (nếu bạn muốn cập nhật)
 
        var newCategoryName = $("#editCategoryName").val();

        // Gửi AJAX request để cập nhật danh mục (sử dụng newCategoryId và newCategoryName)
        $.ajax({
            type: 'get',
            url: 'category/editCategory',
            data: {
               
                categoryName: newCategoryName
            },
            success: function (response) {
                console.log("Chỉnh sửa thành công");
                getlistCategories();
                // Đóng modal sau khi chỉnh sửa thành công
                $("#editCategoryModal").modal("hide");
            },
            error: function (xhr, status, error) {
                console.log("Lỗi khi chỉnh sửa danh mục");
                console.log(error);
                console.log(status);
                // Đóng modal nếu chỉnh sửa thất bại
                $("#editCategoryModal").modal("hide");
            }
        });
    });
}




/*function searchCategories() {
  var categoryName = document.getElementById("searchcategoryName").value;
    $.ajax({
				type : 'get',
				url : 'category/searchCategory',
				data : {
					
					categoryName : categoryName
				},
				 success: function (response) {
            // Xóa dữ liệu cũ trong bảng
            $("#categoryTable tbody").empty();

            // Duyệt qua danh sách categories và thêm chúng vào bảng
            for (var i = 0; i < response.length; i++) {
                var category = response[i];
                var row = '<tr>' +
                    '<td>' + category.id + '</td>' +
                    '<td>' + category.categoryName + '</td>' +
                    '<td>' + category.createAt + '</td>' +
                    // Thêm các cột khác ở đây
                    '</tr>';
                $("#categoryTable tbody").append(row);
            }
        },
				error : function(xhr, status, error) {
					console.log(error);
					console.log(status);
				}
			});

}*/