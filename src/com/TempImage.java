package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Administrator
 *  切塊圖片的entity
 *  除了基本資訊之外
 *  主要就是用imageChunks表示圖片切塊的集
 *  切塊圖片則是以實體檔案的形式存在local端
 */

public class TempImage {
    private String picType = "";
    private String docPhoto = "";
    private String rotateFlag = "";
    private String picSeq = "";
    private String caseNo = "";
    private int maxLength = 0; 
    private Map<Integer, String> imageChunks = null;
    
    public TempImage( int maxLength, String picType, String caseNo, String docPhoto, String rotateFlag, String picSeq ) {
        this.maxLength = maxLength;
        this.picType = picType;
        this.caseNo = caseNo;
        this.docPhoto = docPhoto;
        this.rotateFlag = rotateFlag;
        this.picSeq = picSeq;
        imageChunks = new HashMap<Integer, String>();
    }
    
    //檔案名就是一開始取的UUID
    //主要是要把切塊讀出來建構成imageChunks
    public void init( String fileName ) throws Exception {
        BufferedReader br = null;
        FileReader fr = new FileReader(fileName);
        
        br = new BufferedReader(fr);
        String sCurrentLine = "";
        while ((sCurrentLine = br.readLine()) != null) {
            String chunkIndex = sCurrentLine.split("_")[0];
            String chunkValue = sCurrentLine.split("_")[1];
            imageChunks.put(Integer.valueOf(chunkIndex), chunkValue);
        }
        br.close();
    }
    
    //更新檔案, 把新接收到的圖片切塊存起來
    public void update( String fileName, String newChunk ) throws Exception {
        BufferedWriter bw = null;
        FileWriter fw = new FileWriter(fileName, true);
        
        bw = new BufferedWriter(fw);
        bw.append(newChunk+"\n");
        bw.close();
    }
    
    public void setImageChunks ( int index, String chunkStr ) {
        if( index >= 0 && !isEmptyString(chunkStr) ) {
            imageChunks.put(index, chunkStr);
        } else {
            //錯誤
        }
    }
    
    public void setMaxLength ( int length ) {
        if( length > 0 ) {
            maxLength = length;
        } else {
            //錯誤
        }
    }
    
    //回吐我們實際上要做處理的字串
    //base64 + '（）' + picType +'（）'+caseNo +'（）'+docphoto
    public String toString() {
        StringBuffer output = new StringBuffer();
        String imageStr = "";
        if( imageChunks.size() == maxLength ) {
            for( int index=0 ; index < maxLength; index++) {
                imageStr = imageStr + imageChunks.get(index);
            }
            output.append(imageStr);
            output.append("（）"+picType);
            output.append("（）"+caseNo);
            output.append("（）"+docPhoto);
            
            if(!isEmptyString(rotateFlag)) {
                output.append("（）"+rotateFlag);
            }
            if(!isEmptyString(picSeq)) {
                output.append("（）"+picSeq);
            }
        } else {
            //錯誤
        }
        return output.toString();
    }
    
    private boolean isEmptyString( String str ) {
        if( str == null ) {
            return true;
        }
        if( str.length() <= 0) { 
            return true;
        }
        return false;
    }

    
    public String getPicType(){
        return picType;
    }

    
    public void setPicType(String picType){
        this.picType = picType;
    }

    
    public String getDocPhoto(){
        return docPhoto;
    }

    
    public void setDocPhoto(String docPhoto){
        this.docPhoto = docPhoto;
    }

    
    public String getRotateFlag(){
        return rotateFlag;
    }

    
    public void setRotateFlag(String rotateFlag){
        this.rotateFlag = rotateFlag;
    }

    
    public String getPicSeq(){
        return picSeq;
    }

    
    public void setPicSeq(String picSeq){
        this.picSeq = picSeq;
    }

    
    public String getCaseNo(){
        return caseNo;
    }

    
    public void setCaseNo(String caseNo){
        this.caseNo = caseNo;
    }

    
    public Map<Integer, String> getImageChunks(){
        return imageChunks;
    }

    
    public void setImageChunks(Map<Integer, String> imageChunks){
        this.imageChunks = imageChunks;
    }

    
    public int getMaxLength(){
        return maxLength;
    }
}
