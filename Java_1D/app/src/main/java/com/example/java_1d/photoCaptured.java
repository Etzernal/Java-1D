package com.example.java_1d;

class photoCaptured {
    private static final photoCaptured ourInstance = new photoCaptured();
    private String imgPath;

    static photoCaptured getInstance() {
        return ourInstance;
    }

    private photoCaptured() {
    }

    public String getImgPath(){
        return imgPath;
    }

    public void setImgPath(String path){
        imgPath = path;
    }
}

