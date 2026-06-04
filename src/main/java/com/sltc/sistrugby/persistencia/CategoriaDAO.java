package com.sltc.sistrugby.persistencia;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sltc.sistrugby.modelo.Categoria;

public class CategoriaDAO {

    public Categoria insertar(Categoria c) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (c.getId() == 0) c.setId(RepositorioMemoria.get().nextCategoriaId());
            RepositorioMemoria.get().categorias().put(c.getId(), c);
            return c;
        }
        // Implementación JDBC: similar pattern al JugadorDAO (omitida en demo).
        return c;
    }

    public List<Categoria> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            List<Categoria> l = new ArrayList<>(RepositorioMemoria.get().categorias().values());
            l.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));
            return l;
        }
        return new ArrayList<>();
    }

    public Categoria findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return RepositorioMemoria.get().categorias().get(id);
        }
        return null;
    }

    public Categoria findByNombre(String nombre) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            for (Map.Entry<Integer, Categoria> e :
                    RepositorioMemoria.get().categorias().entrySet()) {
                if (e.getValue().getNombre().equalsIgnoreCase(nombre)) return e.getValue();
            }
        }
        return null;
    }
}
