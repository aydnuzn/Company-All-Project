/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */

let url = "http://localhost:8091/rest/admin/customer/datatable/list";
let tempUrl = "http://localhost:8091/rest/admin/customer/datatable/list";
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
            {"data": "name", "name": "name", "autoWidth": true},
            {"data": "surname", "name": "surname", "autoWidth": true},
            {"data": "email", "name": "email", "autoWidth": true},
            {"data": "tel", "name": "tel", "autoWidth": true},
            {"data": "cu_status", "name": "cu_status", "autoWidth": true},
            {
                "data": "id", "name": "id", "autoWidth": true,
                "render": function (data) {
                    var a = `<div class="ui buttons" style="float: right">
                    <button onclick="changeBan(` + data + `)" class="ui secondary button">Ban</button>
                    <div class="ya da"></div>
                    <a href="http://localhost:8091/admin/address/` + data + `" class="ui secondary button">Adresler</a>
                    <div class="ya da"></div>
                    <button onclick="deleteCustomer(` + data + `)" class="ui negative button">Sil</button>
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

function deleteCustomer(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/customer/delete/' + index,
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

function changeBan(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/customer/changeBan/' + index,
        type: 'PUT',
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
