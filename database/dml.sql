INSERT INTO unidade (nome) VALUES
('KG'),
('TON'),
('LITRO'),
('SACA'),
('CAIXA'),
('UNIDADE'),
('METRO'),
('M2'),
('M3'),
('PACOTE');

INSERT INTO empresa
(
    razaosocial,
    cnpj,
    logradouro,
    municipio,
    numero,
    complemento,
    bairro,
    cep,
    telefone,
    email,
    site,
    usuario,
    senha
)
VALUES
('Agro Brasil LTDA','11111111000101','Rua A','São Paulo','100','','Centro','01000000','11999990001','empresa1@email.com','www.empresa1.com','empresa1','123456'),
('Agro Minas LTDA','11111111000102','Rua B','Belo Horizonte','101','','Centro','01000001','11999990002','empresa2@email.com','www.empresa2.com','empresa2','123456'),
('Campo Forte LTDA','11111111000103','Rua C','Uberaba','102','','Centro','01000002','11999990003','empresa3@email.com','www.empresa3.com','empresa3','123456'),
('Safra Boa LTDA','11111111000104','Rua D','Ribeirão Preto','103','','Centro','01000003','11999990004','empresa4@email.com','www.empresa4.com','empresa4','123456'),
('Leilões Brasil LTDA','11111111000105','Rua E','Campinas','104','','Centro','01000004','11999990005','empresa5@email.com','www.empresa5.com','empresa5','123456'),
('Agro Forte LTDA','11111111000106','Rua F','Goiânia','105','','Centro','01000005','11999990006','empresa6@email.com','www.empresa6.com','empresa6','123456'),
('Fazenda Sul LTDA','11111111000107','Rua G','Curitiba','106','','Centro','01000006','11999990007','empresa7@email.com','www.empresa7.com','empresa7','123456'),
('Plantio Certo LTDA','11111111000108','Rua H','Londrina','107','','Centro','01000007','11999990008','empresa8@email.com','www.empresa8.com','empresa8','123456'),
('Agro Max LTDA','11111111000109','Rua I','Cuiabá','108','','Centro','01000008','11999990009','empresa9@email.com','www.empresa9.com','empresa9','123456'),
('Leilão Premium LTDA','11111111000110','Rua J','Brasília','109','','Centro','01000009','11999990010','empresa10@email.com','www.empresa10.com','empresa10','123456');

INSERT INTO leilao
(
    codigo,
    descricao,
    vendedor,
    inicioprevisto
)
VALUES
(1001,'Leilão de Soja',1,'2026-07-01 09:00:00'),
(1002,'Leilão de Milho',2,'2026-07-02 09:00:00'),
(1003,'Leilão de Café',3,'2026-07-03 09:00:00'),
(1004,'Leilão de Feijão',4,'2026-07-04 09:00:00'),
(1005,'Leilão de Trigo',5,'2026-07-05 09:00:00'),
(1006,'Leilão de Açúcar',6,'2026-07-06 09:00:00'),
(1007,'Leilão de Algodão',7,'2026-07-07 09:00:00'),
(1008,'Leilão de Arroz',8,'2026-07-08 09:00:00'),
(1009,'Leilão de Gado',9,'2026-07-09 09:00:00'),
(1010,'Leilão de Madeira',10,'2026-07-10 09:00:00');

INSERT INTO lote
(
    numerolote,
    descricao,
    quantidade,
    valorinicial,
    unidade,
    leilaoid
)
VALUES
(1,'Soja Premium',1000,50000,'KG',1),
(2,'Milho Safra 2026',2000,70000,'KG',2),
(3,'Café Especial',500,80000,'SACA',3),
(4,'Feijão Carioca',700,30000,'SACA',4),
(5,'Trigo Branco',1500,60000,'TON',5),
(6,'Açúcar Refinado',1000,40000,'TON',6),
(7,'Algodão Pluma',800,55000,'KG',7),
(8,'Arroz Tipo 1',1200,35000,'KG',8),
(9,'Gado Nelore',100,200000,'UNIDADE',9),
(10,'Madeira Eucalipto',300,90000,'M3',10);

INSERT INTO comprador
(
    empresa,
    leilao
)
VALUES
(2,1),
(3,2),
(4,3),
(5,4),
(6,5),
(7,6),
(8,7),
(9,8),
(10,9),
(1,10);