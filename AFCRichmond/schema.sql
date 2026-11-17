-- Script do banco de dados do sistema Sócio Torcedor (PostgreSQL)
-- Estrutura deduzida das consultas do SocioDAO.
--
-- 1) Conectado ao banco "postgres", crie o banco (uma única vez):
--      CREATE DATABASE socio_torcedor;
-- 2) Conecte-se ao banco "socio_torcedor" e execute o restante deste script.

CREATE TABLE IF NOT EXISTS socios (
    numero_socio    INTEGER      PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    cpf             VARCHAR(11)  NOT NULL UNIQUE,
    email           VARCHAR(120) NOT NULL,
    telefone        VARCHAR(11)  NOT NULL,
    data_nascimento DATE         NOT NULL,
    data_cadastro   DATE         NOT NULL,
    plano           VARCHAR(20)  NOT NULL CHECK (plano IN ('Prata', 'Ouro', 'Diamante')),
    status          VARCHAR(20)  NOT NULL CHECK (status IN ('ATIVO', 'CANCELADO'))
);
