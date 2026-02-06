# Glanz-API

API backend para um sistema de organização de eventos, desenvolvida como projeto acadêmico no SENAI.

## 📌 Descrição
Esta API foi construída em Java 17 com Spring Boot para dar suporte a um sistema onde usuários podem gerenciar eventos e convidados.

O sistema possui controle de acesso baseado em papéis (roles), onde:
- Usuários comuns (`USER`) podem acessar apenas os próprios eventos vinculados à sua conta
- Administradores (`ADMIN`) possuem permissão para criar eventos e acessar todos os eventos cadastrados no sistema

> Projeto acadêmico finalizado. Atualmente não está em produção.

## 🚀 Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Security
- MySQL
- Maven
- Git / GitHub

## 🧠 Funcionalidades
- CRUD de eventos com controle de acesso por usuário
- CRUD de usuários e convidados
- Autenticação e autorização com Spring Security
- Controle de permissões baseado em roles (`USER` e `ADMIN`)
- Usuários acessam apenas eventos vinculados à própria conta
- Administradores têm acesso total aos eventos do sistema
- Envio automático de e-mails para convidados
- API REST

## ▶️ Como rodar o projeto localmente

### Pré-requisitos
Antes de começar, você precisa ter instalado:
- Java JDK 17
- Lombok
- Maven
- MySQL
- Git

### Passo a passo

#### 1. Clone o repositório:

    git clone https://github.com/ViniciusFerna/Glanz-API.git

#### 2. Acesse a pasta do projeto:

    cd Glanz-API

#### 3. Crie o banco de dados no MySQL:

    CREATE DATABASE glanz;

#### 4. Configure o arquivo application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/glanz
    
    spring.datasource.username=SEU_USUARIO
    
    spring.datasource.password=SUA_SENHA
    
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    spring.mail.username=${MAIL_USERNAME}
    
    spring.mail.password=${MAIL_PASSWORD}

#### 5. Configure as variáveis de ambiente para envio de e-mails:

    *Windows (PowerShell)*
    
    setx MAIL_USERNAME "seuemail@gmail.com"
    
    setx MAIL_PASSWORD "codigo-de-aplicativo-ou-senha"
    
    *Linux / macOS*
    
    export MAIL_USERNAME="seuemail@gmail.com"
    
    export MAIL_PASSWORD="codigo-de-aplicativo-ou-senha"
  
#### 6. Execute a aplicação:

    mvnw spring-boot:run

A aplicação estará disponível em:
    http://localhost:8080


## 📍 Observações
O envio de e-mails depende da configuração correta das credenciais SMTP.

Este projeto foi desenvolvido com fins educacionais, focado em aprendizado de backend com Spring Boot e Spring Security.
