<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
    <head>
        <title>Start Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h1>Hello World!</h1>
        <h2>Nuevo mensaje</h2>
        <form action="<%=request.getContextPath()%>/nuevoAutorMensaje">
        <p>Introduce tu login: <input type="text" name="login"><br/>
        Introduce tu mensaje: <input type="text" name="mensaje"></p>
        <input type="submit" value="Enviar">
        </form>
    <h2>Acciones</h2>
    <ul>
        <li><a href="<%=request.getContextPath()%>/listaAutores">Listar autores</a></li>
    </ul>
    </body>
</html>
