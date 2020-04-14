# Vehicle Model Mapping

## Description

A vehicle.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **Vehicle Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  vehicleType  |  :white_check_mark:  |    |
|  category  |  :white_check_mark:  |    |
|  location  |    |  :white_check_mark:  |
|  previousLocation  |    |  :white_check_mark:  |
|  speed  |    |  :white_check_mark:  |
|  heading  |    |  :white_check_mark:  |
|  cargoWeight  |  :white_check_mark:  |    |
|  vehicleIdentificationNumber  |  :white_check_mark:  |    |
|  vehiclePlateIdentifier  |  :white_check_mark:  |    |
|  fleetVehicleId  |  :white_check_mark:  |    |
|  dateVehicleFirstRegistered  |  :white_check_mark:  |    |
|  dateFirstUsed  |  :white_check_mark:  |    |
|  purchaseDate  |  :white_check_mark:  |    |
|  mileageFromOdometer  |    |  :white_check_mark:  |
|  vehicleConfiguration  |  :white_check_mark:  |    |
|  color  |  :white_check_mark:  |    |
|  owner  |  :white_check_mark:  |    |
|  feature  |  :white_check_mark:  |    |
|  serviceProvided  |  :white_check_mark:  |    |
|  vehicleSpecialUsage  |  :white_check_mark:  |    |
|  refVehicleModel  |  :white_check_mark:  |    |
|  areaServed  |  :white_check_mark:  |    |
|  serviceStatus  |    |  :white_check_mark:  |
|  dateModified  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/vehicleNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_vehicle",
    "type": "Vehicle",
    "category": {
        "value": ["municipalServices"]
    },
    "vehicleType": {
        "value": "lorry"
    },
    "name": {
        "value": "C Recogida 1"
    },
    "vehiclePlateIdentifier": {
        "value": "3456ABC"
    },
    "refVehicleModel": {
        "type": "Relationship",
        "value": "vehiclemodel:econic"
    },
    "location": {
        "type": "geo:json",
        "value": {
            "type": "Point",
            "coordinates": [-3.164485591715449, 40.62785133667262]
        },
        "metadata": {
            "timestamp": {
                "type": "DateTime",
                "value": "2018-09-27T12:00:00"
            }
        }
    },
    "areaServed": {
        "value": "Centro"
    },
    "serviceStatus": {
        "value": "onRoute"
    },
    "cargoWeight": {
        "value": 314
    },
    "speed": {
        "value": 50,
        "metadata": {
            "timestamp": {
                "type": "DateTime",
                "value": "2018-09-27T12:00:00"
            }
        }
    },
    "serviceProvided": {
        "value": ["garbageCollection", "wasteContainerCleaning"]
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/vehicle \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_vehicle",
    "type": "Vehicle",
    "vehicleType": "lorry",
    "category": ["municipalServices"],
    "location": {
        "type": "Point",
        "coordinates": [-3.164485591715449, 40.62785133667262]
    },
    "name": "C Recogida 1",
    "speed": 50,
    "cargoWeight": 314,
    "serviceStatus": "onRoute",
    "serviceProvided": ["garbageCollection", "wasteContainerCleaning"],
    "areaServed": "Centro",
    "refVehicleModel": "vehiclemodel:econic",
    "vehiclePlateIdentifier": "3456ABC"
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
        "description": "Vehicle Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "Vehicle"
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

#### Vehicle entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_vehicle",
    "type": "Vehicle",
    "vehicleType": "lorry",
    "category": ["municipalServices"],
    "location": {
        "type": "Point",
        "coordinates": [-3.164485591715449, 40.62785133667262]
    },
    "name": "C Recogida 1",
    "speed": 50,
    "cargoWeight": 314,
    "serviceStatus": "onRoute",
    "serviceProvided": ["garbageCollection", "wasteContainerCleaning"],
    "areaServed": "Centro",
    "refVehicleModel": "vehiclemodel:econic",
    "vehiclePlateIdentifier": "3456ABC"
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
