var Game = {};
var Resources = {};
var Messaging = new MessagingTools(Game);

Game.fps = 30;
Game.socket = null;
Game.nextFrame = null;
Game.interval = null;
Game.direction = 'None';

Game.mouse = {x: 0.0, y: 0.0};
Game.serverSnaps = [];
Game.userId = null;
Game.userSquareId = null;
Game.frameTimeLeft = 0;

Game.clientReinterpt = 50;

Game.crossFatness = 4;
Game.crossSize = 40;
Game.bulletSize = 30;
Game.maxBulletsCount = 15;
Game.bulletSpread = 50;

Game.rectSize = 250;
Game.rectInARow = 3;
Game.emptyRect = "#666666";
Game.userRect = "#669900";
Game.enemyRect = "#b30000";

function ClientSnap() {
    this.mouse = {x: 0.0, y: 0.0};
    this.isFiring = false;
    // noinspection JSUnusedGlobalSymbols
    this.frameTime = 0;
}

function PlayerSnap(snapMsg) {
    this.id = 0;
    this.mouse = {x: 0.0, y: 0.0};
    this.score = 0;
    this.isFiring = false;


    if (snapMsg != null) {
        this.id = snapMsg.userId;
        this.isFiring = snapMsg.gameParts.MechanicPartSnap.firing;
        this.score = snapMsg.gameParts.MechanicPartSnap.score;
        this.mouse = {
            x: snapMsg.gameParts.MouseSnap.mouse.x,
            y: snapMsg.gameParts.MouseSnap.mouse.y};
    }
}

function Player() {
    this.id = 0;
    this.mouse = {x: 0.0, y: 0.0};
    this.score = 0;
    this.color= "#FFFFFF";
    this.name = "";
    this.isFiring = false;
}

Player.prototype.draw = function (context) {
    context.fillStyle = this.color;
    context.fillRect(
        this.mouse.x - Game.crossFatness / 2,
        this.mouse.y - Game.crossSize / 2,
        Game.crossFatness,
        Game.crossSize);
    context.fillRect(
        this.mouse.x - Game.crossSize / 2,
        this.mouse.y - Game.crossFatness / 2,
        Game.crossSize,
        Game.crossFatness
    );
};

function Board(squares) {
    this.squares = squares.map(function(square) {
        return square.occupant;
    });
}

Board.prototype.draw = function (context) {
    var x = 0;
    var y = 0;
    var linearLength = 0;

    for (var i = 0; i < this.squares.length; i++) {
        var id = this.squares[i];
        if (id == null) {
            context.fillStyle = Game.emptyRect;
            context.rect(x, y, Game.rectSize, Game.rectSize)
        } else if (id === Game.userId) {
            // context.fillStyle = Game.userRect;
            // context.fillRect(x, y, Game.rectSize, Game.rectSize);
            context.drawImage(Resources.myPic, x, y, Game.rectSize, Game.rectSize)
        } else {
            // context.fillStyle = Game.enemyRect;
            // context.fillRect(x, y, Game.rectSize, Game.rectSize)
            context.drawImage(Resources.enemyPic, x, y, Game.rectSize, Game.rectSize)
        }
        linearLength += Game.rectSize;
        y = Math.floor(linearLength / (Game.rectSize * Game.rectInARow)) * Game.rectSize;
        x = linearLength - y  * Game.rectInARow;
    }
};

function Bullet(x, y, img) {
    this.x = x;
    this.y = y;
    this.img = img;
}

Bullet.prototype.draw = function (context) {
    context.drawImage(this.img, this.x - Game.bulletSize / 2, this.y - Game.bulletSize / 2, Game.bulletSize, Game.bulletSize)
};

function ServerSnap() {
    this.players = [];
    this.board = [];
    this.serverFrameTime = 0;
    this.elapsed  = 0;
}

Resources.preloadImg = function(url) {
    var img = new Image();
    img.src = url;
    return img;
};

Resources.bulletRotator = new function () {
    this.nextBullet = 0;

    this.nextImg = function () {
        var img = Resources.bullets[this.nextBullet];
        this.nextBullet = (this.nextBullet + 1) % Resources.bullets.length;
        return img;
    }
};

Game.initialize = function () {
    this.players = [];
    this.board = {};
    this.bullets = [];
    Resources.enemyPic = Resources.preloadImg("images/hero4.jpg");
    Resources.myPic = Resources.preloadImg("images/hero5.jpg");
    Resources.bullets = [];
    Resources.bullets[0] = Resources.preloadImg("images/bullet1.png");
    Resources.bullets[1] = Resources.preloadImg("images/bullet2.png");
    Resources.bullets[2] = Resources.preloadImg("images/bullet3.png");
    Resources.bullets[3] = Resources.preloadImg("images/bullet4.png");
    canvas = document.getElementById('playground');
    if (!canvas.getContext) {
        Console.log('Error: 2d canvas not supported by this browser.');
        return;
    }
    this.context = canvas.getContext('2d');

    window.addEventListener('mousemove', function (e) {
        if (Game.userId != null) {
            Game.players[Game.userId].mouse.x = e.offsetX;
            Game.players[Game.userId].mouse.y = e.offsetY;
        }
    });
    window.addEventListener('click', function (e) {
        if (Game.userId != null) {
            Game.players[Game.userId].mouse.x = e.offsetX;
            Game.players[Game.userId].mouse.y = e.offsetY;
            Game.players[Game.userId].isFiring = true;
        }
    });

    Game.connect();
};

Game.startGameLoop = function () {
    if (window.requestAnimationFrame) {
        Game.nextFrame = function () {
            requestAnimationFrame(Game.run);
        };
    } else if (window.webkitRequestAnimationFrame) {
        Game.nextFrame = function () {
            webkitRequestAnimationFrame(Game.run);
        };
    } else if (window.mozRequestAnimationFrame) {
        Game.nextFrame = function () {
            mozRequestAnimationFrame(Game.run);
        };
    } else {
        Game.interval = setInterval(Game.run, 1000 / Game.fps);
    }
    if (Game.nextFrame != null) {
        Game.nextFrame();
    }
};

Game.addBullet = function (x, y, img) {
    Game.bullets.push(new Bullet(x, y, img));
    var count = Game.bullets.length;
    if (count > Game.maxBulletsCount) {
        Game.bullets = Game.bullets.slice(count - Game.maxBulletsCount, count - 1)
    }
};

Game.stopGameLoop = function () {
    Game.nextFrame = null;
    if (Game.interval != null) {
        clearInterval(Game.interval);
    }
};

Game.draw = function () {
    var context = this.context;
    this.context.clearRect(0, 0, Game.rectSize * Game.rectInARow, Game.rectSize * Game.rectInARow);
    Game.board.draw(context);
    for (var id in this.players) {
        if (id != Game.userId) {
            this.players[id].draw(context);
        }
    }
    Game.bullets.forEach(function(bullet) {
        bullet.draw(context)
    });
};

Game.addSquare = function (id, name, color, gunColor) {
    Game.entities[id] = new Square();
    Game.entities[id].color = color;
    Game.entities[id].gunColor = gunColor;
    Game.entities[id].name = name;
};

Game.addPlayer = function (id, name, color, mouse) {
    Game.players[id] = new Player();
    Game.players[id].color = color;
    Game.players[id].name = name;
    Game.players[id].mouse = mouse;

};

Game.updatePlayer = function (id, mouse, isFiring) {
    Game.players[id].color = color;
    Game.players[id].isFiring = isFiring;
    Game.players[id].mouse = mouse;
};

Game.removeSquare = function (id) {
    Game.entities[id] = null;
    // Force GC.
    delete Game.entities[id];
};

Game.lastFrameTime = 0;
Game.initialSkipTime = Game.clientReinterpt;


Game.run = (function () {
    var skipTicks = 1000 / Game.fps, nextGameTick = (new Date).getTime();
    return function () {
        var time = (new Date).getTime();
        var frameTime = time - Game.lastFrameTime;
        while (time > nextGameTick) {
            nextGameTick += skipTicks;
        }
        Game.sendClientSnap(frameTime);
        if (Game.initialSkipTime < 0) {
            Game.updateObjects(frameTime);
        } else {
            Game.initialSkipTime -= frameTime;
        }
        Game.draw();
        if (Game.nextFrame != null) {
            Game.nextFrame();
        }
        Game.lastFrameTime = time;

    };
})();

Game.updateObjects = function(frameTime) {
    var moveTimeOnSnap = 0;
    var snapFinished = false;
    var player = null;
    var moveTime = frameTime + Game.frameTimeLeft;
    Game.players[Game.userId].isFiring = false;
    //lagging
    if (Game.serverSnaps.length > 2) {
        window.console.info("lags detected");
        var snapAdjustTo =  Game.serverSnaps[Game.serverSnaps.length - 2];
        for (var i = 0; i < snapAdjustTo.players.length; i++) {
            var playerSnap = snapAdjustTo.players[i];
            Game.players[playerSnap.id].mouse = playerSnap.mouse;
            Game.players[playerSnap.id].isFiring = playerSnap.isFiring;
        }

        Game.serverSnaps = [Game.serverSnaps[Game.serverSnaps.length - 1]];
        return;
    }
    while (moveTime > 0) {
        if (Game.serverSnaps.length < 1) {
            Game.frameTimeLeft += moveTime;
            return
        }
        var serverSnap = Game.serverSnaps[0];
        if (serverSnap.serverFrameTime - serverSnap.elapsed > moveTime) {
            moveTimeOnSnap = moveTime;
            snapFinished = false;
        } else {
            moveTimeOnSnap = serverSnap.serverFrameTime - serverSnap.elapsed;
            snapFinished = true;
        }
        for (i = 0; i < serverSnap.players.length; i++) {
            player = serverSnap.players[i];
            if (player.id === Game.userId) {
                continue;
            }

            var mouse = Game.players[player.id].mouse;
            var overallDX = player.mouse.x - mouse.x;
            var overallDY = player.mouse.y - mouse.y;
            mouse.x += overallDX * moveTimeOnSnap / serverSnap.serverFrameTime;
            mouse.y += overallDY * moveTimeOnSnap / serverSnap.serverFrameTime;
        }
        if (snapFinished) {
            Game.serverSnaps.splice(0,1);
            Game.board = serverSnap.board;
            Game.updateScores(serverSnap.players);
            for (i = 0; i < serverSnap.players.length; i++) {
                player = serverSnap.players[i];
                if (player.isFiring) {
                    var bulletX = player.mouse.x + Math.random() * Game.bulletSpread - Game.bulletSpread / 2;
                    var bulletY = player.mouse.y + Math.random() * Game.bulletSpread - Game.bulletSpread / 2;
                    Game.addBullet(bulletX, bulletY, Resources.bulletRotator.nextImg())
                }
            }
            moveTime -= moveTimeOnSnap;
        } else {
            serverSnap.elapsed += moveTimeOnSnap;
            moveTime = 0;
        }
    }
};

Game.sendClientSnap = function (frameTime) {
    var snap = new ClientSnap();
    var me = Game.players[Game.userId];
    snap.mouse = me.mouse;
    snap.frameTime = frameTime;
    snap.isFiring = me.isFiring;
    Messaging.sendClientSnap(snap);
};

Game.onServerSnapArrived = function (snapRaw) {
    var serverSnap = new ServerSnap();
    serverSnap.timeArrived = (new Date()).getTime();
    snapRaw.players.map(function (playerRaw) {
        return new PlayerSnap(playerRaw);
    }).forEach(function (playerSnap) {
        serverSnap.players.push(playerSnap);
    });
    serverSnap.board = new Board(snapRaw.board.squares);

    serverSnap.serverFrameTime = snapRaw.serverFrameTime;
    Game.serverSnaps.push(serverSnap);
    //Todo: Wait for another snap and launch animation with Game.clientReinterpt delay
};

Game.tryStartGame = function () {
    Messaging.sendJoinGameMsg();
};

Game.onGameFinished = function(finishMessage) {
    Console.log('Game has finished!')

    switch(finishMessage.overcome) {
        case "WIN": Console.log("You WIN!!");
        break;
        case "LOSE": Console.log("You LOSE(");
        break;
        case "DRAW": Console.log("Match come to the draw. Nobody wins");
    }
    Game.stopGameLoop();
};

Game.onGameStarted = function (initMessage) {
    var initPlayer = function (playerRaw, name, color) {
        var playerSnap = new PlayerSnap(playerRaw);
        Game.addPlayer(playerSnap.id, name, color, playerSnap.mouse);

        Console.log('Info: ' + name + ' joins the game!')
    };

    Game.lastFrameTime = (new Date).getTime();
    Game.userId = initMessage.self;
    Game.enemyId = initMessage.enemy;

    initPlayer(initMessage.players[Game.userId], initMessage.names[Game.userId], initMessage.colors[Game.userId]);
    initPlayer(initMessage.players[Game.enemyId], initMessage.names[Game.enemyId], initMessage.colors[Game.enemyId]);


    $(document).ready(function () {
        $("#my_name").html(Game.players[Game.userId].name);
        $("#enemy_name").html(Game.players[Game.enemyId].name);
    });
    Game.board = new Board(initMessage.board.squares);

    Game.startGameLoop();
};

Game.updateScores = function (players) {
    players.forEach(function(player) {
        Game.players[player.id].score = player.score;
    });
    $("#my_scores").html(Game.players[Game.userId].score);
    $("#enemy_scores").html(Game.players[Game.enemyId].score);
};

Game.connect = (function () {
    Game.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/game");

    Game.socket.onopen = function () {
        // Socket open.. start the game loop.
        Console.log('Info: WebSocket connection opened.');
        Console.log('Info: Waiting for another player...');
        try {
            Game.tryStartGame();
        } catch (ex) {
            Game.socket.close(1001, "error: exeception occured during the initialization stage: " + ex);
        }
    };

    Game.socket.onclose = function (event) {
        if (event.wasClean && event.code === 1000) {
            Console.log('Info: Web socket session is over');
            return;
        }
        Console.log('Error: WebSocket was unexpectedly closed. Code: ' + event.code + '. Reason: "' + event.reason+'"');
        Game.stopGameLoop();
    };

    Game.socket.onmessage = function (event) {
        var message = JSON.parse(event.data);

        if (message.class === "InitGame$Request") {
            Game.onGameStarted(message);
            return;
        }
        if (message.class === "FinishGame") {
            Game.onGameFinished(message);
            return;
        }
        if (message.class === "ServerSnap") {
            Game.onServerSnapArrived(message);
        }
    };
});

var Console = {};

Console.log = (function (message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = message;
    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
});

Game.initialize();