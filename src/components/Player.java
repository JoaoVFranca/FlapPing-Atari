package components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/*
Essa é a classe usada pros jogadores com controle
*/

public class Player extends Entidade {

    // Variaveis auxiliares para a lógica do pulo
    private int InteracoesA = -1, InteracoesB = 0;

    private int AxisY = -1; // Eixo Y para movimento

    // Força do pulo e Gravidade
    private float Aceleracao = 25;
    private float Gravity = 300;

    // Condições para pular(PuloPermitido deve ser true e foiAbatido deve ser false)
    public boolean PuloPermitido = true;
    public boolean foiAbatido = false;

    // Som de pulo
    private Sound JumpSound;

    /*
    Skins:
    [0]: representa o jogador normalmente
    [1]: representa o jogador com o jetpack ligado
    [2]: representa a skin usada no momento
    */
    private Texture[] Skins = new Texture[3];
    public Texture[] GetSkins() { return Skins; }
    public void SetAtualTexture(Texture t) { this.Skins[2] = t; }

    float velocidade;
    
    public Player(GameScreen j, int x, int y, int width, int height, Texture[] skins) {
        super(j, x, y, width, height);
        this.Skins = skins;
        // Prepara o som de pulo
        JumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_pulo.wav"));
    }

    private void playJumpSound() { // Toca som de pulo
        JumpSound.play(.1f);
    }

    public void Mover() {

        /*
        Quando InteraçõesA == -1, quer dizer que a CPU não está sobre ação do pulo
        Quando InteraçoesA != -1, ela está sobre ação do pulo
        */

        if (InteracoesA != -1) {
            SmoothJump();
        }

        if (InteracoesA == -1) {// Se não estiver sobre força do pulo, a gravidade funciona normalmente
            // Quanto mais próximo ao chao, mais forte é a forca da gravidade resultante
            float finalGravity = Gravity + (30 * (Application.V_HEIGHT/this.y));
            this.y += (AxisY * finalGravity * Gdx.graphics.getDeltaTime());     
        }

        testaColisaoVertical();

    }

    private void testaColisaoVertical() {
        if (this.y < 0) {
            SetPosition((int)this.x, 0);
            PuloPermitido = true;
            foiAbatido = false;
            if (Gravity > 300)
                Gravity = 300;
        }
        if (this.y > Application.V_HEIGHT - this.height)
            SetPosition((int)this.x,(int) (Application.V_HEIGHT - this.height));
    }

    /*
    Essa função faz 10 interações aumentando a posição Y(aumenta menos a cada interação),
    depois faz 3 interações diminuindo a posição Y(diminui mais a cada interação).
    */
    public void SmoothJump() {
        if (InteracoesA == 10) {
            AxisY = -1;
            if (InteracoesB != 3) {
                InteracoesB++;
                this.y += (AxisY * ((Aceleracao/InteracoesA) * InteracoesB));
            } else {
                InteracoesA = -1;
                InteracoesB = 0;
            }
        } else {
            AxisY = 1;
            InteracoesA++;
            this.y += (AxisY * (Aceleracao/InteracoesA));
        }
    }

    public void Jump() {
        if (!PuloPermitido || foiAbatido) {
            return;
        }
        playJumpSound();
        InteracoesA = 0;

        this.PuloPermitido = false;
    }

    /* 
    Essa função é chamada quando o pássaro toca o jogador, assim ele é abatido,
    só irá se recuperar depois de atingir o chão.
    */
    public void Fall() {
        Gravity = 2000;
        foiAbatido = true;
        PuloPermitido = false;
    }

}