## FONDO INVERSION - API REST SPRING BOOT

Aplicación de gestión de inversiones y fondos. Desarrollada con Spring Boot 3.3.0, JPA/Hibernate y MySQL.

### Architektura del Proyecto

- **controller/**: Endpoints REST para clientes, productos, disponibilidades, inscripciones, usuarios, sucursales y visitas.
- **services/**: Lógica de negocio y validaciones.
- **model/**: Entidades JPA mapeadas a tablas de BD.
- **repository/**: Interfaces de acceso a datos.
- **DTO/**: Objetos de transferencia de datos.
- **config/**: Configuración de seguridad (Spring Security).
- **exception/**: Manejo de excepciones personalizadas.
- **util/**: Utilidades de respuesta API.

### Endpoints Disponibles

Consultar archivo: **Insomnia_2026-03-12.yaml** para lista completa de endpoints REST y ejemplos de uso.


###   Base de datos en DOCKER  ###
## 1. Ejecutar Docker  -> SINO, NO FUNCIONARÁ NADA

docker compose -f docker-compose.mysql.yml up -d

## rectificar que se activo 

docker ps

## Acceso para escribir querys directos

docker exec -it mysql_fondos_inversion bash 
##   ó 
docker exec -it mysql_fondos_inversion /bin/bash


##  LOGS ESPERADOS

docker compose -f docker-compose.mysql.yml up -d
[+] Running 2/2
 ✔ Network prueba_default            Created 0.2s 
 ✔ Container mysql_fondos_inversion  Started   


 ## Conectarse de manera sencilla por JDBC ->
 # PUEDE OCACIOANR PROBELMAS SI SE CONECTA DE OTRA MANERA
 
jdbc:mysql://localhost:3306/fondos_inversion_db?user=root&password=root_password_segura&allowPublicKeyRetrieval=true&useSSL=false"

 ## ###########################################################################
 ## #### para la iniciazlición del proyecto se uso(https://start.spring.io/) ##
 ## ###########################################################################

se usa estrategia 
IMPLICIT NAMING STRATEGY
Define cómo generar nombres cuando NO hay @Column explícito.

ImplicitNamingStrategyComponentPathImpl → Mantiene: fechaVisita → fechaVisita
JpaCompliantImplicitNamingStrategy → Convierte: fechaVisita → fecha_visita
PHYSICAL NAMING STRATEGY
Define cómo transformar finalmente el nombre para SQL.

PhysicalNamingStrategyStandardImpl → Sin cambios: fechaVisita → fechaVisita
SpringPhysicalNamingStrategy → Aplica conversión: fechaVisita → fecha_visita

### Fase 2 - Queries SQL

El archivo **init.sql** contiene las queries para crear estructura y datos iniciales. Será ejecutado en la fase 2 de pruebas.

### Despliegue

El proyecto se subirá al repositorio con volumen de base de datos persistente en Docker.











