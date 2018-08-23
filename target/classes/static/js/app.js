
    var map;
    var markers = {};


    function initMap() {
        var icon = {
            url: "/img/jumbo.png", // url
            scaledSize: new google.maps.Size(50, 50), // scaled size
            origin:
            new google.maps.Point(0, 0), // origin
            anchor: new google.maps.Point(0, 0) // anchor
        };

        map = new google.maps.Map(document.getElementById('map'), {
            center: {
                lat: 52.3702,
                lng: 4.8952
            },
            zoom: 8
        });


        fetch('/store', {method: 'GET'})
            .then(function (response) {
                return response.json();
            })
            .then(function (stores) {
                stores.forEach(function (element) {
                    var marker = new google.maps.Marker({
                        position: {lat: element.location.latitude, lng: element.location.longitude},
                        map: map,
                        uuid:element.links[0].href
                    });
                    debugger;
                   markers[element.uuid] = marker;
                    marker.addListener('click', function (arg) {

                        fetch(this.uuid, {method: 'GET'})
                            .then(function (response) {
                                return response.json();
                            }).then(function (storeitem) {

                              var content = '<div id=\"content\">' +
                                ' <br> <b>Address Name:</b>' + storeitem.address.addressName +
                                '<br> <b>City:    </b>' + storeitem.address.city +
                                '<br> <b>Postal Code:    </b>' + storeitem.address.postalCode +
                                '<br> <b>Street:    </b>' + storeitem.address.street +
                                '<br> <b>Street2:    </b>' + storeitem.address.street2 +
                                '<br> <b>Street3:    </b>' + storeitem.address.street3 +
                                '<br> <b>Lat:    </b>' + storeitem.location.latitude +
                                '<br> <b>Long:    </b>' + storeitem.location.longitude +
                                '<br> <b>Today Open:    </b>' + padDigits(storeitem.todayOpen.hour, 2) + ':' + padDigits(storeitem.todayOpen.minute, 2) +
                                '<br> <b>Today Close:    </b>' + padDigits(storeitem.todayClose.hour, 2) + ':' + padDigits(storeitem.todayClose.minute, 2) +
                                '</div>';

                                var infowindow = new google.maps.InfoWindow({
                                    content: content
                                });

                                 infowindow.open(map, marker);

                            }).catch(function (error) {
                                console.log('Request failed', error)
                            });
                    });
                });
            })
            .catch(function (error) {
                console.log('Request failed', error)
            });

        map.addListener('click', function (arg) {
            let stores = fetch('/store/neighbors?lat=' + arg.latLng.lat() + "&lang=" + arg.latLng.lng() + "&numberOfNeighbors=5", {
                method: 'GET',
            }).then(function (response) {
                return response.json();
            }).then(function (stores) {
                stores.forEach(function (element) {
                    let uid = element.uuid;
                    markers[uid].setAnimation(google.maps.Animation.BOUNCE);
                    setTimeout(function () {
                        markers[uid].setAnimation(null);
                    }, 750);
                });
            }).catch(function (error) {
                console.log('Request failed', error)
            });
        });

    }

    function padDigits(number, digits) {
        return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
    }





















