import java.awt.*;

abstract class Sprite2D { 
    protected int x,y,xCoord,yCoord,imgHeight,imgWidth,xSpeed,ySpeed;
    protected Image img1, img2;
    protected Dimension dim;
    protected Boolean dead = false;

    public Sprite2D(Image i, Dimension d) {
        img1 = i;
        dim = d;
        imgHeight = img1.getHeight(null);
        imgWidth = img1.getWidth(null);
    }

    public void setPosition(double x, double y){
        xCoord = ((int)x - imgHeight/2);
        yCoord = ((int)y - imgWidth/2);        
    }

    public void editXSpeed(double x){
        xSpeed += (int)x;
    }

    public void setXSpeed(double x){
        xSpeed = (int)x;
    }

    public boolean move(){return false;}

    public void paint(Graphics g) {
        g.drawImage(img1, xCoord, yCoord, null);
    }

    public Image getImg1(){
        return img1;
    }

    public Image getImg2(){
        return img2;
    }

}