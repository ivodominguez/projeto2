/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
/**
 *
 * @author mgn-1
 */

@WebServlet(urlPatterns = {"/site"})
public class SiteServlet extends HttpServlet{
    private int id = 0;
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { 
        if (req.getParameter("page").equalsIgnoreCase("cadastro")) {
            HttpSession session = req.getSession();
            if (session.getAttribute("logado") != null && session.getAttribute("logado").equals(Boolean.TRUE)) {

                PrintWriter writer = res.getWriter();
                res.setContentType("text/html; encoding=utf-8");
                writer.println("<!DOCTYPE HTML>");
                writer.println("<html>");
                writer.println("    <head>");
                writer.println("        <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
                writer.println("        <title>Gerencia de Blogs</title>");
                writer.println("    </head>");
                writer.println("    <body>");
                writer.println("        <h1>Gerencia de Blogs</h1>");
                writer.println("        <h2>ID do usuario: " + req.getSession().getAttribute("usuario") + "</h2>");

                writer.println("<fieldset>");
                writer.println("<legend><b>Formulário de conteúdo do blog:</b></legend>");
                writer.println("<form action=\"site\" method=\"POST\" accept-charset=\"utf-8\"></br>");
                //writer.println("                                      enctype=\"multipart/form-data\">");
                writer.println("    <label for=\"titulo1\">Titulo 1: </label>");
                writer.println("    <input type=\"text\" required name=\"titulo1\" value=\"\" id=\"titulo1\"/></br>");
                writer.println("    <label for=\"texto1\">Texto 1: </label>");
                writer.println("    <input type=\"text\" required name=\"texto1\" value=\"\" id=\"texto1\"/></br></br>");

                writer.println("    <label for=\"titulo2\">Titulo 2: </label>");
                writer.println("    <input type=\"text\" required name=\"titulo2\" value=\"\" id=\"titulo2\"/></br>");
                writer.println("    <label for=\"texto2\">Texto 2: </label>");
                writer.println("    <input type=\"text\" required name=\"texto2\" value=\"\" id=\"texto2\"/></br></br>");

                writer.println("    <label for=\"titulo3\">Titulo 3: </label>");
                writer.println("    <input type=\"text\" required name=\"titulo3\" value=\"\" id=\"titulo3\"/></br>");
                writer.println("    <label for=\"texto3\">Texto 3: </label>");
                writer.println("    <input type=\"text\" required name=\"texto3\" value=\"\" id=\"texto3\"/></br></br>");

                writer.println("    <label for=\"titulo4\">Titulo 4: </label>");
                writer.println("    <input type=\"text\" required name=\"titulo4\" value=\"\" id=\"titulo4\"/></br>");
                writer.println("    <label for=\"texto4\">Texto 4: </label>");
                writer.println("    <input type=\"text\" required name=\"texto4\" value=\"\" id=\"texto4\"/></br></br>");
                
                writer.println("     <input type=\"file\" name=\"arquivo\" value=\"\" />");
                
                writer.println("    <input type=\"submit\" value=\"adicionar\"></p>");
                writer.println("</form>");
                writer.println("</fieldset>");

                writer.println("<ul>");
                try {
                    Connection conexao = Conexao.abrir();
                    ResultSet rs = Conexao.listarConteudosPorIdUsuario(conexao, (int) req.getSession().getAttribute("usuario"));
                    while (rs.next()) {
                        writer.println("    <li><a href=\"site?page=site&id=" + rs.getInt("id") + "\">" + rs.getInt("id") + " - " + rs.getString("titulo1") + "</a>");

                        writer.println("<form style=\"display: inline-block; padding-left: 8px;\" action=\"site\" method=\"POST\" accept-charset=\"utf-8\">");
                        writer.println("    <input type=\"hidden\" name=\"id\" value=" + rs.getInt("id") + ">");
                        writer.println("    <input type=\"submit\" name=\"metodo\" value=\"deletar\"/>");
                        writer.println("</form>");

                        writer.println("    </li>");
                    }
                    conexao.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                writer.println("</ul></br>");
                writer.println("<a href=\"index.html\">Voltar para o Inicio</a>");
                writer.println("    </body>");
                writer.println("</html>");

            } else {
                res.sendRedirect("session");
            }
        } else if (req.getParameter("page").equalsIgnoreCase("pesquisa")) {
                try {
                PrintWriter writer = res.getWriter();
                res.setContentType("text/html; encoding=utf-8");
                writer.println("<!DOCTYPE HTML>");
                writer.println("<html>");
                writer.println("    <head>");
                writer.println("        <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
                writer.println("        <title>Lista de Sites</title>");
                writer.println("    </head>");
                writer.println("    <body>");
                writer.println("        <h1>Lista de Sites</h1>");
                writer.println("        <form action=\"site\" method=\"GET\">");
                writer.println("            <input type=\"hidden\" name=\"page\" value=\"pesquisa\">");
                writer.println("            <input type=\"text\" name=\"campo_pesquisar\">");
                writer.println("            <input type=\"submit\" value=\"buscar\">");
                writer.println("        </form>");
                writer.println("        <ol>");

                Connection conexao = Conexao.abrir();
                ResultSet rs = Conexao.listarConteudos(conexao);

                while (rs.next()) {
                    if (req.getParameter("campo_pesquisar").equalsIgnoreCase("")) {
                        writer.println("<li><a href=\"site?page=site&id=" + rs.getInt("id") + "\">"  + rs.getString("titulo1") + "</a>");
                    } else if (rs.getString("texto1").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("titulo1").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("titulo2").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("titulo3").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("texto1").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("texto2").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("texto3").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("titulo4").contains(req.getParameter("campo_pesquisar"))
                            || rs.getString("texto4").contains(req.getParameter("campo_pesquisar"))) {
                        writer.println("<li><a href=\"site?page=site&id=" + rs.getInt("id") + "\">" + rs.getString("titulo1") + "</a>");
                    }
                }

                writer.println("</ol>");
                writer.println("<a href=\"index.html\">Voltar para o Inicio</a>");
                writer.println("    </body>");
                writer.println("</html>");

            } catch (SQLException ex) {
                Logger.getLogger(SiteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                //Procurar blog por ID
                Connection conexao = Conexao.abrir();
                ResultSet rs = Conexao.ConteudoPorId(conexao, id);
                rs.next();
                
                PrintWriter writer = res.getWriter();
                res.setContentType("text/html; encoding=utf-8");
                res.setContentType("text/html;charset=UTF-8");
                writer.println("<!DOCTYPE html>");
                writer.println("<html>");
                writer.println("    <head>");
                writer.println("        <title> Tapestry </title>");
                //writer.println("        <link href= \"<%=request.getContextPath()%>styles.css\" rel=\"stylesheet\" type=\"text/css\">");
                //writer.println("        <link rel=\"stylesheet\" type = \"text/css\" href=\"styles.css\">");
                writer.println("        <link rel= 'stylesheet' href='styles.css' type='text/css' />");
                writer.println("    </head>");
                writer.println("    <body>");
                writer.println("        <div class = \"inicio\">");
                writer.println("            <div class = \"principal\">");
                writer.println("                <h1 class = \"titulo\"><a>Tapestry</a>");
                writer.println("                    <span>by TEMPATED</span>");
                writer.println("                </h1>");
                writer.println("            </div>");
                writer.println("            <div class = \"div-menu\">");
                writer.println("                <ul>");
                writer.println("                    <li class = \"menu\" id = \"homepage\">Homepage</li>");
                writer.println("                    <li class = \"menu\">Three Column</li>");
                writer.println("                    <li class = \"menu\">Two Column</li>");
                writer.println("                    <li class = \"menu\">Two Column</li>");
                writer.println("                    <li class = \"menu\">One Column</li>");
                writer.println("                </ul>");
                writer.println("            </div>");
                writer.println("            <div class = \"imagem-menu\">");
                writer.println("                <img src = \"imagens/menu.png\">");
                writer.println("            </div>");
                writer.println("        </div>");
                writer.println("        <div class = \"inicio\">");
                writer.println("            <div id = \"imagem-principal\">");
                writer.println("                <a><img src = \"imagens/pics01.jpg\"></a>");
                writer.println("            </div>");
                writer.println("        </div>");
                writer.println("        <div class = \"inicio\">");
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img1\">");
                writer.println("                    <h2 class = \"titulo2\">Maecenas luctus lectus</h2>");
                writer.println("                    <p class = \"subtitulo\">In posuere eleifend odio. Quisque semper augue mattis maecenas ligula.</p>");
                writer.println("                    <p><a href = \"#\"><img src = \"imagens/pics02.jpg\"></a></p>");
                writer.println("                </section>");
                writer.println("            </div>");
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img2\">");
                writer.println("                    <h2 class = \"titulo2\">Etiam posuere augue</h2>");
                writer.println("                    <p class = \"subtitulo\">Donec leo. Vivamus nibh in augue. Praesent a lacus at urna congue rutrum.</p>");
                writer.println("                    <p><a href = \"#\"><img src = \"imagens/pics03.jpg\"></a></p>");
                writer.println("                </section>");
                writer.println("            </div>");
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">Fusce ultrices fringilla</h2>");
                writer.println("                    <p class = \"subtitulo\">Pellentesque pede. Donec pulvinar metus. In eu lectus pulvinar mollis.</p>");
                writer.println("                    <p><a href = \"#\"><img src = \"imagens/pics04.jpg\"></a></p>");
                writer.println("                </section>");
                writer.println("            </div>");
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">Nulla luctus eleifend</h2>");
                writer.println("                    <p class = \"subtitulo\">In posuere eleifend odio. Quisque semper augue sed maecenas ligula.</p>");
                writer.println("                    <p><a href = \"#\"><img src = \"imagens/pics05.jpg\"></a></p>");
                writer.println("                </section>");
                writer.println("            </div>");       
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">"+ rs.getString("titulo1") + "</h2>");
                writer.println("                    <p class = \"subtitulo\">"+rs.getString("texto1")+"</p>");
                writer.println("                </section>");
                writer.println("            </div>");              
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">"+ rs.getString("titulo2") + "</h2>");
                writer.println("                    <p class = \"subtitulo\">"+rs.getString("texto2")+"</p>");              
                writer.println("                </section>");
                writer.println("            </div>");
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">"+ rs.getString("titulo2") + "</h2>");
                writer.println("                    <p class = \"subtitulo\">"+rs.getString("texto2")+"</p>");              
                writer.println("                </section>");
                writer.println("            </div>"); 
                writer.println("            <div class = \"final\">");
                writer.println("                <section id = \"img3\">");
                writer.println("                    <h2 class = \"titulo2\">"+ rs.getString("titulo4") + "</h2>");
                writer.println("                    <p class = \"subtitulo\">"+rs.getString("texto4")+"</p>");
                writer.println("                </section>");
                writer.println("            <div class = \"final\" id = \"bloco\">");
                writer.println("                <b>Etiam rhoncus volutpat</b>");
                writer.println("                <img src = \"imagens/seta.jpg\">");
                writer.println("            </div>");
                writer.println("        </div>");
                writer.println("        <div id = \"imagem-principal\">");
                writer.println("             <a><img src = \"imagens/" + rs.getString("imagem")+ "\"></a>");
                writer.println("        </div>");
                writer.println("        <a href=\"index.html\">Voltar para o Inicio</a>");
                writer.println("    </body>");
                writer.println("</html>");
                } catch (SQLException ex) {
                    Logger.getLogger(SiteServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    public void doPost(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        try {
            HttpSession session = req.getSession();

            if (session.getAttribute("logado") != null && session.getAttribute("logado").equals(Boolean.TRUE)) {

                Connection conexao = Conexao.abrir();
                ResultSet rs = Conexao.getUserByEmail(conexao, req.getParameter("email"));
                rs.next();

                if (req.getParameter("metodo") != null && req.getParameter("metodo").equals("deletar")) {
                    Conexao.excluirConteudo(conexao, Integer.parseInt(req.getParameter("id")));
                    res.sendRedirect("site?page=cadastro");
                } else if (req.getParameter("metodo") != null && req.getParameter("metodo").equals("adicionar")) {
                    System.out.println("ADICIONAR CONTEUDO " + (int) session.getAttribute("usuario") + " " + req.getParameter("titulo1"));
                }
                Conexao.adicionarConteudo(conexao,
                        (int) session.getAttribute("usuario"),
                        req.getParameter("titulo1"),
                        req.getParameter("titulo2"),
                        req.getParameter("titulo3"),
                        req.getParameter("texto1"),
                        req.getParameter("texto2"),
                        req.getParameter("texto3"),
                        req.getParameter("titulo4"),
                        req.getParameter("texto4"),
                        req.getParameter("imagem"));
                        
                Part part = req.getPart("arquivo");
                String images_path = req.getServletContext().getRealPath("/imagens");
                InputStream in = part.getInputStream();
                Files.copy(in, Paths.get(images_path + "/" + part.getSubmittedFileName()), StandardCopyOption.REPLACE_EXISTING);
                part.delete();  
                
                /*res.getWriter().println("<h1>Conteudo cadastrado com sucesso</h1>"
                        + "     <a href=\"blog?page=pesquisa\"><b><h3>Ir pagina com blogs</h3></b></a>"
                        + "     <a href=\"index.html\"><-- Home</a>");*/
                res.sendRedirect("site?page=cadastro");
                
                
                conexao.close();
            } else {
                res.sendRedirect("session");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}