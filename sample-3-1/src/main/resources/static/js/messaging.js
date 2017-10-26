var MessagingTools = function (Game) {
    var joinGameMessage = {};
    joinGameMessage.class = "JoinGame$Request";

    this.sendJoinGameMsg = function () {
        Game.socket.send(JSON.stringify(joinGameMessage));
    };

    this.sendClientSnap = function(snap) {
        snap.class = "ClientSnap";
        Game.socket.send(JSON.stringify(snap));
    }
};
