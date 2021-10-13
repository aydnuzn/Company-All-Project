let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getContentList(pageableNumber);
    } else {
        getContentListSearch(this.value, pageableNumber);
    }
});

//Arama Var
function getContentListSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/content/list/' + searchKey + "/" + index,
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
getContentList(pageableNumber);
let oldData = [];

function getContentList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/content/list/' + index,
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
            <td>` + itm.content_title + `</td>
            <td>` + itm.content_brief_description + `</td>
            <td>` + itm.content_detailed_description + `</td>
            <td>` + convertStatus(itm.content_status) + `</td>
            <td >
                <div class="ui buttons" style="float: right">
                   
                    <button onclick="deleteContent(` + itm.id + `)"   class="ui negative button">Sil</button>
                </div>                      
            </td>
          </tr>`;
    }
    $('#contentTbody').html(html);
}

function deleteContent(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/content/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getContentList(pageableNumber);
            } else {
                pageableNumber = 1;
                getContentList(pageableNumber);
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
    $('#contentPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getContentList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getContentListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}


function convertStatus(status) {
    if (status == 1) {
        return "Aktif";
    } else {
        return "Pasif";
    }
}