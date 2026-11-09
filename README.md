# be-quarkus-documents

Documents microservice of Nexo: it will hold the ID documents and medical certificates of the members (domain
arrives in Phase 4). Today it is the platform skeleton: tenant-safe persistence, outbox and inbox, health, and local
authorization. It is also the template that the other generated skeletons are cloned from.

## What is in it

- Root path `/nexo-doc`, port `8083` in dev.
- Own Postgres schema `documents`, own database role `svc_doc`, own Flyway history.
- `outbox` and `inbox` tables (copied from the `be-quarkus-messaging` README).
- `access_grant`: global local projection of "which identity holds which roles in which association". It is read
  unscoped by `AccessGrantDirectory` (the `IdentityDirectory` of this service), so the service authorizes callers
  without calling the users service (D-06). An identity without a grant gets `403 IDENTITY_NOT_LINKED`; with several
  grants the `x-association-id` header must pick one of them. Unknown role names in a grant are ignored and logged.
- In Phase 1 the table is filled only by test fixtures. The `identity.access.changed` feed from users fills it in
  Phase 2 (ROL-13), ordered by `access_version`.

## Run in dev

Start the compose stack from `nexo-platform` (Postgres on host port 55432, RabbitMQ, Keycloak on 9090), then:

```
mvn quarkus:dev
```

Health: `http://localhost:8083/q/health`.

## Test

```
mvn verify -Dquarkus.http.test-port=18084
```

Tests use Quarkus Dev Services (Postgres, RabbitMQ, Keycloak with the `nexo` realm); Docker must be running.
The port override avoids clashes when several services are tested at the same time.

## Rename points when cloning

Package `it.nexoapp.doc`, config prefix `doc` and class `DocumentsConfig`, `app.code`/`app.name`/`app.description`,
schema `documents`, role `svc_doc`, dev port `8083`, artifactId `documents`, pom `<name>`, `ArchitectureTest`
package, exchange `nexo.doc` in `HealthTest`, schema prefix `documents.` in `AccessGrantDirectoryTest`.
