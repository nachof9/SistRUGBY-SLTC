package com.sltc.sistrugby.presentacion;

import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.negocio.JugadorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel de gestión del padrón de jugadores.
 * Permite listar, registrar, modificar y dar de baja jugadores (CU02, CU03, CU04).
 */
public class JugadoresPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JugadorService jugadorService = new JugadorService();

    private JTable             tablaJugadores;
    private DefaultTableModel  modeloTabla;

    private JTextField  txtNombre;
    private JTextField  txtApellido;
    private JTextField  txtDni;
    private JTextField  txtFechaNac;
    private JTextField  txtPosicion;
    private JTextField  txtDivision;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnBaja;
    private JButton btnLimpiar;

    private Integer idJugadorSeleccionado = null;

    public JugadoresPanel() {
        initUI();
        cargarDatos();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Tabla ──────────────────────────────────────────────────────────────
        String[] columnas = {"ID", "Apellido", "Nombre", "DNI", "Posición", "División", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaJugadores = new JTable(modeloTabla);
        tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaJugadores.setRowHeight(22);
        tablaJugadores.getTableHeader().setBackground(new Color(0x1565C0));
        tablaJugadores.getTableHeader().setForeground(Color.WHITE);
        tablaJugadores.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaJugadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormularioDesdeTabla();
        });
        add(new JScrollPane(tablaJugadores), BorderLayout.CENTER);

        // ── Formulario ────────────────────────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(0xF8FBFF));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Datos del jugador"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre   = agregarCampo(pnlForm, gbc, "Nombre:",    0);
        txtApellido = agregarCampo(pnlForm, gbc, "Apellido:",  1);
        txtDni      = agregarCampo(pnlForm, gbc, "DNI:",       2);
        txtFechaNac = agregarCampo(pnlForm, gbc, "Fecha nac. (dd/MM/yyyy):", 3);
        txtPosicion = agregarCampo(pnlForm, gbc, "Posición:",  4);
        txtDivision = agregarCampo(pnlForm, gbc, "ID División:", 5);

        // Botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlBotones.setBackground(new Color(0xF8FBFF));

        btnNuevo   = crearBoton("Nuevo",     new Color(0x1565C0));
        btnGuardar = crearBoton("Guardar",   new Color(0x2E7D32));
        btnBaja    = crearBoton("Dar de baja", new Color(0xC62828));
        btnLimpiar = crearBoton("Limpiar",   Color.GRAY);

        pnlBotones.add(btnNuevo);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnBaja);
        pnlBotones.add(btnLimpiar);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        pnlForm.add(pnlBotones, gbc);

        add(pnlForm, BorderLayout.SOUTH);

        // Acciones
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnBaja.addActionListener(e -> darDeBaja());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        try {
            List<Jugador> lista = jugadorService.listarActivos();
            for (Jugador j : lista) {
                modeloTabla.addRow(new Object[]{
                    j.getId(),
                    j.getApellido(),
                    j.getNombre(),
                    j.getDni(),
                    j.getPosicion(),
                    j.getIdDivision(),
                    j.getEstado()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar jugadores: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            Jugador j = new Jugador(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtDni.getText().trim(),
                LocalDate.parse(txtFechaNac.getText().trim(), FMT),
                txtPosicion.getText().trim(),
                Integer.parseInt(txtDivision.getText().trim())
            );

            if (idJugadorSeleccionado == null) {
                jugadorService.registrar(j);
                JOptionPane.showMessageDialog(this, "Jugador registrado correctamente.");
            } else {
                j.setId(idJugadorSeleccionado);
                jugadorService.modificar(j);
                JOptionPane.showMessageDialog(this, "Jugador modificado correctamente.");
            }

            limpiarFormulario();
            cargarDatos();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBaja() {
        if (idJugadorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná un jugador de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Confirmar la baja del jugador seleccionado?",
            "Confirmar baja", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                jugadorService.darDeBaja(idJugadorSeleccionado);
                JOptionPane.showMessageDialog(this, "Baja aplicada correctamente.");
                limpiarFormulario();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tablaJugadores.getSelectedRow();
        if (fila < 0) return;
        idJugadorSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtApellido.setText((String) modeloTabla.getValueAt(fila, 1));
        txtNombre.setText  ((String) modeloTabla.getValueAt(fila, 2));
        txtDni.setText     ((String) modeloTabla.getValueAt(fila, 3));
        txtPosicion.setText((String) modeloTabla.getValueAt(fila, 4));
        txtDivision.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
    }

    public void limpiarFormulario() {
        idJugadorSeleccionado = null;
        txtNombre.setText("");  txtApellido.setText(""); txtDni.setText("");
        txtFechaNac.setText(""); txtPosicion.setText(""); txtDivision.setText("");
        tablaJugadores.clearSelection();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc, String label, int fila) {
        gbc.gridy = fila; gbc.gridx = 0; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        JTextField txt = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txt, gbc);
        return txt;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        return btn;
    }
}
