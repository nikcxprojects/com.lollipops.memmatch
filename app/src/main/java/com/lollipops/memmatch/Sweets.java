package com.lollipops.memmatch;

public class Sweets {

    int pos;
    int image;
    String nameImage;
    int checked;

    public Sweets(){}

    public Sweets(int pos,int image,String nameImage,int checked) {
        this.pos = pos ;
        this.image = image ;
        this.nameImage = nameImage ;
        this.checked = checked ;

    }

    public int getPos(){ return pos; }
    public int getImage(){ return image; }
    public int getChecked(){ return checked; }
    public void setChecked(int checked) { this.checked = checked; }
    public String getNameImage(){ return nameImage; }
}
