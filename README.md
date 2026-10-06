# LivePoll

Sistema de votação em tempo real com painel administrativo Web
e aplicativo Android para participantes.

## Tecnologias

- Web: React + Vite
- Mobile: Kotlin + Android SDK
- Backend: Java 21 + Spring Boot
- Banco de dados: PostgreSQL
- Cache e contagem rápida: Redis
- Infraestrutura: Docker Compose

## Arquitetura

O projeto utiliza um monorepo para organizar as aplicações
Web e Mobile, a API REST e a infraestrutura compartilhada.

## Estrutura

- `backend/`: API REST Spring Boot
- `web/`: painel administrativo React
- `mobile/`: aplicativo Android Kotlin
- `infrastructure/`: Docker Compose e configuração local
- `docs/`: arquitetura e documentação técnica
