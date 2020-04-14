# WeatherForecast Model Mapping

## Description

This entity contains a harmonised description of a Weather Forecast. This entity is primarily associated with the vertical segments of the environment and agriculture but is applicable to many different applications.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **WeatherForecast Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
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
|  dateRetrieved  |    |  :white_check_mark:  |
|  dateIssued  |    |  :white_check_mark:  |
|  validity  |    |  :white_check_mark:  |
|  validFrom  |    |  :white_check_mark:  |
|  validTo  |    |  :white_check_mark:  |
|  source  |  :white_check_mark:  |    |
|  refPointOfInterest  |  :white_check_mark:  |    |
|  weatherType  |    |  :white_check_mark:  |
|  visibility  |    |  :white_check_mark:  |
|  temperature  |    |  :white_check_mark:  |
|  feelsLikeTemperature  |    |  :white_check_mark:  |
|  relativeHumidity  |    |  :white_check_mark:  |
|  precipitationProbability  |    |  :white_check_mark:  |
|  windDirection  |    |  :white_check_mark:  |
|  windSpeed  |    |  :white_check_mark:  |
|  dayMinimum  |    |  :white_check_mark:  |
|  dayMaximum  |    |  :white_check_mark:  |
|  uVIndexMax  |    |  :white_check_mark:  |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/weatherForecastNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_weatherForecast",
    "type": "WeatherForecast",
    "dayMinimum": {
        "value": {
            "feelsLikeTemperature": 11,
            "temperature": 11,
            "relativeHumidity": 0.7
      }
    },
    "feelsLikeTemperature": {
        "value": 12
    },
    "dataProvider": {
        "value": "TEF"
    },
    "temperature": {
        "value": 12
    },
    "validTo": {
        "type": "DateTime",
        "value": "2016-12-01T23:00:00.00Z"
    },
    "weatherType": {
        "value": "overcast"
    },
    "precipitationProbability": {
        "value": 0.15
    },
    "dayMaximum": {
        "value": {
            "feelsLikeTemperature": 15,
            "temperature": 15,
            "relativeHumidity": 0.9
        }
    },
    "source": {
        "value": "http://www.aemet.es/xml/municipios/localidad_46250.xml"
    },
    "windSpeed": {
        "value": 0
    },
    "validity": {
        "value": "2016-12-01T18:00:00+01:00/2016-12-02T00:00:00+01:00"
    },
    "dateIssued": {
        "type": "DateTime",
        "value": "2016-12-01T10:40:01.00Z"
    },
    "address": {
        "type": "PostalAddress",
        "value": {
            "addressCountry": "Spain",
            "postalCode": "46005",
            "addressLocality": "Valencia"
        }
    },
    "dateRetrieved": {
        "type": "DateTime",
        "value": "2016-12-01T12:57:24.00Z"
    },
    "validFrom": {
        "type": "DateTime",
        "value": "2016-12-01T17:00:00.00Z"
    },
    "relativeHumidity": {
        "value": 0.85
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/weatherForecast \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_weatherForecast",
    "type": "WeatherForecast",
    "address": {
        "addressCountry": "Spain",
        "postalCode": "46005",
        "addressLocality": "Valencia"
    },
    "dataProvider": "TEF",
    "dateIssued": "2016-12-01T10:40:01.00Z",
    "dateRetrieved": "2016-12-01T12:57:24.00Z",
    "dayMaximum": {
        "feelsLikeTemperature": 15,
        "temperature": 15,
        "relativeHumidity": 0.9
    },
    "dayMinimum": {
        "feelsLikeTemperature": 11,
        "temperature": 11,
        "relativeHumidity": 0.7
    },
    "feelsLikeTemperature": 12,
    "precipitationProbability": 0.15,
    "relativeHumidity": 0.85,
    "source": "http://www.aemet.es/xml/municipios/localidad_46250.xml",
    "temperature": 12,
    "validFrom": "2016-12-01T17:00:00.00Z",
    "validTo": "2016-12-01T23:00:00.00Z",
    "validity": "2016-12-01T18:00:00+01:00/2016-12-02T00:00:00+01:00",
    "weatherType": "overcast",
    "windSpeed": 0
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
        "description": "WeatherForecast Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "WeatherForecast"
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

#### WeatherForecast entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_weatherForecast",
    "type": "WeatherForecast",
    "address": {
        "addressCountry": "Spain",
        "postalCode": "46005",
        "addressLocality": "Valencia"
    },
    "dataProvider": "TEF",
    "dateIssued": "2016-12-01T10:40:01.00Z",
    "dateRetrieved": "2016-12-01T12:57:24.00Z",
    "dayMaximum": {
        "feelsLikeTemperature": 15,
        "temperature": 15,
        "relativeHumidity": 0.9
    },
    "dayMinimum": {
        "feelsLikeTemperature": 11,
        "temperature": 11,
        "relativeHumidity": 0.7
    },
    "feelsLikeTemperature": 12,
    "precipitationProbability": 0.15,
    "relativeHumidity": 0.85,
    "source": "http://www.aemet.es/xml/municipios/localidad_46250.xml",
    "temperature": 12,
    "validFrom": "2016-12-01T17:00:00.00Z",
    "validTo": "2016-12-01T23:00:00.00Z",
    "validity": "2016-12-01T18:00:00+01:00/2016-12-02T00:00:00+01:00",
    "weatherType": "overcast",
    "windSpeed": 0
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
