/*
  Consulta 1.a (schema do projeto / H2 ou SQL Server)

  Enunciado:
  Edificios no Estado de Sao Paulo que contem 30 apartamentos
  com pelo menos 2 quartos e metragem minima de 100 m2,
  ordenados pela metragem do apartamento (decrescente).
*/
SELECT
    e.CODIGO_EDIFICIO,
    e.NOME_EDIFICIO,
    c.NOME_CIDADE,
    c.ESTADO,
    a.CODIGO_APARTAMENTO,
    a.METRAGEM,
    a.ANDAR,
    a.NUMERO_QUARTOS
FROM TABELA_EDIFICIO e
INNER JOIN TABELA_CIDADE c
        ON c.CODIGO_CIDADE = e.CODIGO_CIDADE
INNER JOIN TABELA_APARTAMENTO a
        ON a.CODIGO_EDIFICIO = e.CODIGO_EDIFICIO
WHERE c.ESTADO = 'SP'
  AND a.NUMERO_QUARTOS >= 2
  AND a.METRAGEM >= 100
  AND e.CODIGO_EDIFICIO IN (
        SELECT a2.CODIGO_EDIFICIO
        FROM TABELA_APARTAMENTO a2
        INNER JOIN TABELA_EDIFICIO e2
                ON e2.CODIGO_EDIFICIO = a2.CODIGO_EDIFICIO
        INNER JOIN TABELA_CIDADE c2
                ON c2.CODIGO_CIDADE = e2.CODIGO_CIDADE
        WHERE c2.ESTADO = 'SP'
          AND a2.NUMERO_QUARTOS >= 2
          AND a2.METRAGEM >= 100
        GROUP BY a2.CODIGO_EDIFICIO
        HAVING COUNT(*) = 30
  )
ORDER BY a.METRAGEM DESC;
