INSERT INTO `bd_croma`.`permissoes_usuario` (`id`, `manutencaoAtendimento`, `manutencaoEmpresa`, `manutencaoNotasFiscais`, `manutencaoRegistroEntrada`, `manutencaoUsuario`, `manutencaoVisitante`) VALUES ('1', b'1', b'1', b'1', b'1', b'1', b'1');
INSERT INTO `bd_croma`.`usuario` (`codigo`, `confirmaSenha`, `nome`, `senha`, `permissoes_id`) VALUES ('1', '102030', 'FELIPE', '102030', '1');
INSERT INTO `bd_croma`.`empresa` (`id`, `cnpj`, `dataCadastro`, `nome`, `status`) VALUES ('1', '07347306000320', '2019-03-01 00:00:00', 'CROMA REVESTIMENTOS TÉCNICOS', 'ATIVO');
INSERT INTO `bd_croma`.`empresa` (`id`, `cnpj`, `dataCadastro`, `nome`, `status`) VALUES ('2', '07347306000168', '2019-03-01 00:00:00', 'TECNOCURVA IND METALURGICA LTDA', 'ATIVO');
INSERT INTO `bd_croma`.`visitante`(`id`,`cpf`,`nome`,`rg`,`status`,`tipo`) VALUES('1','23010433808','FELIPE JUNIOR DE JESUS MARQUES','407990549',b'1','PRESTADOR');
INSERT INTO `bd_croma`.`visitante`(`id`,`cpf`,`nome`,`rg`,`status`,`tipo`) VALUES('2','77074975036','FELIPE JUNIOR DE JESUS MARQUES','407990547',b'1','PRESTADOR');
INSERT INTO `bd_croma`.`prestador_empresa`(`prestador_id`,`empresa_id`) VALUES('1','1');
INSERT INTO `bd_croma`.`prestador_empresa`(`prestador_id`,`empresa_id`) VALUES('2','2');