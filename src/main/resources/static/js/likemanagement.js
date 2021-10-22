let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getLikeList(pageableNumber);
    } else {
        getLikeListSearch(this.value, pageableNumber);
    }
});

//Arama Var
function getLikeListSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/like/list/' + searchKey + "/" + index,
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
getLikeList(pageableNumber);
let oldData = [];

function getLikeList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/like/list/' + index,
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
            <td>` + itm.customer + `</td>
            <td>` + itm.product + `</td>
            <td>` + itm.score + `</td>
            <td>` + itm.date + `</td>
          
            <td >
                <div class="ui buttons" style="float: right">
                    <button onclick="deleteLike(` + itm.id + `)" class="ui negative inverted button">Sil</button>
                </div>                      
            </td>
          </tr>`;
    }
    $('#likeTbody').html(html);
}

function deleteLike(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/like/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getLikeList(pageableNumber);
            } else {
                pageableNumber = 1;
                getLikeList(pageableNumber);
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
    $('#likePNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getLikeList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getLikeListSeach('" + searchKeyTrim + "'" + "," + i + ")";
    }
}

function getAllLikeList(index) {
    console.log("Deneme Content All Likes11111")
    $.ajax({
        url: 'http://localhost:8091/rest/admin/like/allLikeList/' + index,
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log(data);
                pageableNumber = index;
                oldData = data.RESULT;
                getRows2(data.RESULT);
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

getAllLikeList(pageableNumber);


function getRows2(data) {
    let html = ``;

    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        console.log("Deneme Content All Likes")
        console.log(data)
        html += `<tr>
            <td>` + itm.productID + `</td>
            <td>` + itm.totalScore + `</td>
        
          </tr>`;
    }
    $('#allLikeTbody').html(html);
}