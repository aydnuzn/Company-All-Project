function addDistrictSelect(data) {

    let html = `<option data-subtext="0" value="0">Seçim Yapınız</option>`;
    if (data != null) {
        for (let i = 0; i < data.COUNT; i++) {
            const item = data.RESULT[i];
            html += `<option data-subtext=` + item.district_name + ` value=` + item.id + `>` + item.district_name + `</option>`;
        }
    }
    $('#company_district').html(html);
    //$("#company_district").select('refresh'); //Hata veriyor.
};

function getXDistrict(index) {
    $.ajax({
        url: 'http://localhost:8091/getXDistricts/' + index,
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log(data);
                addDistrictSelect(data);//Seçilen ile göre ilçeleri getirme.
            } else {
                console.log("Seçilen il numarasına kayıtlı ilçe bulunamadı.");
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

$('#company_city').change(function () {
    getXDistrict($("#company_city option:selected").attr("value"));
});
