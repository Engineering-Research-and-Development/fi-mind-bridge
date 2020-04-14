# VehicleModel Model Mapping

## Description

This entity models a particular vehicle model, including all properties which are common to multiple vehicle instances belonging to such model.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **VehicleModel Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  vehicleType  |  :white_check_mark:  |    |
|  brandName  |  :white_check_mark:  |    |
|  modelName  |  :white_check_mark:  |    |
|  manufacturerName  |  :white_check_mark:  |    |
|  vehicleModelDate  |  :white_check_mark:  |    |
|  cargoVolume  |  :white_check_mark:  |    |
|  fuelType  |  :white_check_mark:  |    |
|  fuelConsumption  |  :white_check_mark:  |    |
|  height  |  :white_check_mark:  |    |
|  width  |  :white_check_mark:  |    |
|  depth  |  :white_check_mark:  |    |
|  weight  |  :white_check_mark:  |    |
|  vehicleEngine  |  :white_check_mark:  |    |
|  image  |  :white_check_mark:  |    |
|  url  |  :white_check_mark:  |    |
|  dateModified  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/vehicleModelNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_vehicleModel",
    "type": "VehicleModel",
    "name": {
        "value": "MBenz-Econic2014"
    },
    "cargoVolume": {
        "value": 1000
    },
    "modelName": {
        "value": "Econic"
    },
    "brandName": {
        "value": "Mercedes Benz"
    },
    "manufacturerName": {
        "value": "Daimler"
    },
    "fuelType": {
        "value": "diesel"
    },
    "vehicleType": {
        "value": "lorry"
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/vehicleModel \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_vehicleModel",
    "type": "VehicleModel",
    "name": "MBenz-Econic2014",
    "brandName": "Mercedes Benz",
    "modelName": "Econic",
    "manufacturerName": "Daimler",
    "vehicleType": "lorry",
    "cargoVolume": 1000,
    "fuelType": "diesel"
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
        "description": "VehicleModel Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "VehicleModel"
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

#### VehicleModel entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_vehicleModel",
    "type": "VehicleModel",
    "name": "MBenz-Econic2014",
    "brandName": "Mercedes Benz",
    "modelName": "Econic",
    "manufacturerName": "Daimler",
    "vehicleType": "lorry",
    "cargoVolume": 1000,
    "fuelType": "diesel"
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
