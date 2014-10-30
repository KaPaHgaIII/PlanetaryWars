public class Game {


    public Game() {
        Gameplay gameplay = new Gameplay();
        gameplay.startPlanets();
        UI ui = new UI(gameplay);
        ui.start();
    }


    public static void main(String[] args) {
        new Game();
    }

}