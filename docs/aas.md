# Asset Administration Shell Model Mapping

## Description

This entity models an Asset Administration Shell and could be used to monitor digital twins of industrial assets trough MindSphere Fleet Manager.

## Mapping

We can distinguish two major actors in MindSphere ecosystem: _**assets**_ and _**aspects**_ :
* The former represent the digital representation of a machine or an automation system, whose behavior are characterized by properties defined as _**asset variables**_. Asset variables are defined individually on an asset type (thus, aren't grouped or shared through inheritance) and are immutable.
* The latter, instead, represent data modeling mechanism for assets and are characterized by properties defined as _**aspect variables**_. Aspect variables define the dynamic behavior of an asset, thus, change over time.

N.B. : Giving the high complexity of the AAS Data Model, mapping is not reversible and only submodels having idShort equals to _**DataSheet**_ get mapped into MindSphere aspect variables

```json
{
  "id":"fimind_aasx",
  "type":"AssetAdministrationShell",
  "schemaLocation": "http://www.admin-shell.io/aas/1/0 AAS.xsd http://www.admin-shell.io/IEC61360/1/0 IEC61360.xsd",
  "assetAdministrationShells": [...],
  "assets": [...],
  "submodels": [
    {...},
    {...},
    {
      "semanticId": {
        "keys": []
      },
      "qualifiers": [],
      "identification": {
        "idType": "URI",
        "id": "http://smart.festo.com/id/instance/3002_1142_3091_3839"
      },
      "idShort": "Datasheet",
      "modelType": {
        "name": "Submodel"
      },
      "kind": "Instance",
      "submodelElements": [
        {
          "value": "18",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAP898#003",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "Size",
          "category": "CONSTANT",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "1500",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAI855#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "FlowRate",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "2.5 ... 10",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAB836#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "OperatingPressure",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "IP 65",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-BAG975#009",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "IP",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "220",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-BAD875#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "Weight",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "26",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAC191#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "SwitchTimeOn",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "20",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAC192#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "SwitchTimeOff",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "24",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAH658#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "CoilVoltage",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "integer"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "G1/4",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "PneuConnection1",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "G1/4",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "PneuConnection2",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "G1/4",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "PneuConnection3",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "G1/4",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "PneuConnection4",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        },
        {
          "value": "G1/4",
          "semanticId": {
            "keys": [
              {
                "type": "ConceptDescription",
                "local": true,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          },
          "constraints": [],
          "idShort": "PneuConnection5",
          "category": "PARAMETER",
          "modelType": {
            "name": "Property"
          },
          "valueType": {
            "dataObjectType": {
              "name": "string"
            }
          },
          "kind": "Instance"
        }
      ]
    }
  ],
  "conceptDescriptions": [...]
}
```

## Usage

Some examples below:

#### Key-value pairs example

```json
curl -X POST \
  http://{server-url}:{server-port}/fimind/webapi/aas \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id":"fimind_aasx",
    "type":"AssetAdministrationShell",
    "schemaLocation": "http://www.admin-shell.io/aas/1/0 AAS.xsd http://www.admin-shell.io/IEC61360/1/0 IEC61360.xsd",
    "assetAdministrationShells": [
      {
        "derivedFrom": {
          "keys": [
            {
              "type": "AssetAdministrationShell",
              "local": false,
              "value": "www.admin-shell.io/aas/sample-series-aas/1/1",
              "index": 0,
              "idType": "URI"
            }
          ]
        },
        "asset": {
          "keys": [
            {
              "type": "Asset",
              "local": true,
              "value": "http://www.festo.com/catalog/163142",
              "index": 0,
              "idType": "URI"
            }
          ]
        },
        "submodels": [
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/5312_1142_3091_4614",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/0542_1142_3091_8974",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/3002_1142_3091_3839",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/7343_0272_3091_4440",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/8460_0142_3091_7451",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/1392_0142_3091_0152",
                "index": 0,
                "idType": "URI"
              }
            ]
          },
          {
            "keys": [
              {
                "type": "Submodel",
                "local": true,
                "value": "http://smart.festo.com/id/instance/7043_6172_3091_5867",
                "index": 0,
                "idType": "URI"
              }
            ]
          }
        ],
        "conceptDictionaries": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/aas/5140_0142_3091_4340"
        },
        "administration": {
          "version": "1",
          "revision": "0"
        },
        "idShort": "CPE18",
        "modelType": {
          "name": "AssetAdministrationShell"
        }
      }
    ],
    "assets": [
      {
        "identification": {
          "idType": "URI",
          "id": "http://www.festo.com/catalog/163142"
        },
        "idShort": "163142",
        "modelType": {
          "name": "Asset"
        },
        "kind": "Instance",
        "descriptions": [
          {
            "language": "DE",
            "text": "Magnetventil"
          },
          {
            "language": "EN",
            "text": "solenoid valve"
          }
        ]
      }
    ],
    "submodels": [
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/8460_0142_3091_7451"
        },
        "idShort": "MCAD",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "mimeType": "application/pdf",
            "value": "https://cad.festo.com/CADpreview.html?part=163142",
            "constraints": [],
            "idShort": "3D-CAD-preview",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            }
          },
          {
            "mimeType": "application/step",
            "value": "/aasx/mcad/163142_CPE18-M1H-5L-1_4.stp",
            "constraints": [],
            "idShort": "File",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            }
          },
          {
            "mimeType": "application/cad",
            "value": "http://www.festo.com/cad/163142",
            "constraints": [],
            "idShort": "Downloads",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            },
            "kind": "Instance"
          }
        ]
      },
      {
        "semanticId": {
          "keys": [
            {
              "type": "Submodel",
              "local": false,
              "value": "http://smart.festo.com/id/type/submodel/festoecad/1/1",
              "index": 0,
              "idType": "URI"
            }
          ]
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/1392_0142_3091_0152"
        },
        "idShort": "ECAD",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "mimeType": "application/EPLAN_edz",
            "value": "/aasx/ecad/FES.163142.edz",
            "constraints": [],
            "idShort": "File",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            }
          }
        ]
      },
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/3002_1142_3091_3839"
        },
        "idShort": "Datasheet",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "value": "18",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAP898#003",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "Size",
            "category": "CONSTANT",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "1500",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAI855#001",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "FlowRate",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "2.5 ... 10",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAB836#006",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "OperatingPressure",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "IP 65",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-BAG975#009",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "IP",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "220",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-BAD875#006",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "Weight",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "26",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAC191#006",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "SwitchTimeOn",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "20",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAC192#006",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "SwitchTimeOff",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "24",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAH658#001",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "CoilVoltage",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "integer"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "G1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAK168#004",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "PneuConnection1",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "G1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAK168#004",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "PneuConnection2",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "G1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAK168#004",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "PneuConnection3",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "G1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAK168#004",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "PneuConnection4",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "G1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAK168#004",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "PneuConnection5",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          }
        ]
      },
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/5312_1142_3091_4614"
        },
        "idShort": "Identification",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "value": "Festo AG & Co. KG",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-BAA001#006",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "Manufacturer",
            "category": "CONSTANT",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "http://www.festo.com",
            "constraints": [],
            "idShort": "ManufacturerURI",
            "category": "PARAMETER",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "langString"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "CPE18-M1H-5L-1/4",
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-BAD847#003",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "ProductCode",
            "category": "CONSTANT",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            },
            "kind": "Instance"
          },
          {
            "value": "CPE18-M1H-5L-1/4",
            "constraints": [],
            "idShort": "SerialNumber",
            "category": "CONSTANT",
            "modelType": {
              "name": "Property"
            },
            "valueType": {
              "dataObjectType": {
                "name": "string"
              }
            }
          }
        ]
      },
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/0542_1142_3091_8974"
        },
        "idShort": "DocuVDI2770",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "ordered": false,
            "allowDuplicates": false,
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-AAD001#001",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "DocCatalogue",
            "category": "PARAMETER",
            "modelType": {
              "name": "SubmodelElementCollection"
            },
            "value": [
              {
                "value": "Universal valve CPE",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD004#007",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "DocTitle",
                "category": "PARAMETER",
                "modelType": {
                  "name": "Property"
                },
                "valueType": {
                  "dataObjectType": {
                    "name": "string"
                  }
                },
                "kind": "Instance"
              },
              {
                "value": "CPE-G_EN.pdf",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD005#005",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "FileName",
                "category": "PARAMETER",
                "modelType": {
                  "name": "Property"
                },
                "valueType": {
                  "dataObjectType": {
                    "name": "string"
                  }
                },
                "kind": "Instance"
              },
              {
                "value": "02/2019",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD005#006",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "DocVersion",
                "category": "PARAMETER",
                "modelType": {
                  "name": "Property"
                },
                "valueType": {
                  "dataObjectType": {
                    "name": "string"
                  }
                },
                "kind": "Instance"
              },
              {
                "mimeType": "application/pdf",
                "value": "/aasx/docu/CPE-G_EN.pdf",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD005#008",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "File",
                "category": "PARAMETER",
                "modelType": {
                  "name": "File"
                },
                "kind": "Instance"
              },
              {
                "mimeType": "application/pdf",
                "value": "http://www.festo.com/docu/163142",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD000#001",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "Downloads",
                "category": "PARAMETER",
                "modelType": {
                  "name": "File"
                },
                "kind": "Instance"
              },
              {
                "value": "ENGB",
                "semanticId": {
                  "keys": [
                    {
                      "type": "ConceptDescription",
                      "local": true,
                      "value": "0173-1#02-AAD000#002",
                      "index": 0,
                      "idType": "IRDI"
                    }
                  ]
                },
                "constraints": [],
                "idShort": "DocLanguage",
                "category": "PARAMETER",
                "modelType": {
                  "name": "Property"
                },
                "valueType": {
                  "dataObjectType": {
                    "name": "string"
                  }
                },
                "kind": "Instance"
              }
            ],
            "kind": "Instance"
          }
        ]
      },
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/7043_6172_3091_5867"
        },
        "idShort": "AML",
        "category": "CONSTANT",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "mimeType": "application/aml",
            "value": "/aasx/aml/Festo_163142_CPE18-M1H-5L-14_solenoid_valve.amlx",
            "constraints": [],
            "idShort": "File",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            }
          }
        ]
      },
      {
        "semanticId": {
          "keys": []
        },
        "qualifiers": [],
        "identification": {
          "idType": "URI",
          "id": "http://smart.festo.com/id/instance/7343_0272_3091_4440"
        },
        "idShort": "SpareParts",
        "category": "CONSTANT",
        "modelType": {
          "name": "Submodel"
        },
        "kind": "Instance",
        "submodelElements": [
          {
            "mimeType": "application/pdf",
            "value": "http://www.festo.com/spareparts/163142",
            "constraints": [],
            "idShort": "Link",
            "category": "CONSTANT",
            "modelType": {
              "name": "File"
            },
            "kind": "Instance"
          },
          {
            "ordered": false,
            "allowDuplicates": false,
            "semanticId": {
              "keys": [
                {
                  "type": "ConceptDescription",
                  "local": true,
                  "value": "0173-1#02-BAD344#008",
                  "index": 0,
                  "idType": "IRDI"
                }
              ]
            },
            "constraints": [],
            "idShort": "SparePartsList",
            "category": "PARAMETER",
            "modelType": {
              "name": "SubmodelElementCollection"
            },
            "value": [],
            "kind": "Instance"
          }
        ]
      }
    ],
    "conceptDescriptions": [
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-BAA001#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Herstellername"
                    },
                    {
                      "language": "en",
                      "text": "Manufacturer name"
                    }
                  ]
                },
                "shortName": "Manufacturer",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Bezeichnung für eine natürliche oder juristische Person, die für die Auslegung, Herstellung und Verpackung sowie die Etikettierung eines Produkts im Hinblick auf das 'Inverkehrbringen' im eigenen Namen verantwortlich ist"
                    },
                    {
                      "language": "en",
                      "text": "legally valid designation of the natural or judicial person which is directly responsible for the design, production, packaging and labeling of a product in respect to its being brought into circulation used by manufacturer"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-BAA001#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-BAD847#003"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "idShort": "",
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Hersteller-Artikelnummer"
                    },
                    {
                      "language": "en",
                      "text": "Manufacturer product number"
                    }
                  ]
                },
                "shortName": "ManProdCode",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "eindeutiger Produktschlüssel des Herstellers"
                    },
                    {
                      "language": "en",
                      "text": "unique product identifier of the manufacturer"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-BAD847#003",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAP898#003"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Baugröße"
                    },
                    {
                      "language": "en",
                      "text": "Construction size"
                    }
                  ]
                },
                "shortName": "DS_ConstSize",
                "unit": "mm",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Untergruppe einer Bauart, deren Unterscheidungsmerkmale im Allgemeinen die Abmessungen sind, eine Baugröße kann mehrere Ausführungsformen umfassen, die sich vorrangig in den geometrischen Abmessungen unterscheiden"
                    },
                    {
                      "language": "en",
                      "text": "Subgroup of a construction type, whose differentiating features are generally the dimensions. A construction size can comprise several designs which differ primarily in their geometric dimensions"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAP898#003",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAI855#001"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "en",
                      "text": "nominal volume flow rate"
                    },
                    {
                      "language": "DE",
                      "text": "Nennvolumenstrom"
                    }
                  ]
                },
                "shortName": "DS_NomVolFlowRate",
                "unit": "l/min",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "en",
                      "text": "describes the nominal volume flow rate"
                    },
                    {
                      "language": "DE",
                      "text": "Nennvolumenstrom"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAI855#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAB836#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "max. Betriebsdruck"
                    },
                    {
                      "language": "en",
                      "text": "max. operating pressure"
                    }
                  ]
                },
                "shortName": "DS_OpPressure",
                "unit": "bar",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "größter Druck, der in einem System beim Betrieb (unter normalen Betriebsbedingungen) auftritt bzw. auftreten darf"
                    },
                    {
                      "language": "en",
                      "text": "Max. pressure which occurs or may occur in an operating system (under normal operating conditions)"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAB836#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-BAG975#009"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Schutzart (IP)"
                    },
                    {
                      "language": "en",
                      "text": "Protection type IP"
                    }
                  ]
                },
                "shortName": "DS_ProtTypeIP",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Einteilung entsprechend dem Schutz des Betriebsmittels gegen äußere Einflüsse jeglicher Art und Schutz vor sich bewegenden Teilen im Betriebsmittel"
                    },
                    {
                      "language": "en",
                      "text": "1) Information on the protection against direct contact and against penetration by foreign bodies, dust and/or water 2) Protection of the operating medium within the housing through adapted constructive design based on defined ambient conditions (moisture, water, dust, solid foreign bodies) 3) Protection of person against contact with dangerous parts within the housing"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-BAG975#009",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-BAD875#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Nettogewicht"
                    },
                    {
                      "language": "en",
                      "text": "Net weight"
                    }
                  ]
                },
                "shortName": "DS_NetWeight",
                "unit": "g",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Masse des Wägegutes ohne Verpackung und ohne Transportgerät. Da unter „Gewicht“ in diesem Fall die Masse verstanden wird, ist auch die Maßeinheit (Kilo-) Gramm als SI-Einheit zu verwenden"
                    },
                    {
                      "language": "en",
                      "text": "Dimensions of the weigh item without packaging and without transport device. As, in this case, 'weight' means dimensions, the measure unit (kilo) gram is to be used as the SI unit"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-BAD875#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAC191#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Ventilschaltzeit (von 0 nach 1)"
                    },
                    {
                      "language": "en",
                      "text": "Valve switching times from 0 to 1"
                    }
                  ]
                },
                "shortName": "DS_ValveSwitchTimeOn",
                "unit": "ms",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Zeit zwischen dem Einschaltbefehl und dem Ansteuern des Bauteils, das die Richtung, den Druck oder den Volumenstrom eines Fluids steuert oder regelt"
                    },
                    {
                      "language": "en",
                      "text": "Time between the switch-on command and the control of a component which controls or regulates the direction, the pressure or volume flow of a fluid"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAC191#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAC192#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Ventilschaltzeit (von 1 nach 0)"
                    },
                    {
                      "language": "en",
                      "text": "Valve switching times from 0 to 1"
                    }
                  ]
                },
                "shortName": "DS_VlaveSwitchTimeOff",
                "unit": "ms",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Zeit zwischen dem Ausschaltbefehl und dem Absteuern des Bauteils, das die Richtung, den Druck oder den Volumenstrom eines Fluids steuert oder regelt"
                    },
                    {
                      "language": "en",
                      "text": "Time between the switch-on command and the control of a component which controls or regulates the direction, the pressure or volume flow of a fluid"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAC192#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAH658#001"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Spulenspannung"
                    },
                    {
                      "language": "en",
                      "text": "coil voltage"
                    }
                  ]
                },
                "shortName": "DS_CoilVoltage",
                "unit": "V",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "elektrische Potentialdifferenz, die zwischen den beiden Enden einer Spule vorherrscht"
                    },
                    {
                      "language": "en",
                      "text": "electrical potential difference which exists between the two ends of a coil"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAH658#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAK168#004"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Gewindeanschlussgröße"
                    },
                    {
                      "language": "en",
                      "text": "thread connection size"
                    }
                  ]
                },
                "shortName": "DS_PneuConnection",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "gibt die Größe für den Gewindeanschluss an"
                    },
                    {
                      "language": "en",
                      "text": "description of the size of the thread connection size"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAK168#004",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD001#001"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": [
                {
                  "type": "GlobalReference",
                  "local": false,
                  "value": "www.admin-shell.io/DataSpecificationTemplates/DataSpecificationIEC61360",
                  "index": 0,
                  "idType": "URI"
                }
              ]
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Dokumentationsgruppe"
                    },
                    {
                      "language": "en",
                      "text": "Documentation item"
                    }
                  ]
                },
                "shortName": "DocumentationItem",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Gruppe von Merkmalen, die Zugriff gibt auf eine Dokumentation für ein Asset, beispielhaft struktuiert nach VDI 2770."
                    },
                    {
                      "language": "en",
                      "text": "Collection of properties, which gives access to documentation of an asset, structured exemplary-wise according to VDI 2770."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD001#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD004#007"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Titel des Dokuments"
                    },
                    {
                      "language": "en",
                      "text": "Documentation title"
                    }
                  ]
                },
                "shortName": "DocTitle",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Titel des Dokuments, wie vom Hersteller/ Erbringer des Assets vorgegeben."
                    },
                    {
                      "language": "en",
                      "text": "Title of documentation, as described by producer of the asset."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD004#007",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD005#005"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Dateiname des Dokuments"
                    },
                    {
                      "language": "en",
                      "text": "Filename of documentation"
                    }
                  ]
                },
                "shortName": "FileName",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Name der bereitgestellten Datei, wie vom Hersteller des Assets vorgesehen."
                    },
                    {
                      "language": "en",
                      "text": "Name of embedded file, as described by producer of the asset."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD005#005",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD005#006"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Versionsstand der bereitgestellten Datei, wie vom Hersteller des Assets vorgesehen."
                    },
                    {
                      "language": "en",
                      "text": "Version of embedded file, as described by producer of the asset."
                    }
                  ]
                },
                "shortName": "DocVersion",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "zusammen mit dem Produkt ausgelieferte Dokumentation, welche über das Produkt und seine Handhabung detailliert informiert"
                    },
                    {
                      "language": "en",
                      "text": "Documentation supplied with the product providing detailed information about the product and application thereof"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD005#006",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD005#008"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Enthaltene Doku. Datei"
                    },
                    {
                      "language": "en",
                      "text": "Embedded Doc. file"
                    }
                  ]
                },
                "shortName": "File",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Verweis/ BLOB auf enthaltene Dokumentations-Datei."
                    },
                    {
                      "language": "en",
                      "text": "Reference/ BLOB to embedded documentation file."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD005#008",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD000#002"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Dokumentensprache"
                    },
                    {
                      "language": "en",
                      "text": "Language of the document"
                    }
                  ]
                },
                "shortName": "DocLang",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Landessprache in der das Dolument verfasst ist."
                    },
                    {
                      "language": "en",
                      "text": "Language of the document."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD000#002",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-AAD000#001"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Dateidownload"
                    },
                    {
                      "language": "en",
                      "text": "File download"
                    }
                  ]
                },
                "shortName": "Downloads",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "de",
                      "text": "Online-Link auf Datei."
                    },
                    {
                      "language": "en",
                      "text": "Online link to file."
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-AAD000#001",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      },
      {
        "identification": {
          "idType": "IRDI",
          "id": "0173-1#02-BAD344#008"
        },
        "administration": {
          "version": "",
          "revision": "1"
        },
        "modelType": {
          "name": "ConceptDescription"
        },
        "embeddedDataSpecifications": [
          {
            "hasDataSpecification": {
              "keys": []
            },
            "dataSpecificationContent": {
              "dataSpecificationIEC61360": {
                "preferredName": {
                  "langString": [
                    {
                      "language": "en",
                      "text": "Spare parts list present"
                    },
                    {
                      "language": "de",
                      "text": "Ersatzteilliste vorhanden"
                    }
                  ]
                },
                "shortName": "SparePartsList",
                "unit": "",
                "sourceOfDefinition": [],
                "dataType": "",
                "definition": {
                  "langString": [
                    {
                      "language": "en",
                      "text": "whether the documentation contains a list of spare parts"
                    },
                    {
                      "language": "de",
                      "text": "Angabe, ob die Dokumentation eine Ersatzteilliste enthält"
                    }
                  ]
                }
              }
            }
          }
        ],
        "isCaseOf": [
          {
            "keys": [
              {
                "type": "GlobalReference",
                "local": false,
                "value": "0173-1#02-BAD344#008",
                "index": 0,
                "idType": "IRDI"
              }
            ]
          }
        ]
      }
    ]
}'
```
