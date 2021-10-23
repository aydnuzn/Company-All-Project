/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

$.ajax({
    url: '/getLogo',
    type: 'GET',
    contentType: "application/json",
    dataType: 'json',
    success: function (data) {
        if (data) {
            debugger;
            if (data.company.company_logo == null) {
                $('.logoImage').attr('src', "/uploads/logos/anonim.jpeg");
            }else {
                $('.logoImage').attr('src', "/uploads/logos/" + data.company.id + '/' + data.company.company_logo);
            }
        } else {
            console.log("Veri tabanında fotoğrafı yok.");
        }
    },
    error: function (err) {
        console.log(err);
    }
});