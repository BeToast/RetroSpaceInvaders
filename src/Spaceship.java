import java.awt.*;

public class Spaceship extends Sprite2D{
    public Spaceship(Image i, Dimension d){
        super(i,d);
        setXSpeed(0);
        setPosition(dim.width/2, dim.height - imgHeight - 5);
    } 

    public boolean move(){
        xCoord += xSpeed; 
        return true;
    }

}
