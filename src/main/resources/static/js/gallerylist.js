/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */
$(document).ready(function() {
    $('.special.cards .image').dimmer({
        on: 'hover'
    });
});

function fncDelete(data) {
    //alert("Sİl" + data);
    $.ajax({
        url: '/delete/gallery/' + data,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            debugger;
            if(data.STATUS){
                console.log(data.MESSAGE);
                location.reload();
            }else{
                console.log(data.MESSAGE);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}




