# Clinic Scheduler API

## Documentação de Produto, Backlog e Plano de Desenvolvimento

Este documento descreve um projeto backend completo para um sistema de agendamento de consultas clínicas, simulando um fluxo real de trabalho com papéis de Product Owner, Scrum Master e Project Manager.

O objetivo é deixar o projeto pronto para que um desenvolvedor consiga pegar as tarefas, entender o domínio, implementar as funcionalidades, escrever testes e executar a aplicação em ambiente local com Docker.

---

# 1. Visão Geral do Produto

## 1.1. Nome do projeto

**Clinic Scheduler API**

## 1.2. Problema a ser resolvido

Clínicas pequenas e médias costumam controlar consultas por planilhas, agendas manuais ou sistemas pouco integrados. Isso aumenta o risco de conflitos de horário, agendamentos duplicados, cancelamentos mal registrados e falhas de comunicação com pacientes.

O projeto propõe uma API backend para gerenciar pacientes, médicos, especialidades e consultas, com validações de agenda e notificações assíncronas por mensageria.

## 1.3. Objetivo do MVP

Criar uma API REST que permita:

- cadastrar pacientes;
- cadastrar médicos;
- cadastrar especialidades;
- associar médicos a especialidades;
- consultar médicos por especialidade;
- agendar consultas;
- cancelar consultas;
- impedir conflitos de horário;
- publicar eventos assíncronos de agendamento e cancelamento;
- gerar notificações internas a partir desses eventos;
- possuir testes unitários e de integração;
- rodar localmente com Docker Compose.

## 1.4. Stack sugerida

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring AMQP
- PostgreSQL
- RabbitMQ
- Docker Compose
- Flyway
- JUnit 5
- Mockito
- AssertJ
- Testcontainers
- Swagger/OpenAPI
- Maven ou Gradle

---

# 2. Papéis do Projeto

## 2.1. Product Owner

Responsável por definir o valor do produto, priorizar funcionalidades, esclarecer regras de negócio e validar entregas.

Decisões do PO neste projeto:

- o foco inicial é agendamento de consultas;
- notificações serão simuladas no banco, sem envio real de e-mail ou WhatsApp;
- pagamento, prontuário e autenticação avançada ficam fora do MVP;
- a prioridade é ter regras de agenda bem testadas.

## 2.2. Scrum Master

Responsável por organizar o fluxo de trabalho, remover impedimentos, garantir que as histórias estejam claras e manter o time trabalhando em entregas pequenas e incrementais.

Decisões do Scrum Master neste projeto:

- trabalhar com sprints curtas;
- quebrar tarefas grandes em histórias pequenas;
- exigir critérios de aceite claros;
- exigir testes antes de considerar uma história concluída;
- manter Definition of Ready e Definition of Done.

## 2.3. Project Manager

Responsável por organizar escopo, riscos, dependências, cronograma e entregas.

Decisões do Project Manager neste projeto:

- dividir o desenvolvimento em 4 sprints;
- priorizar primeiro estrutura, banco e CRUDs essenciais;
- implementar agendamento apenas depois de médico, paciente e especialidade existirem;
- deixar autenticação para uma fase posterior.

---

# 3. Escopo

## 3.1. Dentro do MVP

O MVP inclui:

1. CRUD de pacientes.
2. CRUD de médicos.
3. CRUD de especialidades.
4. Associação entre médico e especialidade.
5. Consulta de médicos por especialidade.
6. Agendamento de consulta.
7. Cancelamento de consulta.
8. Consulta de agenda por médico.
9. Validação de conflito de horário.
10. Validação contra agendamento no passado.
11. Validação contra paciente com duas consultas no mesmo horário.
12. Publicação de evento ao criar consulta.
13. Publicação de evento ao cancelar consulta.
14. Consumo dos eventos e geração de notificações internas.
15. Migrations com Flyway.
16. Testes unitários.
17. Testes de integração com PostgreSQL e RabbitMQ.
18. Docker Compose.
19. Swagger/OpenAPI.

## 3.2. Fora do MVP

Não entra na primeira versão:

1. Interface web.
2. Aplicativo mobile.
3. Pagamento.
4. Prontuário médico.
5. Prescrição médica.
6. Teleconsulta.
7. Envio real de e-mail, SMS ou WhatsApp.
8. Integração com Google Calendar.
9. Integração com convênios.
10. Controle financeiro.
11. Multi-clínica.
12. Autenticação complexa com permissões finas.

---

# 4. Arquitetura Inicial

## 4.1. Tipo de arquitetura

Para o MVP, usar um **monólito modular**.

A aplicação será um único projeto Spring Boot, mas separada internamente por módulos de domínio.

Estrutura sugerida:

```text
src/main/java/com/example/clinicscheduler
├── ClinicSchedulerApplication.java
├── config
├── shared
│   ├── exception
│   ├── validation
│   └── response
├── patient
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── doctor
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── specialty
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── appointment
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── event
├── notification
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
└── messaging
    ├── config
    ├── producer
    └── consumer
```

## 4.2. Justificativa da arquitetura

O monólito modular é recomendado para este projeto porque:

- é mais simples de desenvolver;
- facilita testes locais;
- evita complexidade desnecessária de microsserviços;
- permite separar responsabilidades por domínio;
- pode evoluir futuramente para serviços separados.

## 4.3. Banco de dados

Banco principal:

```text
PostgreSQL
```

Migrations:

```text
Flyway
```

## 4.4. Mensageria

Tecnologia escolhida:

```text
RabbitMQ
```

Eventos do MVP:

```text
AppointmentCreatedEvent
AppointmentCanceledEvent
```

Fluxo esperado:

```text
API cria consulta
        ↓
AppointmentService salva no PostgreSQL
        ↓
AppointmentEventProducer publica evento no RabbitMQ
        ↓
NotificationConsumer consome evento
        ↓
NotificationService cria notificação no banco
```

---

# 5. Modelo de Domínio

## 5.1. Entidades principais

### Patient

Representa o paciente da clínica.

Campos sugeridos:

```text
id: UUID
name: String
cpf: String
email: String
phone: String
birthDate: LocalDate
active: Boolean
createdAt: LocalDateTime
updatedAt: LocalDateTime
```

### Doctor

Representa o médico.

Campos sugeridos:

```text
id: UUID
name: String
crm: String
email: String
phone: String
active: Boolean
createdAt: LocalDateTime
updatedAt: LocalDateTime
```

### Specialty

Representa a especialidade médica.

Campos sugeridos:

```text
id: UUID
name: String
active: Boolean
createdAt: LocalDateTime
updatedAt: LocalDateTime
```

### DoctorSpecialty

Relaciona médicos e especialidades.

Campos sugeridos:

```text
id: UUID
doctorId: UUID
specialtyId: UUID
```

Observação: um médico pode ter várias especialidades e uma especialidade pode pertencer a vários médicos.

### Appointment

Representa uma consulta agendada.

Campos sugeridos:

```text
id: UUID
patientId: UUID
doctorId: UUID
specialtyId: UUID
scheduledAt: LocalDateTime
durationMinutes: Integer
status: AppointmentStatus
cancelReason: String
createdAt: LocalDateTime
updatedAt: LocalDateTime
canceledAt: LocalDateTime
```

Status possíveis:

```text
SCHEDULED
CANCELED
DONE
NO_SHOW
```

Para o MVP, implementar apenas:

```text
SCHEDULED
CANCELED
```

### Notification

Representa uma notificação interna gerada a partir de um evento.

Campos sugeridos:

```text
id: UUID
recipientType: NotificationRecipientType
recipientId: UUID
title: String
message: String
read: Boolean
createdAt: LocalDateTime
```

Tipos de destinatário:

```text
PATIENT
DOCTOR
ADMIN
```

---

# 6. Regras de Negócio

## RN01 — Paciente obrigatório

Uma consulta só pode ser agendada para um paciente existente e ativo.

## RN02 — Médico obrigatório

Uma consulta só pode ser agendada com um médico existente e ativo.

## RN03 — Especialidade obrigatória

Uma consulta só pode ser agendada para uma especialidade existente e ativa.

## RN04 — Médico deve atender a especialidade

O médico selecionado precisa estar associado à especialidade da consulta.

Exemplo: não permitir agendar uma consulta de cardiologia com um médico que só atende dermatologia.

## RN05 — Não permitir consulta no passado

O campo `scheduledAt` precisa ser maior que a data e hora atual.

## RN06 — Não permitir conflito de agenda do médico

Um médico não pode ter duas consultas no mesmo intervalo de horário.

Exemplo:

```text
Consulta A: 10:00 até 10:30
Consulta B: 10:15 até 10:45
```

A Consulta B deve ser recusada.

## RN07 — Não permitir conflito de agenda do paciente

Um paciente não pode ter duas consultas no mesmo intervalo de horário.

## RN08 — Duração padrão da consulta

Se a duração não for informada, usar duração padrão de 30 minutos.

## RN09 — Cancelamento permitido apenas para consulta agendada

Somente consultas com status `SCHEDULED` podem ser canceladas.

## RN10 — Consulta cancelada não gera conflito

Consultas com status `CANCELED` não devem ser consideradas na verificação de conflito de agenda.

## RN11 — Motivo do cancelamento

Ao cancelar uma consulta, deve ser informado um motivo com pelo menos 5 caracteres.

## RN12 — Evento de criação

Ao criar uma consulta com sucesso, publicar `AppointmentCreatedEvent`.

## RN13 — Evento de cancelamento

Ao cancelar uma consulta com sucesso, publicar `AppointmentCanceledEvent`.

## RN14 — Notificação de consulta criada

Ao consumir `AppointmentCreatedEvent`, gerar uma notificação interna para o paciente.

## RN15 — Notificação de consulta cancelada

Ao consumir `AppointmentCanceledEvent`, gerar uma notificação interna para o paciente.

---

# 7. Contratos de API

## 7.1. Patients

### Criar paciente

```http
POST /api/v1/patients
```

Request:

```json
{
  "name": "Maria Souza",
  "cpf": "12345678900",
  "email": "maria@email.com",
  "phone": "84999999999",
  "birthDate": "1990-05-10"
}
```

Response `201 Created`:

```json
{
  "id": "uuid",
  "name": "Maria Souza",
  "cpf": "12345678900",
  "email": "maria@email.com",
  "phone": "84999999999",
  "birthDate": "1990-05-10",
  "active": true
}
```

### Listar pacientes

```http
GET /api/v1/patients?page=0&size=10
```

### Buscar paciente por ID

```http
GET /api/v1/patients/{id}
```

### Atualizar paciente

```http
PUT /api/v1/patients/{id}
```

### Inativar paciente

```http
DELETE /api/v1/patients/{id}
```

Observação: usar exclusão lógica, alterando `active` para `false`.

---

## 7.2. Doctors

### Criar médico

```http
POST /api/v1/doctors
```

Request:

```json
{
  "name": "Dr. João Lima",
  "crm": "CRM-RN-12345",
  "email": "joao.lima@clinica.com",
  "phone": "84988887777"
}
```

### Associar especialidade ao médico

```http
POST /api/v1/doctors/{doctorId}/specialties/{specialtyId}
```

### Listar médicos por especialidade

```http
GET /api/v1/doctors?specialtyId={specialtyId}
```

### Buscar médico por ID

```http
GET /api/v1/doctors/{id}
```

### Atualizar médico

```http
PUT /api/v1/doctors/{id}
```

### Inativar médico

```http
DELETE /api/v1/doctors/{id}
```

---

## 7.3. Specialties

### Criar especialidade

```http
POST /api/v1/specialties
```

Request:

```json
{
  "name": "Cardiologia"
}
```

### Listar especialidades

```http
GET /api/v1/specialties?page=0&size=10
```

### Buscar especialidade por ID

```http
GET /api/v1/specialties/{id}
```

### Atualizar especialidade

```http
PUT /api/v1/specialties/{id}
```

### Inativar especialidade

```http
DELETE /api/v1/specialties/{id}
```

---

## 7.4. Appointments

### Agendar consulta

```http
POST /api/v1/appointments
```

Request:

```json
{
  "patientId": "uuid",
  "doctorId": "uuid",
  "specialtyId": "uuid",
  "scheduledAt": "2026-06-01T10:00:00",
  "durationMinutes": 30
}
```

Response `201 Created`:

```json
{
  "id": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "specialtyId": "uuid",
  "scheduledAt": "2026-06-01T10:00:00",
  "durationMinutes": 30,
  "status": "SCHEDULED"
}
```

Erros possíveis:

```text
400 Bad Request — dados inválidos
404 Not Found — paciente, médico ou especialidade não encontrada
409 Conflict — conflito de agenda
422 Unprocessable Entity — regra de negócio violada
```

### Buscar consulta por ID

```http
GET /api/v1/appointments/{id}
```

### Listar consultas por médico

```http
GET /api/v1/appointments?doctorId={doctorId}&from=2026-06-01&to=2026-06-30
```

### Listar consultas por paciente

```http
GET /api/v1/appointments?patientId={patientId}&from=2026-06-01&to=2026-06-30
```

### Cancelar consulta

```http
PATCH /api/v1/appointments/{id}/cancel
```

Request:

```json
{
  "reason": "Paciente solicitou cancelamento"
}
```

Response `200 OK`:

```json
{
  "id": "uuid",
  "status": "CANCELED",
  "cancelReason": "Paciente solicitou cancelamento",
  "canceledAt": "2026-05-23T15:30:00"
}
```

---

## 7.5. Notifications

### Listar notificações de um paciente

```http
GET /api/v1/notifications?recipientType=PATIENT&recipientId={patientId}
```

### Marcar notificação como lida

```http
PATCH /api/v1/notifications/{id}/read
```

---

# 8. Eventos de Mensageria

## 8.1. Exchange

```text
clinic.appointments.exchange
```

Tipo:

```text
topic
```

## 8.2. Filas

```text
clinic.notifications.appointment-created.queue
clinic.notifications.appointment-canceled.queue
```

## 8.3. Routing keys

```text
appointment.created
appointment.canceled
```

## 8.4. AppointmentCreatedEvent

```json
{
  "eventId": "uuid",
  "eventType": "AppointmentCreatedEvent",
  "occurredAt": "2026-05-23T15:00:00",
  "appointmentId": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "specialtyId": "uuid",
  "scheduledAt": "2026-06-01T10:00:00",
  "durationMinutes": 30
}
```

## 8.5. AppointmentCanceledEvent

```json
{
  "eventId": "uuid",
  "eventType": "AppointmentCanceledEvent",
  "occurredAt": "2026-05-23T15:30:00",
  "appointmentId": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "reason": "Paciente solicitou cancelamento"
}
```

## 8.6. Comportamento esperado dos consumidores

Ao receber `AppointmentCreatedEvent`, o sistema deve criar uma notificação:

```text
Título: Consulta agendada
Mensagem: Sua consulta foi agendada com sucesso para {scheduledAt}.
```

Ao receber `AppointmentCanceledEvent`, o sistema deve criar uma notificação:

```text
Título: Consulta cancelada
Mensagem: Sua consulta foi cancelada. Motivo: {reason}.
```

---

# 9. Tratamento de Erros

## 9.1. Padrão de resposta de erro

Usar um formato padronizado:

```json
{
  "timestamp": "2026-05-23T15:40:00",
  "status": 409,
  "error": "Conflict",
  "message": "Doctor already has an appointment in this time range",
  "path": "/api/v1/appointments"
}
```

## 9.2. Erros principais

### Recurso não encontrado

```text
404 Not Found
```

Exemplos:

- paciente não encontrado;
- médico não encontrado;
- especialidade não encontrada;
- consulta não encontrada.

### Erro de validação

```text
400 Bad Request
```

Exemplos:

- nome vazio;
- e-mail inválido;
- CPF vazio;
- data em formato inválido.

### Conflito

```text
409 Conflict
```

Exemplos:

- médico já possui consulta no horário;
- paciente já possui consulta no horário;
- CPF já cadastrado;
- CRM já cadastrado.

### Regra de negócio

```text
422 Unprocessable Entity
```

Exemplos:

- tentativa de cancelar consulta já cancelada;
- tentativa de agendar consulta no passado;
- médico não atende a especialidade selecionada.

---

# 10. Backlog do Produto

## Épico 1 — Setup e infraestrutura

Objetivo: preparar o projeto para desenvolvimento local, banco, migrations, mensageria e documentação.

Histórias:

- US001 — Criar projeto Spring Boot.
- US002 — Configurar Docker Compose com PostgreSQL e RabbitMQ.
- US003 — Configurar Flyway.
- US004 — Configurar Swagger/OpenAPI.
- US005 — Criar tratamento global de erros.

## Épico 2 — Gestão de pacientes

Objetivo: permitir cadastro e manutenção de pacientes.

Histórias:

- US006 — Criar paciente.
- US007 — Listar pacientes.
- US008 — Buscar paciente por ID.
- US009 — Atualizar paciente.
- US010 — Inativar paciente.

## Épico 3 — Gestão de médicos e especialidades

Objetivo: permitir cadastro de médicos, especialidades e relacionamento entre eles.

Histórias:

- US011 — Criar especialidade.
- US012 — Listar especialidades.
- US013 — Criar médico.
- US014 — Associar médico a especialidade.
- US015 — Listar médicos por especialidade.
- US016 — Inativar médico.

## Épico 4 — Agendamento de consultas

Objetivo: permitir criação, consulta e cancelamento de consultas com validações de agenda.

Histórias:

- US017 — Agendar consulta.
- US018 — Impedir consulta no passado.
- US019 — Impedir conflito de agenda do médico.
- US020 — Impedir conflito de agenda do paciente.
- US021 — Listar consultas por médico.
- US022 — Listar consultas por paciente.
- US023 — Cancelar consulta.

## Épico 5 — Mensageria e notificações

Objetivo: publicar eventos de consulta e gerar notificações internas.

Histórias:

- US024 — Publicar evento de consulta criada.
- US025 — Publicar evento de consulta cancelada.
- US026 — Consumir evento de consulta criada.
- US027 — Consumir evento de consulta cancelada.
- US028 — Listar notificações.
- US029 — Marcar notificação como lida.

## Épico 6 — Qualidade e testes

Objetivo: garantir confiabilidade por meio de testes automatizados.

Histórias:

- US030 — Criar testes unitários de PatientService.
- US031 — Criar testes unitários de DoctorService.
- US032 — Criar testes unitários de AppointmentService.
- US033 — Criar testes de integração dos repositories.
- US034 — Criar testes de integração da API de agendamento.
- US035 — Criar testes de integração com RabbitMQ.

---

# 11. User Stories Detalhadas

## US001 — Criar projeto Spring Boot

Como desenvolvedor, quero uma aplicação Spring Boot inicial configurada, para que o time possa iniciar o desenvolvimento do backend.

### Critérios de aceite

- O projeto deve usar Java 21.
- O projeto deve conter Spring Web.
- O projeto deve conter Spring Data JPA.
- O projeto deve conter Spring Validation.
- O projeto deve conter Spring AMQP.
- O projeto deve conter driver do PostgreSQL.
- O projeto deve compilar sem erros.
- O endpoint de health check deve responder.

### Tarefas técnicas

- Criar projeto no Spring Initializr.
- Definir pacote base.
- Configurar `application.yml`.
- Criar controller simples de health check ou usar Actuator.
- Criar estrutura inicial de pacotes.

---

## US002 — Configurar Docker Compose com PostgreSQL e RabbitMQ

Como desenvolvedor, quero subir PostgreSQL e RabbitMQ com Docker Compose, para ter um ambiente local padronizado.

### Critérios de aceite

- `docker compose up` deve subir PostgreSQL.
- `docker compose up` deve subir RabbitMQ.
- A aplicação deve conseguir conectar ao banco.
- A interface de administração do RabbitMQ deve estar disponível.
- As credenciais devem estar documentadas no README.

### Tarefas técnicas

- Criar `docker-compose.yml`.
- Criar serviço `postgres`.
- Criar serviço `rabbitmq` com management plugin.
- Configurar variáveis de ambiente.
- Configurar volumes.

---

## US003 — Configurar Flyway

Como desenvolvedor, quero controlar o schema do banco com migrations, para garantir versionamento e reprodutibilidade.

### Critérios de aceite

- Flyway deve executar ao subir a aplicação.
- Deve existir uma migration inicial.
- As tabelas principais devem ser criadas por migration.
- O projeto não deve depender de `ddl-auto=create` em ambiente normal.

### Tarefas técnicas

- Adicionar dependência do Flyway.
- Configurar local das migrations.
- Criar `V1__create_initial_tables.sql`.
- Ajustar `spring.jpa.hibernate.ddl-auto=validate`.

---

## US004 — Configurar Swagger/OpenAPI

Como desenvolvedor, quero documentação automática da API, para facilitar testes e integração.

### Critérios de aceite

- Swagger UI deve estar acessível.
- Endpoints devem aparecer agrupados por recurso.
- DTOs devem aparecer na documentação.
- Deve haver título e descrição da API.

### Tarefas técnicas

- Adicionar `springdoc-openapi`.
- Configurar informações da API.
- Usar anotações em controllers quando necessário.

---

## US005 — Criar tratamento global de erros

Como consumidor da API, quero receber erros padronizados, para entender corretamente falhas de validação e regras de negócio.

### Critérios de aceite

- Erros de validação devem retornar `400`.
- Recursos inexistentes devem retornar `404`.
- Conflitos devem retornar `409`.
- Violações de regra de negócio devem retornar `422`.
- A resposta deve conter `timestamp`, `status`, `error`, `message` e `path`.

### Tarefas técnicas

- Criar `GlobalExceptionHandler` com `@RestControllerAdvice`.
- Criar exceções customizadas.
- Criar DTO de erro.
- Tratar `MethodArgumentNotValidException`.

---

## US006 — Criar paciente

Como recepcionista, quero cadastrar pacientes, para que consultas possam ser agendadas para eles.

### Critérios de aceite

- Deve ser possível criar paciente com nome, CPF, e-mail, telefone e data de nascimento.
- Nome é obrigatório.
- CPF é obrigatório e único.
- E-mail deve ter formato válido.
- Paciente deve ser criado como ativo.
- Deve retornar `201 Created`.

### Tarefas técnicas

- Criar entidade `Patient`.
- Criar migration da tabela `patients`.
- Criar DTOs `CreatePatientRequest` e `PatientResponse`.
- Criar repository.
- Criar service.
- Criar controller.
- Criar teste unitário para CPF duplicado.
- Criar teste de integração do endpoint.

---

## US007 — Listar pacientes

Como recepcionista, quero listar pacientes cadastrados, para localizar rapidamente registros existentes.

### Critérios de aceite

- Deve retornar lista paginada.
- Deve permitir filtrar apenas pacientes ativos.
- Deve retornar nome, CPF, e-mail, telefone e status.

### Tarefas técnicas

- Criar endpoint paginado.
- Implementar query no repository.
- Criar teste de integração.

---

## US008 — Buscar paciente por ID

Como recepcionista, quero buscar um paciente pelo ID, para visualizar seus dados completos.

### Critérios de aceite

- Deve retornar `200 OK` quando o paciente existir.
- Deve retornar `404 Not Found` quando o paciente não existir.

### Tarefas técnicas

- Criar endpoint `GET /patients/{id}`.
- Implementar service.
- Criar teste unitário.
- Criar teste de integração.

---

## US009 — Atualizar paciente

Como recepcionista, quero atualizar dados de um paciente, para manter o cadastro correto.

### Critérios de aceite

- Deve permitir alterar nome, e-mail, telefone e data de nascimento.
- Não deve permitir duplicar CPF.
- Deve atualizar `updatedAt`.
- Deve retornar `404` se o paciente não existir.

### Tarefas técnicas

- Criar DTO `UpdatePatientRequest`.
- Implementar método no service.
- Criar teste unitário.
- Criar teste de integração.

---

## US010 — Inativar paciente

Como recepcionista, quero inativar um paciente, para impedir novos agendamentos sem apagar seu histórico.

### Critérios de aceite

- Deve alterar `active` para `false`.
- Não deve remover fisicamente do banco.
- Paciente inativo não pode receber novas consultas.

### Tarefas técnicas

- Implementar exclusão lógica.
- Ajustar regra de agendamento.
- Criar teste unitário.
- Criar teste de integração.

---

## US011 — Criar especialidade

Como administrador, quero cadastrar especialidades médicas, para classificar os atendimentos oferecidos.

### Critérios de aceite

- Nome da especialidade é obrigatório.
- Nome da especialidade deve ser único.
- Especialidade deve ser criada como ativa.
- Deve retornar `201 Created`.

### Tarefas técnicas

- Criar entidade `Specialty`.
- Criar migration.
- Criar DTOs.
- Criar repository.
- Criar service.
- Criar controller.
- Criar testes.

---

## US012 — Listar especialidades

Como recepcionista, quero listar especialidades, para escolher a especialidade no momento do agendamento.

### Critérios de aceite

- Deve retornar lista paginada.
- Deve permitir listar apenas especialidades ativas.

### Tarefas técnicas

- Criar endpoint.
- Criar query paginada.
- Criar teste de integração.

---

## US013 — Criar médico

Como administrador, quero cadastrar médicos, para que eles possam receber consultas.

### Critérios de aceite

- Nome é obrigatório.
- CRM é obrigatório e único.
- E-mail deve ser válido.
- Médico deve ser criado como ativo.
- Deve retornar `201 Created`.

### Tarefas técnicas

- Criar entidade `Doctor`.
- Criar migration.
- Criar DTOs.
- Criar repository.
- Criar service.
- Criar controller.
- Criar testes unitários.
- Criar testes de integração.

---

## US014 — Associar médico a especialidade

Como administrador, quero associar médicos às suas especialidades, para permitir agendamentos corretos.

### Critérios de aceite

- Deve associar um médico existente a uma especialidade existente.
- Não deve permitir associação duplicada.
- Médico inativo não pode ser associado.
- Especialidade inativa não pode ser associada.

### Tarefas técnicas

- Criar tabela de relacionamento.
- Criar endpoint.
- Implementar validações.
- Criar teste unitário.
- Criar teste de integração.

---

## US015 — Listar médicos por especialidade

Como recepcionista, quero listar médicos por especialidade, para escolher um profissional no agendamento.

### Critérios de aceite

- Deve retornar apenas médicos ativos.
- Deve retornar apenas médicos associados à especialidade informada.
- Deve retornar `404` se a especialidade não existir.

### Tarefas técnicas

- Criar endpoint com filtro por `specialtyId`.
- Implementar query no repository.
- Criar teste de integração.

---

## US017 — Agendar consulta

Como recepcionista, quero agendar uma consulta, para reservar um horário entre paciente e médico.

### Critérios de aceite

- Deve receber paciente, médico, especialidade, data/hora e duração.
- Deve validar se paciente existe e está ativo.
- Deve validar se médico existe e está ativo.
- Deve validar se especialidade existe e está ativa.
- Deve validar se médico atende à especialidade.
- Deve impedir consulta no passado.
- Deve impedir conflito de horário do médico.
- Deve impedir conflito de horário do paciente.
- Deve salvar consulta com status `SCHEDULED`.
- Deve publicar evento `AppointmentCreatedEvent`.
- Deve retornar `201 Created`.

### Tarefas técnicas

- Criar entidade `Appointment`.
- Criar enum `AppointmentStatus`.
- Criar migration.
- Criar DTO `CreateAppointmentRequest`.
- Criar DTO `AppointmentResponse`.
- Criar repository com queries de conflito.
- Criar service com validações.
- Criar producer de evento.
- Criar controller.
- Criar testes unitários de regra de negócio.
- Criar teste de integração do endpoint.

---

## US018 — Impedir consulta no passado

Como sistema, quero recusar agendamentos no passado, para manter a integridade da agenda.

### Critérios de aceite

- Se `scheduledAt` for menor ou igual ao momento atual, retornar erro.
- Deve retornar `422 Unprocessable Entity`.
- A consulta não deve ser salva no banco.
- Nenhum evento deve ser publicado.

### Tarefas técnicas

- Implementar validação no service.
- Usar `Clock` injetável para facilitar testes.
- Criar teste unitário.
- Criar teste de integração.

---

## US019 — Impedir conflito de agenda do médico

Como sistema, quero impedir que um médico tenha duas consultas no mesmo horário, para evitar conflitos de agenda.

### Critérios de aceite

- O sistema deve considerar intervalo entre início e fim da consulta.
- Consultas canceladas não entram na validação.
- Se houver sobreposição, retornar `409 Conflict`.
- A consulta não deve ser salva.
- Nenhum evento deve ser publicado.

### Regra de sobreposição

Duas consultas conflitam quando:

```text
novoInicio < consultaExistenteFim
E
novoFim > consultaExistenteInicio
```

### Tarefas técnicas

- Criar query para detectar conflito do médico.
- Criar método no repository.
- Criar teste unitário.
- Criar teste de integração com banco.

---

## US020 — Impedir conflito de agenda do paciente

Como sistema, quero impedir que um paciente tenha duas consultas no mesmo horário, para evitar agendamentos simultâneos.

### Critérios de aceite

- O sistema deve considerar intervalo entre início e fim.
- Consultas canceladas não entram na validação.
- Se houver conflito, retornar `409 Conflict`.
- A consulta não deve ser salva.
- Nenhum evento deve ser publicado.

### Tarefas técnicas

- Criar query para detectar conflito do paciente.
- Criar método no repository.
- Criar teste unitário.
- Criar teste de integração com banco.

---

## US021 — Listar consultas por médico

Como médico, quero visualizar minha agenda, para saber quais consultas tenho em determinado período.

### Critérios de aceite

- Deve filtrar por `doctorId`.
- Deve aceitar período `from` e `to`.
- Deve ordenar por data/hora crescente.
- Deve retornar consultas agendadas e canceladas, com seus status.

### Tarefas técnicas

- Criar endpoint.
- Criar query no repository.
- Criar teste de integração.

---

## US022 — Listar consultas por paciente

Como recepcionista, quero visualizar consultas de um paciente, para acompanhar o histórico de agendamentos.

### Critérios de aceite

- Deve filtrar por `patientId`.
- Deve aceitar período `from` e `to`.
- Deve ordenar por data/hora crescente.

### Tarefas técnicas

- Criar endpoint.
- Criar query no repository.
- Criar teste de integração.

---

## US023 — Cancelar consulta

Como recepcionista, quero cancelar uma consulta, para liberar o horário e registrar o motivo do cancelamento.

### Critérios de aceite

- Deve permitir cancelar apenas consulta com status `SCHEDULED`.
- Deve exigir motivo com pelo menos 5 caracteres.
- Deve atualizar status para `CANCELED`.
- Deve preencher `cancelReason`.
- Deve preencher `canceledAt`.
- Deve publicar `AppointmentCanceledEvent`.
- Deve retornar `200 OK`.

### Tarefas técnicas

- Criar DTO `CancelAppointmentRequest`.
- Criar método no service.
- Criar producer de evento.
- Criar endpoint.
- Criar teste unitário.
- Criar teste de integração.

---

## US024 — Publicar evento de consulta criada

Como sistema, quero publicar um evento quando uma consulta for criada, para que outros módulos possam reagir de forma assíncrona.

### Critérios de aceite

- O evento deve ser publicado após a consulta ser salva.
- O evento deve conter `eventId`, `occurredAt`, `appointmentId`, `patientId`, `doctorId`, `specialtyId`, `scheduledAt` e `durationMinutes`.
- O evento deve usar routing key `appointment.created`.

### Tarefas técnicas

- Criar classe do evento.
- Criar producer.
- Configurar exchange.
- Configurar routing key.
- Criar teste com mock do `RabbitTemplate`.

---

## US025 — Publicar evento de consulta cancelada

Como sistema, quero publicar um evento quando uma consulta for cancelada, para gerar notificação assíncrona.

### Critérios de aceite

- O evento deve ser publicado após atualização da consulta.
- O evento deve conter motivo do cancelamento.
- O evento deve usar routing key `appointment.canceled`.

### Tarefas técnicas

- Criar classe do evento.
- Criar producer.
- Criar teste com mock do `RabbitTemplate`.

---

## US026 — Consumir evento de consulta criada

Como sistema, quero consumir eventos de consulta criada, para gerar notificação ao paciente.

### Critérios de aceite

- Ao consumir `AppointmentCreatedEvent`, criar uma notificação no banco.
- A notificação deve ser do tipo `PATIENT`.
- A notificação deve ser criada como não lida.
- Em caso de erro, a mensagem não deve ser perdida silenciosamente.

### Tarefas técnicas

- Criar consumer com `@RabbitListener`.
- Criar `NotificationService`.
- Criar entidade `Notification`.
- Criar migration.
- Criar teste de integração.

---

## US027 — Consumir evento de consulta cancelada

Como sistema, quero consumir eventos de consulta cancelada, para gerar notificação ao paciente.

### Critérios de aceite

- Ao consumir `AppointmentCanceledEvent`, criar uma notificação no banco.
- A mensagem deve conter o motivo do cancelamento.
- A notificação deve ser criada como não lida.

### Tarefas técnicas

- Criar consumer.
- Criar método no `NotificationService`.
- Criar teste de integração.

---

## US028 — Listar notificações

Como usuário do sistema, quero listar notificações de um destinatário, para acompanhar eventos importantes.

### Critérios de aceite

- Deve filtrar por `recipientType`.
- Deve filtrar por `recipientId`.
- Deve ordenar por data de criação decrescente.
- Deve retornar status de leitura.

### Tarefas técnicas

- Criar endpoint.
- Criar repository.
- Criar service.
- Criar teste de integração.

---

## US029 — Marcar notificação como lida

Como usuário do sistema, quero marcar uma notificação como lida, para organizar minhas notificações.

### Critérios de aceite

- Deve alterar `read` para `true`.
- Deve retornar `404` se a notificação não existir.
- Deve ser idempotente: marcar como lida uma notificação já lida não deve gerar erro.

### Tarefas técnicas

- Criar endpoint.
- Criar service.
- Criar teste unitário.
- Criar teste de integração.

---

# 12. Plano de Sprints

## Sprint 0 — Preparação técnica

Objetivo: deixar o projeto pronto para desenvolvimento.

Histórias:

- US001 — Criar projeto Spring Boot.
- US002 — Configurar Docker Compose.
- US003 — Configurar Flyway.
- US004 — Configurar Swagger/OpenAPI.
- US005 — Criar tratamento global de erros.

Entrega esperada:

- aplicação sobe localmente;
- banco e RabbitMQ sobem com Docker;
- migrations executam;
- Swagger disponível;
- padrão de erros definido.

---

## Sprint 1 — Cadastros básicos

Objetivo: implementar as entidades necessárias para permitir agendamento.

Histórias:

- US006 — Criar paciente.
- US007 — Listar pacientes.
- US008 — Buscar paciente por ID.
- US009 — Atualizar paciente.
- US010 — Inativar paciente.
- US011 — Criar especialidade.
- US012 — Listar especialidades.
- US013 — Criar médico.
- US014 — Associar médico a especialidade.
- US015 — Listar médicos por especialidade.

Entrega esperada:

- pacientes, médicos e especialidades funcionais;
- relacionamento médico-especialidade implementado;
- testes dos principais fluxos.

---

## Sprint 2 — Agendamento

Objetivo: implementar o núcleo do produto.

Histórias:

- US017 — Agendar consulta.
- US018 — Impedir consulta no passado.
- US019 — Impedir conflito de agenda do médico.
- US020 — Impedir conflito de agenda do paciente.
- US021 — Listar consultas por médico.
- US022 — Listar consultas por paciente.
- US023 — Cancelar consulta.

Entrega esperada:

- agendamento funcional;
- cancelamento funcional;
- regras de conflito implementadas;
- testes unitários e de integração das regras principais.

---

## Sprint 3 — Mensageria e notificações

Objetivo: implementar fluxo assíncrono com RabbitMQ.

Histórias:

- US024 — Publicar evento de consulta criada.
- US025 — Publicar evento de consulta cancelada.
- US026 — Consumir evento de consulta criada.
- US027 — Consumir evento de consulta cancelada.
- US028 — Listar notificações.
- US029 — Marcar notificação como lida.

Entrega esperada:

- eventos publicados no RabbitMQ;
- consumidores funcionando;
- notificações persistidas no banco;
- testes de integração com RabbitMQ.

---

## Sprint 4 — Qualidade, documentação e refinamento

Objetivo: aumentar qualidade e preparar projeto para portfólio.

Histórias:

- US030 — Criar testes unitários de PatientService.
- US031 — Criar testes unitários de DoctorService.
- US032 — Criar testes unitários de AppointmentService.
- US033 — Criar testes de integração dos repositories.
- US034 — Criar testes de integração da API de agendamento.
- US035 — Criar testes de integração com RabbitMQ.

Entrega esperada:

- cobertura adequada das regras de negócio;
- README completo;
- Swagger revisado;
- projeto pronto para GitHub.

---

# 13. Definition of Ready

Uma história só pode entrar em desenvolvimento quando:

- possui descrição clara;
- possui critérios de aceite;
- possui regras de negócio relacionadas;
- possui dependências identificadas;
- possui endpoints ou comportamento esperado definidos;
- pode ser implementada e testada dentro de uma sprint;
- não depende de decisão pendente do Product Owner.

---

# 14. Definition of Done

Uma história só é considerada concluída quando:

- código implementado;
- regras de negócio atendidas;
- testes unitários criados ou atualizados;
- testes de integração criados quando necessário;
- migrations criadas quando houver alteração no banco;
- documentação Swagger atualizada;
- tratamento de erros adequado;
- logs relevantes adicionados;
- código revisado;
- aplicação sobe localmente;
- não existem testes quebrando.

---

# 15. Estratégia de Testes

## 15.1. Testes unitários

Usar:

```text
JUnit 5
Mockito
AssertJ
```

Focar em:

- services;
- regras de negócio;
- validações de domínio;
- producers com mock de RabbitMQ;
- tratamento de exceções.

Exemplos de testes unitários:

```text
AppointmentServiceTest
- deveAgendarConsultaComSucesso
- naoDeveAgendarConsultaNoPassado
- naoDeveAgendarQuandoMedicoTemConflito
- naoDeveAgendarQuandoPacienteTemConflito
- naoDeveAgendarQuandoMedicoNaoAtendeEspecialidade
- deveCancelarConsultaComSucesso
- naoDeveCancelarConsultaJaCancelada
```

## 15.2. Testes de integração

Usar:

```text
SpringBootTest
Testcontainers
PostgreSQLContainer
RabbitMQContainer
MockMvc
```

Focar em:

- repositories;
- endpoints REST;
- migrations;
- integração com PostgreSQL;
- publicação e consumo de mensagens RabbitMQ.

Exemplos:

```text
AppointmentControllerIT
- deveCriarConsultaERetornar201
- deveRetornar409QuandoMedicoTemConflito
- deveRetornar422QuandoConsultaEstaNoPassado
- deveCancelarConsultaERetornar200
```

```text
NotificationMessagingIT
- deveCriarNotificacaoAoConsumirAppointmentCreatedEvent
- deveCriarNotificacaoAoConsumirAppointmentCanceledEvent
```

---

# 16. Estratégia de Branches

Modelo simples para projeto individual ou pequeno time:

```text
main
└── develop
    ├── feature/US006-create-patient
    ├── feature/US017-create-appointment
    └── feature/US024-publish-appointment-created-event
```

## Regras

- `main`: versão estável.
- `develop`: versão em desenvolvimento.
- `feature/*`: implementação de histórias.
- Pull request de `feature/*` para `develop`.
- Merge em `main` apenas ao fechar sprint ou release.

---

# 17. Convenção de Commits

Usar Conventional Commits:

```text
feat: add patient creation endpoint
fix: prevent appointment conflict for doctor
test: add appointment service unit tests
refactor: extract appointment validation service
docs: update API documentation
chore: configure docker compose
```

Exemplos com user story:

```text
feat(US006): add patient creation
feat(US017): add appointment scheduling
fix(US019): prevent doctor schedule overlap
test(US023): add appointment cancellation tests
```

---

# 18. README Esperado

O projeto deve conter um `README.md` com:

```text
# Clinic Scheduler API

## Descrição
## Tecnologias
## Requisitos
## Como executar com Docker
## Como executar os testes
## Variáveis de ambiente
## Endpoints principais
## Fluxo de mensageria
## Regras de negócio
## Decisões arquiteturais
```

Comandos esperados:

```bash
docker compose up -d
./mvnw spring-boot:run
./mvnw test
```

Ou, usando Gradle:

```bash
docker compose up -d
./gradlew bootRun
./gradlew test
```

---

# 19. Docker Compose Sugerido

Exemplo inicial:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: clinic-postgres
    environment:
      POSTGRES_DB: clinic_scheduler
      POSTGRES_USER: clinic
      POSTGRES_PASSWORD: clinic
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:3-management
    container_name: clinic-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: clinic
      RABBITMQ_DEFAULT_PASS: clinic

volumes:
  postgres_data:
```

---

# 20. Application.yml Sugerido

```yaml
spring:
  application:
    name: clinic-scheduler-api

  datasource:
    url: jdbc:postgresql://localhost:5432/clinic_scheduler
    username: clinic
    password: clinic

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  flyway:
    enabled: true

  rabbitmq:
    host: localhost
    port: 5672
    username: clinic
    password: clinic

server:
  port: 8080
```

---

# 21. Migrations Iniciais Sugeridas

## V1__create_patients_table.sql

```sql
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(150),
    phone VARCHAR(20),
    birth_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## V2__create_doctors_table.sql

```sql
CREATE TABLE doctors (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    crm VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(150),
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## V3__create_specialties_table.sql

```sql
CREATE TABLE specialties (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## V4__create_doctor_specialties_table.sql

```sql
CREATE TABLE doctor_specialties (
    id UUID PRIMARY KEY,
    doctor_id UUID NOT NULL REFERENCES doctors(id),
    specialty_id UUID NOT NULL REFERENCES specialties(id),
    CONSTRAINT uk_doctor_specialty UNIQUE (doctor_id, specialty_id)
);
```

## V5__create_appointments_table.sql

```sql
CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL REFERENCES patients(id),
    doctor_id UUID NOT NULL REFERENCES doctors(id),
    specialty_id UUID NOT NULL REFERENCES specialties(id),
    scheduled_at TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    cancel_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    canceled_at TIMESTAMP
);

CREATE INDEX idx_appointments_doctor_scheduled_at
ON appointments(doctor_id, scheduled_at);

CREATE INDEX idx_appointments_patient_scheduled_at
ON appointments(patient_id, scheduled_at);
```

## V6__create_notifications_table.sql

```sql
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    recipient_type VARCHAR(30) NOT NULL,
    recipient_id UUID NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);
```

---

# 22. Priorização MoSCoW

## Must Have

- Criar paciente.
- Criar médico.
- Criar especialidade.
- Associar médico a especialidade.
- Agendar consulta.
- Cancelar consulta.
- Impedir conflito de médico.
- Impedir conflito de paciente.
- Publicar evento de consulta criada.
- Gerar notificação interna.
- Testes das principais regras.

## Should Have

- Listagem paginada.
- Swagger bem documentado.
- Testes de integração com RabbitMQ.
- Exclusão lógica de pacientes, médicos e especialidades.

## Could Have

- Autenticação JWT.
- Perfis de acesso.
- Histórico de alterações.
- Reagendamento de consulta.
- Disponibilidade configurável por médico.

## Won't Have no MVP

- Pagamento.
- Prontuário.
- Envio real de mensagens.
- Integração externa.
- Frontend.

---

# 23. Riscos do Projeto

## Risco 1 — Regra de conflito de horário implementada incorretamente

Mitigação:

- escrever testes unitários e de integração com vários cenários de sobreposição;
- documentar claramente a regra matemática de conflito.

## Risco 2 — Mensagem publicada antes da transação ser confirmada

Mitigação:

- no MVP, aceitar simplicidade;
- em evolução futura, implementar Outbox Pattern.

## Risco 3 — Projeto crescer sem organização

Mitigação:

- manter pacotes por domínio;
- evitar colocar toda regra em controllers;
- services devem concentrar casos de uso;
- repositories devem cuidar apenas do acesso a dados.

## Risco 4 — Testes de integração lentos

Mitigação:

- separar testes unitários de integração;
- usar Testcontainers apenas onde necessário;
- reaproveitar containers quando possível.

---

# 24. Melhorias Futuras

Após o MVP, evoluir com:

1. Autenticação JWT.
2. Perfis de acesso.
3. Reagendamento de consulta.
4. Configuração de horários de atendimento por médico.
5. Bloqueio de horários indisponíveis.
6. Histórico de mudanças da consulta.
7. Envio real de e-mail.
8. Integração com WhatsApp.
9. Integração com Google Calendar.
10. Prontuário básico.
11. Pagamento.
12. Outbox Pattern para eventos confiáveis.
13. CI com GitHub Actions.
14. Deploy com Docker.

---

# 25. Primeira Tarefa Recomendada para o Desenvolvedor

A primeira tarefa deve ser:

```text
US001 — Criar projeto Spring Boot
```

Branch sugerida:

```text
feature/US001-create-spring-project
```

Checklist inicial:

- criar repositório Git;
- criar projeto Spring Boot;
- adicionar dependências principais;
- configurar estrutura de pacotes;
- adicionar README inicial;
- adicionar Docker Compose com PostgreSQL e RabbitMQ;
- subir aplicação localmente;
- abrir Pull Request para `develop`.

---

# 26. Fluxo Real de Trabalho Simulado

## 26.1. Antes de começar uma história

O desenvolvedor deve:

1. Ler a user story.
2. Conferir critérios de aceite.
3. Conferir regras de negócio relacionadas.
4. Criar branch da história.
5. Implementar a menor parte funcional possível.
6. Criar ou atualizar testes.
7. Rodar testes localmente.
8. Abrir Pull Request.

## 26.2. Durante o desenvolvimento

O desenvolvedor deve evitar:

- colocar regra de negócio no controller;
- usar entidade JPA diretamente como request/response;
- criar migrations manuais sem padrão;
- ignorar testes de regras críticas;
- publicar evento antes de validar e salvar a consulta.

## 26.3. Ao abrir Pull Request

O Pull Request deve conter:

```text
Resumo da alteração
User story relacionada
Evidências de teste
Print ou exemplo de request/response, se aplicável
Observações técnicas
```

Modelo:

```text
Resumo:
Implementa criação de paciente.

User Story:
US006 — Criar paciente

O que foi feito:
- Criada entidade Patient
- Criado endpoint POST /api/v1/patients
- Criada migration patients
- Criados testes unitários e de integração

Como testar:
./mvnw test
```

---

# 27. Observações de Design Backend

## 27.1. Evitar entidades como DTO

Não retornar entidades JPA diretamente nos controllers.

Preferir:

```text
CreatePatientRequest
UpdatePatientRequest
PatientResponse
```

## 27.2. Usar services para casos de uso

Exemplo:

```text
AppointmentService.scheduleAppointment()
AppointmentService.cancelAppointment()
```

## 27.3. Usar repositories apenas para persistência

Evitar colocar regra de negócio complexa dentro do repository.

## 27.4. Usar Clock para datas

Para facilitar testes com datas:

```java
private final Clock clock;
```

Assim, testes de consulta no passado ficam previsíveis.

## 27.5. Cuidado com transações

Operações críticas devem usar `@Transactional`, especialmente:

- criar consulta;
- cancelar consulta;
- inativar entidades;
- consumir evento e criar notificação.

---

# 28. Resumo Executivo

Este projeto é adequado para treinar backend em nível realista porque envolve:

- API REST;
- regras de negócio;
- banco relacional;
- JPA;
- validação;
- transações;
- mensageria;
- eventos;
- testes unitários;
- testes de integração;
- Docker;
- documentação;
- organização de backlog;
- fluxo de desenvolvimento próximo do mercado.

A entrega mais importante do MVP é o fluxo:

```text
Cadastrar paciente
Cadastrar médico
Cadastrar especialidade
Associar médico à especialidade
Agendar consulta
Validar conflito
Publicar evento
Gerar notificação
Cancelar consulta
Publicar evento de cancelamento
Gerar notificação de cancelamento
```

Se esse fluxo estiver funcionando e testado, o projeto já estará forte para estudo, prática e portfólio.

