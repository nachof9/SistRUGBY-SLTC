package com.sltc.sistrugby.presentacion;

import com.sltc.sistrugby.modelo.Usuario;
import com.sltc.sistrugby.persistencia.ConexionDB;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Panel de inicio de sesión.
 * Valida credenciales contra la base de datos usando BCrypt.
 * Bloquea la cuenta durante 5 minutos tras 3 intentos fallidos (CU01 — FA 2a).
 */
public class LoginPanel extends JPanel {

    private static final int MAX_INTENTOS   = 3;
    private static final int BLOQUEO_MS     = 5 * 60 * 1000; // 5 minutos

    private final MainFrame mainFrame;

    private JTextField     txtUsuario;
    private JPasswordField txtPassword;
    private JButton        btnIngresar;
    private JLabel         lblMensaje;

    private int  intentosFallidos = 0;
    private long tiempoBloqueo    = 0;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 12, 8, 12);
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("SistRUGBY-SLTC", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0x1565C0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Santiago Lawn Tennis Club", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        add(lblSub, gbc);

        // Separador
        gbc.gridy = 2;
        add(new JSeparator(), gbc);

        // Usuario
        gbc.gridwidth = 1; gbc.gridy = 3; gbc.gridx = 0;
        add(new JLabel("Usuario:"), gbc);
        txtUsuario = new JTextField(18);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        // Contraseña
        gbc.gridy = 4; gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(18);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Botón
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(0x1565C0));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        add(btnIngresar, gbc);

        // Mensaje de error/estado
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        gbc.gridy = 6;
        add(lblMensaje, gbc);

        // Acción Enter en campo de contraseña
        txtPassword.addActionListener(e -> intentarLogin());
        btnIngresar.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        // Verificar bloqueo activo
        if (System.currentTimeMillis() < tiempoBloqueo) {
            long restante = (tiempoBloqueo - System.currentTimeMillis()) / 1000;
            lblMensaje.setText("Cuenta bloqueada. Intentá en " + restante + " segundos.");
            return;
        }

        String usuario  = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Ingresá usuario y contraseña.");
            return;
        }

        try {
            Usuario u = autenticar(usuario, password);
            if (u != null) {
                intentosFallidos = 0;
                limpiar();
                mainFrame.iniciarSesion(u);
            } else {
                intentosFallidos++;
                if (intentosFallidos >= MAX_INTENTOS) {
                    tiempoBloqueo = System.currentTimeMillis() + BLOQUEO_MS;
                    lblMensaje.setText("Cuenta bloqueada por 5 minutos.");
                    btnIngresar.setEnabled(false);
                    Timer timer = new Timer(BLOQUEO_MS, e -> btnIngresar.setEnabled(true));
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    lblMensaje.setText("Credenciales incorrectas. Intentos restantes: "
                                       + (MAX_INTENTOS - intentosFallidos));
                }
            }
        } catch (Exception ex) {
            lblMensaje.setText("Error al conectar con la base de datos.");
            ex.printStackTrace();
        }
    }

    /**
     * Verifica las credenciales contra la base de datos.
     * @return el Usuario autenticado o null si las credenciales son inválidas.
     */
    private Usuario autenticar(String nombreUsuario, String password) throws Exception {
        final String SQL =
            "SELECT id_usuario, nombre_usuario, contrasena_hash, rol, activo "
            + "FROM usuarios WHERE nombre_usuario = ? AND activo = 1";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("contrasena_hash");
                    if (BCrypt.checkpw(password, hash)) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id_usuario"));
                        u.setNombreUsuario(rs.getString("nombre_usuario"));
                        u.setContrasenaHash(hash);
                        u.setRol(Usuario.Rol.valueOf(rs.getString("rol")));
                        u.setActivo(rs.getBoolean("activo"));
                        return u;
                    }
                }
            }
        }
        return null;
    }

    public void limpiar() {
        txtUsuario.setText("");
        txtPassword.setText("");
        lblMensaje.setText("");
    }
}
