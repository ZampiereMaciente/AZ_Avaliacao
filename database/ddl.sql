CREATE TABLE empresa (
    id SERIAL PRIMARY KEY NOT NULL,
    razaoSocial VARCHAR(64) NOT NULL,
    cnpj VARCHAR(32) NOT NULL UNIQUE,
    logradouro VARCHAR(64),
    municipio VARCHAR(64),
    numero VARCHAR(10),
    complemento VARCHAR(64),
    bairro VARCHAR(64),
    cep VARCHAR(16),
    telefone VARCHAR(32),
    email VARCHAR(254) NOT NULL UNIQUE,
    site VARCHAR(254),
    usuario VARCHAR(20) NOT NULL UNIQUE,
    senha VARCHAR(128),
    created_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE leilao (
    id SERIAL PRIMARY KEY NOT NULL,
    codigo INTEGER,
    descricao VARCHAR(60) NOT NULL,
    vendedor INTEGER NOT NULL,
    inicioPrevisto TIMESTAMP NOT NULL,
    created_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT leilao_empresa_fk
        FOREIGN KEY (vendedor)
        REFERENCES empresa(id)
);

CREATE TABLE lote (
    id SERIAL PRIMARY KEY NOT NULL,
    numeroLote INTEGER,
    descricao VARCHAR(60) NOT NULL,
    quantidade NUMERIC NOT NULL,
    valorInicial NUMERIC,
    unidade VARCHAR(128) NOT NULL,

    leilaoId INTEGER NOT NULL,

    created_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT lote_leilao_fk
        FOREIGN KEY (leilaoId)
        REFERENCES leilao(id)
);

CREATE TABLE unidade (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(128) NOT NULL,

    created_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE comprador (
    empresa INTEGER NOT NULL,
    leilao INTEGER NOT NULL,

    PRIMARY KEY (empresa, leilao),

    CONSTRAINT comprador_empresa_fk
        FOREIGN KEY (empresa)
        REFERENCES empresa(id),

    CONSTRAINT comprador_leilao_fk
        FOREIGN KEY (leilao)
        REFERENCES leilao(id)
);