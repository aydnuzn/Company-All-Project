var _URL = window.URL || window.webkitURL;
$("#file").change(function (e) {
    var file, img;
    if ((file = this.files[0])) {
        img = new Image();
        img.onload = function () {
            $('#adv_width').val(this.width);
            $('#adv_height').val(this.height);
        };
        img.onerror = function () {
            alert("not a valid file: " + file.type);
        };
        img.src = _URL.createObjectURL(file);
    }
});

$('.message .close')
    .on('click', function() {
        $(this)
            .closest('.message')
            .transition('fade')
        ;
    })
;