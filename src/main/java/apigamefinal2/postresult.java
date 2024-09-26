package apigamefinal2;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class postresult extends HttpServlet {
private static final long serialVersionUID = 1L;
private static GameScoreManager scoreManager = new GameScoreManager();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String player = req.getParameter("player");
        
        if (player != null && !player.isEmpty()) {
        scoreManager.recordWin(player);
        int updatedScore = scoreManager.getPlayerScore(player);
        resp.getWriter().write("Le score de " + player + " est maintenant de : " + updatedScore);
        } else {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nom du joueur requis !");
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        if ("/get-score".equals(servletPath) && pathInfo != null && !pathInfo.isEmpty()) {
            String player = pathInfo.substring(1);

            if (player != null && !player.isEmpty()) {
                if (scoreManager.playerExists(player)) {
                    int score = scoreManager.getPlayerScore(player);
                    resp.getWriter().write(player + " a " + score + " victoires.");
                } else {
                    resp.getWriter().write("Ce joeueur n'existe pas !");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nom du joueur requis !");
            }
        } else if ("/ranking".equals(servletPath)) {
            List<Map.Entry<String, Integer>> topPlayers = scoreManager.getTopPlayers();
            StringBuilder ranking = new StringBuilder();

            for (Map.Entry<String, Integer> player : topPlayers) {
                ranking.append("Player : ").append(player.getKey())
                       .append(" | Score : ").append(player.getValue()).append("\n");
            }

            resp.setContentType("text/plain");
            resp.getWriter().write(ranking.toString());
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format. Expected: /get-score/{player} or /ranking");
        }
    }





}
