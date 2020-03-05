# FI-MIND (FIWARE-MINDSPHERE) Bridge
The FI-MIND bridge allows to enrich MindSphere environment with FIWARE context data (legacy factory systems, non-production IoT data sources, etc.)
![FI-MIND Schema](https://github.com/Engineering-Research-and-Development/fi-mind-bridge/blob/master/images/FI-MIND.png)

## Configuration
Configure MINDSPHERE settings in *config.properties* file:
```sh
client-id={mindpshere-client-id}
client-secret={mindpshere-client-secret}
tenant={mindpshere-tenant}
```
## Deploy
Generate WAR file executing into project root folder:
```sh
mvn clean install
```
Deploy WAR file (generated into target folder) as usual in your web server.

## Usage
Some examples below:
```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/deviceModel \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"board1",
	"type":"deviceModel",
	"manufacturerName":"Sensirion",
	"modelName":"SHT10",
	"brandName":"Sensirion",
	"category":"soilBoard",
	"controlledProperty":["temperature","humidity"],
	"supportedUnits":["C","%"]
}'
```
```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/deviceNormalized \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"id":"board1",
	"type": "device",
	"category":{
		"value":"soilBoard"
	},
	"location":{
    	"type": "Point",
    	"coordinates": [125.6, 10.1]
	},
	"controlledProperty":{
		"value":["temperature","humidity"]
	},
	"value":{
		"value":"t=19.0;h=59",
		"metadata": {
            "timeInstant": {
                "type": "DateTime",
                "value": "2019-04-01T18:51:28.628Z"
            }
        }
	}
}'
```
## Fiware Subscription
Fi-mind bridge is able to receive notifications from OCB and after create an Asset on MindSphere. 
Example of subscription of a DeviceModel. 


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

[complete FIWARE example](FIWARE.md)


## Supported Features


| __**FIWARE Data Model**__   | __**Status**__   | __**Comment**__   |__**Source**__   |
|-----------------------------|:--------------------------:|--------------------------|--------------------------|
|  AeroAllergenObserved     |      :x:    |Out of Domain|[Aero Allergen Observed data model](https://fiware-datamodels.readthedocs.io/en/latest/Environment/AeroAllergenObserved/doc/spec/index.html)|
|  AirQualityObserved     |      :x:    |Out of Domain|[Air Quality Observed data model](https://fiware-datamodels.readthedocs.io/en/latest/Environment/AirQualityObserved/doc/spec/index.html)|
|  Alert    |      :new_moon:    ||[Alert data model](https://fiware-datamodels.readthedocs.io/en/latest/Alert/doc/spec/index.html)|
|  ArrivalEstimation    |      :x:    |Out of Domain|[Arrival Estimation data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/ArrivalEstimation/doc/spec/index.html)|
|  Beach     |      :x:    |Out of Domain|[Beach data model](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/Beach/doc/spec/index.html)|
|  BikeHireDockingStation     |      :x:    |Out of Domain|[Bike Hire Docking Station data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Bike/BikeHireDockingStation/doc/spec/index.html)|
|  Building     |      :new_moon:    ||[Building data model](https://fiware-datamodels.readthedocs.io/en/latest/Building/Building/doc/spec/index.html)|
|  BuildingOperation     |      :new_moon:    ||[Building Operation data model](https://fiware-datamodels.readthedocs.io/en/latest/Building/BuildingOperation/doc/spec/index.html)|
|  Device     |      :white_check_mark:    ||[Device data model](https://fiware-datamodels.readthedocs.io/en/latest/Device/Device/doc/spec/index.html)|
|  DeviceModel     |     :white_check_mark:     ||[Device Model data model](https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html)|
|  EVChargingStation     |      :x:    |Out of Domain|[Electric Vehicle Charging Station data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/EVChargingStation/doc/spec/index.html)|
|  FlowerBed     |      :x:    |Out of Domain|[Flower Bed data model](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/FlowerBed/doc/spec/index.html)|
|  Garden     |      :x:    |Out of Domain|[Garden data model](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/Garden/doc/spec/index.html)|
|  GreenSpaceRecord     |      :x:    |Out of Domain|[Green Space Record data model](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/GreenspaceRecord/doc/spec/index.html)|
|  GtfsAccessPoint     |      :x:    |Out of Domain|[Gtfs Access Point data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsAccessPoint/doc/spec/index.html)|
|  GtfsAgency     |      :x:    |Out of Domain|[Gtfs Agency data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsAgency/doc/spec/index.html)|
|  GtfsCalendarDataRule     |      :x:    |Out of Domain|[Gtfs Calendar Data Rule data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsCalendarDateRule/doc/spec/index.html)|
|  GtfsCalendarRule     |      :x:    |Out of Domain|[Gtfs Calendar Rule data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsCalendarRule/doc/spec/index.html)|
|  GtfsFrequency     |      :x:    |Out of Domain|[Gtfs Frequency data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsFrequency/doc/spec/index.html)|
|  GtfsRoute     |      :x:    |Out of Domain|[Gtfs Route data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsRoute/doc/spec/index.html)|
|  GtfsService     |      :x:    |Out of Domain|[Gtfs Service data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsService/doc/spec/index.html)|
|  GtfsShape     |      :x:    |Out of Domain|[Gtfs Shape data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsShape/doc/spec/index.html)|
|  GtfsStation     |      :x:    |Out of Domain|[Gtfs Station data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStation/doc/spec/index.html)|
|  GtfsStop     |      :x:    |Out of Domain|[Gtfs Stop data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStop/doc/spec/index.html)|
|  GtfsStopTime     |      :x:    |Out of Domain|[Gtfs Stop Time data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStopTime/doc/spec/index.html)|
|  GtfsTransferRule     |      :x:    |Out of Domain|[Gtfs Transfer Rule data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsTransferRule/doc/spec/index.html)|
|  GtfsTrip     |      :x:    |Out of Domain|[Gtfs Trip data model](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsTrip/doc/spec/index.html)|
|  KeyPerformanceIndicator     |      :x:    |Out of Domain|[Key Performance Indicator data model](https://fiware-datamodels.readthedocs.io/en/latest/KeyPerformanceIndicator/doc/spec/index.html)|
|  Museum     |      :x:    |Out of Domain|[Museum data model](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/Museum/doc/spec/index.html)|
|  NoiseLevelObserved     |      :x:    |Out of Domain|[Noise Level Observed data model](https://fiware-datamodels.readthedocs.io/en/latest/Environment/NoiseLevelObserved/doc/spec/index.html)|
|  OffStreetParking     |      :x:    |Out of Domain|[Off Street Parking data model](https://fiware-datamodels.readthedocs.io/en/latest/Parking/OffStreetParking/doc/spec/index.html)|
|  OnStreetParking     |      :x:    |Out of Domain|[On Street Parking data model](https://fiware-datamodels.readthedocs.io/en/latest/Parking/OnStreetParking/doc/spec/index.html)|
|  Open311:ServiceRequest     |      :x:    |Out of Domain|[Open311:ServiceRequest data model](https://fiware-datamodels.readthedocs.io/en/latest/IssueTracking/Open311_ServiceRequest/doc/spec/index.html)|
|  Open311:ServiceType     |      :x:    |Out of Domain|[Open311:ServiceType data model](https://fiware-datamodels.readthedocs.io/en/latest/IssueTracking/Open311_ServiceType/doc/spec/index.html)|
|  ParkingAccess     |      :x:    |Out of Domain|[Parking Access data model](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingAccess/doc/spec/index.html)|
|  ParkingGroup     |      :x:    |Out of Domain|[Parking Group data model](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingGroup/doc/spec/index.html)|
|  ParkingSpot     |      :x:    |Out of Domain|[Parking Stop data model](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingSpot/doc/spec/index.html)|
|  PointOfInterest     |      :x:    |Out of Domain|[Point of Interest data model](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/PointOfInterest/doc/spec/index.html)|
|  Road     |      :x:    |Out of Domain|[Road data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Road/doc/spec/index.html)|
|  RoadSegment     |      :x:    |Out of Domain|[Road Segment data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/RoadSegment/doc/spec/index.html)|
|  SmartPointOfInteraction     |      :x:    |Out of Domain|[Smart Point of Interaction data model](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInteraction/SmartPointOfInteraction/doc/spec/index.html)|
|  SmartSpot     |      :x:    |Out of Domain|[Smart Spot data model](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInteraction/SmartSpot/doc/spec/index.html)|
|  Streetlight     |      :x:    |Out of Domain|[Street Light data model](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/Streetlight/doc/spec/index.html)|
|  StreetlightModel     |      :x:    |Out of Domain|[Street Light Model data model](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightModel/doc/spec/index.html)|
|  StreetlightGroup     |      :x:    |Out of Domain|[Street Light Group data model](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightGroup/doc/spec/index.html)|
|  StreetlightControlCabinet     |      :x:    |Out of Domain|[Street Light Control Cabinet data model](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightControlCabinet/doc/spec/index.html)|
|  ThreePhaseAcMeasurement     |      :x:    |Out of Domain|[Three Phase AC Measurement data model](https://fiware-datamodels.readthedocs.io/en/latest/Energy/ThreePhaseAcMeasurement/doc/spec/index.html)|
|  TrafficFlowObserved     |      :new_moon:    ||[Traffic Flow Observed data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/TrafficFlowObserved/doc/spec/index.html)|
|  Vehicle     |      :new_moon:    ||[Vehicle data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Vehicle/Vehicle/doc/spec/index.html)|
|  VehicleModel     |      :new_moon:    ||[Vehicle Model data model](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Vehicle/VehicleModel/doc/spec/index.html)|
|  WasteContainer     |      :x:    |Out of Domain|[Waste Container data model](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainer/doc/spec/index.html)|
|  WasteContainerIsle     |      :x:    |Out of Domain|[Waste Container Isle data model](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainerIsle/doc/spec/index.html)|
|  WasteContainerModel     |      :x:    |Out of Domain|[Waste Container Model data model](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainerModel/doc/spec/index.html)|
|  WaterQualityObserved     |      :x:    |Out of Domain|[Water Quality data model](https://fiware-datamodels.readthedocs.io/en/latest/Environment/WaterQualityObserved/doc/spec/index.html)|
|  WeatherForecast     |      :new_moon:    ||[Weather Forecast data model](https://fiware-datamodels.readthedocs.io/en/latest/Weather/WeatherForecast/doc/spec/index.html)|
|  WeatherObserved     |      :new_moon:    ||[Weather Observed data model](https://fiware-datamodels.readthedocs.io/en/latest/Weather/WeatherObserved/doc/spec/index.html)|

