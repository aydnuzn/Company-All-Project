let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getOrdersList(pageableNumber);
    } else {
        getOrdersListSearch(this.value, pageableNumber);
    }
});

//Arama Var
function getOrdersListSearch(searchKey, index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/orders/list/' + searchKey + "/" + index,
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
getOrdersList(pageableNumber);
let oldData = [];

function getOrdersList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/orders/list/' + index,
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
            <td>` + itm.user_id + `</td>
            <td>` + itm.product_id + `</td>
            <td>` + itm.order_amount + `</td>
            <td>` + itm.order_status + `</td>
          
            <td >
                <div class="ui buttons" style="float: right">
                    <button onclick="deleteOrder(` + itm.id + `)" class="ui negative inverted button">Sil</button>
                </div>                      
            </td>
          </tr>`;
    }
    $('#ordersTbody').html(html);
}

function deleteOrder(index) {
    if (oldData.length === 1) {
        pageableNumber = pageableNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/orders/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getOrdersList(pageableNumber);
            } else {
                pageableNumber = 1;
                getOrdersList(pageableNumber);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

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
    $('#ordersPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getOrdersList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getOrdersListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}