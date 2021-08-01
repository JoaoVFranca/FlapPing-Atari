package components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Parede extends Entidade {

    // Textura da parede
    public Texture Skin;
    // Condição de existencia da parede
    public boolean Destruida = false;

    private Sound SomParedeQuebrada;

    Parede(GameScreen j, int xPosition, int yPosition, int Width, int Height) {
        super(j, xPosition, yPosition, Width, Height);
        this.Skin = GetJogo().app.assets.get("assets/barreira.png");
        SomParedeQuebrada = Gdx.audio.newSound(Gdx.files.internal("sounds/som_parede.wav"));
    }

    public void ColisionWall() {
        // Testa se essa parede colidiu com a bola, se sim, a bola rebate e a parede é destruida
        if (this.overlaps(GetJogo().bola) && GetJogo().bola.TempoUltimaColisao > .3f) {
            SomParedeQuebrada.play(.1f);
            GetJogo().bola.dx *= -1;
            Destruida = true;
            GetJogo().bola.TempoUltimaColisao = 0;
        }
    }
    
}
