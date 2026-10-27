/*
  sp_ranking_condominio - original
  Corrigida: sp_ranking_condominio_corrigida.sql
*/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		IT Team
-- Create date: 19/03/2019
-- Description:	Ranking dos Edifícios mais caros
-- =============================================
ALTER PROCEDURE sp_ranking_condominio(@dt_inicio as smalldatetime, @dt_termino as smalldatetime)
AS
BEGIN
	SET NOCOUNT ON;

	IF object_id('tempdb.dbo.#tbl_cidades') IS NOT NULL 
	DROP TABLE #tbl_cidades

	SELECT C.CODIGO_CIDADE,
	C.NOME_CIDADE,
	C.ESTADO
	INTO #tbl_cidades
	FROM TABELA_CIDADE C

	IF object_id('tempdb.dbo.#tbl_edificios') IS NOT NULL 
	DROP TABLE #tbl_edificios

	SELECT E.CODIGO_EDIFICIO,
	E.NOME_EDIFICIO,
	E.ANDARES,
	E.CODIGO_CIDADE,
	C.NOME_CIDADE,
	C.ESTADO
	INTO #tbl_edificios
	FROM TABELA_EDIFICIO E
	INNER JOIN #tbl_cidades C On ( E.CODIGO_CIDADE = C.CODIGO_CIDADE )

	IF object_id('tempdb.dbo.#tbl_apartamentos') IS NOT NULL 
	DROP TABLE #tbl_apartamentos

	SELECT 
	A.CODIGO_APARTAMENTO,
	A.CODIGO_EDIFICIO,
	E.NOME_EDIFICIO,
	A.METRAGEM,
	A.ANDAR,
	A.NUMERO_QUARTOS,
	E.NOME_CIDADE,
	E.ESTADO
	INTO #tbl_apartamentos
	FROM TABELA_APARTAMENTO A
	INNER JOIN #tbl_edificios E ON ( A.CODIGO_EDIFICIO = E.CODIGO_EDIFICIO )
	
	IF object_id('tempdb.dbo.#tbl_pagamentos_condominios') IS NOT NULL 
	DROP TABLE #tbl_apartamentos

	SELECT
	C.CODIGO_APARTAMENTO,
	AVG(C.VALOR_PAGAMENTO) AS VALOR_PAGAMENTO
	INTO #tbl_pagamentos_condominios
	FROM TABELA_PAGAMENTOS_CONDOMINIO C
	WHERE C.DATA_PAGAMENTO BETWEEN @dt_inicio AND @dt_termino
	GROUP BY C.CODIGO_APARTAMENTO

	SELECT 
	TOP 3
	A.CODIGO_APARTAMENTO,
	A.CODIGO_EDIFICIO,
	A.NOME_EDIFICIO,
	A.METRAGEM,
	A.ANDAR,
	A.NUMERO_QUARTOS,
	A.NOME_CIDADE,
	A.ESTADO,
	PC.VALOR_PAGAMENTO
	FROM #tbl_pagamentos_condominios PC
	INNER JOIN #tbl_apartamentos A ON ( PC.CODIGO_APARTAMENTO = A.CODIGO_APARTAMENTO )
	

END
GO
