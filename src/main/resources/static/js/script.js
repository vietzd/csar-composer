function removeFromInternal(id) {
    var url = "http://localhost:8080/api/internal-csars/" + id;
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", url, true);
    xhr.send(null);
    location.reload();
}

function addToInternal(sourceId) {
    var url = "http://localhost:8080/api/internal-csars/add-from-source/" + sourceId;
    // var json = JSON.stringify(sourceId);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
    xhr.send(null);
    // xhr.send(json);
    location.reload();
}