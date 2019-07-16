# Uber Cadence POC

## Introdução

POC usando o UBER Cadence como infra para o orquestrador.

Projetos:

* rgn_ubercandece_analise
* rgn_ubercadence_samples

## Configuração 

### Iniciar serviços de backend

```
# bootstrap cadence/cassandra
cd docker-v0.5.9
docker-compose up -d

# bootstrap rabbitmq
cd ..
docker-compose up -d

```

### Criar um domínio local

```
docker run --network=host -e CADENCE_CLI_SHOW_STACKS=1  ubercadence/cli:master --domain proposta-domain2 domain register --rd 1 --global_domain false
```


## Workflow

### Iniciar

* Crie uma nova proposta: [POST /v1/proposta](http://localhost:8082/swagger-ui.html#/operations/proposta-service-controller/novaPropostaUsingPOST)

### Aprovar

* Pesquise nova análise correspondente à proposta criada anteriormente (procure o ID da proposta): [GET /v1/analise](http://localhost:8084/swagger-ui.html#/analise-controller/listUsingGET)
* Aprove a análise: [POST /v1/analise/{analiseId}/aprovar](http://localhost:8084/swagger-ui.html#/analise-controller/aprovarUsingPOST)

### Inspecionar Proposta aprovada

* Pesquise a proposta: [GET /v1/proposta](http://localhost:8082/swagger-ui.html#/proposta-service-controller/listarUsingGET)

### Links Locais

* rabbit: http://localhost:15672
* cadence: http://localhost:8088/
* orc: http://localhost:8082/swagger-ui.html
* analise: http://localhost:8084/swagger-ui.html

## Status

*OBS* A documentação e os exemplos disponíveis são escaços. A melhor fonte de informações é o canal slack dos desenvolvedores do slack.

### TODO

* Método do workflow fica bloqueado ao chamar `ragna.proposta.infrastructure.workflow.PropostaWorkflowImpl.receberRecomendacaoAnalise()` ao chamar atividade `propostaActivities.receberRecomendacaoAnalise(recomendacaoAnaliseCommand);`
* Observability usando Prometheus
* Deploy no ECS

## Referências

* Site: https://cadenceworkflow.io/
* Slack [(invite)]( https://join.slack.com/t/uber-cadence/shared_invite/enQtNDczNTgxMjYxNDEzLTI5Yzc5ODYwMjg1ZmI3NmRmMTU1MjQ0YzQyZDc5NzMwMmM0NjkzNDE5MmM0NzU5YTlhMmI4NzIzMDhiNzFjMDM)
* [Uber Cadence: Fault Tolerant Actor Framework](https://www.youtube.com/watch?v=qce_AqCkFys&feature=youtu.be&t=1057)
* [Should Uber Cadence activities be part of service implementations?](https://stackoverflow.com/questions/56421654/should-uber-cadence-activities-be-part-of-service-implementations)
* [introduction to cadence](https://banzaicloud.com/blog/introduction-to-cadence/)
* [Cadence Meetup: Cadence Architecture](https://www.youtube.com/watch?v=5M5eiNBUf4Q)
* [Uber Cadence Meetup: Introduction to Cadence (Uber Eats)](https://www.youtube.com/watch?v=-BuIkhlc-RM)
* [[Uber Open Summit 2018] Cadence: The Only Workflow Platform You'll Ever Need](https://www.youtube.com/watch?v=llmsBGKOuWI&feature=youtu.be)
* [Cadence Meetup: Writing a Cadence Workflow](https://www.youtube.com/watch?v=Nbz6XUBKdbM)
* [Cadence: Uber's Workflow Orchestration Engine (github.com)](https://news.ycombinator.com/item?id=19732447)
