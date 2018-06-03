
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgn-1
 */
public class Conexao {

    private static final String USUARIO = "root";
    private static final String SENHA = "mor131622";
    private static final String URL = "jdbc:mysql://localhost/projeto2";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    
    /**
     * Retorna uma conexao com o banco de dados. Em caso de falha retorna null
     *
     * @return
     */
    public static Connection abrir() {
        Connection conexao = null;

        try {
            // Registrar o driver
            Class.forName(DRIVER);
            // Capturar a conexão
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            // Retorna a conexao aberta
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conexao;
    }
    /**
     * Retorna o usuario dono do email
     *
     * @param conexao
     * @param email
     * @return
     */
    public static ResultSet getUserByEmail(Connection conexao, String email) {
        ResultSet r = null;

        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM usuarios WHERE email=?");
            stmt.setString(1, email);
            r = stmt.executeQuery();
            // Retorna a conexao aberta e o result set do comando sql
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }

    /**
     * Lista todos os usuarios no banco de dados.
     *
     * @param conexao
     * @return
     */
    public static ResultSet listarUsuarios(Connection conexao) {
        ResultSet r = null;

        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM usuarios");
            r = stmt.executeQuery();
            // Retorna a conexao aberta e o result set do comando sql
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }

    /**
     * Adiciona um usuario no banco de dados.
     *
     * @param conexao
     * @param email
     * @param senha
     */
    public static void adicionarUsuario(Connection conexao, String email, String senha) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO usuarios (email, senha) VALUES (?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, senha);

            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exclui um usuario do banco de dados segundo um ID.
     *
     * @param conexao
     * @param id
     */
    public static void excluirUsuario(Connection conexao, int id) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM usuarios WHERE id=(?)");
            stmt.setInt(1, id);
            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lista todos os enderecos no banco de dados
     *
     * @param conexao
     * @return
     */
    public static ResultSet listarEnderecos(Connection conexao) {
        ResultSet r = null;

        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM enderecos");
            r = stmt.executeQuery();
            // Retorna a conexao aberta e o result set do comando sql
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }

    /**
     * Adiciona um endereco no banco de dados
     *
     * @param conexao
     * @param id_usuario
     * @param estado
     * @param cidade
     * @param bairro
     * @param rua
     * @param numero
     */
    public static void adicionarEndereco(Connection conexao, int id_usuario, String estado, String cidade, String bairro, String rua, int numero) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO enderecos (id_usuario, estado, cidade, bairro, rua, numero) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, id_usuario);
            stmt.setString(2, estado);
            stmt.setString(3, cidade);
            stmt.setString(4, bairro);
            stmt.setString(5, rua);
            stmt.setInt(6, numero);
            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exclui um endereco do banco de dados segundo um ID.
     *
     * @param conexao
     * @param id
     */
    public static void excluirEndereco(Connection conexao, int id) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM enderecos WHERE id_usuario=(?)");
            stmt.setInt(1, id);
            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Retorna um conteudo segundo um ID.
     * 
     * @param conexao
     * @param id
     * @return
     */
    public static ResultSet ConteudoPorId (Connection conexao, int id) {
        ResultSet r = null;
        
        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM conteudos WHERE id=(?)");
            stmt.setInt(1, id);
            r = stmt.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }
    
    /**
     * Lista de todos os conteudos de um ID de usuario.  
     * 
     * @param conexao
     * @param id_usuario
     * @return
     */
    public static ResultSet listarConteudosPorIdUsuario (Connection conexao, int id_usuario) {
        ResultSet r = null;
        
        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM conteudos WHERE id_usuario=(?)");
            stmt.setInt(1, id_usuario);
            r = stmt.executeQuery();
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }
    
    /**
     * Lista todos os conteudos no banco de dados
     *
     * @param conexao
     * @return
     */
    public static ResultSet listarConteudos(Connection conexao) {
        ResultSet r = null;

        try {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM conteudos");
            r = stmt.executeQuery();
            // Retorna a conexao aberta e o result set do comando sql
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }

    /**
     *Adiciona um conteudo no banco de dados
     * 
     * @param conexao
     * @param id_usuario
     * @param titulo1
     * @param titulo2
     * @param titulo3
     * @param titulo4
     * @param texto1
     * @param texto2
     * @param texto3
     * @param texto4
     * @param imagem
     */
    public static void adicionarConteudo(Connection conexao, int id_usuario, String titulo1, String titulo2, String titulo3, String texto1, String texto2, String texto3, String titulo4, String texto4, String imagem) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO conteudos (id_usuario, titulo1, titulo2, titulo3, texto1, texto2, texto3, titulo4, texto4, imagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, id_usuario);
            stmt.setString(2, titulo1);
            stmt.setString(3, titulo2);
            stmt.setString(4, titulo3);
            stmt.setString(5, texto1);
            stmt.setString(6, texto2);
            stmt.setString(7, texto3);
            stmt.setString(8,titulo4);
            stmt.setString(9, texto4);
            stmt.setString(10, imagem);
            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exclui um conteudo do banco de dados segundo um ID.
     *
     * @param conexao
     * @param id
     */
    public static void excluirConteudo(Connection conexao, int id) {
        try {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM conteudos WHERE id=(?)");
            stmt.setInt(1, id);
            //Executa o statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
