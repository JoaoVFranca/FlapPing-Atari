package components;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Bola extends Entidade {
    // Textura da bola
    public Texture skin;

    // Velocidade no eixo X e Y
    private int VelocidadeX = 200, VelocidadeY = 200;

    // Variáveis para controlar a colisão
    public float TempoUltimaColisao = 0;

    /* 
    Essa variável foi criada para que o passaro não possa 
    bater na bola várias vezes num curto perído de tempo
    */
    public boolean tocouPassaroRecentemente = false;

    // Direção para cada eixo, e escalamento da velocidade
    public double dx = -1, dy = -1, speedScale = 1.5;

    // Sons de pontuação para o jogador A e B, e para a colisão com outras entidades
    private Sound PontoASound, PontoBSound, BallHitSound;
    
    public Bola(GameScreen jogo, int novoX, int novoY, int novaALTURA, int novaLARGURA, Texture skin) {
        super(jogo, novoX, novoY, novaALTURA, novaLARGURA);
        this.SetSize(novaLARGURA, novaALTURA);
        this.SetPosition(Application.V_WIDTH/2, Application.V_HEIGHT/2);
        this.skin = skin;

        // Inicializando os sons
        PontoASound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_pontoA.wav"));
        PontoBSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_pontoB.wav"));
        BallHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_bola.wav"));
    }
    private void PlayAPointSound() { // Toca o Som de pontuação
        if (GetJogo().PontosA != 10)
            PontoASound.play(.1f);
    }
    private void PlayBPointSound() { // Toca o Som de pontuação adversária
        if (GetJogo().PontosB != 10)
            PontoBSound.play(.1f);
    }
    private void PlayHitSound() {BallHitSound.play(.1f);}

    public void Mover() {
        
        TempoUltimaColisao += Gdx.graphics.getDeltaTime();

        // Se a ultima colisão tiver sido a mais de 0.3 segundos, então a bola pode colidir novamente
        if (TempoUltimaColisao > .3f) { 

            // Testa colisão com o Player A
            if (this.overlaps(GetJogo().Player1)) {
                int angle = new Random().nextInt((130 - 55));
                dx = Math.cos(Math.toRadians(angle));
                dy = Math.sin(Math.toRadians(angle));
                speedScale = 1.5;
                dx = Math.abs(dx);

                TempoUltimaColisao = 0;
                tocouPassaroRecentemente = false;
                PlayHitSound();
            }
            
            // Testa colisão com o Passaro
            if (this.overlaps(GetJogo().passaro) && !tocouPassaroRecentemente) {
                tocouPassaroRecentemente = true;
                int angle;
                if (GetJogo().passaro.DirecaoX == 1) {
                    angle = new Random().nextInt(90) * -1;
                    dx = Math.cos(Math.toRadians(angle));
                    dy = Math.sin(Math.toRadians(angle));
                } else {
                    angle = new Random().nextInt(270) * -1;
                    dx = Math.cos(Math.toRadians(angle));
                    dy = Math.sin(Math.toRadians(angle));
                }
                
                speedScale = 2;
                TempoUltimaColisao = 0;
                PlayHitSound();
            }

            if (GetJogo().COOP) {

                // Testa colisão com o Player B
                if (this.overlaps(GetJogo().Player2)) {
                    int angle = new Random().nextInt((130 - 55));
                    dx = Math.cos(Math.toRadians(angle));
                    dy = Math.sin(Math.toRadians(angle));
                    speedScale = 1.5;
                    if (dx > 0)
                        dx *= -1;
    
                    TempoUltimaColisao = 0;
                    tocouPassaroRecentemente = false;
                    PlayHitSound();
                }
            } else {

                // Testa colisão com o Bot
                if (this.overlaps(GetJogo().bot)) {
                    int angle = new Random().nextInt((130 - 55));
                    dx = Math.cos(Math.toRadians(angle));
                    dy = Math.sin(Math.toRadians(angle));
                    speedScale = 1.5;
                    if (dx > 0)
                        dx *= -1;
    
                    TempoUltimaColisao = 0;
                    tocouPassaroRecentemente = false;
                    PlayHitSound();
                }
            }

        }
        
        testePontuacaoA();
        testePontuacaoB();

        testeColidiuTeto();
        testeColidiuChao(); 
            
        this.x += dx * VelocidadeX * Gdx.graphics.getDeltaTime() * speedScale;
        this.y += dy * VelocidadeY * Gdx.graphics.getDeltaTime() * speedScale;
    }

    private void testePontuacaoA() {
        if (this.x > Application.V_WIDTH - this.width) {
            this.SetPosition(Application.V_WIDTH/2, Application.V_HEIGHT/2);
            speedScale = 1.5;
            GetJogo().PontosA++;
            PlayAPointSound();
        }
    }

    private void testePontuacaoB() {
        if (this.x <= 0) {
            this.SetPosition(Application.V_WIDTH/2, Application.V_HEIGHT/2);
            speedScale = 1.5;
            GetJogo().PontosB++;
            if (GetJogo().COOP)
                PlayAPointSound();
            else 
                PlayBPointSound();
        }
    }

    private void testeColidiuTeto() {
        if (this.y >= Application.V_HEIGHT - this.height - 5) {
            tocouPassaroRecentemente = false;
            dy *= -1;
        }
    }

    private void testeColidiuChao() {
        if (this.y <= 5) {
            tocouPassaroRecentemente = false;
            dy *= -1;
        }
    }

}