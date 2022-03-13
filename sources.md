# Notes on used sources of data and other information


## OpenStreetmap

OSM Overpass Query Editor and Download
https://overpass-turbo.eu/

OSM Overpass Syntax
* https://wiki.openstreetmap.org/wiki/Overpass_API/Language_Guide#Multiple_tags
* https://dev.overpass-api.de/overpass-doc/en/criteria/union.html

Overpass query to find all relevant locations in Hamburg:

    [out:json];
    ( 
        area[name="Hamburg"]; 
    ) -> .a;
    (
        node [leisure] (area.a);
        node [amenity="bar|biergarten|cafe|fast_food|food_court|ice_cream|pub|restaurant|college|driving_school|kindergarten|la|guage_school|library|toy_library|music_school|school|university|boat_sharing|social_facility|arts_centre|b|othel|casino|cinema|community_centre|conference_centre|events_venue|fountain|gambling|love_hotel|nightclub|planetarium|public_bookcase|social_centre|stripclub|studio|swingerclub|theatre|post_office|ranger_station|townhall|bbq|bench|drinking_water|shelter|water_point|watering_place|animal_boarding|animal_breeding|ani|al_shelter|baking_oven|childcare|dive_centre|grave_yard|gym|hunting_stand|internet_cafe|kitchen|kneipp_w|ter_cure|lounger|marketplace|monastery|photo_booth|place_of_mourning|place_of_worship|public_bath|public_building"] (area.a);
    );
    out;

A list of all amenities list on OSM (including many non-applicable ones like recycling) can be found [here](src/data/amenities.txt).

OSM relevant keys
* https://wiki.openstreetmap.org/wiki/Key:leisure
* https://wiki.openstreetmap.org/wiki/Key:amenity

## Wikipedia Data

* [Wikipedia Download Page](https://de.wikipedia.org/wiki/Wikipedia:Technik/Datenbank/Download)
* [Direct dump download link](https://dumps.wikimedia.org/dewiki/latest/dewiki-latest-pages-articles.xml.bz2) (6GB)

Parsing the data:
* [WikiPDA](https://github.com/epfl-dlab/WikiPDA/tree/master/WikiPDA-Lib) (extracting pages from Wikipedia via API)
* https://m.mediawiki.org/wiki/Alternative_parsers
* https://github.com/axkr/info.bliki.wikipedia_parser

## Other

* [List of 150 hobbies](https://hobbylark.com/misc/How-to-Choose-a-Hobby)

## Algorithm

### Nearest Neighbar
Efficiently finding candidates profiles and locations for joining.  

* [Efficient and robust approximate nearest neighbor search using Hierarchical Navigable Small World graphs
](https://arxiv.org/ftp/arxiv/papers/1603/1603.09320.pdf)
* [HSNW Java implementation](https://github.com/jelmerk/hnswlib)

### Determining Attributes

* [Apache Open NLP](https://opennlp.apache.org/docs/1.7.2/manual/opennlp.html#intro.general.library.structure)
* [Apache Open NLP Git](https://github.com/apache/opennlp)

## UI

Just some collected links:
* https://www.gather.town/
* https://meet.jit.si/
* https://github.com/danielgatis/rembg