# EventNtfy API - Backend Service

Bem-vindo ao repositório da **EventNtfy**, uma aplicação backend desenvolvida para enviar notificações via e-mail e SMS utilizando **Twilio**, **Spring Boot** e **Docker**.

## Descrição do Projeto

Este projeto consiste em uma API de notificações que envia mensagens tanto via **E-mail** (usando o **Spring Boot Starter Mail**) quanto via **SMS** (utilizando a API do **Twilio**). A aplicação é composta por:

- Uma camada de infraestrutura que gerencia as notificações.
- Uma rotina agendada para enviar as notificações.
- Configuração de **migrations** para gerenciamento de banco de dados.
- **Tests unitários** para garantir a qualidade do código.
- **Docker** para a construção e execução da API em containers.
- **GitHub Actions** para CI/CD, com pipeline para integração contínua (CI) e deploy contínuo (CD).
- Conexão com banco de dados **MySQL** na nuvem.

## Tecnologias Utilizadas

- **Spring Boot** (Framework para o backend)
- **Twilio** (Para envio de SMS)
- **Spring Boot Starter Mail** (Para envio de e-mails)
- **Docker** (Containerização)
- **EC2 (Ubuntu)** (Instância para rodar a API)
- **MySQL** (Banco de dados na nuvem)
- **GitHub Actions** (Para CI/CD)
- **JUnit** (Para testes unitários)

## Funcionalidades

- **Notificação por e-mail**: Envia notificações para usuários de cadastro, cancelamento e chegada do evento via e-mail utilizando o **Spring Boot Starter Mail**.
- **Notificação por SMS**: Envia mensagens de cadastro, cancelamento e chegada de eventos via SMS usando a **API do Twilio**.
- **Rotinas agendadas**: A API contém rotinas programadas para o envio de notificações.
- **Banco de Dados**: Conexão com um banco de dados MySQL, com configuração de migrations.
- **Testes Unitários**: Implementação de testes unitários para validar o funcionamento das classes de serviço.
- **Docker e EC2**: A API é containerizada com Docker e executada em uma instância EC2 com segurança configurada.

## Instalação e Configuração

### Pré-requisitos

- JDK 17 ou superior
- Docker
- Conta no Twilio para API de SMS
- MySQL (ou outro banco de dados configurado | dependendo do banco de dados escolhidos será necessário rever as migrations)
- Conta no GitHub para integrar com GitHub Actions

### Passo a Passo

1. Clone o repositório para o seu ambiente local:

```bash
git clone https://github.com/seu-usuario/projeto-notificacao.git
cd projeto-notificacao
```

2. Configure as variáveis de ambiente necessárias para a aplicação. Crie um arquivo .env ou adicione diretamente no seu ambiente local as seguintes variáveis e seus respectivos valores:

```bash
    EMAIL_USERNAME={seu-email}
    EMAIL_PASSWORD={sua-senha-email-app}
    SMS_ACCOUNT_SID={seu-account-sid}
    SMS_AUTH_TOKEN={seu-auth-token}
    SMS_PHONE_NUMBER={seu-numero-twilio}
    DATABASE_PASSWORD={sua-senha-banco}
    DATABASE_URL={url-banco}
    DATABASE_USERNAME={seu-usuario-banco}
```

### Descrição das variáveis de ambiente:

- **EMAIL_USERNAME**: Seu endereço de e-mail utilizado para envio de notificações.
    - Exemplo: `clausilvaaraujo11@gmail.com`
    - **Como obter**: O e-mail utilizado na configuração de envio de mensagens. Caso utilize Gmail, você pode precisar de uma senha de aplicativo.

- **EMAIL_PASSWORD**: A senha de e-mail utilizada na configuração do envio de e-mails (é recomendável usar senhas de aplicativos, caso a plataforma de e-mail forneça essa opção).
    - Exemplo: `xxxx xxxx xxxx xxxx`
    - **Como obter**: Caso esteja utilizando Gmail, você pode gerar uma senha de aplicativo nas configurações de segurança da sua conta Google.

- **SMS_ACCOUNT_SID**: O **Account SID** da sua conta Twilio. Para obter, você precisa criar uma conta no [Twilio](https://www.twilio.com/) e obter esse valor no painel da plataforma.
    - Exemplo: `XxXxXxXxXxXxXxXxX`
    - **Como obter**: Após criar uma conta no Twilio, vá até o painel e copie o **Account SID** na seção de configurações da sua conta.

- **SMS_AUTH_TOKEN**: O **Auth Token** da sua conta Twilio, que também pode ser encontrado no painel da Twilio.
    - Exemplo: `4321123443211234`
    - **Como obter**: O **Auth Token** é fornecido juntamente com o **Account SID** no painel do Twilio.

- **SMS_PHONE_NUMBER**: O número de telefone fornecido pelo Twilio, que será utilizado para o envio de SMS.
    - Exemplo: `+0110011001`
    - **Como obter**: No painel da sua conta Twilio, você pode obter um número de telefone para enviar SMS.

- **DATABASE_PASSWORD**: A senha para o banco de dados MySQL. Certifique-se de configurar corretamente a senha do banco de dados no arquivo de configuração.
    - Exemplo: `#MinhaSenha123`
    - **Como obter**: Essa é a senha definida para o usuário do banco de dados MySQL.

- **DATABASE_URL**: A URL de conexão com o banco de dados MySQL. No caso de um banco local, use `jdbc:mysql://localhost:3306/nome-do-banco`. Em ambientes na nuvem, forneça o endereço do servidor.
    - Exemplo: `jdbc:mysql://localhost:3306/event_ntfy`
    - **Como obter**: Caso esteja utilizando MySQL local, a URL será `jdbc:mysql://localhost:3306/seu-banco`, caso esteja usando MySQL na nuvem, forneça o endereço completo da instância.

- **DATABASE_USERNAME**: O nome de usuário do banco de dados MySQL.
    - Exemplo: `root`
    - **Como obter**: O nome de usuário do banco de dados que você configurou.

### Configuração do `application.properties`

Configure o arquivo `application.properties` com as variáveis de ambiente mencionadas. Aqui está um exemplo de como as variáveis devem ser utilizadas dentro do arquivo de configuração:

```properties
# E-mail Configuration
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}

# Twilio Configuration
twilio.account.sid=${SMS_ACCOUNT_SID}
twilio.auth.token=${SMS_AUTH_TOKEN}
twilio.phone.number=${SMS_PHONE_NUMBER}

# MySQL Database Configuration
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
````

### Contato

- **Email**: clausilvaaraujo11@gmail.com
- **Telefone**: +55 11 94463-6705
- **LinkedIn**: [Cláudio A. Silva](https://www.linkedin.com/in/claudiodesenvolvedorjava/)

### Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

### Dificuldade do Projeto

Este projeto é classificado como **intermediário a avançado**. Abaixo estão os motivos para essa classificação:

#### Aspectos Desafiadores

1. **Multicamadas de Desenvolvimento**:
    - O projeto utiliza uma arquitetura backend bem estruturada com separação clara entre camadas de infraestrutura e serviços, exigindo boa organização e entendimento do Spring Boot.

2. **Integração com APIs Externas**:
    - A implementação da API do Twilio para envio de SMS requer compreensão sobre autenticação e comunicação com serviços externos.

3. **Automação com CI/CD**:
    - Configurar pipelines de integração contínua (CI) e entrega contínua (CD) no GitHub Actions, além de integrar essas pipelines com uma instância EC2.

4. **Banco de Dados e Migrations**:
    - Gerenciar um banco de dados MySQL com scripts de migração, garantindo que os ambientes de desenvolvimento e produção estejam sincronizados.

5. **Testes Unitários**:
    - Implementar testes unitários para validar as classes de serviço e assegurar a qualidade do código.

6. **Containerização e Deploy**:
    - Construção e execução de containers Docker, além de realizar o deploy em uma instância EC2 com configurações de segurança adequadas.

7. **Configurações Avançadas**:
    - Gerenciamento de variáveis de ambiente e múltiplos arquivos `application.properties` para diferentes ambientes (testes, produção).

8. **Rotinas Programadas**:
    - Agendamento de tarefas automatizadas para envio de notificações, o que exige conhecimento de ferramentas e bibliotecas específicas.

#### Aspectos Simplificadores

1. **Foco no Backend**:
    - O projeto não possui uma interface frontend complexa, o que reduz a necessidade de lidar com frameworks adicionais.

2. **Ferramentas Automatizadas**:
    - Utilização de Docker e GitHub Actions facilita o processo de containerização, deploy e automação.

#### Classificação Final

Este projeto oferece uma oportunidade para desenvolver e consolidar habilidades avançadas em backend, integração de APIs externas, automação com CI/CD e deploy em nuvem. Ele é adequado para desenvolvedores com experiência intermediária em backend ou para aqueles que desejam avançar em habilidades mais complexas.
