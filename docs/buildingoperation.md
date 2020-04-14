# BuildingOperation Model Mapping

## Description

This entity contains a harmonised description of a generic operation (related to smart buildings) applied to the referenced building. The building operation contains dynamic data reported by, or associated with a building or operations applicable to the building. This entity is associated with the vertical segments of smart homes, smart cities, industry and related IoT applications.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **BuildingOperation Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  dataModified  |    |  :white_check_mark:  |
|  dataCreated  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  refBuilding  |  :white_check_mark:  |    |
|  refOperator  |  :white_check_mark:  |    |
|  operationType  |    |  :white_check_mark:  |
|  status  |    |  :white_check_mark:  |
|  result  |    |  :white_check_mark:  |
|  operationSequence  |  :white_check_mark:  |    |
|  refRelatedBuildingOperation  |  :white_check_mark:  |    |
|  startDate  |  :white_check_mark:  |    |
|  endDate  |  :white_check_mark:  |    |
| dateStarted |    |  :white_check_mark:  |
| dateFinished |    |  :white_check_mark:  |
|  refRelatedDeviceOperation  |  :white_check_mark:  |    |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/buildingOperationNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id": "fimind_buildingOperation",
    "type": "BuildingOperation",
    "status": {
        "value": "finished"
    },
    "startDate": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "operationSequence": {
        "value": ["fan_power%3D0", "set_temperature%3D24"]
    },
    "endDate": {
        "type": "DateTime",
        "value": "2016-08-20T10:18:16Z"
    },
    "description": {
        "value": "Air conditioning levels reduced due to out of hours"
    },
    "refRelatedDeviceOperation": {
        "type": "Relationship",
        "value": [
            "36744245-6716-4a28-84c7-0e3d7520f143",
            "33b2b713-9223-40a5-87a0-3f80a1264a6c"
        ]
    },
    "dateCreated": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "dateModified": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "refRelatedBuildingOperation": {
        "type": "Relationship",
        "value": [
            "b4fb8bff-1a8f-455f-8cc0-ca43c069f865",
            "55c24793-3437-4157-9bda-667c9e1531fc"
        ]
    },
    "source": {
        "value": "http://www.example.com"
    },
    "refBuilding": {
        "type": "Relationship",
        "value": "building-a85e3da145c1"
    },
    "result": {
        "value": "ok"
    },
    "operationType": {
        "value": "airConditioning"
    },
    "dateStarted": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "dateFinished": {
        "type": "DateTime",
        "value": "2016-08-20T10:18:16Z"
    },
    "dataProvider": {
        "value": "OperatorA"
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/buildingOperationNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id":"fimind_buildingOperation",
    "type": "BuildingOperation",
    "dateCreated": "2016-08-08T10:18:16Z",
    "dateModified": "2016-08-08T10:18:16Z",
    "source": "http://www.example.com",
    "dataProvider": "OperatorA",
    "refBuilding": "building-a85e3da145c1",
    "operationType": "airConditioning",
    "description": "Air conditioning levels reduced due to out of hours",
    "result": "ok",
    "startDate": "2016-08-08T10:18:16Z",
    "endDate": "2016-08-20T10:18:16Z",
    "dateStarted": "2016-08-08T10:18:16Z",
    "dateFinished": "2016-08-20T10:18:16Z",
    "status": "finished",
    "operationSequence": ["fan_power=0", "set_temperature=24"],
    "refRelatedBuildingOperation": [
        "b4fb8bff-1a8f-455f-8cc0-ca43c069f865",
        "55c24793-3437-4157-9bda-667c9e1531fc"
    ],
    "refRelatedDeviceOperation": [
        "36744245-6716-4a28-84c7-0e3d7520f143",
        "33b2b713-9223-40a5-87a0-3f80a1264a6c"
    ]
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
        "description": "BuildingOperation Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "BuildingOperation"
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

#### BuildingOperation entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_buildingOperation",
    "type": "BuildingOperation",
    "dateCreated": "2016-08-08T10:18:16Z",
    "dateModified": "2016-08-08T10:18:16Z",
    "source": "http://www.example.com",
    "dataProvider": "OperatorA",
    "refBuilding": "building-a85e3da145c1",
    "operationType": "airConditioning",
    "description": "Air conditioning levels reduced due to out of hours",
    "result": "ok",
    "startDate": "2016-08-08T10:18:16Z",
    "endDate": "2016-08-20T10:18:16Z",
    "dateStarted": "2016-08-08T10:18:16Z",
    "dateFinished": "2016-08-20T10:18:16Z",
    "status": "finished",
    "operationSequence": ["fan_power=0", "set_temperature=24"],
    "refRelatedBuildingOperation": [
        "b4fb8bff-1a8f-455f-8cc0-ca43c069f865",
        "55c24793-3437-4157-9bda-667c9e1531fc"
    ],
    "refRelatedDeviceOperation": [
        "36744245-6716-4a28-84c7-0e3d7520f143",
        "33b2b713-9223-40a5-87a0-3f80a1264a6c"
    ]
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
