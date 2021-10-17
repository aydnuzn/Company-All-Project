let liveLocation;
var marker;

function initMap() {

    // Map options
    var options = {
        zoom: 8,
        center: {lat: 41.015137, lng: 28.979530}
    };

    // New map
    var map = new google.maps.Map(document.getElementById('map'), options);

    // Listen for click on map
    google.maps.event.addListener(map, 'click', function (event) {
        if (confirm("Konum tanımlama işlemi gerçekleştirilecek emin misiniz?" +
            " (Bu işlem sadece konum tanımmlanmasını sağlar, değişiklik için 'Güncelle' demeyi unutmayın.)")) {
            addMarker(event.latLng);
        }
    });

    $.ajax({
        url: 'http://localhost:8091/rest/admin/settings/getLocation',
        type: 'GET',
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            if (data.RESULT) {
                var dataResult = {lat: parseFloat(data.RESULT[0]), lng: parseFloat(data.RESULT[1])};
                console.log(dataResult);
                marker = new google.maps.Marker({
                    position: dataResult,
                    map: map,
                });
            } else {
                console.log(data);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });

    // Add Marker Function

    function addMarker(props) {
        if (marker == null) {
            marker = new google.maps.Marker({
                position: props,
                map: map,
            });
        } else {
            marker.setPosition(props);
        }
        updateLocation(props);
    }

    //Find Live Location
    var infoWindow = new google.maps.InfoWindow();
    const locationButton = document.createElement("button");
    locationButton.textContent = "Canlı Konumumu Bul";
    locationButton.classList.add("custom-map-control-button");
    map.controls[google.maps.ControlPosition.TOP_CENTER].push(locationButton);
    locationButton.addEventListener("click", () => {
        // Try HTML5 geolocation.
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const pos = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                    };
                    infoWindow.setPosition(pos);
                    infoWindow.setContent("Konumunuz bulundu.");
                    infoWindow.open(map);
                    map.setCenter(pos);
                    liveLocation = pos;
                },
                () => {
                    handleLocationError(true, infoWindow, map.getCenter());
                }
            );
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }
    });
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(
        browserHasGeolocation
            ? "Error: The Geolocation service failed."
            : "Error: Your browser doesn't support geolocation."
    );
    infoWindow.open(map);
}

function updateLocation(props) {
    const arr = {
        lng: props.lng(),
        lat: props.lat()
    };

    $('#company_lng').val(arr.lng);
    $('#company_lat').val(arr.lat);

    /* $.ajax({
         url: 'http://localhost:8091/rest/admin/settings/updateLocation',
         type: 'PUT',
         contentType: "application/json",
         dataType: 'json',
         data: arr,
         success: function (data) {
             if (data) {
                 console.log("Success");
             } else {
                 console.log("Unsuccess");
             }
         },
         error: function (err) {
             console.log(err);
         }
     });*/

}


//-------------İLÇE
function addDistrictSelect(data) {

    let html = `<option data-subtext="0" value="0">Seçim Yapınız</option>`;
    if (data != null) {
        for (let i = 0; i < data.COUNT; i++) {
            const item = data.RESULT[i];
            html += `<option data-subtext=` + item.district_name + ` value=` + item.id + `>` + item.district_name + `</option>`;
        }
    }
    $('#company_district').html(html);
    $("#company_district").select('refresh');
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


//------------------------------------------------------------------------------------------------------------------

setTimeout(() => {
    $('.dismissButton').click();
}, 2000);

//------------------------------------------------------------------------------------------------------------------
