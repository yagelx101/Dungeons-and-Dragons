package Game.CallBacks;

import Game.Utils.Position;

public interface PositionChangedCallback {
    void send(Position oldPos, Position newPos);
}
