package com;

import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUUID  extends HttpServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4550997041872658025L;

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            core( request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            core( request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void core( HttpServletRequest request, HttpServletResponse response ) throws Exception {
        System.out.println("Phase : get UUID");
        UUID uuid = UUID.randomUUID();
        response.getWriter().print(uuid.toString());
    }
}
