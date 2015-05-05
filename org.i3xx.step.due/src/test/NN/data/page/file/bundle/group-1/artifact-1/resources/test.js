
function login() {
	//alert("Login to OfficeBase 5 (V0012)");
	window.location.replace("/login/login.htm");
	return null;
}

function logout() {
	window.location.replace("/logout/logout.htm");
	return null;
}

function callme(func, persistent) {
	var frm = document.getElementById("result");
	frm.src="/call/"+func+"/"+persistent;

	return null;
}

function readme(func, persistent) {
	var frm = document.getElementById("result");
	frm.src="/data/read";

	return null;
}
