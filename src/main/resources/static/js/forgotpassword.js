/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

$(document).ready(function() {
        $('.ui.form')
            .form({
                fields: {
                    email: {
                        identifier  : 'email',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : 'Please enter your e-mail'
                            },
                            {
                                type   : 'email',
                                prompt : 'Please enter a valid e-mail'
                            }
                        ]
                    },
                }
            });
    });

$('#send_success').html('');
$('#send_fail').html('');
$('#pass_email').val("");

$('#mail_send_message').submit((event) => {
    event.preventDefault();
    debugger;
    const us_email = $('#pass_email').val();
    console.log(us_email);

    $.ajax({
        url: 'http://localhost:8091/rest/forgotpassword/' + us_email,
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data){
            if(data.STATUS == true){
                debugger;
                alert("Mail yollandı");
                $('#send_fail').html('');
                $('#pass_email').val("");
                mailSendSuccess();
            }else{
                debugger;
                if(data.MAILSEND == false){
                    // Sistemsel Hata! Mail gönderilemedi
                    alert("Sistemsel Hata! Mail gönderilemedi");
                    mailSendError();
                }else{
                    // Mail Bulunamadı
                    alert("Mail sisteme kayıtlı değil");
                    invalidMail();
                }

            }
        },
        error: function (err){
            console.log(err);
        }
    });
});

function invalidMail() {
    let html = `<div class="ui red message" style="display: block">
                        <i class="close icon"></i>
                        <div class="header">
                            Başarısız!
                        </div>
                        <strong> Mail Sisteme kayıtlı değil <br> Mevcut bir mail giriniz </strong>
                    </div>`;
    $('#send_fail').html(html);
}

function mailSendError() {
    let html = `<div class="ui red message" style="display: block">
                        <i class="close icon"></i>
                        <div class="header">
                            Sistemsel Hata!
                        </div>
                        <strong>Mail Sisteme kayıtlı<br> Mail gönderilemedi</strong>
                    </div>`;
    $('#send_fail').html(html);
}


function mailSendSuccess() {
    let html = `<div class="ui green message" style="display: block">
                        <i class="close icon"></i>
                        <div class="header">
                            Başarılı!
                        </div>
                        <strong> Mail Başarıyla Gönderildi <br> Mailinizi kontrol ediniz</strong>
                    </div>`;
    $('#send_success').html(html);
}