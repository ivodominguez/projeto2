
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/cadastro"})
public class CadastroServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter writer = res.getWriter();
        res.setContentType("text/html; encoding=utf-8");
        writer.println("<!DOCTYPE HTML>");
        writer.println("<html>");
        writer.println("    <head>");
        writer.println("        <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
        writer.println("        <title>Cadastro</title>");
        writer.println("    </head>");
        writer.println("    <body>");
        writer.println("        <h1>Cadastro de Usuario</h1>");

        writer.println("<fieldset>");
        writer.println("<legend><b>Formulário:</b></legend>");
        writer.println("<form action=\"cadastro\" method=\"POST\" accept-charset=\"utf-8\"></br>");
        writer.println("    <label for=\"email\">E-mail: </label>");
        writer.println("    <input type=\"text\" required name=\"email\" value=\"\" id=\"email\"/></br>");
        writer.println("    <label for=\"senha\">Senha: </label>");
        writer.println("    <input type=\"text\" required name=\"senha\" value=\"\" id=\"senha\"/></br>");
        writer.println("    <label for=\"estado\">Estado: </label>");
        writer.println("    <input type=\"text\" required name=\"estado\" value=\"\" id=\"estado\"/></br>");
        writer.println("    <label for=\"cidade\">Cidade: </label>");
        writer.println("    <input type=\"text\" required name=\"cidade\" value=\"\" id=\"cidade\"/></br>");
        writer.println("    <label for=\"bairro\">Bairro: </label>");
        writer.println("    <input type=\"text\" required name=\"bairro\" value=\"\" id=\"bairro\"/></br>");
        writer.println("    <label for=\"rua\">Rua: </label>");
        writer.println("    <input type=\"text\" required name=\"rua\" value=\"\" id=\"rua\"/></br>");
        writer.println("    <label for=\"numero\">Numero: </label>");
        writer.println("    <input type=\"text\" required name=\"numero\" value=\"\" id=\"numero\"/></br></br>");
        writer.println("    <input type=\"submit\" value=\"adicionar\"></p>");
        writer.println("</form>");
        writer.println("</fieldset>");

        writer.println("<ul>");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/projeto2", "root", "mor131622");
            PreparedStatement p = c.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = p.executeQuery();
            //Connection conexao = Conexao.abrir();
            //ResultSet rs = Conexao.listarUsuarios(conexao);
            
            while (rs.next()) {
                writer.println("    <li>" + rs.getInt("id") + " - " + rs.getString("email") + "");

                writer.println("<form action=\"cadastro\" method=\"POST\" accept-charset=\"utf-8\">");
                writer.println("    <input type=\"hidden\" name=\"id\" value=\"" + rs.getInt("id") + "\"/>");
                writer.println("    <input type=\"submit\" name=\"metodo\" value=\"deletar\"/>");
                writer.println("</form>");

                writer.println("    </li>");
            }
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CadastroServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        writer.println("</ul>");
        writer.println("        <a href=\"index.html\">Voltar para o Inicio</a>");
        writer.println("    </body>");
        writer.println("</html>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Connection conexao = Conexao.abrir();
            ResultSet rs = Conexao.getUserByEmail(conexao, req.getParameter("email"));
            rs.next();

            if (req.getParameter("metodo") != null && req.getParameter("metodo").equals("deletar")) {
                Conexao.excluirEndereco(conexao, Integer.parseInt(req.getParameter("id")));
                Conexao.excluirUsuario(conexao, Integer.parseInt(req.getParameter("id")));
                res.sendRedirect("cadastro");
            } else {
                if (req.getParameter("email").equalsIgnoreCase("")
                        || req.getParameter("senha").equalsIgnoreCase("")
                        || req.getParameter("estado").equalsIgnoreCase("")
                        || req.getParameter("cidade").equalsIgnoreCase("")
                        || req.getParameter("bairro").equalsIgnoreCase("")
                        || req.getParameter("rua").equalsIgnoreCase("")
                        || req.getParameter("numero").equalsIgnoreCase("")
                        || rs.isLast()) {
                    res.getWriter().println("<h1>Erro, email já cadastrado</h1>"
                            + "     <a href=\"cadastro\">Back</a>");
                } else {
                    Conexao.adicionarUsuario(conexao,
                            req.getParameter("email"),
                            req.getParameter("senha"));

                    rs = Conexao.getUserByEmail(conexao, req.getParameter("email"));
                    rs.next();
                    
                    Conexao.adicionarEndereco(conexao,
                            rs.getInt("id"),
                            req.getParameter("estado"),
                            req.getParameter("cidade"),
                            req.getParameter("bairro"),
                            req.getParameter("rua"),
                            Integer.parseInt(req.getParameter("numero")));
                    res.getWriter().println("<h1>Cadastro realizado com sucesso</h1>"
                            + "<h2>Selecione um dos caminhos:</h2>"
                            + "<ul>"
                            + "     <a href=\"sessao\"><b><h3>Iniciar Login</h3></b></a>"
                            + "     <a href=\"cadastro\">Back</a>"
                            + "     <a href=\"index.html\">Home</a>"
                            + "</ul>");
                    //res.sendRedirect("blog");
                }
                conexao.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
