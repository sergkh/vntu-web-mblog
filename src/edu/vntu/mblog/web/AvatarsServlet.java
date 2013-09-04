package edu.vntu.mblog.web;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.services.UsersService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/avatars")
@MultipartConfig(fileSizeThreshold=5*1024*1024, // 5 MB
        maxFileSize=10*1024*1024,               // 10MB
        maxRequestSize=10*1024*1024)            // 10MB
public class AvatarsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String AVATARS_DIR = "/static/img/avatars/";

    private final UsersService usersService = UsersService.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().log("User picture received");

        // gets absolute path of the web application and adds relative path to avatars
        String dirName = request.getServletContext().getRealPath("") + AVATARS_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(dirName);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        HttpSession session = request.getSession(false);

        User user = (User) session.getAttribute(SessionConstants.USER);

        String fileName = null;

        for (Part part : request.getParts()) {
            String extension = getFileExt(part);
            // filename not found possibly user didn't selected any file
            if(extension == null) continue;

            fileName = user.getLogin() + extension;
            part.write(dirName + File.separator + fileName);
        }

        if(fileName != null) {
            usersService.setAvatar(user.getId(), fileName);
        }

        // redirect back to users home page
        response.sendRedirect(request.getContextPath() + "/users/" + user.getLogin());
    }

    /**
     * Extracts file extension from upload file name.
     *
     * @param part uploading part
     * @return file extension from packet header.
     */
    private String getFileExt(Part part) {
        // header looks like:
        // Content-disposition form-data; name="dataFile"; filename="myphoto.png"

        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                String filename = s.substring(s.indexOf("=") + 2, s.length()-1);
                if(filename.isEmpty()) return null;

                return filename.substring(filename.lastIndexOf('.')).toLowerCase();
            }
        }

        return null;
    }
}
