package modelos;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private boolean admin;

    // Constructor sin id (para nuevos usuarios)
    public Usuario(String nombre, String apellido, String email, String contrasena, boolean admin) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.admin = admin;
    }

    // Constructor con id (por si necesitas cargar usuario existente)
    public Usuario(int id, String nombre, String apellido, String email, String contrasena, boolean admin) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.admin = admin;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getemail() { return email; }
    public void setemail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
