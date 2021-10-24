/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

$(document).ready(function (){
    $('#show_pass').on('click', function (){
        debugger;
        let passInput = $('#pass_input');
        let passInputAgain = $('#passagain_input');
        if(passInput.attr('type')==='password'){
            passInput.attr('type','text');
            passInputAgain.attr('type','text');
        }else{
            passInput.attr('type','password');
            passInputAgain.attr('type','password');
        }
    });
});