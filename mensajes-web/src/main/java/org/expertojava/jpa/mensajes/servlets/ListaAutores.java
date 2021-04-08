package org.expertojava.jpa.mensajes.servlets;

import org.expertojava.jpa.mensajes.modelo.Autor;
import org.expertojava.jpa.mensajes.servicio.AutorServicio;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="listaAutores", urlPatterns="/listaAutores")
public class ListaAutores extends HttpServlet {

    @Inject
    AutorServicio autorServicio;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException,
            IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<h1>Lista de autores</h1>");
        out.println("<ul>");
        List<Autor> lista = autorServicio.listAllAutores();
        for (Autor a : lista) {
            out.println("<li> " + a.getNombre() + "</li>");
        }
        out.println("</ul>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
