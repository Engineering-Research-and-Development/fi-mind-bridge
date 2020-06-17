# Alert Model Mapping

## Description

This entity models an alert and could be used to send alerts related to traffic jam, accidents, weather conditions, high level of pollutants and so on. The purpose of the model is to support the generation of notifications for a user or trigger other actions, based on such alerts.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **Alert Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  category  |  :white_check_mark:  |    |
|  subCategory  |  :white_check_mark:  |    |
|  location  |  :white_check_mark:  |    |
|  address  |  :white_check_mark:  |    |
|  dateIssued  |    |  :white_check_mark:  |
|  validFrom  |  :white_check_mark:  |    |
|  validTo  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  alertSource  |  :white_check_mark:  |    |
|  data  |  :white_check_mark:  |    |
|  severity  |    |  :white_check_mark:  |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/alertNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id":"fimind_alert",
    "type": "Alert",
    "category": {
        "value": "traffic"
    },
    "subCategory": {
        "value": "trafficJam"
    },
    "validTo": {
        "value": "2017-01-02T10:25:55.00Z"
    },
    "description": {
        "value": "The road is completely blocked for 3kms"
    },
    "location": {
        "type": "geo:json",
        "value": {
            "type": "Point",
            "coordinates": [-3.712247222222222, 40.423852777777775]
        }
    },
    "dateIssued": {
        "type": "DateTime",
        "value": "2017-01-02T09:25:55.00Z"
    },
    "alertSource": {
        "value": "https://account.lab.fiware.org/users/8"
    },
    "validFrom": {
        "type": "DateTime",
        "value": "2017-01-02T09:25:55.00Z"
    },
    "severity": {
        "value": "high"
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/alert \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_alert",
    "type": "Alert",
    "category": "security",
    "subCategory": "robbery",
    "location": {
        "type": "Point",
        "coordinates": [-3.712247222222222, 40.423852777777775]
    },
    "dateIssued": "2017-04-25T09:25:55.00Z",
    "description": "Potential robbery in main building",
    "alertSource": "Camera1234",
    "severity": "informational"
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
        "description": "Alert Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "Alert"
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

#### Alert entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_alert",
    "type": "Alert",
    "category": "security",
    "subCategory": "robbery",
    "location": {
        "type": "Point",
        "coordinates": [-3.712247222222222, 40.423852777777775]
    },
    "dateIssued": "2017-04-25T09:25:55.00Z",
    "description": "Potential robbery in main building",
    "alertSource": "Camera1234",
    "severity": "informational"
  }'
```

After entity creation, OCB will notify the state change to FI-MIND which will proceed importing the proper entity into MindSphere.
