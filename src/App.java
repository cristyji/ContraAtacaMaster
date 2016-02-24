
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rubén
 */
public class App extends JFrame implements Runnable, KeyListener{
    private int iCaidas; // Numero de caídas
    private int iDirPrincipal; // Direccion del jugador
    private int iVidas; // Vidas del jugador
    private int iPuntos; // Puntos del jugador
    private boolean bPausa; // Pausa del juego
    private boolean bOtraBala; // Permite disparar solo una bala
    private static final int WIDTH = 800;    //Ancho del JFrame
    private static final int HEIGHT = 600;    //Alto del JFrame

    private LinkedList <Malo>lklMalos; // Lista de los malos
    private LinkedList <Bala>lklBalas; // Lista de las balas
    private Base basPrincipal; // Objeto principal
    private Image imaImagenFondo; // Imagen de fondo
    private Image imaPierde; // Imagen a desplegar cuando pierde
    
    /* objetos para manejar el buffer del Applet y 
       que la imagen no parpadee */
    private Image imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    
    /**
     * init
     *
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse y se definen funcionalidades.
     *
     */
    public void init() {
        // Crea listas encadenadas
        lklMalos = new LinkedList<Malo>();
        lklBalas = new LinkedList<Bala>();
        
        // Valores iniciales de las variables
        iCaidas = 0;
        iDirPrincipal = 0;
        iPuntos = 0;
        iVidas = 5;
        bPausa = false;
        bOtraBala = true;
        
        inicializaObjetos();
        
        // Reposiciona objetos
        reposicionaMalos();
        reposicionaPrincipal();
        
        addKeyListener(this); // AGREGO KEYLISTENER
    }
    
    public void inicializaObjetos(){
        // Defino y creo el principal
	URL urlImagenCanasta = this.getClass().getResource("Monkey.png");
	basPrincipal = new Base(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenCanasta));
        
        inicializaMalos();
        
        // Defino y creo la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Fondo.jpg");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        
        // Defino y creo la imagen de pierde.
        URL urlImagenPierde = this.getClass().getResource("GameOver.png");
        imaPierde = Toolkit.getDefaultToolkit().getImage(urlImagenPierde);
    }
    
    public void inicializaMalos(){
        // Genero random de los malos entre 10 y 15
        int iRandom = alAzar(10,15); // Malos normales
        int iRandEsp = alAzar(1,2); // Malos que siguen
        
        // Imagen del malo especial
        URL urlImagenMalo = this.getClass().getResource("Parrot.gif");
        
        // Defino y creo malos especiales
        for(int iC = 0; iC < iRandEsp; iC++){
            // Creo un malo
            Malo malMalo = new Malo(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo), 'e');
            // Añado malo a la lista
            lklMalos.add(malMalo);
        }
        
        // Imagen del malo normal
        urlImagenMalo = this.getClass().getResource("Bananas.png");
        
        // Defino y creo los malos normales
        for(int iC = 0; iC < iRandom - iRandEsp; iC++){
            // Creo un malo
            Malo malMalo = new Malo(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo), 'n');
            // Añado malo a la lista
            lklMalos.add(malMalo);
        }
    }
    
    public void reposicionaMalos(){
        // Se posicionan los malos
        for(Malo malMalo : lklMalos){
            malMalo.setX(alAzar(0, getWidth() - malMalo.getAncho()));
            malMalo.setY(0 - alAzar(0, 5) * 100);
        }
    }
    
    public void reposicionaMalo(Malo malMalo){
        // Se posicionan el malo
        malMalo.setX(alAzar(0, getWidth() - malMalo.getAncho()));
        malMalo.setY(0 - alAzar(0, 5) * 100);
    }
    
    public void reposicionaPrincipal(){
        // Se posiciona el principal
        basPrincipal.setY(getHeight() - basPrincipal.getAlto());
        basPrincipal.setX((getWidth() - basPrincipal.getAncho())/2); 
    }
    
    /**
     * checaColision
     * 
     * Metodo que revisa si la canasta entró en contacto con alguno de los 
     * bordes definidos o con la fruta o si la fruta entró en contacto con
     * el borde inferior.
     * 
     */
    public void checaColision(){
        checaColisionPrincipal();
        checaColisionMalos();
        checaColisionBalas();
    }
    
    public void checaColisionPrincipal(){
        // Checa colision de la canasta con los bordes
        if(basPrincipal.getX() <= 0) { // CHOCA IZQUIERDA
            basPrincipal.setX(0);
        } 
        
        if(basPrincipal.getX() + basPrincipal.getAncho() >= getWidth()) { // CHOCA DERECHA
            basPrincipal.setX(getWidth() - basPrincipal.getAncho());
        }
    }
    
    public void checaColisionMalos(){
        // Checa colision de los malos
        for(Malo malMalo : lklMalos){
            // Revisa contacto con el borde inferior
            if(malMalo.getY() + malMalo.getAlto() >= getHeight()) {
                reposicionaMalo(malMalo);
            }
            
            // Checo la colision entre principal y malo
            if(basPrincipal.colisiona(malMalo)) {
                iPuntos -= 1;
                iCaidas ++;
                if(iVidas > 0 && iCaidas >= 5){
                    iVidas--; // Quita una vida
                    iCaidas = 0; // Resetea las caidas
                }
                reposicionaMalo(malMalo);
            }
        }
    }
    
    public void checaColisionBalas(){
        for(Bala balBala:lklBalas){
            if(balBala.getY() <= 0){
                lklBalas.remove(balBala);
            }
           
            for(Malo malMalo:lklMalos){
                if(balBala.colisiona(malMalo)){
                    lklBalas.remove(balBala);
                    reposicionaMalo(malMalo);
                    iPuntos += 10;
                }
            }
        }
    }
    
    /**
     * start
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     *
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
    
    /**
     * paint
     *
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y
     * define cuando usar ahora el paint
     *
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    @Override
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width,
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Fondo.jpg");
        Image imaImagenFondo =
                Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        graGraficaApplet.drawImage(imaImagenFondo, 0, 0,
                 getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * getWidth
     *
     * Metodo que devuelve el ancho.
     *
     * @return WIDTH que es una constante para el ancho.
     */
    @Override
    public int getWidth(){
        return WIDTH;
    }

    /**
     * getHeight
     *
     * Metodo que devuelve la altura.
     *
     * @return HEIGHT que es una constante para la altura.
     */
    @Override
    public int getHeight(){
        return HEIGHT;
    }
    
    public void actualiza(){
        actualizaMalos();
        actualizaPrincipal();
        actualizaBalas();
    }
    
    public void actualizaMalos(){
        // Actualiza malos
        for(Malo malMalo : lklMalos){
            int iVelMalo = (7 - iVidas) * 3;
            // Actualizo la posicion de los malos
            malMalo.setY(malMalo.getY() + iVelMalo);
        }
    }
    
    public void actualizaPrincipal(){
        // Actualizo posicion del principal        
        switch(iDirPrincipal){
            case 0:
                break;
            case 1:
                basPrincipal.setX(basPrincipal.getX() - 3);
                break;
            case 2:
                basPrincipal.setX(basPrincipal.getX() + 3);
                break;
        }
    }
    
    public void actualizaBalas(){
        // Actualiza balas
        for(Bala balBala : lklBalas){
            balBala.avanza();
        }
    }
    
    /**
     * paint1
     *
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param graDibujo es el objeto de <code>Graphics</code> usado
     * para dibujar.
     *
     */
    public void paint1(Graphics graDibujo) {
        if(iVidas > 0)
        {
            // si la imagen ya se cargo
            if (basPrincipal != null && imaImagenFondo != null && 
                    lklMalos != null) {
                // Dibuja la imagen de fondo
                graDibujo.drawImage(imaImagenFondo, 0, 0, getWidth(), 
                        getHeight(), this);
                // Dibuja la imagen de la canasta en el Applet
                basPrincipal.paint(graDibujo, this);
                // Dibuja las frutas
                for(Malo malMalo : lklMalos){
                    if(malMalo.getVivo()){
                        malMalo.paint(graDibujo, this);
                    }
                }
                // Dibuja las frutas
                for(Bala balBala : lklBalas){
                    if(balBala.getVivo()){
                        balBala.paint(graDibujo, this);
                    }
                }
                                
                graDibujo.drawString("Puntos: " + iPuntos, 10, 50);
                graDibujo.drawString("Vidas: " + iVidas, 10, 70);
                graDibujo.drawString("Caidas: " + iCaidas, 10, 90);
            } // sino se ha cargado se dibuja un mensaje 
            else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        } else {
            graDibujo.drawImage(imaPierde, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /**
     * App
     * 
     * Contructor
     * 
     */
    public App(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        init();
        start();
    }

   /**
    * main
    *
    * Crea instanciacion
    *
    * @param args
    */
    public static void main(String[] args){
        App appNuevo = new App();
        appNuevo.setVisible(true);
    }
    
    @Override
    public void run() {
        while (iVidas > 0) {
            if(!bPausa){
                actualiza();
                checaColision();
            }
            repaint();

            try	{
                // El hilo del juego se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " +
                        iexError.toString());
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    public void disparaBala(char cDir){
        if(bOtraBala){
            // Creo una bala
            URL urlImagenBala = this.getClass().getResource("Coconut.png");
            Bala balBala = new Bala(basPrincipal.getX(), basPrincipal.getY(),
                Toolkit.getDefaultToolkit().getImage(urlImagenBala), cDir, 5);
            // Añado malo a la lista
            lklBalas.add(balBala);
            bOtraBala = false;
        }
    }
    
    /**
     * keyPressed
     *
     * Actualiza la direccion dependiendo de la tecla presionada.
     *
     * @param keyEvent es un evento tipo KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int iTecla = keyEvent.getKeyCode();
        
        switch (iTecla) {
            case KeyEvent.VK_LEFT:
                iDirPrincipal = 1;
                break;
            case KeyEvent.VK_RIGHT:
                iDirPrincipal = 2;
                break;
            case KeyEvent.VK_P:
                bPausa = !bPausa;
                break;
        }
        
        switch (iTecla) {
            case KeyEvent.VK_A:
                disparaBala('i');
                break;
            case KeyEvent.VK_SPACE:
                disparaBala('c');
                break;
            case KeyEvent.VK_S:
                disparaBala('d');
                break;
        }
    }

    /**
     * keyReleased
     *
     * Actualiza la direccion cuando se suelta la tecla.
     *
     * @param keyEvent es un evento tipo KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        iDirPrincipal = 0;
        bOtraBala = true;
    }
    
    /**
     * alAzar
     *
     * Metodo que regresa un numero entero entre dos valores.
     *
     * @param iMin valor minimo
     * @param iMax valor maximo
     * @return Numero entero entre el minimo y el maximo
     */
    public int alAzar(int iMin, int iMax) {
        int iRango = (iMax - iMin) + 1;
        return (int)(Math.random() * iRango) + iMin;
    }
}
