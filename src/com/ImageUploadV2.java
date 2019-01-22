package com;

import java.io.BufferedReader;
import java.io.File;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageUploadV2 extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2453993668448877579L;
    
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

        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        while( (str = br.readLine()) != null ) {
            sb.append(str);
        }    
        String para = sb.toString();
        String[] paraArray = para.split("（）") ;
        String uuid = paraArray[0];
        String chunk = paraArray[3];
        String caseNo = paraArray[4];
        String picType = paraArray[5];
        String docPhoto = paraArray[6];
        String rotateFlag = "";
        String picSeq = "";
        
        int index = 0;
        try {
            String indexStr =  paraArray[1];
            index = Integer.valueOf(indexStr);
        } catch ( Exception ex ) {
            //錯誤
        }
        
        int maxLength = 0;
        try {
            String maxLengthStr = paraArray[2];
            maxLength = Integer.valueOf(maxLengthStr);
        } catch ( Exception ex ) {
            //錯誤
        }
        
        try {

            TempImage temp = new TempImage(maxLength, picType, caseNo, docPhoto, rotateFlag, picSeq);
            String fileName = "/temp/"+uuid+".tmp";
            File uploadTemp = new File(fileName);
            if(!uploadTemp.exists()) {
                uploadTemp.getParentFile().mkdirs();
                try {
                    uploadTemp.createNewFile();
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            temp.init(fileName);
            temp.setImageChunks(index, chunk);
            
            String finalStr = temp.toString();
            if( finalStr.equals("") ) {
                System.out.println("Phase : " +index+"_success");
                temp.update(fileName, index+"_"+chunk);
                response.getWriter().print(index+"_success");
            } else {
                //產生完整字串
                System.out.println(finalStr);
                System.out.println("Phase : full_success");
                response.getWriter().print("full_success");
            }
        } catch (Exception ex ) {
            System.out.println("Phase : " +index+"_failed");
            response.getWriter().print(index+"_failed");
        }
    }
}
