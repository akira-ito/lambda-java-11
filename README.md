# Introdução
Foi criado um micro serviço REST API em java 11 com Spring Boot 2 (Web, DevTools, Test, Lombok), aplicando o conceito de DDD, SOLID e alguns padrões de projetos.

![GitHub Logo](/assets/component.png)

# Cache
Nos cenários que tem muita chamada de dados dinâmicos ou estáticos, são necessários o uso de cache ou otimizações. Para atender o requisito foi feito a implementação do `CacheManager` para gerenciar os caches gerados e uma interface `CacheCondition` para implementações de ciclo de vida do cache.

Para minimizar o tempo entre o carregamento do source em JSON para Entidades, foi necessário criar uma implementação de Cache por Duração (`CacheDurationCondition`), onde é armazenada o tempo que gravou e em seguida é comparado se os dados "expiraram", caso expirado é feita um novo carregamento do source e assim evitando que gere uma inconsistencia.

E tambem foi incluido um cache na resposta após o processamento dos filtros, a nivel de controller. O cache funciona com base dos argumentos recebidos (`CacheArgumentCondition`), com isso cria uma combinação e armazena o resultado, em seguida a chamada é verificado os argumentos com o do cache caso exista é retornado o resultado imediatamente.

# Dominio Imóvel
## Validações
Abstrai as validações que existem para facilitar as novas validações para cada tipo de portal. Para isso existem duas interface principais divididas em responsabilidade menores chamadas `IPortalPropertySaleFilter` e `IPortalPropertyRentalFilter`, para validações de vendas e aluguéis respectivamentes. Um exemplo, para atender o requisito de valores minimos para o portal ZAP foi criada uma classe chamada `PortalZapValidMinValuePropertyFilte` que implementam as duas interfaces para validarem tanto vendas quanto aluguéis.

As validações necessitam de ordem/prioridade, para isso é utilizado o enum `PortalPropertyFilterOrder` com todas as validações criadas, sendo que o seu numero `ordinal()` é a sua ordem para ser respeitado. Validações comuns são implementadas na classe `PortalCommonPropertyFilter`.

## Bounding Box
Existe o serviço `BoundingBoxService` que é responsável por fazer a verificação se está dentro do bounding box do grupo ZAP, dado um determiando imóvel com sua cordenada Lat e Long. Bounding Box Grupo ZAP é carregado na inicialização da aplicação com suas propriedades configuradas.

# Infraestrutura 
Toda implementação "externa/terceira" deve ser usado neste pacote/diretório para ter o baixo acoplamento com as regras de negócios.

## Repositorio mockado do Imóvel
Implementa a interface do repositório do imóvel (`PropertyRepository`) com os dados vindo de um resources. Existem dois metodos a primeira retorna os imóveis paginados e a segunda retorna um par de tamanho total e stream de imóveis. 

# Configurações
As configurações do serviço deve ser configurado no arquivo `application.yml`, bem como os valores constantes e os que utilizam nas regras de negocio. Como por exemplo o valor da porcentagem (`boundingMinSalePricePercentage`) em cima do valor minimo para o portal ZAP.

## application.yml
```yml
zap:
    pagination:
        pageNumber: 1
        pageSize: 10
    boundingBox:
        minlon: -46.693419
        minlat: -23.568704
        maxlon: -46.641146
        maxlat: -23.546686
    filter:
        ZAP:
            minSalePrice: 600_000
            minRentalPrice: 3_500.00
            boundingMinSalePricePercentage: -10
            minSquareMeterSalePrice: 3_500
        VIVA_REAL:
            maxSalePrice: 700_000
            maxRentalPrice: 4_000.0
            boundingMaxRentalPricePercentage: 50
            maxCondoFeePercentage: 30
```

# Tecnologias
- Spring Boot 2

# Executar aplicação
É necessário instalar Maven e Java 11, em seguida instalar as dependencias do projeto.

## Buildar o projeto
```shell
mvn clean install spring-boot:repackage
```

## Executar local
Para rodar em ambiente de desenvolvimento é necessário executar 
```shell 
mvn spring-boot:run
```

## Executar em produção
É necessário gerar o JAR do pojeto através do comando:
```shell 
mvn clean install spring-boot:repackage
```
Após executado será gerado o JAR na pasta `/target` com o nome `api-0.0.1-SNAPSHOT.jar`, existem duas maneira de subir para produção:

1. Executar o arquivo JAR na maquina local ou remota/cloud, através do comando:
    ```shell 
    java -jar target/api-0.0.1-SNAPSHOT.jar
    ```
2. Criar a imagem docker (e/ou publicar no registry), e iniciar o container através dos comandos:
    ```shell 
    docker build --build-arg JAR_FILE=target/api-0.0.1-SNAPSHOT.jar -t zap/api .
    docker run -p 8080:8080 -d --name zap-api -e "SPRING_PROFILES_ACTIVE=prod" zap/api
    ```

## Executar testes
Para executar os testes basta executar o seguinte comando:
```shell 
mvn test
```

# Tarefas
- [X] Carregar source MOCK
- [X] Implementar o controller
    - [X] DTO
    - [X] Converters
- [ ] Configurações
    - [X] Exportar contantes/configurações na application.yml
    - [ ] Configuração em Cloud/Servidor
    - [ ] Por ambiente
- [ ] Mensagens
    - [ ] Externalizar as mensagens
    - [ ] Configurar as mensagens de exceções
- [X] Implementar a paginação
    - [X] Metadados do retorno
    - [X] Pagina atual
- [X] Dominio imóvel
    - [X] Serviço para buscar os imóveis por tipo de origem
    - [X] Interface do repositório para os imóveis
    - [X] Filtros
        - [X] Abstrair as validações (SOLID)
        - [X] ZAP
            - [X] Metros quadraros
            - [X] Valores minimos para o preço e aluguel
        - [X] Viva vida
            - [X] Valor do condominio
            - [X] Valores máximos para o preço e aluguel
- [X] Infraestrutura imóvel
    - [X] Implementação do repositorio de imóveis
    - [X] Converter de JSON para Entidade imóvel
- [ ] Exception
    - [X] Criar exceções especificas por negócio
    - [ ] Implementar o ControllerAdvice
    - [ ] Padronizar as saidas de erros
- [ ] Testes
    - [ ] Criar testes unitários
    - [ ] Criar testes integrados REST
    - [ ] Performance dos testes
    - [ ] SonarQube
- [ ] Implementação do Cache
    - [X] Abstrair o CacheManager
    - [X] Usar cache BD Mock
    - [X] Usar cache REST
    - [ ] Implementar/Melhorar o cache por diferentes tipos de rotas/parametros
    - [ ] Limpar o cache/GC
- [ ] Docker
    - [ ] Exportar em um container
- [ ] Documentação
    - [ ] Swagger
    - [ ] Javadoc
