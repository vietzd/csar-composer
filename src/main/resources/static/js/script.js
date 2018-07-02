function removeFromInternal(id) {
    var url = "http://localhost:8011/api/internal-csars/" + id;
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", url, true);
    xhr.onload = function () {
        location.reload();
    };
    xhr.send(null);
}

function addToInternal(sourceId) {
    var url = "http://localhost:8011/api/internal-csars/add-from-source/" + sourceId;
    // var json = JSON.stringify(sourceId);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
    xhr.onload = function () {
        location.reload();
    };
    xhr.send(null);
    // xhr.send(json);
}