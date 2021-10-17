/*jshint esversion: 6 */
/*jslint latedef:false*/

let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getAnnouncementList(pageableNumber);
    } else {
        getAnnouncementListSearch(this.value, pageableNumber);
    }
});

// Arama var
//Elastic Arama
function getAnnouncementListSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/list/' + searchKey + "/" + index,
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log(data);
                pageableNumber = index;
                oldData = data.RESULT.content;
                getRows(data.RESULT.content);
                getPageNumbers(data.COUNTOFPAGE);
            } else {
                console.log(data);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

//Arama Yok
let pageableNumber = 1;
getAnnouncementList(pageableNumber);
let oldData = [];

function getAnnouncementList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/list/' + index,
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log(data);
                pageableNumber = index;
                oldData = data.RESULT;
                getRows(data.RESULT);
                getPageNumbers(data.COUNTOFPAGE);
            } else {
                console.log(data);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

function getRows(data) {
    let html = ``;
    for (let i = 0; i < data.length; i++) {
        const itm = data[i];

        html += `<tr>
            <td>` + itm.ann_title + `</td>
            <td>` + itm.ann_brief_description + `</td>
            <td>` + itm.ann_type + `</td>
          
            <td>
                <div class="ui buttons" style="float: right">
                    <a href="http://localhost:8091/admin/announcement/` + itm.id + `" class="ui primary button">Düzenle</a>
                    <div class="ya da"></div>
                    <button onclick="deleteAnnouncement(` + itm.id + `)"   class="ui negative button">Sil</button>
                </div>                      
            </td>
          </tr>`;
    }
    $('#announcementTbody').html(html);
}

function deleteAnnouncement(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getAnnouncementList(pageableNumber);
            } else {
                pageableNumber = 1;
                getAnnouncementList(pageableNumber);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

//Değişirse sadece burası değişecek
function getPageNumbers(data) {
    let html = ``;
    if (data === 1) {
        html += `
        <a class="item" onclick="` + getPageableNumber(1) + `">1</a>
        `;
    } else if (data === 2 && pageableNumber == 1) {
        html += `
        <a class="item" onclick="` + getPageableNumber(1) + `">1</a>
        <a class="icon item" onclick="` + getPageableNumber(2) + `">
        <i class="right chevron icon""></i> </a>
        `;
    } else if (data === 2 && pageableNumber == 2) {
        html += `
        <a class="icon item" onclick="` + getPageableNumber(1) + `">
        <i class="left chevron icon" ></i>
        </a>
        <a class="item" onclick="` + getPageableNumber(2) + `">2</a>
        `;
    }
    $('#announcementPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getAnnouncementList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getAnnouncementListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}