let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getOrdersList(pageableNumber);
    }
});

//Arama Var

//----------------------------------------------------------------------------------------------------------------------
//Arama Yok
let pageableNumber = 1;
getOrdersList(pageableNumber);
let oldData = [];

function getOrdersList(index) {
    $.ajax({
        url: 'http://localhost:8091/rest/admin/orders/deliveredList/' + index,
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
            <td>` + itm.customer_address + `</td>
          </tr>`;
    }
    $('#ordersDeliveredTbody').html(html);
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
    $('#ordersDeliveredPNumber').html(html);
}

function getPageableNumber(i) {
    if (searchKey.trim() == "") {
        return "getOrdersList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getOrdersListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}





