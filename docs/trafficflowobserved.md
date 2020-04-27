# TrafficFlowObserved Model Mapping

## Description

An observation of traffic flow conditions at a certain place and time. This entity is primarily associated with the Automotive and Smart City vertical segments and related IoT applications.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **TrafficFlowObserved Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  vehicleType  |  :white_check_mark:  |    |
|  vehicleSubType  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  location  |  :white_check_mark:  |    |
|  address  |  :white_check_mark:  |    |
|  refRoadSegment  |  :white_check_mark:  |    |
|  dateModified  |    |  :white_check_mark:  |
|  laneId  |  :white_check_mark:  |    |
|  dateObserved  |    |  :white_check_mark:  |
|  dateObservedFrom  |    |  :white_check_mark:  |
|  dateObservedTo  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  intensity  |    |  :white_check_mark:  |
|  occupancy  |    |  :white_check_mark:  |
|  averageVehicleSpeed  |    |  :white_check_mark:  |
|  averageVehicleLength  |    |  :white_check_mark:  |
|  congested  |    |  :white_check_mark:  |
|  averageHeadwayTime  |    |  :white_check_mark:  |
|  averageGapDistance  |    |  :white_check_mark:  |
|  laneDirection  |    |  :white_check_mark:  |
|  reversedLane  |    |  :white_check_mark:  |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/trafficFlowObservedNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_trafficFlowObserved",
    "type": "TrafficFlowObserved",
    "dateObserved": {
        "value": "2016-12-07T11:10:00/2016-12-07T11:15:00"
    },
    "vehicleType": {
        "value": "car"
    },
    "laneDirection": {
        "value": "forward"
    },
    "dateObservedFrom": {
        "type": "DateTime",
        "value": "2016-12-07T11:10:00"
    },
    "averageVehicleLength": {
        "value": 9.87
    },
    "averageHeadwayTime": {
        "value": 0.5
    },
    "occupancy": {
        "value": 0.76
    },
    "reversedLane": {
        "value": false
    },
    "dateObservedTo": {
        "type": "DateTime",
        "value": "2016-12-07T11:15:00"
    },
    "intensity": {
        "value": 197
    },
    "laneId": {
        "value": 1
    },
    "location": {
        "type": "geo:json",
        "value": {
            "type": "LineString",
            "coordinates": [
                [-4.73735395519672, 41.6538181849672],
                [-4.73414858659993, 41.6600594193478],
                [-4.73447575302641, 41.659585195093]
            ]
        }
    },
    "address": {
        "type": "PostalAddress",
        "value": {
            "addressLocality": "Valladolid",
            "addressCountry": "ES",
            "streetAddress": "Avenida de Salamanca"
        }
    },
    "averageVehicleSpeed": {
        "value": 52.6
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/trafficFlowObserved \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_trafficFlowObserved",
    "type": "TrafficFlowObserved",
    "vehicleType": "car",
    "laneId": 1,
    "address": {
        "streetAddress": "Avenida de Salamanca",
        "addressLocality": "Valladolid",
        "addressCountry": "ES"
    },
    "location": {
        "type": "LineString",
        "coordinates": [
            [-4.73735395519672, 41.6538181849672],
            [-4.73414858659993, 41.6600594193478],
            [-4.73447575302641, 41.659585195093]
        ]
    },
    "dateObserved": "2016-12-07T11:10:00/2016-12-07T11:15:00",
    "dateObservedFrom": "2016-12-07T11:10:00Z",
    "dateObservedTo": "2016-12-07T11:15:00Z",
    "averageHeadwayTime": 0.5,
    "intensity": 197,
    "occupancy": 0.76,
    "averageVehicleSpeed": 52.6,
    "averageVehicleLength": 9.87,
    "reversedLane": false,
    "laneDirection": "forward"
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
        "description": "TrafficFlowObserved Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "TrafficFlowObserved"
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

#### TrafficFlowObserved entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_trafficFlowObserved",
    "type": "TrafficFlowObserved",
    "vehicleType": "car",
    "laneId": 1,
    "address": {
        "streetAddress": "Avenida de Salamanca",
        "addressLocality": "Valladolid",
        "addressCountry": "ES"
    },
    "location": {
        "type": "LineString",
        "coordinates": [
            [-4.73735395519672, 41.6538181849672],
            [-4.73414858659993, 41.6600594193478],
            [-4.73447575302641, 41.659585195093]
        ]
    },
    "dateObserved": "2016-12-07T11:10:00/2016-12-07T11:15:00",
    "dateObservedFrom": "2016-12-07T11:10:00Z",
    "dateObservedTo": "2016-12-07T11:15:00Z",
    "averageHeadwayTime": 0.5,
    "intensity": 197,
    "occupancy": 0.76,
    "averageVehicleSpeed": 52.6,
    "averageVehicleLength": 9.87,
    "reversedLane": false,
    "laneDirection": "forward"
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
