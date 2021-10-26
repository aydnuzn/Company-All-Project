/*jshint esversion: 6 */
/*jslint latedef:false*/
/*jshint -W087 */
let searchKey = "";
$(document).on('keyup', 'input', function () {
    searchKey = this.value;
    if (this.value.trim() == "") {
        getProductCategoryList(pageNumber);
    } else {
        getProductCategoryListSearch(this.value, pageNumber);
    }
});

let pageNumber = 1;
//Redis
getProductCategoryList(pageNumber);
let oldData = [];
function getProductCategoryList(index){
    $.ajax({
       url: 'http://localhost:8091/rest/admin/product/category/list/' + index,
       type: 'GET',
       contentType: 'application/json',
       dataType: 'json',
       success: function (data){
           debugger;
           if(data){
               debugger;
               console.log(data);
               pageNumber = index;
               oldData = data.RESULT;
               getRows(data.RESULT);
               getPageNumbers(data.COUNTOFPAGE);
           }else{
               debugger;
               console.log(data);
           }
       },
        error: function (err){
           console.log(err);
        }
    });
}

//Elasticsearch
function getProductCategoryListSearch(searchKey, index){
    $.ajax({
        url: 'http://localhost:8091/rest/admin/product/category/list/' + searchKey + "/" + index,
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log(data);
                pageNumber = index;
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

function getRows(data) {
    debugger;
    let html = ``;
    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        if(itm.parent_id == null){
            html += `<tr>
                        <td>`+itm.pr_title+`</td>
                        <td>-</td>
                        <td>
                            <div class="ui buttons" style="float: right">
                                <a href="http://localhost:8091/admin/product/category/`+ itm.id +`" class="ui primary button">Düzenle</a>
                                <div class="ya da"></div>
                            <button onclick='deleteProductCategory(`+ itm.id +`)' class="ui negative button">Sil</button>
                            </div>                      
                        </td>
                    </tr>`;
        }
        for (let j = 0; j < data.length; j++) {
            const itm2 = data[j];
            if(itm.id == itm2.parent_id){
                html += `<tr>
                        <td>-</td>
                        <td>`+itm2.pr_title+`</td>
                        <td>
                            <div class="ui buttons" style="float: right">
                                <a href="http://localhost:8091/admin/product/category/`+ itm2.id +`" class="ui primary button">Düzenle</a>
                                <div class="ya da"></div>
                            <button onclick='deleteProductCategory(`+ itm2.id +`)' class="ui negative button">Sil</button>
                            </div>                      
                        </td>
                    </tr>`;
            }
        }
    }
    $('#productCategoryTbody').html(html);
}

function deleteProductCategory(index){
    if (oldData.length === 1) {
        pageNumber = pageNumber - 1;
    }
    $.ajax({
        url: 'http://localhost:8091/rest/admin/product/category/delete/' + index,
        type: 'DELETE',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.STATUS) {
                getProductCategoryList(pageNumber);
            } else {
                pageNumber = 1;
                getProductCategoryList(pageNumber);
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
        <a class="item" onclick="` + getPageNumber(1) + `">1</a>`;
    } else if (data === 2 && pageNumber == 1) {
        html += `
        <a class="item" onclick="` + getPageNumber(1) + `">1</a>
        <a class="icon item" onclick="` + getPageNumber(2) + `">
            <i class="right chevron icon""></i>
        </a>`;
    } else if (data === 2 && pageNumber == 2) {
        html += `
        <a class="icon item" onclick="` + getPageNumber(1) + `">
            <i class="left chevron icon" ></i>
        </a>
        <a class="item" onclick="` + getPageNumber(2) + `">2</a>
        `;
    }
    $('#productCategoryPNumber').html(html);
}

function getPageNumber(i) {
    if (searchKey.trim() == "") {
        return "getProductCategoryList(" + i + ")";
    } else {
        let searchKeyTrim = searchKey.trim();
        return "getProductCategoryListSearch('" + searchKeyTrim + "'" + "," + i + ")";
    }
}