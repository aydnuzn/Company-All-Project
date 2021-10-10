let liveLocation;

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
        var newContent = prompt("Note");
        var newIconImage;
        if (confirm("Görüntü bayrak olsun mu?")) {
            newIconImage = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
        }

        // Add marker
        var newProps = {
            coords: event.latLng,
            iconImage: newIconImage,
            content: '<h1>' + newContent + '</h1>'
        };
        addMarker(newProps);
    });

    // Loop through markers
    for (var i = 0; i < markers.length; i++) {
        // Add marker
        addMarker(markers[i]);
    }

    // Add Marker Function
    function addMarker(props) {
        var marker = new google.maps.Marker({
            position: props.coords,
            map: map,
        });

        // Check for customicon
        if (props.iconImage) {
            // Set icon image
            marker.setIcon(props.iconImage);
        }

        // Check content
        if (props.content) {
            var infoWindow = new google.maps.InfoWindow({
                content: props.content
            });

            marker.addListener('click', function () {
                infoWindow.open(map, marker);
            });
        }
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

//Ajax ile veritabanından gelecek.
// Array of markers
var markers = [
    {
        coords: {lat: 42.4668, lng: -70.9495},
        iconImage: 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
        content: '<h1>Lynn MA</h1>'
    },
    {
        coords: {lat: 42.8584, lng: -70.9300},
        content: '<h1>Amesbury MA</h1>'
    },
    {
        coords: {lat: 42.7762, lng: -71.0773}
    }
];