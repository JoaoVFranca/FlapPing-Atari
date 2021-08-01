package components;

import com.badlogic.gdx.math.Rectangle;
/*
    Essa é a classe pai para todos as entidades do jogo, como por exemplo a bola, os jogadores,
    parede etc. Tudo que colide herda dessa classe entidade.
*/
public class Entidade extends Rectangle {

    private GameScreen FlapPing;
    public GameScreen GetJogo() { return FlapPing; }

    public void SetPosition(int x, int y) { 
        this.x = x; 
        this.y = y;
    }
   
    public void SetSize(int Width, int Height) { 
        this.width = Width;
        this.height = Height; 
    }

    Entidade(GameScreen j,int xPosition, int yPosition, int Width, int Height) {
        this.FlapPing = j;
        this.x = xPosition;
        this.y = yPosition;
        this.width = Width;
        this.height = Height;
    }

}
