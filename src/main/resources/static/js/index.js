$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求（POST）
	$.post(
		CONTEXT_PATH + "/discuss/add",
			{"title":title,"content":content},
			function (data){
				data = $.parseJSON(data);
				//在提示框显示返回消
				$("#hintBody").text(data.msg);
				//显示提示框
				$("#hintModal").modal("show");
				setTimeout(function(){
					$("#hintModal").modal("hide");
					if (data.code == 0){
						window.location.reload();
					}
				}, 2000);
			}
	)


}