package com.sltc.sistrugby.persistencia;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Club;

public class ClubDAO {

    public Club insertar(Club c) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (c.getId() == 0) c.setId(RepositorioMemoria.get().nextClubId());
            RepositorioMemoria.get().clubes().put(c.getId(), c);
        }
        return c;
    }

    public List<Club> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().clubes().values());
        }
        return new ArrayList<>();
    }

    public Club findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().clubes().get(id);
        return null;
    }
}
