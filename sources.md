


#OpenStreetmap

OSM Overpass Query Editor and Download
https://overpass-turbo.eu/

OSM Overpass Syntax
* https://wiki.openstreetmap.org/wiki/Overpass_API/Language_Guide#Multiple_tags
* https://dev.overpass-api.de/overpass-doc/en/criteria/union.html

Overpass query to find all relevant locations in Hamburg:

    ( area(region="Hamburg"]; ) -> .a;
    (
    node [leisure] (area.a);
    node [amenity=bar|biergarten|cafe|fast_food|food_court|ice_cream|pub|restaurant|college|driving_school|kindergarten|la|guage_school|library|toy_library|music_school|school|university|boat_sharing|social_facility|arts_centre|b|othel|casino|cinema|community_centre|conference_centre|events_venue|fountain|gambling|love_hotel|nightclub|planetarium|public_bookcase|social_centre|stripclub|studio|swingerclub|theatre|post_office|ranger_station|townhall|bbq|bench|drinking_water|shelter|water_point|watering_place|animal_boarding|animal_breeding|ani|al_shelter|baking_oven|childcare|dive_centre|grave_yard|gym|hunting_stand|internet_cafe|kitchen|kneipp_w|ter_cure|lounger|marketplace|monastery|photo_booth|place_of_mourning|place_of_worship|public_bath|public_building] (area.a);
    );
    out;

A list of all amenities list on OSM (including many non-applicable ones like recycling) can be found [here](src/data/amenities.txt).

OSM relevant keys
* https://wiki.openstreetmap.org/wiki/Key:leisure
* https://wiki.openstreetmap.org/wiki/Key:amenity


#Wikipedia Data
* [Wikipedia Download Page](https://de.wikipedia.org/wiki/Wikipedia:Technik/Datenbank/Download)
* [Direct dump download link](https://dumps.wikimedia.org/dewiki/latest/dewiki-latest-pages-articles.xml.bz2) (6GB)