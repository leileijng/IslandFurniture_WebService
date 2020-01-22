package A6_servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.*;
import java.util.Collection;

public class UploadFile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UploadFile() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO Auto-generated method stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO Auto-generated method stub
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            // String disposition = "attachment; fileName=data.csv";
            //response.setHeader("Content-Disposition", disposition);

            Part file = request.getPart("javafile");
			// System.out.println(request.getParameter("javafile"));

            //String fileName = request.getParameter("admin");
            String s = file.getHeader("content-disposition");

            System.out.println(s);

            int index = s.indexOf("filename");

            index = index + 9;//move to the " in filename="

            int endIndex = s.indexOf("\"", index + 1);
            String fileName = s.substring(index + 1, endIndex);
            //fileName="a.html";
            //System.out.println(fileName);
            if (file != null) {

                InputStream fileInputStream = file.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(request.getServletContext().getRealPath("/testSolution/") + "/" + fileName);
                OutputStream fileOutputStream2 = new FileOutputStream("c:/tempFiles/" + fileName);

                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    //System.out.println("!");
                    fileOutputStream.write(nextByte);
                    fileOutputStream2.write(nextByte);

                }
                fileOutputStream.close();
                fileOutputStream2.close();
                fileInputStream.close();

                out.println("Upload Successful!");
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
