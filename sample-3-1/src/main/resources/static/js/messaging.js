var MessagingTools = function (Game) {
    var joinGameMessage = {};
    joinGameMessage.class = "JoinGame$Request";

    this.sendJoinGameMsg = function () {
        send(JSON.stringify(joinGameMessage));
    };

    this.sendClientSnap = function(snap) {
        snap.class = "ClientSnap";
        send(JSON.stringify(snap));
    };

    var send = function(message) {
        if (Game.socket.readyState === Game.socket.CLOSED ||
            Game.socket.readyState === Game.socket.CLOSING) {
            return;
        }
        Game.socket.send(message)
    };
};
