package com.sltc.sistrugby.presentacion;

import com.sltc.sistrugby.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de SistRUGBY-SLTC.
 * Gestiona el ciclo de sesión: Login → Menú principal → Logout.
 * Usa CardLayout para alternar entre el panel de login y los módulos.
 */
public class MainFrame extends JFrame {

    private static final String CARD_LOGIN    = "LOGIN";
    private static final String CARD_JUGADORES = "JUGADORES";

    private final CardLayout   cardLayout    = new CardLayout();
    private final JPanel       cardPanel     = new JPanel(cardLayout);

    private LoginPanel     loginPanel;
    private JugadoresPanel jugadoresPanel;
    private JMenuBar       menuBar;

    private Usuario usuarioActual;

    public MainFrame() {
        super("SistRUGBY-SLTC — Santiago Lawn Tennis Club");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        loginPanel     = new LoginPanel(this);
        jugadoresPanel = new JugadoresPanel();

        cardPanel.add(loginPanel,     CARD_LOGIN);
        cardPanel.add(jugadoresPanel, CARD_JUGADORES);

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    /**
     * Llamado por LoginPanel tras autenticación exitosa.
     * Configura el menú según el rol y muestra el panel inicial.
     */
    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        configurarMenu(usuario);
        jugadoresPanel.cargarDatos();
        cardLayout.show(cardPanel, CARD_JUGADORES);
        setTitle("SistRUGBY-SLTC — " + usuario.getNombreUsuario()
                 + " [" + usuario.getRol() + "]");
    }

    private void configurarMenu(Usuario usuario) {
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0x1565C0));

        // Menú Jugadores (todos los roles)
        JMenu menuJugadores = crearMenu("Jugadores");
        JMenuItem itemListar = new JMenuItem("Listar jugadores activos");
        itemListar.addActionListener(e -> {
            jugadoresPanel.cargarDatos();
            cardLayout.show(cardPanel, CARD_JUGADORES);
        });
        menuJugadores.add(itemListar);

        if (usuario.tieneRol(Usuario.Rol.SECRETARIO)
                || usuario.tieneRol(Usuario.Rol.ADMINISTRADOR)) {
            JMenuItem itemNuevo = new JMenuItem("Registrar nuevo jugador");
            itemNuevo.addActionListener(e -> {
                jugadoresPanel.limpiarFormulario();
                cardLayout.show(cardPanel, CARD_JUGADORES);
            });
            menuJugadores.add(itemNuevo);
        }
        menuBar.add(menuJugadores);

        // Menú Sistema
        JMenu menuSistema = crearMenu("Sistema");
        JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
        itemCerrar.addActionListener(e -> cerrarSesion());
        menuSistema.add(itemCerrar);
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuSistema.add(itemSalir);
        menuBar.add(menuSistema);

        setJMenuBar(menuBar);
        revalidate();
    }

    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("SansSerif", Font.BOLD, 13));
        return menu;
    }

    private void cerrarSesion() {
        usuarioActual = null;
        setJMenuBar(null);
        loginPanel.limpiar();
        cardLayout.show(cardPanel, CARD_LOGIN);
        setTitle("SistRUGBY-SLTC — Santiago Lawn Tennis Club");
        revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
