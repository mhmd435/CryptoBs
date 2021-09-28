package com.besenior.cryptobs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderImageModel {

    @SerializedName("photos")
    private List<PageViewImage> listImage;

    public SliderImageModel(List<PageViewImage> listImage) {
        this.listImage = listImage;
    }

    public List<PageViewImage> getListImage() {
        return listImage;
    }

    public static class PageViewImage{

        @SerializedName("imgUrl")
        private String img;

        public PageViewImage(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }
    }
}
