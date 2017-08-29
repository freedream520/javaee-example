package somepack;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;

@WebServlet(value = "/")
@MultipartConfig
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = "";
        String respMsg = "";

        req.setCharacterEncoding("utf-8");
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = String.join("", req.getParameterValues(name));

            if ("q".equalsIgnoreCase(name)) {
                q = value;
            }
        }

        Message msg = new Message();
        msg.setTimestamp(new Date(System.currentTimeMillis()));
        msg.setText(q);
        msg.setClientId(req.getSession().getId());
        StringWriter xml = new StringWriter();
        JAXB.marshal(msg, xml);

        respMsg = sender.sendMessage(xml.toString());

        resp.getWriter().write(respMsg);
    }
}
