import java.awt.*;
import java.awt.image.*;
import java.util.Iterator;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;

public class InvadersApp extends JFrame implements Runnable, KeyListener {
    private BufferStrategy strategy;
    private static final long serialVersionUID = 1L;
    private static final Dimension WindowSize = new Dimension(800,600);
    private static final int NUMALIENS = 30;
    private int deadAliens = 0;
    private int timesRedrawn = 0;
    private static Image bulletImage = null;
    private Spaceship spaceShip;
    public Alien[] AlienArr = new Alien[NUMALIENS];
    private boolean isInitialised = false, isRunning = false;
    public boolean gameRunning = false;
    public final List<Bullet> bulletList = new ArrayList<Bullet>();
    private static String workingDirectory = System.getProperty("user.dir");

    public InvadersApp() {
        //Create and set up the window.
        this.setTitle("Threads and Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window, centred on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
        addKeyListener(this);

        //double buffer to ensure crip frames
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        //GET IMAGES
        ImageIcon icon = new ImageIcon(workingDirectory + "\\lib\\alien_ship_1.png");
        Image alienImage1 = icon.getImage();
        icon = new ImageIcon(workingDirectory + "\\lib\\alien_ship_2.png");
        Image alienImage2 = icon.getImage();
        icon = new ImageIcon(workingDirectory + "\\lib\\player_ship.png");
        Image spaceShipImage = icon.getImage();
        icon = new ImageIcon(workingDirectory + "\\lib\\bullet.png");
        bulletImage = icon.getImage();

        int spacing = 20;
        // create+initialise (via their constructor) the GameObject instances
        x = alienImage1.getWidth(null) + 10; y = 30;
        for (int i=0; i< NUMALIENS; i++) {
            AlienArr[i] = new Alien(alienImage1, alienImage2 ,WindowSize);    //adds first fram to Sprite and runs constructor. Adds second frame to Sprite
        }
        redrawGrid(spacing);
        spaceShip = new Spaceship(spaceShipImage,WindowSize);
        
        Thread t = new Thread(this);
        t.start();
        isInitialised = true;
        
    }

    public void startNewGame(){
        isRunning = true;
        gameRunning = true;
    }

    public void startNewWave(int spacing){
        redrawGrid(spacing);
    }

    // thread's entry point
    public void run() {
        boolean reverse = false;
        for(;;) {
            if(gameRunning == true){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {System.out.println("lol");}
                
                if(deadAliens >= NUMALIENS){
                    redrawGrid(NUMALIENS);
                    deadAliens = 0;
                }     
                for (int i=0;i<NUMALIENS; i++){
                    if(AlienArr[i].dead == false)      
                        if(AlienArr[i].move()==false){
                            reverse = true;
                        }
                    if(AlienArr[i].checkCollision(bulletList)){
                        AlienArr[i].dead = true;
                        deadAliens++;
                    }
                }
                if(reverse){
                    for (int j=0;j<NUMALIENS; j++){
                        AlienArr[j].reverseDirection();
                    }
                    reverse = false;
                }
                spaceShip.move();
                // update window with new movement
                this.repaint();                
                
            } else {
                this.repaint();
            }           
        }
    }  
    // application's paint method
    public void paint(Graphics g) {
        if (!isInitialised){  
            if(isRunning)
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WindowSize.width, WindowSize.height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("SPACE INVADERS", 180, 280);
            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("PRESS  S P A C E  TO BEGIN", 260, 320);
            }else if(gameRunning){
                // clear canvas
                g = strategy.getDrawGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WindowSize.width, WindowSize.height);
                g.setFont(new Font("Arial",Font.PLAIN,30));
                g.drawString("WAVE: "+timesRedrawn, 20, 500); //this doesnt work idk why ngl.
                // redraw all game objects
                spaceShip.paint(g);
                    Iterator<Bullet> iterator = bulletList.iterator();
                    while(iterator.hasNext()){
                        Bullet b = (Bullet)iterator.next();
                        b.move();
                        b.paint(g);
                    }
                for (int i=0;i<NUMALIENS; i++)
                    if(AlienArr[i].dead == false)
                        AlienArr[i].paint(g);  
                strategy.show();
            }
    }

    //creates Aliens in a grid.
    public void redrawGrid(int spacing){
        int x = 0, y = 0;
        for (int i=0; i< NUMALIENS; i++) {
        if(timesRedrawn != 0)AlienArr[i].xSpeed *= timesRedrawn;
        AlienArr[i].dead = false;
            if(i%6==0){
                x=AlienArr[i].getImg1().getWidth(null);                   //generate grid
                y+=AlienArr[i].getImg1().getHeight(null) + spacing;
            }
            AlienArr[i].setPosition(x,y);// positions the grid 
            x+=AlienArr[i].getImg1().getWidth(null)+spacing;
        }
    }
          
    //make new Bullet
    public void createBullet(Image bulletImage, Dimension WindowSize){   
        Bullet bullet = new Bullet(bulletImage, WindowSize);
        bullet.setPosition(spaceShip.xCoord+spaceShip.imgWidth/2+5, spaceShip.yCoord-spaceShip.imgHeight/2);
        bulletList.add(bullet);
    }
     
    public Image getBulletImage(){
        return bulletImage;
    }

    public List<Bullet> getBulletList(){   
        return bulletList;
    }

    //key input stuff below
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        String keyCode = KeyEvent.getKeyText(e.getKeyCode());
        if(keyCode == "Right"){
            spaceShip.editXSpeed(-5);
        }else if (keyCode == "Left"){
            spaceShip.editXSpeed(5);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String keyCode = KeyEvent.getKeyText(e.getKeyCode());
        if(gameRunning == false){
            if(keyCode == "Space"){
                startNewGame();
            }
        }else{
            if(keyCode == "Right"){
                spaceShip.setXSpeed(5);
            } else if(keyCode == "Left"){
                spaceShip.setXSpeed(-5);
            } else if(keyCode == "Space"){
                createBullet(getBulletImage(),WindowSize);
            }
        }
    }
    
    public static void main(String[] args) {
        InvadersApp w = new InvadersApp();
    }   
}