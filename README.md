# Teste Wallet #

## Design de Arquitetura ##

![image info](./applications-architecture.png)

Obs.: Os componentes em amarelo não foram implemetados, somente idealizados

## Para executar os Projetos ##

Acesse a raiz do repositório e execute:
```
docker-compose -p wallet up -d
```

## Swagger integrado com endpoints para consulta de saldos e extrato

app-bff: http://localhost:8000/swagger-ui/index.html  

external-mock: http://localhost:8010/swagger-ui/index.html  
