/*jshint esversion: 6 */
/*jslint latedef:false*/

let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getAnnCategoryList(pageableNumber);
    } else {
        getAnnCategoryListSearch(this.value, pageableNumber);
    }
});

//Arama Var
function getAnnCategoryListSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/category/list/' + searchKey + "/" + index,
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
getAnnCategoryList(pageableNumber);
let oldData = [];

function getAnnCategoryList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/category/list/' + index,
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
            <td>` + itm.ann_category_title + `</td>
          
            <td >
                <div class="ui buttons" style="float: right">
                    <a href="http://localhost:8091/admin/announcement/category/detail/` + itm.id + `" class="ui button">Detay</a>
                    <div class="ya da"></div>
                    <button onclick="deleteAnnCategory(` + itm.id + `)"   class="ui negative button">Sil</button>
                </div>                      
            </td>
          </tr>`;
    }
    $('#annCategoryTbody').html(html);
}

function deleteAnnCategory(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/announcement/category/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getAnnCategoryList(pageableNumber);
            } else {
                pageableNumber = 1;
                getAnnCategoryList(pageableNumber);
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
    $('#annCategoryPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getAnnCategoryList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getAnnCategoryListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}