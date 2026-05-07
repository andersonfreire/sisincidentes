```mermaid
erDiagram

    %% ENTIDADES PRINCIPAIS

    UNIDADE_ADMINISTRATIVA {
        int codigo PK
        string titulo
        string sigla
        string responsavel
        string contato
    }

    USUARIO {
        int id PK
        string email
        string senha
        string nome
        string matricula
        string telefone
        string funcao
        string observacoes
        int unidade_id FK
    }

    CATEGORIA {
        int id PK
        string nome
        string descricao
        string tipo
    }

    INCIDENTE_VULNERABILIDADE {
        int id PK
        string numero_chamado
        string tarefa_relacionada
        int autor_id FK
        int atribuido_para_id FK
        string assunto
        string tipo
        string situacao
        int unidade_id FK
        int categoria_id FK
        string ip_origem
        string ip_destino
        string nome_host
        datetime data_criacao
        datetime data_alteracao
        datetime data_conclusao
        string tempo_estimado
        string natureza
        string prioridade
        string cc
        text descricao
        text notas
    }

    RELATORIO {
        int id PK
        date periodo_inicio
        date periodo_fim
        string tipo
        string situacao
        string prioridade
        string ip_fonte
    }

    %% RELACIONAMENTOS

    UNIDADE_ADMINISTRATIVA ||--o{ USUARIO : possui
    UNIDADE_ADMINISTRATIVA ||--o{ INCIDENTE_VULNERABILIDADE : relaciona_se_com

    USUARIO ||--o{ INCIDENTE_VULNERABILIDADE : autor_de
    USUARIO ||--o{ INCIDENTE_VULNERABILIDADE : atribuido_para

    CATEGORIA ||--o{ INCIDENTE_VULNERABILIDADE : classifica

    RELATORIO ||--o{ INCIDENTE_VULNERABILIDADE : gera_dados_de
