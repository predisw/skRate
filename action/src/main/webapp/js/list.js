var form = document.frmData;
var frmAction = form.action;
var pageNumber = document.getElementsByName("curPage")[0];
function onlyNum() {
	if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105))) {
			//number key board
		alert("\u8f93\u5165\u9875\u6570\u4e0d\u80fd\u4e3a\u6570\u5b57!");
		pageNumber.value = 1;
	}
}
function first() {
	if (pageNumber.value == "1") {
		alert("\u5f53\u524d\u5df2\u7ecf\u662f\u9996\u9875!");
		return;
	}
	pageNumber.value = "1";
	form.action = frmAction;
	form.submit();
}
function prev() {
	var currentPage = parseInt(pageNumber.value);
	if (currentPage - 1 < 1) {
		alert("\u5df2\u7ecf\u6ca1\u6709\u4e0a\u9875!");
		return;
	}
	pageNumber.value = (currentPage - 1);
	form.action = frmAction;
	form.submit();
}
function next() {
	var currentPage = parseInt(pageNumber.value);
	if (currentPage + 1 > parseInt(form.totalPage.value)) {
		alert("\u5df2\u7ecf\u6ca1\u6709\u4e0b\u9875!");
		return;
	}
	pageNumber.value = (currentPage + 1);
	form.action = frmAction;
	form.submit();
}
function last() {
	if (pageNumber.value == form.totalPage.value) {
		alert("\u5f53\u524d\u5df2\u7ecf\u662f\u5c3e\u9875!");
		return;
	}
	pageNumber.value = form.totalPage.value;
	form.action = frmAction;
	form.submit();
}
function sub() {
	var currentPage = parseInt(pageNumber.value);
	if (currentPage > parseInt(form.totalPage.value) || currentPage < 1) {
		alert("\u6ca1\u6709\u7b2c" + currentPage + "\u9875!");
		return false;
	}
	return true;
}





