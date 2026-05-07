# 🛡️ Sistema de Gestão de Vulnerabilidades e Incidentes de Segurança (SiGVIS)

O **SiGVIS** é um sistema voltado para o **registro, acompanhamento e gestão de vulnerabilidades e incidentes de segurança da informação**.  
Seu objetivo é centralizar o tratamento de ocorrências de segurança em uma organização, garantindo **controle, rastreabilidade e eficiência na resposta a incidentes**.

---

## ⚙️ Requisitos Funcionais

### **RF01 — Gerenciamento de Unidades Administrativas**
O sistema deve permitir **cadastrar, consultar, atualizar e excluir** Unidades Administrativas.

**Atributos:**
- **Código** — Identificador único da unidade.
- **Título** — Nome completo da unidade administrativa.
- **Sigla** — Abreviação oficial da unidade.
- **Responsável** — Nome do responsável pela unidade.
- **Contato** — Informações de contato (telefone, e-mail ou ramal).

---

### **RF02 — Gerenciamento de Usuários**
O sistema deve permitir **cadastrar, consultar, atualizar e excluir** Usuários.

**Atributos:**
- **E-mail** — Endereço de e-mail institucional do usuário.
- **Senha** — Credenciais de acesso ao sistema.
- **Nome** — Nome completo do usuário.
- **Matrícula** — Identificação funcional do colaborador.
- **Unidade Administrativa** — Associação do usuário à unidade cadastrada.
- **Número de telefone** — Contato direto do usuário.
- **Função** — Cargo ou papel desempenhado.
  - Analista de TI  
  - Coordenador de SI  
  - Gestor de TI  
  - Superintendente de TI  
  - Técnico de TI
- **Observações** — Campo livre para anotações adicionais.

---

### **RF03 — Gerenciamento de Vulnerabilidades e Incidentes de Segurança**
O sistema deve permitir **cadastrar, consultar, atualizar e excluir** vulnerabilidades e incidentes de segurança.

**Atributos:**
- **ID** — Identificador único do registro.
- **Número do chamado** — Código de referência do incidente/vulnerabilidade.
- **Tarefa relacionada** — Número do chamado correlacionado (se aplicável).
- **Autor** — Usuário que criou o registro.
- **Atribuído para** — Usuário responsável pela análise ou resolução.
- **Assunto** — Breve descrição do incidente.
- **Tipo** — Classificação do registro:
  - Incidente  
  - Vulnerabilidade
- **Situação:**
  - Aberta  
  - Em análise  
  - Resolvida
- **Unidade Administrativa** — Unidade à qual o incidente está vinculado.
- **Endereço IP de origem** — IP da fonte de origem.
- **Endereço IP de destino** — IP do sistema ou ativo afetado.
- **Nome do host** — Identificação do dispositivo envolvido.
- **Data e hora da criação** — Registro de quando o chamado foi criado.
- **Data e hora da alteração** — Última modificação registrada.
- **Data e hora da conclusão** — Encerramento da ocorrência.
- **Tempo estimado para resolução** — Duração prevista para correção.
- **Natureza** — Caracterização do incidente.
- **Categoria:**
  - Comprometimento de sistema  
  - Copyright (direitos autorais)  
  - Desfiguração de páginas  
  - Destruição de dados  
  - Escravização de ativo  
  - Injeção de código  
  - Malware  
  - Negação de serviço (DoS/DDoS)  
  - Phishing / Scam  
  - Spam  
  - Sequestro de dados (Ransomware)  
  - Vazamento de informações
- **Prioridade:**
  - Crítica  
  - Alta  
  - Média  
  - Baixa
- **CC** — Lista de e-mails a serem notificados.
- **Descrição** — Comentários e detalhes do incidente.
- **Notas** — Anotações sobre a resolução.

---

### **RF04 — Relatórios de Vulnerabilidades e Incidentes**
O sistema deve permitir **gerar relatórios personalizados** sobre vulnerabilidades e incidentes de segurança.

**Atributos de filtro e agrupamento:**
- **Período** — Intervalo de tempo a ser considerado.
- **Tipo** — Incidente ou Vulnerabilidade.
- **Categoria** — Listagem das categorias disponíveis.
- **Situação:**
  - Aberta  
  - Em análise  
  - Fechada
- **Prioridade:**
  - Crítica  
  - Alta  
  - Média  
  - Baixa
- **Endereço IP da fonte** — Filtro por origem do incidente.

Os relatórios devem possibilitar **análises quantitativas e qualitativas**, facilitando a tomada de decisão e a priorização de ações corretivas.

---

