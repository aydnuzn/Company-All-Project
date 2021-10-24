/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

$(document).ready(function() {
    $('.special.cards .image').dimmer({
        on: 'hover'
    });
});

function fncDelete(galleryid,imageid) {
    //alert("Sİl-Image" + galleryid + " - " + imageid);
    /*'/delete/gallery/'+galleryid+'/'+imageid,*/
    $.ajax({
        url: 'http://localhost:8091/delete/gallery/'+galleryid+'/'+imageid,
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
            debugger;
            console.log(err);
        }
    });
}
