package com.example.ajeo.Models;

import android.net.Uri;

public class ModalClass {

    String imageName;
    Uri imgModel;

    public ModalClass(String imageName, Uri imgModel) {
        this.imageName = imageName;
        this.imgModel = imgModel;
    }

    public Uri getImgModel() {
        return imgModel;
    }

    public void setImgModel(Uri imgModel) {
        this.imgModel = imgModel;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
