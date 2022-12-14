package com.example.kopagas.model;

import java.util.ArrayList;

public class Items {
    private ArrayList<Item> items;
    private ArrayList<Images> images;

    public Items() {

    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

}