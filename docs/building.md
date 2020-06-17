# Building Model Mapping

## Description

This entity contains a harmonised description of a Building. This entity is associated with the vertical segments of smart homes, smart cities, industry and related IoT applications.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **Building Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  dataModified  |    |  :white_check_mark:  |
|  dataCreated  |  :white_check_mark:  |    |
|  owner  |  :white_check_mark:  |    |
|  category  |  :white_check_mark:  |    |
|  location  |  :white_check_mark:  |    |
|  containedInPlace  |  :white_check_mark:  |    |
|  address  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  occupier  |  :white_check_mark:  |    |
|  floorsAboveGround  |  :white_check_mark:  |    |
|  floorsBelowGround  |  :white_check_mark:  |    |
|  mapUrl  |  :white_check_mark:  |    |
| openingHours |    |  :white_check_mark:  |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/buildingNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id": "fimind_building",
    "type": "Building",
    "category": {
        "value": ["office"]
    },
    "floorsBelowGround": {
        "value": 0
    },
    "description": {
        "value": "Office block"
    },
    "floorsAboveGround": {
        "value": 7
    },
    "occupier": {
        "type": "Relationship",
        "value": ["9830f692-7677-11e6-838b-4f9fb3dc5a4f"]
    },
    "mapUrl": {
        "type": "URL",
        "value": "http://www.example.com"
    },
    "dateCreated": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "source": {
        "value": "http://www.example.com"
    },
    "location": {
        "type": "geo:json",
        "value": {
            "type": "Polygon",
            "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
        }
    },
    "address": {
        "type": "PostalAddress",
        "value": {
            "addressLocality": "London",
            "postalCode": "EC4N 8AF",
            "streetAddress": "25 Walbrook"
        }
    },
    "owner": {
        "type": "Relationship",
        "value": [
            "cdfd9cb8-ae2b-47cb-a43a-b9767ffd5c84",
            "1be9cd61-ef59-421f-a326-4b6c84411ad4"
        ]
    },
    "openingHours": {
        "value": ["Mo-Fr 10:00-19:00", "Sa 10:00-22:00", "Su 10:00-21:00"]
    },
    "dataProvider": {
        "value": "OperatorA"
    },
    "dateModified": {
        "type": "DateTime",
        "value": "2016-08-08T10:18:16Z"
    },
    "containedInPlace": {
        "value": {
            "type": "Polygon",
            "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
        }
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/building \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_building",
    "type": "Building",
    "dateCreated": "2016-08-08T10:18:16Z",
    "dateModified": "2016-08-08T10:18:16Z",
    "source": "http://www.example.com",
    "dataProvider": "OperatorA",
    "category": ["office"],
    "containedInPlace": {
        "type": "Polygon",
        "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
    },
    "location": {
        "type": "Polygon",
        "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
    },
    "address": {
        "addressLocality": "London",
        "postalCode": "EC4N 8AF",
        "streetAddress": "25 Walbrook"
    },
    "owner": [
        "cdfd9cb8-ae2b-47cb-a43a-b9767ffd5c84",
        "1be9cd61-ef59-421f-a326-4b6c84411ad4"
    ],
    "occupier": ["9830f692-7677-11e6-838b-4f9fb3dc5a4f"],
    "floorsAboveGround": 7,
    "floorsBelowGround": 0,
    "description": "Office block",
    "mapUrl": "http://www.example.com",
    "openingHours": ["Mo-Fr 10:00-19:00", "Sa 10:00-22:00", "Su 10:00-21:00"]
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
        "description": "Building Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "Building"
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

#### Building entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_building",
    "type": "Building",
    "dateCreated": "2016-08-08T10:18:16Z",
    "dateModified": "2016-08-08T10:18:16Z",
    "source": "http://www.example.com",
    "dataProvider": "OperatorA",
    "category": ["office"],
    "containedInPlace": {
        "type": "Polygon",
        "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
    },
    "location": {
        "type": "Polygon",
        "coordinates": [[[100, 0], [101, 0], [101, 1], [100, 1], [100, 0]]]
    },
    "address": {
        "addressLocality": "London",
        "postalCode": "EC4N 8AF",
        "streetAddress": "25 Walbrook"
    },
    "owner": [
        "cdfd9cb8-ae2b-47cb-a43a-b9767ffd5c84",
        "1be9cd61-ef59-421f-a326-4b6c84411ad4"
    ],
    "occupier": ["9830f692-7677-11e6-838b-4f9fb3dc5a4f"],
    "floorsAboveGround": 7,
    "floorsBelowGround": 0,
    "description": "Office block",
    "mapUrl": "http://www.example.com",
    "openingHours": ["Mo-Fr 10:00-19:00", "Sa 10:00-22:00", "Su 10:00-21:00"]
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
