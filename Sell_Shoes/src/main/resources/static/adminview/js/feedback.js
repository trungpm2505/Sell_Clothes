
document.addEventListener("DOMContentLoaded", function() {
    // Gọi hàm để lấy danh sách size khi trang web được tải
    getlistFeedbacks(0, '');
});

function searchFeedback() {
    var keyword = document.getElementById('searchFeedbackName').value;
    getlistFeedbacks(0,keyword);
    console.log(keyword)
}
  function getAllFeedbacks() {
	  getlistFeedbacks(0, ''); // Lấy tất cả danh mục
 }

 function getlistFeedbacks(page, keyword) {
     $.ajax({
         type: "GET",
         url: "/feedback/getFeedbackPage", // Endpoint đã chỉnh sửa
         data: {
             page: page,
             keyword: keyword
         },
         success: function (response) {
             // Xóa dữ liệu cũ trong tbody
             $("#feedbackTable tbody").empty();

             if (response && response.feedbackResponseDtos) {
                 var feedbacks = response.feedbackResponseDtos;

                 // Duyệt qua danh sách categories và thêm chúng vào tbody
                 for (var i = 0; i < feedbacks.length; i++) {
                     var feedback = feedbacks[i];

                     var row = '<tr>' +
                     	 '<td>' + (i+1) + '</td>' +
                         '<td>' + feedback.fullname + '</td>' +
                         '<td>' + feedback.email + '</td>' +
                         '<td>' + feedback.phone + '</td>' +
                         '<td>' + feedback.createAt + '</td>' +
                         '<td>' +
                         '<span class="btn btn-primary" onclick="detailFeedback(' + feedback.id + ')" ><i class="fas fa-pencil-alt"></i>Detail</span>' +
                         '</td>' +
                         '</tr>';
                     $("#feedbackTable tbody").append(row);
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
         pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'getlistFeedbacks(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

             var startPage = currentPage > 2 ? currentPage - 2 : 0;
             var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;
				
             for (var i = startPage; i <= endPage; i++) {
                 pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="getlistFeedbacks(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
             }
             
            //Náº¿u cÃ³ quÃ¡ nhiá»u trang (totalPages > 5) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm má»t nÃºt cháº¥m ...
             if ((totalPages > 5) &&  (currentPage < totalPages - 3)) {
                 pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
             }
            //Náº¿u khÃ´ng pháº£i lÃ  trang cuá»i cÃ¹ng (totalPages-1 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) khÃ´ng pháº£i lÃ  trang thá»© hai tá»« cuá»i (totalPages-2 != currentPage) vÃ  trang hiá»n táº¡i (currentPage) nhá» hÆ¡n totalPages-3, hÃ m sáº½ thÃªm nÃºt Äiá»u hÆ°á»ng Äáº¿n trang cuá»i cÃ¹ng.
             if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
                 pagination += '<li><a class="page-link" onclick="getlistFeedbacks(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
             }
			
         pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'getlistFeedbacks(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
         pagination += '</ul>';
          
          $(navId).append(pagination);
     }


 
	 function detailFeedback(id) {
		    $("#editFeedbackModal").modal("show");

		    $.ajax({
		        type: 'get',
		        url: 'feedback/getFeedback',
		        contentType: 'application/json',
		        data: {
		            id: id
		        },
		        success: function (response) {
		            $('#detailFeedbackName').text(response.fullname);
		            $('#detailFeedbackPhone').text(response.phone);
		            $('#detailFeedbackDay').text(response.createAt);
		            $('#detailFeedbackEmail').text(response.email);
		            $('#detailFeedbackSub').text(response.subjectName);
		            $('#detailFeedbackNote').text(response.note);
		        },
		        error: function (jqXHR, textStatus, errorMessage) {
		            console.log(jqXHR.status);
		        }
		    });
		}
