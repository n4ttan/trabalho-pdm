# 🛠️ Gestão de Chamados — App Android (Trabalho PDM)

Aplicativo **Android nativo em Java** para registro e acompanhamento de **chamados/atendimentos** (estilo help-desk). Desenvolvido como trabalho da disciplina de **Programação para Dispositivos Móveis (PDM)**, com persistência local em **SQLite**.

## ✨ Funcionalidades

- **Cadastro de chamados** com título, local, descrição, tipo, status, data, imagem e solução
- **Listagem** de chamados em `RecyclerView` com adapter customizado
- **Atendimento** — abrir um chamado e registrar a solução
- **Filtro** de chamados por critérios
- **Estatísticas** dos chamados registrados
- **Tela Sobre** com informações do app
- Persistência **offline** em banco de dados SQLite (`chamados.db`)

## 🧱 Telas (Activities)

| Activity | Responsabilidade |
|---|---|
| `MainActivity` | Menu principal e navegação |
| `CadastroActivity` | Registro de novos chamados |
| `ListaActivity` | Lista de todos os chamados |
| `AtendimentoActivity` | Detalhe e atendimento de um chamado |
| `FiltroActivity` | Filtragem de chamados |
| `EstatisticasActivity` | Estatísticas/resumos |
| `SobreActivity` | Informações sobre o app |

## 🛠️ Tecnologias

- **Java** — linguagem do app
- **Android SDK** (AppCompat, RecyclerView)
- **SQLite** via `SQLiteOpenHelper` (`DBHelper`)
- **Gradle** — build do projeto

## 🚀 Como rodar

```bash
git clone https://github.com/n4ttan/trabalho-pdm.git
```

1. Abra o projeto no **Android Studio**
2. Aguarde o Gradle sincronizar as dependências
3. Selecione um emulador ou dispositivo físico
4. Clique em **Run ▶** para compilar e instalar o app

> Requer Android Studio com o Android SDK configurado.

## 📁 Estrutura

```
trabalho-pdm/
├── app/
│   └── src/main/
│       ├── java/com/example/trabalhopdm/
│       │   ├── MainActivity.java
│       │   ├── CadastroActivity.java
│       │   ├── ListaActivity.java
│       │   ├── AtendimentoActivity.java
│       │   ├── FiltroActivity.java
│       │   ├── EstatisticasActivity.java
│       │   ├── SobreActivity.java
│       │   ├── ChamadoRecyclerAdapter.java
│       │   ├── DBHelper.java        → criação e acesso ao SQLite
│       │   └── MeuApp.java
│       ├── res/                     → layouts, drawables e recursos
│       └── AndroidManifest.xml
├── build.gradle / settings.gradle
└── README.md
```

## 📝 Sobre

Trabalho acadêmico da disciplina de PDM, demonstrando os fundamentos do desenvolvimento Android: múltiplas Activities, navegação por Intents, listas com RecyclerView e persistência local com SQLite.
