import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserPasswordTableForm extends JFrame implements ActionListener {
    private JPanel formPanel;
    private JTextField idField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton loginButton;
    private JButton encryptButton;
    private JButton showTableButton;
    private JButton exitButton;

    private List<UserPassword> userPasswords;
    private JTable table;

    public UserPasswordTableForm() {
        setTitle("Tabla de Usuarios y Contraseñas");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar el JFrame en la pantalla
        setLayout(new BorderLayout()); // Cambio en el layout del JFrame

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel idLabel = new JLabel("ID:");
        JLabel usernameLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        JLabel userTypeLabel = new JLabel("Tipo de Usuario:");

        idField = new JTextField(10);
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        userTypeComboBox = new JComboBox<>(new String[]{"Administrativo", "Usuario Fullfiller", "Analista de IT",
                "Usuario Final", "Usuario de Prueba"});

        loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(this);
        encryptButton = new JButton("Encriptar");
        encryptButton.addActionListener(this);
        showTableButton = new JButton("Mostrar Tabla");
        showTableButton.addActionListener(this);
        exitButton = new JButton("Salir");
        exitButton.addActionListener(this);

        // Estilos personalizados
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color labelColor = new Color(51, 51, 51);
        Color buttonColor = new Color(29, 152, 66); // Color RGB (29, 152, 66)

        idLabel.setFont(labelFont);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        userTypeLabel.setFont(labelFont);
        idLabel.setForeground(labelColor);
        usernameLabel.setForeground(labelColor);
        passwordLabel.setForeground(labelColor);
        userTypeLabel.setForeground(labelColor);

        idField.setFont(fieldFont);
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        userTypeComboBox.setFont(fieldFont);

        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.white);
        encryptButton.setBackground(buttonColor);
        encryptButton.setForeground(Color.white);
        showTableButton.setBackground(buttonColor);
        showTableButton.setForeground(Color.white);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.white);

        panel.add(idLabel);
        panel.add(idField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(userTypeLabel);
        panel.add(userTypeComboBox);
        panel.add(loginButton);
        panel.add(encryptButton);
        panel.add(showTableButton);
        panel.add(exitButton);

        add(panel, BorderLayout.CENTER); // Ubicar el panel en el centro del JFrame

        userPasswords = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Establecer el estilo visual de la aplicación como "Nimbus"
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            UserPasswordTableForm form = new UserPasswordTableForm();
            form.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            int id = Integer.parseInt(idField.getText());
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            String userType = (String) userTypeComboBox.getSelectedItem();

            boolean found = false;
            for (UserPassword userPassword : userPasswords) {
                if (userPassword.getId() == id && userPassword.getUsername().equals(username) && userPassword.getPassword().equals(password)) {
                    found = true;
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "ID, nombre de usuario o contraseña incorrectos. Inténtalo de nuevo.");
            }

            idField.setText("");
            usernameField.setText("");
            passwordField.setText("");
        } else if (e.getSource() == encryptButton) {
            int id = Integer.parseInt(idField.getText());
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            String userType = (String) userTypeComboBox.getSelectedItem();

            String encryptedUsername = encryptText(username);
            String encryptedPassword = encryptText(password);

            UserPassword userPassword = new UserPassword(id, encryptedUsername, encryptedPassword, userType);
            userPasswords.add(userPassword);

            JOptionPane.showMessageDialog(this, "<html><b>Usuario encriptado:</b><br>ID: " + id
                    + "<br>Usuario: " + encryptedUsername + "<br>Contraseña: " + encryptedPassword
                    + "<br>Tipo de Usuario: " + userType + "</html>");

            idField.setText("");
            usernameField.setText("");
            passwordField.setText("");
        } else if (e.getSource() == showTableButton) {
            showTable();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private String encryptText(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showTable() {
        JFrame tableFrame = new JFrame("Tabla de Usuarios y Contraseñas");
        tableFrame.setSize(600, 400);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());
        tableFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Usuario", "Contraseña", "Tipo de Usuario"};
        Object[][] data = new Object[userPasswords.size()][4];
        for (int i = 0; i < userPasswords.size(); i++) {
            UserPassword userPassword = userPasswords.get(i);
            data[i][0] = userPassword.getId();
            data[i][1] = userPassword.getUsername();
            data[i][2] = userPassword.getPassword();
            data[i][3] = userPassword.getUserType();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Cambio en la fuente del encabezado de la tabla
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Cambio en la fuente de los datos de la tabla

        JScrollPane scrollPane = new JScrollPane(table);

        tableFrame.add(scrollPane, BorderLayout.CENTER);

        tableFrame.setVisible(true);
    }

    private static class UserPassword {
        private int id;
        private String username;
        private String password;
        private String userType;

        public UserPassword(int id, String username, String password, String userType) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.userType = userType;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getUserType() {
            return userType;
        }
    }
}
