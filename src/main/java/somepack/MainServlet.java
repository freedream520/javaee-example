package somepack;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class MainServlet extends HttpServlet {

    @Inject
    Sender sender;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        if (q == null || q.isEmpty()) {
            req.setAttribute("message", sender.sendMessage("empty query given. send normal status"));
        }
        else {
            req.setAttribute("message", sender.sendMessage(q));
        }
        req.setAttribute("status", sender.getStatus());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
