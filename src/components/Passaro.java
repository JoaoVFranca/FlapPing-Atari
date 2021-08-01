package components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class Passaro extends Entidade {

    // Limitadores da área de voo do pássaro
    private int Piso, Teto; 

    // Direção no eixo X e Y
    public int DirecaoX, DirecaoY;

    // Texturas do pássaro
    private Texture[] Skins = new Texture[3];
    public Texture GetAtualSkin() { return Skins[2]; }

    // Som de colisão com jogadores
    private Sound PlayerHitSound;

    // Velocidade
    private int FlySpeed = 400;

    //Variáveis de tempo para auxiliar no trajeto de voo. 
    private long lastTimeUtil;
    private long nanoSegundo = 1000000000;

    // Controle de colisão
    private float TempoUltimaColisao = 0;
    private float cooldownColisao = .5f;
    
    public Passaro(GameScreen j, int xPosition, int yPosition, int Width, int Height, Texture[] skins) {
        super(j, xPosition, yPosition, Width, Height);
        // Inicializa suas direções iniciais
        this.DirecaoX = 1;
        this.DirecaoY = -1;

        // Delimita sua área de voo
        this.Teto = Application.V_HEIGHT - 200;
        this.Piso = 200;

        this.Skins = skins;
        // Armazena o tempo
        lastTimeUtil = TimeUtils.nanoTime();
        // Prepara o som de colisão com o jogador
        PlayerHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_passaro.wav"));
    }

    private void playHitSound() { // Toca som de colisão com o jogador
        PlayerHitSound.play(.1f);
    }

    public void Mover() {

        TempoUltimaColisao += Gdx.graphics.getDeltaTime();

        // Testa colisão com o jogador A
        if (this.overlaps(GetJogo().Player1) && TempoUltimaColisao > cooldownColisao) {
            playHitSound();
            TempoUltimaColisao = 0;
            GetJogo().Player1.Fall();
        }
        
        if (GetJogo().COOP) {
            // Testa colisão com o jogador B
            if (this.overlaps(GetJogo().Player2) && TempoUltimaColisao > cooldownColisao) {
                playHitSound();
                TempoUltimaColisao = 0;
                GetJogo().Player2.Fall();
            }
        } else {
            // Testa colisão com o bot
            if (this.overlaps(GetJogo().bot) && TempoUltimaColisao > cooldownColisao) {
                playHitSound();
                TempoUltimaColisao = 0;
                GetJogo().bot.Fall();
            }
        }

        testeColisaoHorizontal();
        testeColisaoVertical();

        // A cada 1 segundo, o pássaro inverte sua direção vertical
        if (TimeUtils.nanoTime() - lastTimeUtil > nanoSegundo) {
            DirecaoY *= -1;
            lastTimeUtil = TimeUtils.nanoTime();
        }

        this.x += this.DirecaoX * FlySpeed * Gdx.graphics.getDeltaTime();
        this.y += this.DirecaoY * FlySpeed * Gdx.graphics.getDeltaTime();
    }

    private void testeColisaoHorizontal() {
        if (this.x > 680) {
            this.DirecaoX *= -1;
            this.Skins[2] = this.Skins[1];
        }

        if (this.x <= 0) {
            this.DirecaoX *= -1;
            this.Skins[2] = Skins[0];
        }
    }

    private void testeColisaoVertical() {
        if (this.y <= Piso) {
            this.SetPosition((int) this.x, Piso);        
        }
        if (this.y >= Teto) {
            this.SetPosition((int) this.x, Teto);
        }
    }

}
