# FI-MIND (FIWARE-MINDSPHERE) Bridge
The FI-MIND bridge close the gap between MindSphere and FIWARE environments, easing the process of sharing assets and context data (legacy factory systems, non-production IoT data sources, etc.) between the two worlds.
![FI-MIND Schema](docs/images/FI-MIND.png)

## Contents

-   [Configuration](#configuration)
-   [Deploy](#deploy)
-   [API](#api)
-   [Supported Features](#supported-features)
-   [Testing](#testing)
-   [License](#license)

## Configuration
Configure MINDSPHERE settings in *config.properties* file:
```sh
ocb-url=http://{ocb-host}:{ocb-port}
client-id={mindpshere-client-id}
client-secret={mindpshere-client-secret}
tenant={mindpshere-tenant}
```
## Deploy
Generate WAR file executing into project root folder:
```sh
mvn clean install
```
Deploy WAR file (generated into target folder), as usual, in your web server.


## API

|     |     Service     |                          Description                          |
| --- | :-------------: | :-----------------------------------------------------------: |
| POST | `/ocb-export` | Export an existing MindSphere asset into Orion Context Broker |
| POST | `/fiware-notification` | Notification service to ingest data coming after subscription to Orion Context Broker |
||
| POST | `/alert` | Import an Alert entity into MindSphere |
| DELETE | `/alert?id={alert_id}` | Delete an Alert Asset from MindSphere |
| POST | `/alertNormalized` | Import a NGSI compliant Alert entity into MindSphere |
| POST | `/building` | Import a Building entity into MindSphere |
| DELETE | `/building?id={building_id}` | Delete a Building Asset from MindSphere |
| POST | `/buildingNormalized` | Import a NGSI compliant Building entity into MindSphere |
| POST | `/buildingOperation` | Import a BuildingOperation entity into MindSphere |
| DELETE | `/buildingOperation?id={buildingOperation_id}` | Delete a BuildingOperation Asset from MindSphere |
| POST | `/buildingOperationNormalized` | Import a NGSI compliant Building Operation entity into MindSphere |
| POST | `/device` | Import a Device entity into MindSphere |
| DELETE | `/device?id={device_id}` | Delete a Device Asset from MindSphere |
| POST | `/deviceNormalized` | Import a NGSI compliant Device entity into MindSphere |
| POST | `/deviceModel` | Import a Device Model entity into MindSphere |
| DELETE | `/deviceModel?id={deviceModel_id}` | Delete a DeviceModel Asset from MindSphere |
| POST | `/deviceModelNormalized` | Import a NGSI compliant Device Model entity into MindSphere |
| POST | `/trafficFlowObserved` | Import a TrafficFlowObserved entity into MindSphere |
| DELETE | `/trafficFlowObserved?id={trafficFlowObserved_id}` | Delete a TrafficFlowObserved Asset from MindSphere |
| POST | `/trafficFlowObservedNormalized` | Import a NGSI compliant TrafficFlowObserved entity into MindSphere |
| POST | `/vehicle` | Import a Vehicle entity into MindSphere |
| DELETE | `/vehicle?id={vehicle_id}` | Delete a Vehicle Asset from MindSphere |
| POST | `/vehicleNormalized` | Import a NGSI compliant Vehicle entity into MindSphere |
| POST | `/vehicleModel` | Import a VehicleModel entity into MindSphere |
| DELETE | `/vehicleModel?id={vehicleModel_id}` | Delete a VehicleModel Asset from MindSphere |
| POST | `/vehicleModelNormalized` | Import a NGSI compliant VehicleModel entity into MindSphere |
| POST | `/weatherForecast` | Import a Weather Forecast entity into MindSphere |
| DELETE | `/weatherForecast?id={weatherForecast_id}` | Delete a WeatherForecast Asset from MindSphere |
| POST | `/weatherForecastNormalized` | Import a NGSI compliant WeatherForecast entity into MindSphere |
| POST | `/weatherObserved` | Import a Weather Observed entity into MindSphere |
| DELETE | `/weatherObserved?id={weatherObserved_id}` | Delete a WeatherObserved Asset from MindSphere |
| POST | `/weatherObservedNormalized` | Import a NGSI compliant Weather Observed entity into MindSphere |
||
| POST | `/aas` | Import an Asset Administration Shell entity into MindSphere |
| DELETE | `/aas?id={aas_id}` | Delete an Asset Administration Shell Asset from MindSphere |

## Supported Features
FI-MIND helps you setting up a two-way channel to share context data between FIWARE and MindSphere, back and forth. The two different channels are described below:

##### FIWARE to MindSphere
This channel let you export a subset of FIWARE Data Models into Mindsphere Environment *without loss of information*. In the table below is reported the documentation for each of the chosen FIWARE Data Models to be shared in MindSphere.

| __**FIWARE Data Model**__   | __**Status**__   | __**Comment**__   |__**FI-MIND Documentation**__   |
|-----------------------------|:--------------------------:|--------------------------|:--------------------------:|
|  [AeroAllergenObserved](https://fiware-datamodels.readthedocs.io/en/latest/Environment/AeroAllergenObserved/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [AirQualityObserved](https://fiware-datamodels.readthedocs.io/en/latest/Environment/AirQualityObserved/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Alert](https://fiware-datamodels.readthedocs.io/en/latest/Alert/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/alert.md)  |
|  [ArrivalEstimation](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/ArrivalEstimation/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Beach](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/Beach/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [BikeHireDockingStation](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Bike/BikeHireDockingStation/doc/spec/index.html)     |  :x:  |Out of Domain|  |
|  [Building](https://fiware-datamodels.readthedocs.io/en/latest/Building/Building/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/building.md)  |
|  [BuildingOperation](https://fiware-datamodels.readthedocs.io/en/latest/Building/BuildingOperation/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/buildingoperation.md)  |
|  [Device](https://fiware-datamodels.readthedocs.io/en/latest/Device/Device/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/device.md)   |
|  [DeviceModel](https://fiware-datamodels.readthedocs.io/en/latest/Device/DeviceModel/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/devicemodel.md)   |
|  [EVChargingStation](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/EVChargingStation/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [FlowerBed](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/FlowerBed/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Garden](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/Garden/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GreenSpaceRecord](https://fiware-datamodels.readthedocs.io/en/latest/ParksAndGardens/GreenspaceRecord/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsAccessPoint](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsAccessPoint/doc/spec/index.html)|  :x:  |  Out of Domain  |  |
|  [GtfsAgency](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsAgency/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsCalendarDataRule](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsCalendarDateRule/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsCalendarRule](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsCalendarRule/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsFrequency](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsFrequency/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsRoute](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsRoute/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsService](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsService/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsShape](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsShape/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsStation](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStation/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsStop](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStop/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsStopTime](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsStopTime/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsTransferRule](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsTransferRule/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [GtfsTrip](https://fiware-datamodels.readthedocs.io/en/latest/UrbanMobility/GtfsTrip/doc/spec/index.html)  | :x:  |  Out of Domain|  |
|  [KeyPerformanceIndicator](https://fiware-datamodels.readthedocs.io/en/latest/KeyPerformanceIndicator/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Museum](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/Museum/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [NoiseLevelObserved](https://fiware-datamodels.readthedocs.io/en/latest/Environment/NoiseLevelObserved/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [OffStreetParking](https://fiware-datamodels.readthedocs.io/en/latest/Parking/OffStreetParking/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [OnStreetParking](https://fiware-datamodels.readthedocs.io/en/latest/Parking/OnStreetParking/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Open311:ServiceRequest](https://fiware-datamodels.readthedocs.io/en/latest/IssueTracking/Open311_ServiceRequest/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Open311:ServiceType](https://fiware-datamodels.readthedocs.io/en/latest/IssueTracking/Open311_ServiceType/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [ParkingAccess](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingAccess/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [ParkingGroup](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingGroup/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [ParkingSpot](https://fiware-datamodels.readthedocs.io/en/latest/Parking/ParkingSpot/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [PointOfInterest](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInterest/PointOfInterest/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Road](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Road/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [RoadSegment](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/RoadSegment/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [SmartPointOfInteraction](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInteraction/SmartPointOfInteraction/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [SmartSpot](https://fiware-datamodels.readthedocs.io/en/latest/PointOfInteraction/SmartSpot/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [Streetlight](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/Streetlight/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [StreetlightModel](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightModel/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [StreetlightGroup](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightGroup/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [StreetlightControlCabinet](https://fiware-datamodels.readthedocs.io/en/latest/StreetLighting/StreetlightControlCabinet/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [ThreePhaseAcMeasurement](https://fiware-datamodels.readthedocs.io/en/latest/Energy/ThreePhaseAcMeasurement/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [TrafficFlowObserved](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/TrafficFlowObserved/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/trafficflowobserved.md)  |
|  [Vehicle](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Vehicle/Vehicle/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/vehicle.md)  |
|  [VehicleModel](https://fiware-datamodels.readthedocs.io/en/latest/Transportation/Vehicle/VehicleModel/doc/spec/index.html)  |  :white_check_mark:  | | :books: [Documentation](docs/vehiclemodel.md)  |
|  [WasteContainer](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainer/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [WasteContainerIsle](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainerIsle/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [WasteContainerModel](https://fiware-datamodels.readthedocs.io/en/latest/WasteManagement/WasteContainerModel/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [WaterQualityObserved](https://fiware-datamodels.readthedocs.io/en/latest/Environment/WaterQualityObserved/doc/spec/index.html)  |  :x:  |  Out of Domain  |  |
|  [WeatherForecast](https://fiware-datamodels.readthedocs.io/en/latest/Weather/WeatherForecast/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/weatherforecast.md)  |
|  [WeatherObserved](https://fiware-datamodels.readthedocs.io/en/latest/Weather/WeatherObserved/doc/spec/index.html)  |  :white_check_mark:  |  |  :books: [Documentation](docs/weatherobserved.md)  |

##### MindSphere to FIWARE

Given an existing asset in MindSphere, FIMIND let you export the resource to FIWARE, simply by passing: __assetId__, __fiwareService__ and __fiwareServicePath__ as body of a HTTP POST request under the path of /ocb-export.

The export process will extract asset variables and aspect variables of the chosen asset mapping them in the proper format to be ingested by [Orion Context Broker](https://fiware-orion.readthedocs.io/en/master/).

Exporting is not restricted to FIWARE Data Models only, **every MindSphere asset** can be exported even though the mapping **may not be reversible without extending the current APIs.**

###### Example

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/ocb-export \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "assetId":"mindsphere_asset_id",
    "fiwareService":"connector",
    "fiwareServicePath":"/demo"
  }'

```

##### Asset Administration Shell to MindSphere
FI-MIND bridge, expressing its interest in Industry 4.0 trend, enables support for [Asset Administration Shell (AAS)](https://www.zvei.org/en/subjects/industrie-4-0/details-of-the-asset-administration-shell/), a key concept of I4.0, used to describe an asset electronically in a standardized manner.  Its purpose is to exchange asset-related data among industrial assets and between assets and production orchestration systems or engineering tools. Given the high complexity of a AAS model,

FI-MIND bridge currently only supports an unidirectional binding, thus, not allowing you to export a AAS model from the corresponding Mindsphere Asset.

| __**ZVEI Data Model**__   | __**Status**__   | __**Comment**__   |__**FI-MIND Documentation**__   |
|-----------------------------|:--------------------------:|--------------------------|:--------------------------:|
|  [Asset Administration Shell](https://www.plattform-i40.de/PI40/Redaktion/EN/Downloads/Publikation/vws-in-detail-presentation.pdf?__blob=publicationFile&v=10)  |  :construction:  |  |  :books: [Documentation](docs/aas.md)  |

## Testing
You can test POST REST API by adding the header:

```
debug-mode : true
```
to the request.

##### Example

> Request

```sh
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/alert \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'debug-mode: true' \
  -d '{
	"id":"fimind_alert_test",
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
> Response

```json
{
    "result": "Test gone fine",
    "message": null
}
```

WebServer logs will, then,  display the proper mapping of the given entity for MindSphere.

## License
The FI-MIND bridge is licensed under the

GNU Affero General Public License v3.0
