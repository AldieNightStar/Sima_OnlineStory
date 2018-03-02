// ======================================
// Ajax
// ======================================
function ajget(url){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, false);
    xhr.send();
    return xhr.responseText;
}

function ajpost(url, data){
	var xhr = new XMLHttpRequest();
	xhr.open('POST', url, false);
	xhr.send(data);
	return xhr.responseText;
}