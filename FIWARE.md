# FI-MIND (FIWARE-MINDSPHERE) Bridge


## Fiware Subscription
 
+ DeviceModel subscription example. 


Subrscription DeviceModel
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

+ Device subscription example

Subrscription Device
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

## Fiware: DeviceModel/Device entity creation on OCB
 
+ DeviceModel Create. 


Create DeviceModel 
```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
  "id": "boardTest",
  "type": "DeviceModel",
  "source": "https://fiware.github.io/dataModels/common-schema.json#/definitions/GSMA-Commons",
  "dataProvider":"https://fiware.github.io/dataModels/specs/Device/device-schema.json#/definitions/Device-Commons",
  "category": ["sensor"],
  "deviceClass ": "C01",
  "controlledProperty": ["humidity","temperature"],
  "function": ["sensing"],
  "supportedProtocol": ["http"],
  "supportedUnits": ["%","C"],
  "energyLimitationClass": "E2",
  "brandName": "myDevice",
  "modelName": "S4Container 345",
  "manufacturerName": "myDevice Inc.",
  "name": "myDevice Sensor for Containers 345",
  "description": "myDevice Sensor for Containers 345",
 "documentation":"https://fiware.github.io/dataModels/specs/Device/device-schema.json#/definitions/Device-Commons",
  "image":"https://github.com/Engineering-Research-and-Development/fi-mind-bridge/raw/master/images/FI-MIND.png",
  "dateModified": "2019-06-24T13:00:00Z",
  "dateCreated": "2019-06-24T13:00:00Z"
}'
```

+ Device Create. 


Create Device 
```sh
curl -X POST \
  http://{ocb-server-url}:{ocb-server-port}/v2/entities?options=keyValues \
  -H 'content-type: application/json' \
  -H 'fiware-service: connector' \
  -H 'fiware-servicepath: /demo' \
  -d '{
  "id": "boardTest",
        "type": "Device",
        "source": "Device Source",
        "dataProvider":"http://person.org/leon",
        "category": ["sensor"],
        "controlledProperty": ["humidity","temperature"],
        "controlledAsset":["wastecontainer-Osuna-100"],
        "mnc": "06",
        "mcc": "217",
        "macAddress":["aa:Bb:00:cc:33:ww"],
        "ipAddress": ["192.14.56.99"],
        "supportedProtocol":["mqtt"],
        "configuration":[],
        "location":{
        	    "type": "Point",
    			"coordinates": [125.6, 10.1]
        },
        "name": "device3name",
        "description": "device2description",
        "dateInstalled":"2019-06-25T14:00:00Z",
        "dateFirstUsed": "2019-06-25T14:00:00Z",
        "dateManufactured":"2019-06-25T14:00:00Z",
        "hardwareVersion": "0.1",
        "softwareVersion": "0.1",
        "firmwareVersion": "0.1",
        "osVersion": "0.1",
        "dateLastCalibration": "2019-06-25T14:00:00Z",
        "serialNumber": "9845A",
        "provider": "company X",
        "refDeviceModel":"myDevice-wastecontainer-sensor-345",
        "batteryLevel": 0.75,
        "rssi": 0.86,
        "deviceState": "ok",
        "dateLastValueReported": "2019-06-25T14:00:00Z",
        "value": "h%3D0.22%3Bt%3D21.2",
        "dateModified": "2019-06-25T14:00:00Z",
        "dateCreated":  "2019-06-25T14:00:00Z",
        "owner": ["http://person.org/leon"]
}'
```



