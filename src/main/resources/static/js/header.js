function checkSearchInput() {
	if($("#search_input").val() == "") {
		alert("검색어를 입력하세요.");
		return false;
	}
	else {
		return true;
	}
}