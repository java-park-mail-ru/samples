$(document).ready(function () {
    // var working = false;
    $("#form").submit(function(ev){
        var login = $("#login").val();
        if (!login || login == "") {
            return;
        }
        var request = JSON.stringify({"login" : login});
        $.ajax({
            type: 'POST',
            contentType: "application/json",
            url: "/api/guest/",
            data: request,
            success: function(response) {
                if (response.login) {
                    $("#answer").html("Hi, " + response.login);
                }
                setTimeout(function () {
                    window.location = "/squares.html";

                }, 2000);
            },
            error: function(data) {
                window.console.warn("error logining in: " + data.responseText);
                $("#answer").html("Error: " + data.responseJSON.message);
            }
        });
        ev.stopPropagation();
        return false;

    });
});


