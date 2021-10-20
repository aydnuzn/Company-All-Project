/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

let url = "http://localhost:8091/rest/admin/survey/datatable/list";
let tempUrl = "http://localhost:8091/rest/admin/survey/datatable/list";
let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        console.log("redis");
        tempUrl = url;
        $('#exampleTable').DataTable().ajax.url(tempUrl).load();
    } else {
        console.log("Elastic -->" + this.value);
        tempUrl = url + '/' + this.value;
        console.log("tempUrl = " + tempUrl);
        $('#exampleTable').DataTable().ajax.url(tempUrl).load();
    }
});

$(document).ready(function () {
    var tablem = $('#exampleTable').DataTable({
        //dom: 'Bfrtip',
        "retrieve": true,
        "processing": true,
        "serverSide": true,
        "searching": true,
        "paging": true,
        "dom": '<"top"i>rt<"bottom"lp><"clear">', // for hide default global search box
        "ajax": ({
            "url": tempUrl,
            "type": 'Get',
            "datatype": 'json',
            dataSrc: function (d) {
                d.draw = d.DRAW;
                d.recordsTotal = d.COUNT;   //--> Sayfada bulunan toplam kayıt bilgisini veren kısım
                d.recordsFiltered = d.COUNT;   //--> Paggination için gerekli kısım
                return d.RESULT;
            }
        }),
        "columns": [
            {"data": "id", "name": "id", "autoWidth": true},
            {"data": "survey_title", "name": "survey_title", "autoWidth": true},
            {
                "data": "id", "name": "id", "autoWidth": true,
                "render": function (data) {
                    var a = `<div class="ui buttons" style="float: right">
                    <a href="http://localhost:8091/admin/survey/detail/` + data + `" class="ui primary button">Düzenle</a>
                    <div class="ya da"></div>
                    <button onclick="deleteSurvey(` + data + `)" class="ui negative button">Sil</button>
                </div> `;
                    return a;
                }
            }
        ],
        language: {
            "zeroRecords": 'Kayıt Bulunamadı.',
            "infoEmpty": 'Eşleşen Kayıt Bulunamadı.',
            "infoFiltered": ''
        }
    });
});

function deleteSurvey(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/survey/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            debugger;
            $('#exampleTable').DataTable().ajax.reload();
        },
        error: function (err) {
            console.log(err);
        }
    });
}
