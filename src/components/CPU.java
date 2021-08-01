package components;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/*
Essa é uma classe similar a classe Player.java, porém os objetos dessa classe se movem
sozinhos durante o jogo.
*/

public class CPU extends Entidade{

    // Variáveis para auxilio no pulo
    private int InteracoesA = -1, InteracoesB = 0;

    // Eixo de movimento vertical
    private int AxisY = -1;

    // Forca inicial do pulo
    private float Aceleracao = 40;

    // Gravidade
    private float Gravity = 300;

    // Som de pulo
    private Sound JumpSound;

    // Condições para pular(PuloPermitido deve ser true e foiAbatido deve ser false)
    public boolean PuloPermitido = true;
    public boolean foiAbatido = false;

    /*
    Skins:
    [0]: representa a CPU normalmente
    [1]: representa a CPU com o jetpack ligado
    [2]: representa a skin usada no momento
    */
    private Texture[] Skins = new Texture[3];
    public Texture[] GetSkins() { return Skins; }
    public void SetAtualTexture(Texture t) { this.Skins[2] = t; }
    
    public CPU(GameScreen j, int x, int y, int width, int height, Texture[] skins) {
        super(j, x, y, width, height);
        this.Skins = skins;
        // Prepara o som de pulo
        JumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_pulo.wav"));
    }

    private void playJumpSound() {// Toca o som de pulo
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

        if (InteracoesA == -1) { // Se não estiver sobre força do pulo, a gravidade funciona normalmente
            // Quanto mais próximo ao chao, mais forte é a forca da gravidade resultante
            float finalGravity = Gravity + (30 * (Application.V_HEIGHT/this.y));
            this.y += (AxisY * finalGravity * Gdx.graphics.getDeltaTime());
        }

        testeColisãoVertical();

        movimentaCPU(); 

    }

    private void movimentaCPU() {
        // A CPU só irá começar a pular quando a bola vem em sua direção
        if(this.y < GetJogo().bola.y - 25 && GetJogo().bola.dx > 0 ){
            Jump();
            this.Skins[2] = Skins[1];
        }
    }

    private void testeColisãoVertical() {
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
                PuloPermitido = true;
                this.Skins[2] = Skins[0];
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
        PuloPermitido = false;
        foiAbatido = true;
    }
}
