#2012-06-07 (mettere a mano gli id dopo aver eseguito ogni singola query)
INSERT INTO `UTENTE_MALICE` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `cognome`, `nome`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, '', 'Monitor');

INSERT INTO `RUOLO_MALICE` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `tipoRuolo`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 'ROLE_MONITOR');
    
INSERT INTO `UNTA_OPER_AUN` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `codDstNzle`, `codNumFax`, `codNumTel`, `codUntaOperAun`, `description`, `indMail`, `shortDescription`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, NULL, NULL, NULL, 0, 'KP Italy Srl', '', 'KP');
    
INSERT INTO `COMP_PTF` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `codCompPtf`, `denCompPtf`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 0, 'Dummy');
    
INSERT INTO `ACCOUNT_UTENTE` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `abilitato`, `email`, `password`, `username`, `utenteId`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 1, 'portale-gar@kubepartners.it', '$2a$10$htIJjpQGNSeV77hQt1o3/OczuBCaSQxa2jula3HqCc025KO3aXT/q', 'kpmonitoring', 0);

    INSERT INTO `CNL_VND` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `codCnlVnd`, `codCompPtf`, `denCnlVnd`, `compPtf_ID`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 0, 0, 'MONITOR', 0);
    
    INSERT INTO `PUN_VND` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `codCnlVnd`, `codCompPtf`, `codPunVnd`, `codUntaOperAun`, `datChiPunVnd`, `datCostPunVnd`, `denPunVnd`, `cnlVnd_ID`, `compPtf_ID`, `untaOperAun_ID`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 0, 0, '0000-1', 0, NULL, NULL, NULL, 0, 0, 0);
    
    INSERT INTO `ACC_PUN_VND_ROL` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `accountId`, `puntoVendita_ID`, `rolId`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, 0, 0, 0);

    INSERT INTO `ALIAS_NAME` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `DESCR`, `SHORT_DESCR`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, '0-Monitor', 'Mon');

    INSERT INTO `ENCODING_NAME` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `DESCR`, `SHORT_DESCR`)
VALUES
    (0, 'manuale', 'manuale', '2012-06-07 00:00:00', '2012-06-07 00:00:00', 0, '0-Monitor', 'Mon');

INSERT INTO `ENTITY_ALIAS` (`ID`, `COD_UTE_AGR`, `COD_UTE_INS`, `TMST_AGR_RIG`, `TMST_INS_RIG`, `VERSION`, `ALIAS_CODE`, `ALIAS_NAME`, `ENCODING_CODE`, `ID_UOA`)
VALUES
    (9000000000000000005, 'manuale', 'manuale', '2012-06-07 11:43:02', '2012-06-07 11:43:02', 0, '0', 2, 3, 0),
    (9000000000000000004, 'manuale', 'manuale', '2012-06-07 11:43:02', '2012-06-07 11:43:02', 0, '0', 4, 1, 0),
    (9000000000000000003, 'manuale', 'manuale', '2012-06-07 11:43:02', '2012-06-07 11:43:02', 0, '0', 1, 6, 0),
    (9000000000000000002, 'manuale', 'manuale', '2012-06-07 11:43:02', '2012-06-07 11:43:02', 0, '0', 1, 4, 0),
    (9000000000000000001, 'manuale', 'manuale', '2012-06-07 11:43:02', '2012-06-07 11:43:02', 0, '0', 1, 2, 0);

#eliminati da UNTA_OPER_AUNT gli slash dalle short_description, altrimenti lancia eccezione nella creazione del file temporaneo .xls  in chiusura

#2012-07-11
update `ENTITY_ALIAS` set id = id+100  # a mano riportato a 1,2,3,4,5 id relativi al record monitoraggio (9000000000000000001 ... 9000000000000000005)