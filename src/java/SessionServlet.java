
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

@WebServlet(urlPatterns = {"/session"})
public class SessionServlet extends HttpServlet {

    @Override
    public void doGet (HttpServletRequest req,
                       HttpServletResponse res) throws IOException {
        PrintWriter writer = res.getWriter();
        writer.println("<!DOCTYPE HTML>");
        writer.println("<html>");
        writer.println("    <head>");
        writer.println("        <meta http-equiv=\"content-type\"");
        writer.println("              content=\"text/html; charset=utf-8\"/>");
        writer.println("        <title>Login do Usuario</title>");
        writer.println("    </head>");
        writer.println("    <body>");
        writer.println("        <h1><b>Login</b></h1>");
        writer.println("        <form action=\"session\" method=\"POST\">");
        writer.println("        <fieldset>");
        writer.println("            <legend>Login</legend>");
        writer.println("            <label for=\"email\">E-mail: </label>");
        writer.println("            <input type=\"text\" require name=\"email\" value=\"\">");
        writer.println("            <label for=\"senha\">Senha: </label>");
        writer.println("            <input type=\"password\" require name=\"senha\" value=\"\">");
        writer.println("            <input type=\"submit\" require value=\"logar\">");
        writer.println("        </fieldset>");
        writer.println("        </form>");
        writer.println("        <a href=\"index.html\">Voltar para o Inicio</a>");
        writer.println("    </body>");
        writer.println("</html>");
    }

    @Override
    public void doPost (HttpServletRequest req,
                        HttpServletResponse res) throws IOException {     

        try {
            /*
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/projeto2", "root", "mor131622");
            PreparedStatement p = c.prepareStatement("SELECT * FROM usuarios WHERE email=?");
            p.setString(1, req.getParameter("email"));
            ResultSet rs = p.executeQuery();*/
            Connection conexao = Conexao.abrir();
            ResultSet rs = Conexao.getUserByEmail(conexao, req.getParameter("email"));
            
            rs.next();
            if (req.getParameter("email").equalsIgnoreCase(rs.getString("email")) &&
                req.getParameter("senha").equalsIgnoreCase(rs.getString("senha"))) {
                req.getSession().setAttribute("logado", new Boolean(true));
                req.getSession().setAttribute("usuario", rs.getInt("id"));
                //res.getWriter().println("<h1>Usuário Logado</h1>");
                res.sendRedirect("site?page=cadastro");
            } else {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().println("<h1>Login não realizado com sucesso, email ou senha incorretos.</h1>"
                        + "     <a href=\"session\"><b><h3>Back</h3></b></a>");
            }
            conexao.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
