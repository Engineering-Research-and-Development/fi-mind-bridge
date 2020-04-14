# Device Model Mapping

## Description

An apparatus (hardware + software + firmware) intended to accomplish a particular task (sensing the environment, actuating, etc.). A Device is a tangible object which contains some logic and is producer and/or consumer of data. A Device is always assumed to be capable of communicating electronically via a network.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

|  **Device Attribute**  |  **Asset Variable**  |  **Aspect Variable**  |
|-----------------------------|:--------------------------:|:--------------------------:|
|  id  |  :white_check_mark:  |    |
|  type  |  :white_check_mark:  |    |
|  source  |  :white_check_mark:  |    |
|  dataProvider  |  :white_check_mark:  |    |
|  category  |  :white_check_mark:  |    |
|  controlledProperty  |  :white_check_mark:  |    |
|  controlledAsset  |    |  :white_check_mark:  |
|  mnc  |  :white_check_mark:  |    |
|  mcc  |  :white_check_mark:  |    |
|  macAddress  |  :white_check_mark:  |    |
|  ipAddress  |    |  :white_check_mark:  |
|  supportedProtocol  |  :white_check_mark:  |    |
|  configuration  |  :white_check_mark:  |    |
|  location  |  :white_check_mark:  |    |
|  name  |  :white_check_mark:  |    |
|  description  |  :white_check_mark:  |    |
|  dateInstalled  |  :white_check_mark:  |    |
|  dateFirstUsed  |  :white_check_mark:  |    |
|  dateManufactured  |  :white_check_mark:  |    |
|  hardwareVersion  |    |  :white_check_mark:  |
|  softwareVersion  |    |  :white_check_mark:  |
|  firmwareVersion  |    |  :white_check_mark:  |
|  osVersion  |    |  :white_check_mark:  |
|  dateLastCalibration  |    |  :white_check_mark:  |
|  serialNumber  |  :white_check_mark:  |    |
|  provider  |  :white_check_mark:  |    |
|  refDeviceModel  |  :white_check_mark:  |    |
|  batteryLevel  |    |  :white_check_mark:  |
|  rssi  |    |  :white_check_mark:  |
|  deviceState  |    |  :white_check_mark:  |
|  dateLastValueReported  |    |  :white_check_mark:  |
|  value  |    |  :white_check_mark:  |
|  dateModified  |    |  :white_check_mark:  |
|  dateCreated  |  :white_check_mark:  |    |
|  owner  |  :white_check_mark:  |    |

## Usage

Some examples below:

#### NGSI example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/deviceNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_device",
    "type": "Device",
    "category": {
        "value": ["sensor"]
    },
    "batteryLevel": {
        "value": 0.75
    },
    "dateFirstUsed": {
        "type": "DateTime",
        "value": "2014-09-11T11:00:00Z"
    },
    "controlledAsset": {
        "value": ["wastecontainer-Osuna-100"]
    },
    "serialNumber": {
        "value": "9845A"
    },
    "mcc": {
        "value": "214"
    },
    "value": {
        "value": "l%3D0.22%3Bt%3D21.2"
    },
    "refDeviceModel": {
        "type": "Relationship",
        "value": "myDevice-wastecontainer-sensor-345"
    },
    "rssi": {
        "value": 0.86
    },
    "controlledProperty": {
        "value": ["fillingLevel", "temperature"]
    },
    "owner": {
        "value": ["http://person.org/leon"]
    },
    "mnc": {
        "value": "07"
    },
    "ipAddress": {
        "value": ["192.14.56.78"]
    },
    "deviceState": {
        "value": "ok"
    }
}'
```
#### Key-value pairs example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/device \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"fimind_device",
    "type": "Device",
    "category": ["sensor"],
    "controlledProperty": ["fillingLevel","temperature"],
    "controlledAsset":["wastecontainer-Osuna-100"],
    "ipAddress": ["192.14.56.78"],
    "mcc": "214",
    "mnc": "07",
    "batteryLevel": 0.75,
    "serialNumber": "9845A",
    "refDeviceModel":"myDevice-wastecontainer-sensor-345",
    "rssi": 0.86,
    "value": "l=0.22;t=21.2",
    "deviceState": "ok",
    "dateFirstUsed": "2014-09-11T11:00:00Z",
    "owner": ["http://person.org/leon"]
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
        "description": "Device Test Sub",
        "subject": {
          "entities": [
            {
              "idPattern": ".*",
              "type": "Device"
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

#### Device entity creation on OCB

```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
    "id":"fiware_device",
    "type": "Device",
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
