package org.expertojava.jpa.mensajes.servlets;

import org.expertojava.jpa.mensajes.servicio.AutorServicio;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="nuevoAutorMensaje", urlPatterns="/nuevoAutorMensaje")
public class NuevoAutorMensaje extends HttpServlet {

    @Inject
    AutorServicio autorServicio;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException,
            IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String mensaje = request.getParameter("mensaje");
        System.out.println(login);
        System.out.println(mensaje);

        autorServicio.createAutorMensaje(login, login, mensaje);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<h3>Autor y mensaje correctos</h3>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
