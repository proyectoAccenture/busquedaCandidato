# Sección detallada de la actualización

### [Volver a la tabla de endpoints `updateCategory`](service-documentation-endpoints-update-category.md)

### Detalle técnico del endpoint updateCategory

Este documento describe en detalle el funcionamiento del endpoint **`updateCategory`**, utilizado para actualizar categorías en el sistema. A continuación se presentan la estructura del request y response JSON, junto con los posibles errores que pueden ocurrir durante su ejecución.

**URL:**

```http
PUT http://localhost:8080/api/v1/categories
```

Descripción:
Este endpoint permite actualizar los campos de una categoría existente usando un identificador lógico (por ejemplo, el nombre actual de la categoría) y una lista de parámetros a modificar.

---

### Request - updateCategory

**Body:**

```json
{
  "category": "Comida",
  "params": [
    {
      "field": "name",
      "value": "Comida Nueva"
    },
    {
      "field": "iamge",
      "value": "nuevaImage.svg"
    }
  ]
}
```

---

### Response - updateCategory

**Response code:**  
`200 OK`

```json
{
  "code": "200",
  "message": "SUCCESS",
  "messageId": "peace757",
  "data": {
    "id": "6846ea754ee0585b0fbde860",
    "name": "Comida Nueva",
    "image": "https://cdn.example.com/logos/nuevaImage.svg",
    "creationDate": "2025-06-09T09:06:45.681093Z",
    "modificationDate": "2025-06-09T09:06:45.681093Z",
    "createdBy": "Juan Julio",
    "priority": 0,
    "active": false
  }
}
```
---
