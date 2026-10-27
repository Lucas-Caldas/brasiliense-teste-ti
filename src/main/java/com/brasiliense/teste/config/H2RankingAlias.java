package com.brasiliense.teste.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Alias H2 da sp_ranking_condominio (mesmo retorno da procedure SQL Server). */
public final class H2RankingAlias {

    private H2RankingAlias() {
    }

    public static ResultSet ranking(Connection conn, Timestamp dtInicio, Timestamp dtTermino) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("""
                SELECT TOP 3
                       a.CODIGO_APARTAMENTO,
                       a.CODIGO_EDIFICIO,
                       e.NOME_EDIFICIO,
                       a.METRAGEM,
                       a.ANDAR,
                       a.NUMERO_QUARTOS,
                       c.NOME_CIDADE,
                       c.ESTADO,
                       SUM(p.VALOR_PAGAMENTO) AS VALOR_PAGAMENTO
                FROM TABELA_PAGAMENTOS_CONDOMINIO p
                INNER JOIN TABELA_APARTAMENTO a ON a.CODIGO_APARTAMENTO = p.CODIGO_APARTAMENTO
                INNER JOIN TABELA_EDIFICIO e ON e.CODIGO_EDIFICIO = a.CODIGO_EDIFICIO
                INNER JOIN TABELA_CIDADE c ON c.CODIGO_CIDADE = e.CODIGO_CIDADE
                WHERE p.DATA_PAGAMENTO BETWEEN ? AND ?
                GROUP BY a.CODIGO_APARTAMENTO,
                         a.CODIGO_EDIFICIO,
                         e.NOME_EDIFICIO,
                         a.METRAGEM,
                         a.ANDAR,
                         a.NUMERO_QUARTOS,
                         c.NOME_CIDADE,
                         c.ESTADO
                ORDER BY VALOR_PAGAMENTO DESC
                """);
        ps.setTimestamp(1, dtInicio);
        ps.setTimestamp(2, dtTermino);
        return ps.executeQuery();
    }
}
