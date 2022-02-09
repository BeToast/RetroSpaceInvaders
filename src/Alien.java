import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Alien extends Sprite2D{
    int framesDrawn = 0;
    boolean dead = false;
    public Alien(Image i1, Image i2, Dimension d){
        super(i1,d);
        img2 = i2;
        framesDrawn = 0;
        setXSpeed(4);
    }

    public boolean checkCollision(List<Bullet> bulletList){
        Iterator<Bullet> iterator = bulletList.iterator();
        y = ((int)yCoord - imgHeight/2);
        x = ((int)xCoord - imgWidth/2); 
        while(iterator.hasNext()){
            Bullet bullet = (Bullet)iterator.next();
            int y2 = ((int)bullet.yCoord - bullet.imgHeight/2);
            int x2 = ((int)bullet.xCoord - bullet.imgWidth/2);          
            if(!dead &&
                ( (x<x2 && x+bullet.imgWidth>x2) || (x2<x && x2+bullet.imgWidth>x) ) 
                && 
                ( (y<y2 && y+bullet.imgHeight>y2) || (y2<y && y2+bullet.imgHeight>y) )
            ) {
                return true;
            }
        }
        return false;
    }
    
    public void reverseDirection(){
        yCoord+=imgHeight;
        xSpeed*=-1;
    }

    @Override
    public boolean move(){
        if(xSpeed > 0){
            if(xCoord >= dim.width-imgWidth){
                xCoord+=xSpeed;
                return false;
            }              
        }else if(xSpeed < 0){
            if(xCoord <= 0){
                xCoord+=xSpeed;
                return false;
            }         
        }
    xCoord+=xSpeed;
    return true;   
    }

    @Override
    public void paint(Graphics g) {
        framesDrawn++;
        if (framesDrawn%100<50)
            g.drawImage(img1, xCoord, yCoord, null);
        else
            g.drawImage(img2, xCoord, yCoord, null);
    }
}
