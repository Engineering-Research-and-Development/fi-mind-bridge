# DeviceModel Model Mapping

## Description

This entity captures the static properties of a Device.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **DeviceModel Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  category  |  :white_check_mark:  |    |
|  deviceClass  |  :white_check_mark:  |    |
|  controlledProperty  |  :white_check_mark:  |    |
|  function  |  :white_check_mark:  |    |
|  supportedProtocol  |  :white_check_mark:  |    |
|  supportedUnits  |  :white_check_mark:  |    |
|  energyLimitationClass  |  :white_check_mark:  |    |
|  brandName  |  :white_check_mark:  |    |
|  modelName  |  :white_check_mark:  |    |
|  manufacturerName  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  documentation  |  :white_check_mark:  |    |
|  image  |  :white_check_mark:  |    |
|  dateModified  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |
|  owner  |  :white_check_mark:  |    |
|  annotations  |  :white_check_mark:  |    |
|  alternateName  |  :white_check_mark:  |    |
|  color  |  :white_check_mark:  |    |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/deviceModelNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_deviceModel",
    "type": "DeviceModel",
    "category": {
        "value": ["sensor"]
    },
    "function": {
        "value": ["sensing"]
    },
    "modelName": {
        "value": "S4Container 345"
    },
    "name": {
        "value": "myDevice Sensor for Containers 345"
    },
    "brandName": {
        "value": "myDevice"
    },
    "manufacturerName": {
        "value": "myDevice Inc."
    },
    "controlledProperty": {
        "value": ["fillingLevel", "temperature"]
    },
    "supportedUnits": {
        "value": ["%", "c°"]
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/deviceModel \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_deviceModel",
    "type": "DeviceModel",
    "name": "myDevice Sensor for Containers 345",
    "brandName": "myDevice",
    "modelName": "S4Container 345",
    "manufacturerName": "myDevice Inc.",
    "category": ["sensor"],
    "function": ["sensing"],
    "controlledProperty": ["fillingLevel", "temperature"],
    "supportedUnits": ["%", "c°"]
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
        "description": "DeviceModel Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "DeviceModel"
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

#### DeviceModel entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_deviceModel",
    "type": "DeviceModel",
    "name": "myDevice Sensor for Containers 345",
    "brandName": "myDevice",
    "modelName": "S4Container 345",
    "manufacturerName": "myDevice Inc.",
    "category": ["sensor"],
    "function": ["sensing"],
    "controlledProperty": ["fillingLevel", "temperature"],
    "supportedUnits": ["%", "c°"]
    }
}'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
