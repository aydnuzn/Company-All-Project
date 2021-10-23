/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

$(document).ready( function () {
     $.ajax({
         url: '/admin/product/categorylist',
         type: 'GET',
         contentType: "application/json",
         dataType: 'json',
         success: function (data) {
             debugger;
             checkboxCheck(data);
             console.log(data);
         },
         error: function (err) {
             console.log(err);
         }
     });
});

function checkboxCheck(arr){
    $("input[name='pr_categories']").each( function () {
        for (let i=0; i<arr.length; i++){
            if(arr[i] == $(this).val()){
                //alert($(this).val());
                this.checked = true;
                break;
            }
        }
    });
}
