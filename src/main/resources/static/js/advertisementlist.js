let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getAdvList(pageableNumber);
    } else {
        getAdvSearch(this.value, pageableNumber);
    }
});

//Arama Var
function getAdvSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/advertisement/list/' + searchKey + "/" + index,
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

//----------------------------------------------------------------------------------------------------------------------
//Arama Yok
let pageableNumber = 1;
getAdvList(pageableNumber);
let oldData = [];

function getAdvList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/advertisement/list/' + index,
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

//----------------------------------------------------------------------------------------------------------------------

function getRows(data) {
    let html = ``;

    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        html += `<tr>
            <td>` + itm.adv_title + `</td>
            <td>` + itm.adv_shown_number + `</td>
            <td>` + itm.adv_date_begin + `</td>
            <td>` + itm.adv_date_end + `</td>
            <td><img style="width: auto; height: 100px" src="/uploads/advertisement/` + itm.adv_image + `"></td>
            <td>` + itm.adv_width + `</td>
            <td>` + itm.adv_height + `</td>
            <td>` + itm.adv_link + `</td>
            <td>
                <div class="ui buttons" style="float: right">
                    <a href="http://localhost:8091/admin/advertisement/`+itm.id+`" class="ui primary button">Düzenle</a>
                    <div class="ya da"></div>
                    <button onclick="deleteAdv(` + itm.id + `)"   class="ui negative button">Sil</button>
                </div>
           </td>
          </tr>`;
    }
    $('#advTbody').html(html);
}

function deleteAdv(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/advertisement/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getAdvList(pageableNumber);
            } else {
                pageableNumber = 1;
                getAdvList(pageableNumber);
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
    $('#advPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getAdvList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getAdvListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}


