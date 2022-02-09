import java.awt.*;

public class Bullet extends Sprite2D {
    
    public Bullet(Image i, Dimension d){
        super(i,d);
        ySpeed = 4;
    }

    @Override
    public boolean move(){
        yCoord-=ySpeed; 
        return true;   
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img1, xCoord, yCoord, null);
    }
}
