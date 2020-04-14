# WeatherObserved Model Mapping

## Description

An observation of weather conditions at a certain place and time.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **WeatherObserved Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  dateModified  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  location  |  :white_check_mark:  |    |
|  address  |  :white_check_mark:  |    |
|  dateObserved  |    |  :white_check_mark:  |
|  source  |  :white_check_mark:  |    |
|  refDevice  |  :white_check_mark:  |    |
|  refPointOfInterest  |  :white_check_mark:  |    |
|  weatherType  |    |  :white_check_mark:  |
|  dewPoint  |    |  :white_check_mark:  |
|  visibility  |    |  :white_check_mark:  |
|  temperature  |    |  :white_check_mark:  |
|  relativeHumidity  |    |  :white_check_mark:  |
|  precipitation  |    |  :white_check_mark:  |
|  windDirection  |    |  :white_check_mark:  |
|  windSpeed  |    |  :white_check_mark:  |
|  atmosphericPressure  |    |  :white_check_mark:  |
|  pressureTendency  |    |  :white_check_mark:  |
|  solarRadiation  |    |  :white_check_mark:  |
|  illuminance  |    |  :white_check_mark:  |
|  streamGauge  |    |  :white_check_mark:  |
|  snowHeight  |    |  :white_check_mark:  |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/weatherObservedNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_weatherObserved",
    "type": "WeatherObserved",
    "dateObserved": {
        "type": "DateTime",
        "value": "2016-11-30T07:00:00.00Z"
    },
    "illuminance": {
        "value": 1000
    },
    "temperature": {
        "value": 3.3
    },
    "precipitation": {
        "value": 0
    },
    "atmosphericPressure": {
        "value": 938.9
    },
    "pressureTendency": {
        "value": 0.5
    },
    "refDevice": {
        "type": "Relationship",
        "value": "device-0A3478"
    },
    "source": {
        "value": "http://www.aemet.es"
    },
    "windSpeed": {
        "value": 2
    },
    "location": {
        "type": "geo:json",
        "value": {
            "type": "Point",
            "coordinates": [-4.754444444, 41.640833333]
        }
    },
    "stationName": {
        "value": "Valladolid"
    },
    "address": {
        "type": "PostalAddress",
        "value": {
            "addressLocality": "Valladolid",
            "addressCountry": "ES"
        }
    },
    "stationCode": {
        "value": "2422"
    },
    "dataProvider": {
        "value": "TEF"
    },
    "windDirection": {
        "value": -45
    },
    "relativeHumidity": {
        "value": 1
    },
    "streamGauge": {
        "value": 50
    },
    "snowHeight": {
        "value": 20
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/weatherObserved \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_weatherObserved",
    "type": "WeatherObserved",
    "address": {
        "addressLocality": "Valladolid",
        "addressCountry": "ES"
    },
    "atmosphericPressure": 938.9,
    "dataProvider": "TEF",
    "dateObserved": "2016-11-30T07:00:00.00Z",
    "location": {
        "type": "Point",
        "coordinates": [-4.754444444, 41.640833333]
    },
    "precipitation": 0,
    "pressureTendency": 0.5,
    "relativeHumidity": 1,
    "source": "http://www.aemet.es",
    "stationCode": "2422",
    "stationName": "Valladolid",
    "temperature": 3.3,
    "windDirection": -45,
    "windSpeed": 2,
    "illuminance": 1000
}'
```

## Fiware Subscription

FI-MIND also supports subscription to [Orion Context Broker](https://fiware-orion.readthedocs.io/en/master/).

#### Orion Context Broker subscription

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/subscriptions \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d ' {
        "description": "WeatherObserved Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "WeatherObserved"
            }
          ],
          "condition": {
            "attrs": [
            ]
          }
        },
        "notification": {
          "http": {
            "url": "http://{server-url}:{server-port}/fimind/webapi/fiware-notification"
          },
          "attrs": [
          ],
          "metadata": ["dateCreated", "dateModified"]
        },
        "throttling": 5
      }'
```

#### WeatherObserved entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_weatherObserved",
    "type": "WeatherObserved",
    "address": {
        "addressLocality": "Valladolid",
        "addressCountry": "ES"
    },
    "atmosphericPressure": 938.9,
    "dataProvider": "TEF",
    "dateObserved": "2016-11-30T07:00:00.00Z",
    "location": {
        "type": "Point",
        "coordinates": [-4.754444444, 41.640833333]
    },
    "precipitation": 0,
    "pressureTendency": 0.5,
    "relativeHumidity": 1,
    "source": "http://www.aemet.es",
    "stationCode": "2422",
    "stationName": "Valladolid",
    "temperature": 3.3,
    "windDirection": -45,
    "windSpeed": 2,
    "illuminance": 1000
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
