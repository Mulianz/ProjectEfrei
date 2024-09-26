package apigamefinal2;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8082);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(new ServletHolder(new hello()), "/hello");
        handler.addServlet(new ServletHolder(new ping()), "/ping");
        handler.addServlet(new ServletHolder(new postresult()), "/post-game-result");
        handler.addServlet(new ServletHolder(new postresult()), "/get-score/*");
        handler.addServlet(new ServletHolder(new postresult()), "/ranking");


        server.start();
        server.join();
    }
}